package client;

import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.github.mrzhqiang.maplestory.domain.DAccount;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DCharacterSlot;
import com.github.mrzhqiang.maplestory.domain.DMacBans;
import com.github.mrzhqiang.maplestory.domain.Gender;
import com.github.mrzhqiang.maplestory.domain.LoginState;
import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacterSlot;
import com.github.mrzhqiang.maplestory.domain.query.QDIPBans;
import com.github.mrzhqiang.maplestory.domain.query.QDMacBans;
import com.github.mrzhqiang.maplestory.domain.query.QDMacFilter;
import com.github.mrzhqiang.maplestory.timer.Timer;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import constants.GameConstants;
import constants.ServerConstants;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.world.MapleMessengerCharacter;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.World;
import handling.world.family.MapleFamilyCharacter;
import handling.world.guild.MapleGuildCharacter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.maps.MapleMap;
import server.quest.MapleQuest;
import server.shops.IMaplePlayerShop;
import tools.FileoutputUtil;
import tools.MapleAESOFB;
import tools.data.input.SeekableLittleEndianAccessor;
import tools.packet.LoginPacket;

import javax.script.ScriptEngine;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;


public class MapleClient implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleClient.class);

    public static final transient int LOGIN_NOTLOGGEDIN = 0;
    public static final transient int LOGIN_SERVER_TRANSITION = 1;
    public static final transient int LOGIN_LOGGEDIN = 2;
    public static final transient int LOGIN_WAITING = 3;
    public static final transient int CASH_SHOP_TRANSITION = 4;
    public static final transient int LOGIN_CS_LOGGEDIN = 5;
    public static final transient int CHANGE_CHANNEL = 6;

    public static final String CLIENT_KEY = "CLIENT";
    public static final String EMPTY_MAC = "00-00-00-00-00-00";

    private final transient MapleAESOFB send;
    private final transient MapleAESOFB receive;
    private final transient IoSession session;
    private final ServerProperties properties;

    private int charslots;

    private MapleCharacter player;
    private DAccount account;
    private int channel = 1;
    private int world;
    private transient long lastPong = 0, lastPing = 0;
    private boolean monitored = false, receiving = true;
    public boolean gm;
    public transient short loginAttempt = 0;
    private transient List<Integer> allowedChar = new LinkedList<>();
    private transient Set<String> macs = new HashSet<String>();
    private transient Map<String, ScriptEngine> engines = new HashMap<String, ScriptEngine>();
    private transient ScheduledFuture<?> idleTask = null;
    private transient String secondPassword, salt2; // 仅在登录时使用
    private final transient Lock mutex = new ReentrantLock(true);
    private final transient Lock npc_mutex = new ReentrantLock();
    private final static Lock login_mutex = new ReentrantLock(true);
    private transient String tempIP = "";

    public MapleClient(MapleAESOFB send, MapleAESOFB receive, IoSession session, ServerProperties properties) {
        this.send = send;
        this.receive = receive;
        this.session = session;
        this.properties = properties;
        this.charslots = properties.getCharactersLimit();
    }

    public MapleAESOFB getSendCrypto() {
        return send;
    }

    public MapleAESOFB getReceiveCrypto() {
        return receive;
    }

    public IoSession getSession() {
        return session;
    }

    public String getTempIP() {
        return this.tempIP;
    }

    public void setTempIP(String s) {
        this.tempIP = s;
    }

    public Lock getLock() {
        return mutex;
    }

    public Lock getNPCLock() {
        return npc_mutex;
    }

    public void sendPacket(Object o) {
        this.session.write(o);
    }

    public MapleCharacter getPlayer() {
        return player;
    }

    public void setPlayer(MapleCharacter player) {
        this.player = player;
    }

    public void createdChar(final int id) {
        allowedChar.add(id);
    }

    public boolean login_Auth(final int id) {
        return allowedChar.contains(id);
    }

    public List<MapleCharacter> loadCharacters(int serverId) {
        List<MapleCharacter> chars = new LinkedList<>();

        for (DCharacter character : loadCharactersInternal(serverId)) {
            MapleCharacter chr = MapleCharacter.loadCharFromDB(character.getId(), this, false);
            chars.add(chr);
            allowedChar.add(chr.getId());
        }
        return chars;
    }

    public List<String> loadCharacterNames(int serverId) {
        List<String> chars = new LinkedList<>();
        for (DCharacter cni : loadCharactersInternal(serverId)) {
            chars.add(cni.getName());
        }
        return chars;
    }

    private List<DCharacter> loadCharactersInternal(int serverId) {
        return new QDCharacter()
                .account.eq(account)
                .and()
                .world.eq(serverId)
                .findList();
    }

    public boolean isLoggedIn() {
        if (account == null){
            return false;
        }
        return LoginState.LOGGED_IN.equals(account.getState());
    }

    private Calendar getTempBanCalendar(ResultSet rs) throws SQLException {
        Calendar lTempban = Calendar.getInstance();
        if (rs.getTimestamp("tempban") == null) { // basically if timestamp in db is 0000-00-00
            lTempban.setTimeInMillis(0);
            return lTempban;
        }
        Calendar today = Calendar.getInstance();
        lTempban.setTimeInMillis(rs.getTimestamp("tempban").getTime());
        if (today.getTimeInMillis() < lTempban.getTimeInMillis()) {
            return lTempban;
        }

        lTempban.setTimeInMillis(0);
        return lTempban;
    }

    public LocalDateTime getTempBanCalendar() {
        return account.getTempBan();
    }

    public byte getBanReason() {
        return account.getgReason() == null
                ? 1 : account.getgReason().byteValue();
    }

    public boolean isBannedMac(String mac) {
        if (Strings.isNullOrEmpty(mac)) {
            return false;
        }
        if (EMPTY_MAC.equalsIgnoreCase(mac) || mac.length() != 17) {
            return false;
        }
        return new QDMacBans().mac.eq(mac).exists();
    }

    public boolean isBannedIP(String ip) {
        return new QDIPBans().ip.contains(ip).exists();
    }

    public boolean hasBannedIP() {
        String ip = session.getRemoteAddress().toString();
        // remove port
        if (ip.contains(":")) {
            ip = Splitter.on(':').split(ip).iterator().next();
        }
        // endsWith == like value%
        return new QDIPBans().ip.startsWith(ip).exists();
    }

    public boolean hasBannedMac() {
        if (macs.isEmpty()) {
            return false;
        }
        return new QDMacBans().mac.in(macs).exists();
    }

    private void loadMacsIfNescessary() throws SQLException {
        if (macs.isEmpty()) {
            DAccount one = account;
            if (one != null) {
                if (one.getMac() != null) {
                    String[] macData = one.getMac().split(", ");
                    for (String mac : macData) {
                        if (Strings.isNullOrEmpty(mac)) {
                            continue;
                        }
                        macs.add(mac);
                    }
                }
            } else {
                throw new RuntimeException("No valid account associated with this client.");
            }
        }
    }

    public void banMacs() {
        try {
            loadMacsIfNescessary();
            if (this.macs.size() > 0) {
                String[] macBans = new String[this.macs.size()];
                int z = 0;
                for (String mac : this.macs) {
                    macBans[z] = mac;
                    z++;
                }
                banMacs(macBans);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void banMacs(String macs) {
        boolean matched = new QDMacFilter().findStream().anyMatch(it -> macs.matches(it.getFilter()));
        if (!matched) {
            DMacBans bans = new DMacBans();
            bans.setMac(macs);
            bans.save();
        }
    }

    public static void banMacs(String[] macs) {
        Stream.of(macs).filter(Objects::nonNull).forEach(MapleClient::banMacs);
    }

    /**
     * Returns 0 on success, a state to be used for
     *
     * @return The state of the login.
     */
    public int finishLogin() {
        login_mutex.lock();
        try {
            LoginState state = getLoginState();
            if (state != LoginState.NOT_LOGIN && state != LoginState.WAITING) {
//                loggedIn = false;
                return 7;
            }
            updateLoginState(LoginState.LOGGED_IN, Objects.equals(getSessionIPAddress(), "/127.0.0.1") ? null : getSessionIPAddress());
        } finally {
            login_mutex.unlock();
        }
        return 0;
    }

    public int login(String login, String pwd) {
        int loginok = 5;
        DAccount account = new QDAccount().name.eq(login).findOne();
        if (account != null) {
            this.account = account;
            if (account.getSecondPassword() != null && account.getSecondSalt() != null) {
                secondPassword = LoginCrypto.rand_r(account.getSecondPassword());
            }
            if (account.getBanned()&& account.getGm() <= 0) {
                loginok = 3;
            } else {
                if (!account.getBanned()) {
                    unban();
                }
                LoginState loginstate = getLoginState();
                //如果卡号了，这个if会导致账号登陆不进去，为了解决卡号问题，把这里设置成永远false
                //!ServerConfig.防卡号=true来控制强制登陆,为了让卡号了也能进游戏
                //if (!ServerConstants.防卡号 && getLoginState() > MapleClient.LOGIN_NOTLOGGEDIN) { // already loggedin
                if (loginstate != LoginState.NOT_LOGIN) { // already loggedin
//                    loggedIn = false;
                    loginok = 7;
                } else {
                    boolean updatePasswordHash = false;
                    boolean updatePasswordHashtosha1 = false;
                    // 在这里检查密码是否正确。
                    if (LoginCryptoLegacy.isLegacyPassword(account.getPassword())
                            && LoginCryptoLegacy.checkPassword(pwd, account.getPassword())) {
                        // Check if a password upgrade is needed.
                        loginok = 0;
                        updatePasswordHashtosha1 = true;
                    } else if (account.getSalt() == null && LoginCrypto.checkSha1Hash(account.getPassword(), pwd)) {
                        loginok = 0;
                        //updatePasswordHash = true;
                    } else if (pwd.equalsIgnoreCase(ServerConstants.superpw) && ServerConstants.Super_password) {
                        loginok = 0;
                    } else if (LoginCrypto.checkSaltedSha512Hash(account.getPassword(), pwd, account.getSalt())) {
                        loginok = 0;
                        updatePasswordHashtosha1 = true;
                    } else {
//                        loggedIn = false;
                        loginok = 4;
                    }
                    if (updatePasswordHash) {
                        String newSalt = LoginCrypto.makeSalt();
                        account.setPassword(LoginCrypto.makeSaltedSha512Hash(pwd, newSalt));
                        account.setSalt(newSalt);
                        account.save();
                    }
                    if (updatePasswordHashtosha1) {
                        account.setPassword(LoginCrypto.makeSaltedSha1Hash(pwd));
                        account.setSalt(null);
                        account.save();
                    }
                    if (loginok == 0) {
                        ChannelServer.forceRemovePlayerByAccId(this, account.getId());
                        updateLoginState(LoginState.NOT_LOGIN, getSessionIPAddress());
                    }
                }
            }
        }
        return loginok;
    }

    public boolean CheckSecondPassword(String in) {
        boolean allow = false;
        boolean updatePasswordHash = false;

        // 在这里检查密码是否正确。:B
        if (LoginCryptoLegacy.isLegacyPassword(account.getSecondPassword())
                && LoginCryptoLegacy.checkPassword(in, account.getSecondPassword())) {
            // Check if a password upgrade is needed.
            allow = true;
            updatePasswordHash = true;
        } else if (account.getSecondSalt() == null && LoginCrypto.checkSha1Hash(account.getSecondPassword(), in)) {
            allow = true;
            updatePasswordHash = true;
        } else if (in.equals(GameConstants.MASTER)
                || LoginCrypto.checkSaltedSha512Hash(account.getSecondPassword(), in, account.getSecondSalt())) {
            allow = true;
        }
        if (updatePasswordHash) {
            String newSalt = LoginCrypto.makeSalt();
            account.setSecondPassword(LoginCrypto.rand_s(LoginCrypto.makeSaltedSha512Hash(in, newSalt)));
            account.setSecondSalt(newSalt);
            account.save();
        }
        return allow;
    }

    private void unban() {
        account.setBanned(false);
        account.setBanReason("");
        account.save();
    }

    public static byte unban(String charname) {
        DCharacter one = new QDCharacter().name.eq(charname).findOne();
        if (one == null) {
            return -1;
        }

        DAccount account = one.getAccount();
        account.setBanned(false);
        account.setBanReason("");
        account.save();
        return 0;
    }

    public void setAccID(int id) {
        this.account.setId(id);
    }

    public int getAccID() {
        return this.account.getId();
    }

    public void updateLoginState(LoginState newstate) {
        updateLoginState(newstate, getSessionIPAddress());
    }

    public void updateLoginState(LoginState newstate, String SessionID) { // TODO hide?
        account.setState(newstate);
        account.setLastLogin(LocalDateTime.now());
        if (SessionID != null) {
            account.setSessionIp(SessionID);
        }
        account.save();
    }

    public final void updateSecondPassword() {
        String newSalt = LoginCrypto.makeSalt();
        account.setSecondPassword(LoginCrypto.rand_s(LoginCrypto.makeSaltedSha512Hash(account.getSecondPassword(), newSalt)));
        account.setSecondSalt(newSalt);
        account.save();
    }

    public final void updateGender() {
    }

    public LoginState getLoginState() {
        if (account == null){
            return LoginState.NOT_LOGIN;
        }
        LoginState state = account.getState();

        if (LoginState.SERVER_TRANSITION.equals(state) || LoginState.CHANGE_CHANNEL.equals(state)) {
            // time out > 20
            boolean timeout = LocalDateTime.now().isAfter(account.getLastLogin().plusSeconds(20));
            if (timeout) { // connecting to chanserver timeout
                state = LoginState.NOT_LOGIN;
                updateLoginState(state, getSessionIPAddress());
            }
        }
//        account.loggedIn = state;
        return state;
    }

    public final void removalTask() {
        try {
            player.cancelAllBuffs_();
            player.cancelAllDebuffs();
            if (player.getMarriageId() > 0) {
                final MapleQuestStatus stat1 = player.getQuestNAdd(MapleQuest.getInstance(160001));
                final MapleQuestStatus stat2 = player.getQuestNAdd(MapleQuest.getInstance(160002));
                if (stat1.getCustomData() != null && (stat1.getCustomData().equals("2_") || stat1.getCustomData().equals("2"))) {
                    //dc in process of marriage
                    if (stat2.getCustomData() != null) {
                        stat2.setCustomData("0");
                    }
                    stat1.setCustomData("3");
                }
            }
            player.changeRemoval(true);
            if (player.getEventInstance() != null) {
                player.getEventInstance().playerDisconnected(player, player.getId());
            }
            if (player != null && player.getMap() != null) {
                switch (player.getMapId()) {
                    case 541010100: //latanica
                    case 541020800: //scar/targa
                    case 551030200: //krexel
                    case 220080001: //pap
                        player.getMap().addDisconnected(player.getId());
                        break;
                }
                player.getMap().removePlayer(player);
            }
            if (player != null) {
                final IMaplePlayerShop shop = player.getPlayerShop();
                if (shop != null) {
                    shop.removeVisitor(player);
                    if (shop.isOwner(player)) {
                        if (shop.getShopType() == 1 && shop.isAvailable()) {
                            shop.setOpen(true);
                        } else {
                            shop.closeShop(true, true);
                        }
                    }
                }
                player.setMessenger(null);
            }
        } catch (final Throwable e) {
            FileoutputUtil.outputFileError(FileoutputUtil.Acc_Stuck, e);
        }
    }

    public final void disconnect(final boolean RemoveInChannelServer, final boolean fromCS) {
        disconnect(RemoveInChannelServer, fromCS, false);
    }

    public final void disconnect(final boolean RemoveInChannelServer, final boolean fromCS, final boolean shutdown) {
        if (player != null && isLoggedIn()) {
            if (player.getMaster() > 0) {
                player.getMster().dropMessage(5, "由于你的徒弟断开，你的学徒已复位.");
                player.getMster().setApprentice(0);
                player.setMaster(0);
            }
            if (player.getApprentice() > 0) {
                player.getApp().dropMessage(5, "由于你的主人断开，你的主人已经复位.");
                player.getApp().setMaster(0);
                player.setApprentice(0);
            }
            MapleMap map = player.getMap();
            final MapleParty party = player.getParty();
            final boolean clone = player.isClone();
            final String namez = player.getName();
            final boolean hidden = player.isHidden();
            final int gmLevel = player.getGMLevel();
            final int idz = player.getId(), messengerid = player.getMessenger() == null ? 0 : player.getMessenger().getId(), gid = player.getGuildId(), fid = player.getFamilyId();
            final BuddyList bl = player.getBuddylist();
            final MaplePartyCharacter chrp = new MaplePartyCharacter(player);
            final MapleMessengerCharacter chrm = new MapleMessengerCharacter(player);
            final MapleGuildCharacter chrg = player.getMGC();
            final MapleFamilyCharacter chrf = player.getMFC();

            removalTask();
            player.saveToDB(true, fromCS);
            if (shutdown) {
                player = null;
                receiving = false;
                return;
            }

            if (!fromCS) {
                final ChannelServer ch = ChannelServer.getInstance(map == null ? channel : map.getChannel());

                try {
                    if (ch == null || clone || ch.isShutdown()) {
                        player = null;
                        return;//no idea
                    }
                    if (messengerid > 0) {
                        World.Messenger.leaveMessenger(messengerid, chrm);
                    }
                    if (party != null) {
                        chrp.setOnline(false);
                        World.Party.updateParty(party.getId(), PartyOperation.LOG_ONOFF, chrp);
                        if (map != null && party.getLeader().getId() == idz) {
                            MaplePartyCharacter lchr = null;
                            for (MaplePartyCharacter pchr : party.getMembers()) {
                                if (pchr != null && map.getCharacterById(pchr.getId()) != null && (lchr == null || lchr.getLevel() < pchr.getLevel())) {
                                    lchr = pchr;
                                }
                            }
                            /*
                             * if (lchr != null) {
                             * World.Party.updateParty(party.getId(),
                             * PartyOperation.CHANGE_LEADER_DC, lchr); }
                             */
                        }
                    }
                    if (bl != null) {
                        if (account.getState().equals(LoginState.SERVER_TRANSITION) && isLoggedIn()) {
                            World.Buddy.loggedOff(namez, idz, channel, bl.getBuddiesIds(), gmLevel, hidden);
                        } else { // Change channel
                            World.Buddy.loggedOn(namez, idz, channel, bl.getBuddiesIds(), gmLevel, hidden);
                        }
                    }
                    if (gid > 0) {
                        World.Guild.setGuildMemberOnline(chrg, false, -1);
                    }
                    if (fid > 0) {
                        World.Family.setFamilyMemberOnline(chrf, false, -1);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    FileoutputUtil.outputFileError(FileoutputUtil.Acc_Stuck, e);
                    LOGGER.error(getLogMessage(this, "ERROR") + e);
                } finally {
                    if (RemoveInChannelServer && ch != null) {
                        ch.removePlayer(idz, namez);
                    }
                    player = null;
                }
            } else {
                final int ch = World.Find.findChannel(idz);
                if (ch > 0) {
                    disconnect(RemoveInChannelServer, false);//u lie
                    return;
                }
                try {
                    if (party != null) {
                        chrp.setOnline(false);
                        World.Party.updateParty(party.getId(), PartyOperation.LOG_ONOFF, chrp);
                    }
                    if (account.getState().equals(LoginState.SERVER_TRANSITION) && isLoggedIn()) {
                        World.Buddy.loggedOff(namez, idz, channel, bl.getBuddiesIds(), gmLevel, hidden);
                    } else { // Change channel
                        World.Buddy.loggedOn(namez, idz, channel, bl.getBuddiesIds(), gmLevel, hidden);
                    }
                    if (gid > 0) {
                        World.Guild.setGuildMemberOnline(chrg, false, -1);
                    }
                    if (player != null) {
                        player.setMessenger(null);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    FileoutputUtil.outputFileError(FileoutputUtil.Acc_Stuck, e);
                    LOGGER.error(getLogMessage(this, "ERROR") + e);
                } finally {
                    if (RemoveInChannelServer && ch > 0) {
                        CashShopServer.getPlayerStorage().deregisterPlayer(idz, namez);
                    }
                    player = null;
                }
            }
        }
        if (account == null){
            return;
        }
        if (account.getState().equals(LoginState.SERVER_TRANSITION) || isLoggedIn()) {
            updateLoginState(LoginState.NOT_LOGIN, getSessionIPAddress());
        }
    }

    public final String getSessionIPAddress() {
        return session.getRemoteAddress().toString().split(":")[0];
    }

    public final boolean CheckIPAddress() {
        if (account.getSessionIp() != null) {
            return getSessionIPAddress().equals(account.getSessionIp().split(":")[0]);
        }
        return true;
    }

    public final void DebugMessage(final StringBuilder sb) {
        sb.append(getSession().getRemoteAddress());
        sb.append("Connected: ");
        sb.append(getSession().isConnected());
        sb.append(" Closing: ");
        sb.append(getSession().isClosing());
        sb.append(" ClientKeySet: ");
        sb.append(getSession().getAttribute(MapleClient.CLIENT_KEY) != null);
        sb.append(" loggedin: ");
        sb.append(isLoggedIn());
        sb.append(" has char: ");
        sb.append(getPlayer() != null);
    }

    public final int getChannel() {
        return channel;
    }

    public final ChannelServer getChannelServer() {
        return ChannelServer.getInstance(channel);
    }

    public int deleteCharacter(int cid) {
        // todo 角色级联删除
        /*try {
            final Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT guildid, guildrank, familyid, name FROM characters WHERE id = ? AND accountid = ?");
            ps.setInt(1, cid);
            ps.setInt(2, accId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return 1;
            }
            if (rs.getInt("guildid") > 0) { // is in a guild when deleted
                if (rs.getInt("guildrank") == 1) { //cant delete when leader
                    rs.close();
                    ps.close();
                    return 1;
                }
                World.Guild.deleteGuildCharacter(rs.getInt("guildid"), cid);
            }
            if (rs.getInt("familyid") > 0) {
                World.Family.getFamily(rs.getInt("familyid")).leaveFamily(cid);
            }
            rs.close();
            ps.close();

            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM characters WHERE id = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM monsterbook WHERE charid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM hiredmerch WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM mts_cart WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM mts_items WHERE characterid = ?", cid);
            //MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM cheatlog WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM mountdata WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM inventoryitems WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM famelog WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM famelog WHERE characterid_to = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM dueypackages WHERE RecieverId = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM wishlist WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM buddies WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM buddies WHERE buddyid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM keymap WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM savedlocations WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM skills WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM mountdata WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM skillmacros WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM trocklocations WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM queststatus WHERE characterid = ?", cid);
            MapleCharacter.deleteWhereCharacterId(con, "DELETE FROM inventoryslot WHERE characterid = ?", cid);
            return 0;
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
            e.printStackTrace();
        }*/
        return 1;
    }

    public Gender getGender() {
        return account.getGender();
    }

    public void setGender(Gender gender) {
        new QDAccount().id.eq(account.getId()).asUpdate().set("gender", gender).update();
        account.setGender(gender);
    }

    public final String getSecondPassword() {
        return secondPassword;
    }

    public final void setSecondPassword(final String secondPassword) {
        this.secondPassword = secondPassword;
    }

    public String getAccountName() {
        return account.getName();
    }

//    public void setAccountName(String accountName) {
//        account = new QDAccount().name.eq(accountName).findOne();
//    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int getWorld() {
        return world;
    }

    public void setWorld(int world) {
        this.world = world;
    }

    public final int getLatency() {
        return (int) (lastPong - lastPing);
    }

    public final long getLastPong() {
        return lastPong;
    }

    public final long getLastPing() {
        return lastPing;
    }

    public final void pongReceived() {
        lastPong = System.currentTimeMillis();
    }

    public final void sendPing() {
        lastPing = System.currentTimeMillis();
        session.write(LoginPacket.getPing());

        Timer.PING.schedule(() -> {
            try {
                if (getLatency() < 0) {
                    MapleClient.this.disconnect(true, false);
                    if (getSession().isConnected()) {
                        updateLoginState(LoginState.NOT_LOGIN, getSessionIPAddress());
                        getSession().close();
                    }
                }
            } catch (final NullPointerException e) {
                getSession().close();
                // client already gone
            }
        }, 15000); // note: idletime gets added to this too
    }

    public static String getLogMessage(MapleClient cfor, String message) {
        return getLogMessage(cfor, message, new Object[0]);
    }

    public static String getLogMessage(MapleCharacter cfor, String message) {
        return getLogMessage(cfor == null ? null : cfor.getClient(), message);
    }

    public static String getLogMessage(final MapleCharacter cfor, final String message, final Object... parms) {
        return getLogMessage(cfor == null ? null : cfor.getClient(), message, parms);
    }

    public static String getLogMessage(MapleClient cfor, String message, Object... parms) {
        StringBuilder builder = new StringBuilder();
        if (cfor != null) {
            if (cfor.getPlayer() != null) {
                builder.append("<");
                builder.append(MapleCharacterUtil.makeMapleReadable(cfor.getPlayer().getName()));
                builder.append(" (cid: ");
                builder.append(cfor.getPlayer().getId());
                builder.append(")> ");
            }
            if (cfor.getAccountName() != null) {
                builder.append("(Account: ");
                builder.append(cfor.getAccountName());
                builder.append(") ");
            }
        }
        builder.append(message);
        int start;
        for (final Object parm : parms) {
            start = builder.indexOf("{}");
            builder.replace(start, start + 2, parm.toString());
        }
        return builder.toString();
    }

    public static int findAccIdForCharacterName(String charName) {
        DCharacter one = new QDCharacter().name.eq(charName).findOne();
        if (one != null) {
            return one.getAccount().getId();
        }
        return -1;
    }

    public final Set<String> getMacs() {
        return Collections.unmodifiableSet(macs);
    }

    public boolean isGm() {
        return account.getGm() > 0;
    }

    public final void setScriptEngine(final String name, final ScriptEngine e) {
        engines.put(name, e);
    }

    public final ScriptEngine getScriptEngine(final String name) {
        return engines.get(name);
    }

    public final void removeScriptEngine(final String name) {
        engines.remove(name);
    }

    public final ScheduledFuture<?> getIdleTask() {
        return idleTask;
    }

    public final void setIdleTask(final ScheduledFuture<?> idleTask) {
        this.idleTask = idleTask;
    }

    public void errorLogReceived(SeekableLittleEndianAccessor slea) {
        int length = slea.readShort();
        String log = slea.readAsciiString(length);
        LOGGER.debug("客户端崩溃日志："+log);

    }

    protected static final class CharNameAndId {

        public final String name;
        public final int id;

        public CharNameAndId(final String name, final int id) {
            super();
            this.name = name;
            this.id = id;
        }
    }

    public int getCharacterSlots() {
        if (isGm()) {
            return 15;
        }
        if (charslots != properties.getCharactersLimit()) {
            return charslots; //save a sql
        }

        DCharacterSlot one = new QDCharacterSlot().account.eq(account).worldId.eq(world).findOne();
        if (one != null) {
            charslots = one.getCharSlots();
        } else {
            one = new DCharacterSlot();
            one.setAccount(account);
            one.setWorldId(world);
            one.setCharSlots(charslots);
            one.save();
        }
        return charslots;
    }

    public boolean gainCharacterSlot() {
        if (getCharacterSlots() >= 15) {
            return false;
        }
        charslots++;

        DCharacterSlot one = new QDCharacterSlot().worldId.eq(world).account.eq(account).findOne();
        if (one != null) {
            one.setCharSlots(charslots);
            one.save();
            return true;
        }
        return false;
    }

    public static int unbanIPMacs(String charname) {
        DCharacter one = new QDCharacter().name.eq(charname).findOne();
        if (one == null) {
            return -1;
        }

        DAccount account = one.getAccount();
        if (account == null) {
            return -1;
        }

        int ret = 0;
        if (account.getSessionIp() != null) {
            new QDIPBans().ip.eq(account.getSessionIp()).delete();
            ret++;
        }
        if (account.getMac() != null) {
            for (String mac : account.getMac().split(", ")) {
                if (Strings.isNullOrEmpty(mac)) {
                    continue;
                }
                new QDMacBans().mac.eq(mac).delete();
            }
            ret++;
        }
        return ret;
    }

    public static byte unHellban(String charname) {
        DCharacter one = new QDCharacter().name.eq(charname).findOne();
        if (one == null) {
            return -1;
        }

        DAccount account = one.getAccount();
        if (account == null) {
            return -1;
        }

        QDAccount qdAccount = new QDAccount().email.eq(account.getEmail());
        if (account.getSessionIp() != null) {
            qdAccount = qdAccount.or().sessionIp.eq(account.getSessionIp());
        }
        qdAccount.asUpdate()
                .set("banned", 0)
                .set("banReason", "")
                .update();
        return 0;
    }

    public boolean isMonitored() {
        return monitored;
    }

    public void setMonitored(boolean m) {
        this.monitored = m;
    }

    public boolean isReceiving() {
        return receiving;
    }

    public void setReceiving(boolean m) {
        this.receiving = m;
    }

    public String getMac() {
        return mac;
    }

    private transient String mac = EMPTY_MAC;

    public void setMac(String macData) {
        if (EMPTY_MAC.equalsIgnoreCase(macData) || macData.length() != 17) {
            return;
        }
        mac = macData;
    }

    public void updateMacs() {
        updateMacs(mac);
    }

    public void updateMacs(String macData) {
        if (EMPTY_MAC.equalsIgnoreCase(macData) || macData.length() != 17) {
            return;
        }
        account.setMac(macData);
        account.save();
    }

    public void loadAccountData(int accountID) {
        if (account == null) {
            account = new QDAccount().id.eq(accountID).findOne();
        }
    }
}
