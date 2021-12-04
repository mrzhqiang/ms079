package handling.world;

import client.BuddyEntry;
import client.BuddyList;
import client.BuddyList.BuddyAddResult;
import client.BuddyList.BuddyOperation;
import client.MapleCharacter;
import client.MapleCoolDownValueHolder;
import client.MapleDiseaseValueHolder;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import client.inventory.PetDataFactory;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DFamily;
import com.github.mrzhqiang.maplestory.domain.DGuild;
import com.github.mrzhqiang.maplestory.domain.VCharacterAggregate;
import com.github.mrzhqiang.maplestory.domain.query.QDFamily;
import com.github.mrzhqiang.maplestory.domain.query.QDGuild;
import com.github.mrzhqiang.maplestory.domain.query.QVCharacterAggregate;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import handling.MaplePacket;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.PlayerStorage;
import handling.world.family.MapleFamily;
import handling.world.family.MapleFamilyCharacter;
import handling.world.guild.MapleBBSThread;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildAlliance;
import handling.world.guild.MapleGuildCharacter;
import handling.world.guild.MapleGuildSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.mrzhqiang.maplestory.timer.Timer;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import tools.CollectionUtil;
import tools.MaplePacketCreator;
import tools.packet.PetPacket;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public final class World {

    private static final Logger LOGGER = LoggerFactory.getLogger(World.class);

    public static void init() {
        Find.init();
        Guild.init();
        Alliance.init();
        Family.init();
        Messenger.init();
        Party.init();
    }

    public static String getStatus() throws RemoteException {
        StringBuilder ret = new StringBuilder();
        int totalUsers = 0;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            ret.append("Channel ");
            ret.append(cs.getChannel());
            ret.append(": ");
            int channelUsers = cs.getConnectedClients();
            totalUsers += channelUsers;
            ret.append(channelUsers);
            ret.append(" users\n");
        }
        ret.append("Total users online: ");
        ret.append(totalUsers);
        ret.append("\n");
        return ret.toString();
    }

    public static Map<Integer, Integer> getConnected() {
        Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
        int total = 0;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            int curConnected = cs.getConnectedClients();
            ret.put(cs.getChannel(), curConnected);
            total += curConnected;
        }
        ret.put(0, total);
        return ret;
    }

    public static List<CheaterData> getCheaters() {
        List<CheaterData> allCheaters = new ArrayList<CheaterData>();
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            allCheaters.addAll(cs.getCheaters());
        }
        Collections.sort(allCheaters);
        return CollectionUtil.copyFirst(allCheaters, 10);
    }

    public static boolean isConnected(String charName) {
        return Find.findChannel(charName) > 0;
    }

    public static void toggleMegaphoneMuteState() {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            cs.toggleMegaphoneMuteState();
        }
    }

    public static void ChannelChange_Data(CharacterTransfer Data, int characterid, int toChannel) {
        getStorage(toChannel).registerPendingPlayer(Data, characterid);
    }

    public static boolean isCharacterListConnected(List<String> charName) {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (final String c : charName) {
                if (cs.getPlayerStorage().getCharacterByName(c) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasMerchant(int accountID) {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            if (cs.containsMerchant(accountID)) {
                return true;
            }
        }
        return false;
    }

    public static PlayerStorage getStorage(int channel) {
        if (channel == -20) {
            return CashShopServer.getPlayerStorageMTS();
        } else if (channel == -10) {
            return CashShopServer.getPlayerStorage();
        }
        return ChannelServer.getInstance(channel).getPlayerStorage();
    }

    public static String getAllowLoginTip(List<String> charNames) {
        StringBuilder ret = new StringBuilder("账号下其他角色在游戏: ");
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (String name : charNames) {
                if (cserv.isConnected(name)) {
                    ret.append(name);
                    ret.append(" ");
                }
            }
        }
        return ret.toString();
    }

    public static int getPendingCharacterSize() {
        int ret = CashShopServer.getPlayerStorage().pendingCharacterSize();
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            ret += cserv.getPlayerStorage().pendingCharacterSize();
        }
        return ret;
    }

    public static class Party {

        private static final Map<Integer, MapleParty> PARTY_MAP = Maps.newConcurrentMap();
        private static final AtomicInteger RUNNING_PARTY_ID = new AtomicInteger();

        public static void init() {
            int party = new QVCharacterAggregate()
                    .select(QVCharacterAggregate.alias().party)
                    .findOneOrEmpty()
                    .map(VCharacterAggregate::getParty)
                    .map(integer -> integer + 2)
                    .orElse(1);
            RUNNING_PARTY_ID.set(party);
        }

        public static void partyChat(int partyid, String chattext, String namefrom) {
            MapleParty party = getParty(partyid);
            if (party == null) {
                throw new IllegalArgumentException("no party with the specified partyid exists");
            }

            for (MaplePartyCharacter partychar : party.getMembers()) {
                int ch = Find.findChannel(partychar.getName());
                if (ch > 0) {
                    MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(partychar.getName());
                    if (chr != null && !chr.getName().equalsIgnoreCase(namefrom)) { //Extra check just in case
                        chr.getClient().getSession().write(MaplePacketCreator.multiChat(namefrom, chattext, 1));
                    }
                }
            }
        }

        public static void updateParty(int partyid, PartyOperation operation, MaplePartyCharacter target) {
            MapleParty party = getParty(partyid);
            if (party == null) {
                return; //Don't update, just return. And definitely don't throw a damn exception.
                //throw new IllegalArgumentException("no party with the specified partyid exists");
            }
            switch (operation) {
                case JOIN:
                    party.addMember(target);
                    break;
                case EXPEL:
                case LEAVE:
                    party.removeMember(target);
                    break;
                case DISBAND:
                    disbandParty(partyid);
                    break;
                case SILENT_UPDATE:
                case LOG_ONOFF:
                    party.updateMember(target);
                    break;
                case CHANGE_LEADER:
                case CHANGE_LEADER_DC:
                    party.setLeader(target);
                    break;
                default:
                    throw new RuntimeException("Unhandeled updateParty operation " + operation.name());
            }
            if ((operation == PartyOperation.LEAVE) || (operation == PartyOperation.EXPEL)) {
                int ch = Find.findChannel(target.getName());
                if (ch > 0) {
                    MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(target.getName());
                    if (chr != null) {
                        chr.getClient().getSession().write(MaplePacketCreator.updateParty(chr.getClient().getChannel(), party, operation, target));
                        chr.setParty(null);
                    }
                }
                if ((target.getId() == party.getLeader().getId()) && (party.getMembers().size() > 0)) {
                    MaplePartyCharacter lchr = null;
                    for (MaplePartyCharacter pchr : party.getMembers()) {
                        if ((pchr != null) && ((lchr == null) || (lchr.getLevel() < pchr.getLevel()))) {
                            lchr = pchr;
                        }
                    }
                    if (lchr != null) {
                        updateParty(party.getId(), PartyOperation.CHANGE_LEADER_DC, lchr);
                    }
                }
            }
            if (party.getMembers().size() <= 0) {
                disbandParty(party.getId());
            }
            for (MaplePartyCharacter partychar : party.getMembers()) {
                int ch = Find.findChannel(partychar.getName());
                if (ch > 0) {
                    MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(partychar.getName());
                    if (chr != null) {
                        if (operation == PartyOperation.DISBAND) {
                            chr.setParty(null);
                        } else {
                            chr.setParty(party);
                        }
                        chr.getClient().getSession().write(MaplePacketCreator.updateParty(chr.getClient().getChannel(), party, operation, target));
                    }
                }
            }
            switch (operation) {
                case LEAVE:
                case EXPEL:
                    int ch = Find.findChannel(target.getName());
                    if (ch > 0) {
                        MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(target.getName());
                        if (chr != null) {
                            chr.getClient().getSession().write(MaplePacketCreator.updateParty(chr.getClient().getChannel(), party, operation, target));
                            chr.setParty(null);
                        }
                    }
            }
        }

        public static MapleParty createParty(MaplePartyCharacter chrfor) {
            int partyid = RUNNING_PARTY_ID.getAndIncrement();
            MapleParty party = new MapleParty(partyid, chrfor);
            PARTY_MAP.put(party.getId(), party);
            return party;
        }

        public static MapleParty getParty(Integer partyid) {
            return PARTY_MAP.get(partyid);
        }

        public static MapleParty disbandParty(int partyid) {
            return PARTY_MAP.remove(partyid);
        }
    }

    public static class Buddy {

        public static void buddyChat(int[] recipientCharacterIds, int cidFrom, String nameFrom, String chattext) {
            for (int characterId : recipientCharacterIds) {
                int ch = Find.findChannel(characterId);
                if (ch > 0) {
                    MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(characterId);
                    if (chr != null && chr.getBuddylist().containsVisible(cidFrom)) {
                        chr.getClient().getSession().write(MaplePacketCreator.multiChat(nameFrom, chattext, 0));
                    }
                }
            }
        }

        private static void updateBuddies(int characterId, int channel, Collection<Integer> buddies, boolean offline, int gmLevel, boolean isHidden) {
            for (Integer buddy : buddies) {
                int ch = Find.findChannel(buddy);
                if (ch > 0) {
                    MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(buddy);
                    if (chr != null) {
                        BuddyEntry ble = chr.getBuddylist().get(characterId);
                        if (ble != null && ble.isVisible()) {
                            int mcChannel;
                            if (offline || (isHidden && chr.getGMLevel() < gmLevel)) {
                                ble.setChannel(-1);
                                mcChannel = -1;
                            } else {
                                ble.setChannel(channel);
                                mcChannel = channel - 1;
                            }
                            chr.getBuddylist().put(ble);
                            chr.getClient().sendPacket(MaplePacketCreator.updateBuddyChannel(ble.getCharacterId(), mcChannel));
                        }
                    }
                }
            }
        }

        public static void buddyChanged(int cid, DCharacter character, int channel, BuddyOperation operation, String group) {
            int ch = Find.findChannel(cid);
            if (ch > 0) {
                final MapleCharacter addChar = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(cid);
                if (addChar != null) {
                    final BuddyList buddylist = addChar.getBuddylist();
                    switch (operation) {
                        case ADDED:
                            if (buddylist.contains(character.getId())) {
                                buddylist.put(new BuddyEntry(character, group, channel, true));
                                addChar.getClient().getSession().write(MaplePacketCreator.updateBuddyChannel(character.getId(), channel - 1));
                            }
                            break;
                        case DELETED:
                            if (buddylist.contains(character.getId())) {
                                buddylist.put(new BuddyEntry(character, group, -1, buddylist.get(character.getId()).isVisible()));
                                addChar.getClient().getSession().write(MaplePacketCreator.updateBuddyChannel(character.getId(), -1));
                            }
                            break;
                    }
                }
            }
        }

        public static BuddyAddResult requestBuddyAdd(String addName, int channelFrom, DCharacter character) {
            int ch = Find.findChannel(character.getId());
            if (ch > 0) {
                MapleCharacter addChar = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(addName);
                if (addChar != null) {
                    final BuddyList buddylist = addChar.getBuddylist();
                    if (buddylist.isFull()) {
                        return BuddyAddResult.BUDDYLIST_FULL;
                    }
                    if (!buddylist.contains(character.getId())) {
                        buddylist.addBuddyRequest(addChar.getClient(), channelFrom, character);
                    } else if (buddylist.containsVisible(character.getId())) {
                        return BuddyAddResult.ALREADY_ON_LIST;
                    }
                }
            }
            return BuddyAddResult.OK;
        }

        public static void loggedOn(String name, int characterId, int channel, Collection<Integer> buddies, int gmLevel, boolean isHidden) {
            updateBuddies(characterId, channel, buddies, false, gmLevel, isHidden);
        }

        public static void loggedOff(String name, int characterId, int channel, Collection<Integer> buddies, int gmLevel, boolean isHidden) {
            updateBuddies(characterId, channel, buddies, true, gmLevel, isHidden);
        }
    }

    public static class Messenger {

        private static final Map<Integer, MapleMessenger> MESSENGERS = Maps.newConcurrentMap();
        private static final AtomicInteger RUNNING_MESSENGER_ID = new AtomicInteger();

        public static void init() {
            RUNNING_MESSENGER_ID.set(1);
        }

        public static MapleMessenger createMessenger(MapleMessengerCharacter chrfor) {
            int messengerid = RUNNING_MESSENGER_ID.getAndIncrement();
            MapleMessenger messenger = new MapleMessenger(messengerid, chrfor);
            MESSENGERS.put(messenger.getId(), messenger);
            return messenger;
        }

        public static void declineChat(String target, String namefrom) {
            int ch = Find.findChannel(target);
            if (ch > 0) {
                ChannelServer cs = ChannelServer.getInstance(ch);
                MapleCharacter chr = cs.getPlayerStorage().getCharacterByName(target);
                if (chr != null) {
                    MapleMessenger messenger = chr.getMessenger();
                    if (messenger != null) {
                        chr.getClient().getSession().write(MaplePacketCreator.messengerNote(namefrom, 5, 0));
                    }
                }
            }
        }

        public static MapleMessenger getMessenger(int messengerid) {
            return MESSENGERS.get(messengerid);
        }

        public static void leaveMessenger(int messengerid, MapleMessengerCharacter target) {
            MapleMessenger messenger = getMessenger(messengerid);
            if (messenger == null) {
                throw new IllegalArgumentException("No messenger with the specified messengerid exists");
            }
            int position = messenger.getPositionByName(target.getName());
            messenger.removeMember(target);

            for (MapleMessengerCharacter mmc : messenger.getMembers()) {
                if (mmc != null) {
                    int ch = Find.findChannel(mmc.getId());
                    if (ch > 0) {
                        MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(mmc.getName());
                        if (chr != null) {
                            chr.getClient().getSession().write(MaplePacketCreator.removeMessengerPlayer(position));
                        }
                    }
                }
            }
        }

        public static void silentLeaveMessenger(int messengerid, MapleMessengerCharacter target) {
            MapleMessenger messenger = getMessenger(messengerid);
            if (messenger == null) {
                throw new IllegalArgumentException("No messenger with the specified messengerid exists");
            }
            messenger.silentRemoveMember(target);
        }

        public static void silentJoinMessenger(int messengerid, MapleMessengerCharacter target) {
            MapleMessenger messenger = getMessenger(messengerid);
            if (messenger == null) {
                throw new IllegalArgumentException("No messenger with the specified messengerid exists");
            }
            messenger.silentAddMember(target);
        }

        public static void updateMessenger(int messengerid, String namefrom, int fromchannel) {
            MapleMessenger messenger = getMessenger(messengerid);
            int position = messenger.getPositionByName(namefrom);

            for (MapleMessengerCharacter messengerchar : messenger.getMembers()) {
                if (messengerchar != null && !messengerchar.getName().equals(namefrom)) {
                    int ch = Find.findChannel(messengerchar.getName());
                    if (ch > 0) {
                        MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                        if (chr != null) {
                            MapleCharacter from = ChannelServer.getInstance(fromchannel).getPlayerStorage().getCharacterByName(namefrom);
                            chr.getClient().getSession().write(MaplePacketCreator.updateMessengerPlayer(namefrom, from, position, fromchannel - 1));
                        }
                    }
                }
            }
        }

        public static void joinMessenger(int messengerid, MapleMessengerCharacter target, String from, int fromchannel) {
            MapleMessenger messenger = getMessenger(messengerid);
            if (messenger == null) {
                throw new IllegalArgumentException("No messenger with the specified messengerid exists");
            }
            messenger.addMember(target);
            int position = messenger.getPositionByName(target.getName());
            for (MapleMessengerCharacter messengerchar : messenger.getMembers()) {
                if (messengerchar != null) {
                    int mposition = messenger.getPositionByName(messengerchar.getName());
                    int ch = Find.findChannel(messengerchar.getName());
                    if (ch > 0) {
                        MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                        if (chr != null) {
                            if (!messengerchar.getName().equals(from)) {
                                MapleCharacter fromCh = ChannelServer.getInstance(fromchannel).getPlayerStorage().getCharacterByName(from);
                                chr.getClient().getSession().write(MaplePacketCreator.addMessengerPlayer(from, fromCh, position, fromchannel - 1));
                                fromCh.getClient().getSession().write(MaplePacketCreator.addMessengerPlayer(chr.getName(), chr, mposition, messengerchar.getChannel() - 1));
                            } else {
                                chr.getClient().getSession().write(MaplePacketCreator.joinMessenger(mposition));
                            }
                        }
                    }
                }
            }
        }

        public static void messengerChat(int messengerid, String chattext, String namefrom) {
            MapleMessenger messenger = getMessenger(messengerid);
            if (messenger == null) {
                throw new IllegalArgumentException("No messenger with the specified messengerid exists");
            }

            for (MapleMessengerCharacter messengerchar : messenger.getMembers()) {
                if (messengerchar != null && !messengerchar.getName().equals(namefrom)) {
                    int ch = Find.findChannel(messengerchar.getName());
                    if (ch > 0) {
                        MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                        if (chr != null) {

                            chr.getClient().getSession().write(MaplePacketCreator.messengerChat(chattext));
                        }
                    }
                } //Whisp Monitor Code
                else if (messengerchar != null) {
                    int ch = Find.findChannel(messengerchar.getName());
                    if (ch > 0) {
                        MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                    }
                }
                //
            }
        }

        public static void messengerInvite(String sender, int messengerid, String target, int fromchannel, boolean gm) {

            if (isConnected(target)) {

                int ch = Find.findChannel(target);
                if (ch > 0) {
                    MapleCharacter from = ChannelServer.getInstance(fromchannel).getPlayerStorage().getCharacterByName(sender);
                    MapleCharacter targeter = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(target);
                    if (targeter != null && targeter.getMessenger() == null) {
                        if (!targeter.isGM() || gm) {
                            targeter.getClient().getSession().write(MaplePacketCreator.messengerInvite(sender, messengerid));
                            from.getClient().getSession().write(MaplePacketCreator.messengerNote(target, 4, 1));
                        } else {
                            from.getClient().getSession().write(MaplePacketCreator.messengerNote(target, 4, 0));
                        }
                    } else {
                        from.getClient().getSession().write(MaplePacketCreator.messengerChat(sender + " : " + target + " is already using Maple Messenger"));
                    }
                }
            }

        }
    }

    public static class Guild {

        private static final Map<Integer, MapleGuild> ID_GUILD_CACHED = Maps.newConcurrentMap();
        private static final Map<String, MapleGuild> NAME_GUILD_CACHED = Maps.newConcurrentMap();

        public static void init() {
            LOGGER.info(">>> 初始化 [家族系统]");
            Stopwatch started = Stopwatch.createStarted();
            for (MapleGuild guild : MapleGuild.loadAll()) {
                if (guild.isValid()) {
                    ID_GUILD_CACHED.put(guild.getId(), guild);
                    NAME_GUILD_CACHED.put(guild.getName(), guild);
                }
            }
            LOGGER.info("<<< [家族系统] 初始化完毕，耗时：{}", started.stop());
        }

        public static int createGuild(int leaderId, String name) {
            return MapleGuild.createGuild(leaderId, name);
        }

        public static MapleGuild getGuild(Integer id) {
            MapleGuild ret = ID_GUILD_CACHED.get(id);
            if (ret != null) {
                return ret;
            }

            Optional<DGuild> optional = new QDGuild().id.eq(id).findOneOrEmpty();
            if (!optional.isPresent()) {
                return null;
            }

            ret = new MapleGuild(optional.get());
            if (ret.getId() <= 0 || !ret.isValid()) { //failed to load
                return null;
            }
            ID_GUILD_CACHED.put(id, ret);
            NAME_GUILD_CACHED.put(ret.getName(), ret);
            return ret;
        }

        public static MapleGuild getGuildByName(String guildName) {
            MapleGuild guild = NAME_GUILD_CACHED.get(guildName);
            if (guild != null) {
                return guild;
            }

            for (MapleGuild g : ID_GUILD_CACHED.values()) {
                if (g.getName().equalsIgnoreCase(guildName)) {
                    NAME_GUILD_CACHED.put(guildName, g);
                    return g;
                }
            }
            return null;
        }

        public static MapleGuild getGuild(MapleCharacter mc) {
            return getGuild(mc.getGuildId());
        }

        public static void setGuildMemberOnline(MapleGuildCharacter mc, boolean bOnline, int channel) {
            MapleGuild g = getGuild(mc.character.getGuild().getId());
            if (g != null) {
                g.updateMemberOnline(mc.character.getId(), bOnline, channel);
            }
        }

        public static void guildPacket(int gid, MaplePacket message) {
            MapleGuild g = getGuild(gid);
            if (g != null) {
                g.broadcast(message);
            }
        }

        public static boolean addGuildMember(MapleGuildCharacter mc) {
            MapleGuild g = getGuild(mc.character.getGuild().getId());
            if (g != null) {
                return g.addMember(mc);
            }
            return false;
        }

        public static void leaveGuild(MapleGuildCharacter mc) {
            MapleGuild g = getGuild(mc.character.getGuild().getId());
            if (g != null) {
                g.memberLeave(mc);
            }
        }

        public static void guildChat(int gid, String name, int cid, String msg) {
            MapleGuild g = getGuild(gid);
            if (g != null) {
                g.chat(name, cid, msg);
            }
        }

        public static void changeRank(int gid, int cid, int newRank) {
            MapleGuild g = getGuild(gid);
            if (g != null) {
                g.changeRank(cid, newRank);
            }
        }

        public static void expelMember(MapleGuildCharacter initiator, String name, int cid) {
            MapleGuild g = getGuild(initiator.character.getGuild().getId());
            if (g != null) {
                g.removeMember(initiator, name, cid);
            }
        }

        public static void setGuildNotice(int gid, String notice) {
            MapleGuild g = getGuild(gid);
            if (g != null) {
                g.notice(notice);
            }
        }

        public static void memberLevelJobUpdate(MapleGuildCharacter mc) {
            MapleGuild g = getGuild(mc.character.getGuild().getId());
            if (g != null) {
                g.updateMemberInfo(mc);
            }
        }

        public static void changeRankTitle(int gid, String[] ranks) {
            MapleGuild g = getGuild(gid);
            if (g != null) {
                g.changeRankTitle(ranks);
            }
        }

        public static void setGuildEmblem(int gid, short bg, byte bgcolor, short logo, byte logocolor) {
            MapleGuild g = getGuild(gid);
            if (g != null) {
                g.setEmblem(bg, bgcolor, logo, logocolor);
            }
        }

        public static void disbandGuild(int gid) {
            MapleGuild g = getGuild(gid);
            if (g != null) {
                g.disbandGuild();
                ID_GUILD_CACHED.remove(gid);
            }
        }

        public static void deleteGuildCharacter(int guildid, int charid) {

            //ensure it's loaded on world server
            //setGuildMemberOnline(mc, false, -1);
            MapleGuild g = getGuild(guildid);
            if (g != null) {
                MapleGuildCharacter mc = g.getMGC(charid);
                if (mc != null) {
                    if (mc.character.getGuildRank() > 1) //not leader
                    {
                        g.memberLeave(mc);
                    } else {
                        g.disbandGuild();
                    }
                }
            }
        }

        public static boolean increaseGuildCapacity(int gid) {
            MapleGuild g = getGuild(gid);
            if (g != null) {
                return g.increaseCapacity();
            }
            return false;
        }

        public static void gainGP(int gid, int amount) {
            MapleGuild g = getGuild(gid);
            if (g != null) {
                g.gainGP(amount);
            }
        }

        public static int getGP(final int gid) {
            final MapleGuild g = getGuild(gid);
            if (g != null) {
                return g.getGP();
            }
            return 0;
        }

        public static int getInvitedId(final int gid) {
            final MapleGuild g = getGuild(gid);
            if (g != null) {
                return g.getInvitedId();
            }
            return 0;
        }

        public static void setInvitedId(final int gid, final int inviteid) {
            final MapleGuild g = getGuild(gid);
            if (g != null) {
                g.setInvitedId(inviteid);
            }
        }

        public static int getGuildLeader(final String guildName) {
            final MapleGuild mga = getGuildByName(guildName);
            if (mga != null) {
                return mga.getLeaderId();
            }
            return 0;
        }

        public static void save() {
            LOGGER.debug("Saving guilds...");
            for (MapleGuild a : ID_GUILD_CACHED.values()) {
                a.writeToDB(false);
            }
        }

        public static List<MapleBBSThread> getBBS(final int gid) {
            final MapleGuild g = getGuild(gid);
            if (g != null) {
                return g.getBBS();
            }
            return null;
        }

        public static int addBBSThread(final int guildid, final String title, final String text, final int icon, final boolean bNotice, final int posterID) {
            final MapleGuild g = getGuild(guildid);
            if (g != null) {
                return g.addBBSThread(title, text, icon, bNotice, posterID);
            }
            return -1;
        }

        public static void editBBSThread(final int guildid, final int localthreadid, final String title, final String text, final int icon, final int posterID, final int guildRank) {
            final MapleGuild g = getGuild(guildid);
            if (g != null) {
                g.editBBSThread(localthreadid, title, text, icon, posterID, guildRank);
            }
        }

        public static void deleteBBSThread(final int guildid, final int localthreadid, final int posterID, final int guildRank) {
            final MapleGuild g = getGuild(guildid);
            if (g != null) {
                g.deleteBBSThread(localthreadid, posterID, guildRank);
            }
        }

        public static void addBBSReply(final int guildid, final int localthreadid, final String text, final int posterID) {
            final MapleGuild g = getGuild(guildid);
            if (g != null) {
                g.addBBSReply(localthreadid, text, posterID);
            }
        }

        public static void deleteBBSReply(final int guildid, final int localthreadid, final int replyid, final int posterID, final int guildRank) {
            final MapleGuild g = getGuild(guildid);
            if (g != null) {
                g.deleteBBSReply(localthreadid, replyid, posterID, guildRank);
            }
        }

        public static void changeEmblem(int gid, int affectedPlayers, MapleGuildSummary mgs) {
            Broadcast.sendGuildPacket(affectedPlayers, MaplePacketCreator.guildEmblemChange(gid, mgs.getLogoBG(), mgs.getLogoBGColor(), mgs.getLogo(), mgs.getLogoColor()), -1, gid);
            setGuildAndRank(affectedPlayers, -1, -1, -1);    //respawn player
        }

        public static void setGuildAndRank(int cid, int guildid, int rank, int alliancerank) {
            int ch = Find.findChannel(cid);
            if (ch == -1) {
                // LOGGER.debug("ERROR: cannot find player in given channel");
                return;
            }
            MapleCharacter mc = getStorage(ch).getCharacterById(cid);
            if (mc == null) {
                return;
            }
            boolean bDifferentGuild;
            if (guildid == -1 && rank == -1) { //just need a respawn
                bDifferentGuild = true;
            } else {
                bDifferentGuild = guildid != mc.getGuildId();
                mc.setGuildId(guildid);
                mc.setGuildRank((byte) rank);
                mc.setAllianceRank((byte) alliancerank);
                mc.saveGuildStatus();
            }
            if (bDifferentGuild && ch > 0) {
                mc.getMap().broadcastMessage(mc, MaplePacketCreator.removePlayerFromMap(cid), false);
                mc.getMap().broadcastMessage(mc, MaplePacketCreator.spawnPlayerMapobject(mc), false);
            }
        }
    }

    public static class Broadcast {

        public static void broadcastSmega(byte[] message) {
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.broadcastSmega(message);
            }
        }

        public static void broadcastGMMessage(byte[] message) {
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.broadcastGMMessage(message);
            }
        }

        public static void broadcastMessage(byte[] message) {
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.broadcastMessage(message);
            }
        }

        public static void sendPacket(List<Integer> targetIds, MaplePacket packet, int exception) {
            MapleCharacter c;
            for (int i : targetIds) {
                if (i == exception) {
                    continue;
                }
                int ch = Find.findChannel(i);
                if (ch < 0) {
                    continue;
                }
                c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(i);
                if (c != null) {
                    c.getClient().getSession().write(packet);
                }
            }
        }

        public static void sendGuildPacket(int targetIds, MaplePacket packet, int exception, int guildid) {
            if (targetIds == exception) {
                return;
            }
            int ch = Find.findChannel(targetIds);
            if (ch < 0) {
                return;
            }
            final MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(targetIds);
            if (c != null && c.getGuildId() == guildid) {
                c.getClient().getSession().write(packet);
            }
        }

        public static void sendFamilyPacket(int targetIds, MaplePacket packet, int exception, int guildid) {
            if (targetIds == exception) {
                return;
            }
            int ch = Find.findChannel(targetIds);
            if (ch < 0) {
                return;
            }
            final MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(targetIds);
            if (c != null && c.getFamilyId() == guildid) {
                c.getClient().getSession().write(packet);
            }
        }

        public static void broadcastMessage(MaplePacket serverNotice) {
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                cs.broadcastMessage(serverNotice);
            }
        }
    }

    public static class Find {

        private static final Map<Integer, Integer> ID_CHANNEL_CACHED = Maps.newConcurrentMap();
        private static final Map<String, Integer> NAME_CHANNEL_CACHED = Maps.newConcurrentMap();

        public static void init() {
            // 提前类加载，初始化上面两个缓存映射
        }

        /**
         * 注册用户及对应的频道。
         * <p>
         * todo 改为 用户数据（组合 account characters 之类的实体）映射频道实体
         * <p>
         * todo 或者是 用户 ID 映射频道实体
         * <p>
         * 目前只是登录的时候，以及进入商城的时候调用
         */
        public static void register(Integer id, String name, Integer channel) {
            if (id == null || name == null) {
                return;
            }
            ID_CHANNEL_CACHED.put(id, channel);
            NAME_CHANNEL_CACHED.put(name.toLowerCase(), channel);
        }

        public static int findChannel(Integer id) {
            if (id == null) {
                return -1;
            }
            Integer ret = ID_CHANNEL_CACHED.get(id);
            if (ret == null) {
                return -1;
            }
            // -10 CS -20 MTS 如果不是前面这两个频道，并且在频道服务中也无法找到此 ID 对应的频道，那么强制注销用户
            if (ret != -10 && ret != -20 && ChannelServer.getInstance(ret) == null) { //wha
                Find.forceDeregister(id);
                return -1;
            }
            return ret;
        }

        public static int findChannel(String name) {
            if (name == null) {
                return -1;
            }
            Integer ret = NAME_CHANNEL_CACHED.get(name.toLowerCase());
            if (ret == null) {
                return -1;
            }
            // -10 CS -20 MTS 如果不是前面这两个频道，并且在频道服务中也无法找到此 ID 对应的频道，那么强制注销用户
            if (ret != -10 && ret != -20 && ChannelServer.getInstance(ret) == null) { //wha
                Find.forceDeregister(name);
                return -1;
            }
            return ret;
        }

        /**
         * 通过用户 ID 强制注销。
         *
         * @param id 用户 ID，登录时会进行记录，绑定对应的频道 ID，其实可以作为真实的在线用户列表。
         */
        public static void forceDeregister(Integer id) {
            if (id == null) {
                return;
            }
            ID_CHANNEL_CACHED.remove(id);
        }

        public static void forceDeregister(String name) {
            if (name == null) {
                return;
            }
            NAME_CHANNEL_CACHED.remove(name.toLowerCase());
        }

        public static void forceDeregister(Integer id, String name) {
            if (id == null || name == null) {
                return;
            }
            ID_CHANNEL_CACHED.remove(id);
            NAME_CHANNEL_CACHED.remove(name.toLowerCase());
        }

        public static CharacterIdChannelPair[] multiBuddyFind(Integer charIdFrom, Collection<Integer> characterIds) {
            List<CharacterIdChannelPair> foundsChars = Lists.newArrayListWithCapacity(characterIds.size());
            for (Integer id : characterIds) {
                int channel = Find.findChannel(id);
                if (channel > 0) {
                    foundsChars.add(new CharacterIdChannelPair(id, channel));
                }
            }
            Collections.sort(foundsChars);
            return foundsChars.toArray(new CharacterIdChannelPair[0]);
        }
    }

    public static class Alliance {

        private static final Map<Integer, MapleGuildAlliance> ALLIANCES_CACHED = Maps.newConcurrentMap();

        public static void init() {
            LOGGER.info(">>> 初始化 [家族联盟系统]");
            Stopwatch started = Stopwatch.createStarted();
            for (MapleGuildAlliance alliance : MapleGuildAlliance.loadAll()) {
                ALLIANCES_CACHED.put(alliance.getId(), alliance);
            }
            LOGGER.info("<<< [家族联盟系统] 初始化完毕，耗时：{}", started.stop());
        }

        public static MapleGuildAlliance getAlliance(Integer allianceId) {
            MapleGuildAlliance ret = ALLIANCES_CACHED.get(allianceId);
            if (ret != null) {
                return ret;
            }

            ret = MapleGuildAlliance.findById(allianceId);
            if (ret != null) {
                ALLIANCES_CACHED.put(allianceId, ret);
                return ret;
            }
            return null;
        }

        public static int getAllianceLeader(int allianceid) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                return alliance.getLeaderId();
            }
            return 0;
        }

        public static void updateAllianceRanks(int allianceid, String[] ranks) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                alliance.setRank(ranks);
            }
        }

        public static void updateAllianceNotice(int allianceid, String notice) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                alliance.setNotice(notice);
            }
        }

        public static boolean canInvite(int allianceid) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                return alliance.getCapacity() > alliance.getNoGuilds();
            }
            return false;
        }

        public static boolean changeAllianceLeader(int allianceid, int cid) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                return alliance.setLeaderId(cid);
            }
            return false;
        }

        public static boolean changeAllianceRank(int allianceid, int cid, int change) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                return alliance.changeAllianceRank(cid, change);
            }
            return false;
        }

        public static boolean changeAllianceCapacity(int allianceid) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                return alliance.setCapacity();
            }
            return false;
        }

        public static boolean disbandAlliance(int allianceid) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                return alliance.disband();
            }
            return false;
        }

        public static boolean addGuildToAlliance(int allianceid, int gid) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                return alliance.addGuild(gid);
            }
            return false;
        }

        public static boolean removeGuildFromAlliance(int allianceid, int gid, boolean expelled) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                return alliance.removeGuild(gid, expelled);
            }
            return false;
        }

        public static void sendGuild(int allianceid) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                sendGuild(MaplePacketCreator.getAllianceUpdate(alliance), -1, allianceid);
                sendGuild(MaplePacketCreator.getGuildAlliance(alliance), -1, allianceid);
            }
        }

        public static void sendGuild(MaplePacket packet, int exceptionId, int allianceid) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                for (int i = 0; i < alliance.getNoGuilds(); i++) {
                    int gid = alliance.getGuildId(i);
                    if (gid > 0 && gid != exceptionId) {
                        Guild.guildPacket(gid, packet);
                    }
                }
            }
        }

        public static boolean createAlliance(String alliancename, int cid, int cid2, int gid, int gid2) {
            MapleGuildAlliance alliance = MapleGuildAlliance.createAndSave(cid, alliancename, gid, gid2);
            if (alliance == null) {
                return false;
            }

            MapleGuild guild = Guild.getGuild(gid);
            MapleGuild guild2 = Guild.getGuild(gid2);
            guild.setAllianceId(alliance.getId());
            guild2.setAllianceId(alliance.getId());
            guild.changeARank(true);
            guild2.changeARank(false);

            sendGuild(MaplePacketCreator.createGuildAlliance(alliance), -1, alliance.getId());
            sendGuild(MaplePacketCreator.getAllianceInfo(alliance), -1, alliance.getId());
            sendGuild(MaplePacketCreator.getGuildAlliance(alliance), -1, alliance.getId());
            sendGuild(MaplePacketCreator.changeAlliance(alliance, true), -1, alliance.getId());
            return true;
        }

        public static void allianceChat(int gid, String name, int cid, String msg) {
            MapleGuild guild = Guild.getGuild(gid);
            if (guild != null) {
                MapleGuildAlliance alliance = getAlliance(guild.getAllianceId());
                if (alliance != null) {
                    for (int i = 0; i < alliance.getNoGuilds(); i++) {
                        MapleGuild mapleGuild = Guild.getGuild(alliance.getGuildId(i));
                        if (mapleGuild != null) {
                            mapleGuild.allianceChat(name, cid, msg);
                        }
                    }
                }
            }
        }

        public static void setNewAlliance(int gid, int allianceid) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            MapleGuild guild = Guild.getGuild(gid);
            if (alliance != null && guild != null) {
                for (int i = 0; i < alliance.getNoGuilds(); i++) {
                    if (gid == alliance.getGuildId(i)) {
                        guild.setAllianceId(allianceid);
                        guild.broadcast(MaplePacketCreator.getAllianceInfo(alliance));
                        guild.broadcast(MaplePacketCreator.getGuildAlliance(alliance));
                        guild.broadcast(MaplePacketCreator.changeAlliance(alliance, true));
                        guild.changeARank();
                        guild.writeToDB(false);
                    } else {
                        MapleGuild g_ = Guild.getGuild(alliance.getGuildId(i));
                        if (g_ != null) {
                            g_.broadcast(MaplePacketCreator.addGuildToAlliance(alliance, guild));
                            g_.broadcast(MaplePacketCreator.changeGuildInAlliance(alliance, guild, true));
                        }
                    }
                }
            }
        }

        public static void setOldAlliance(int gid, boolean expelled, int allianceid) {
            MapleGuildAlliance alliance = getAlliance(allianceid);
            MapleGuild g_ = Guild.getGuild(gid);
            if (alliance != null) {
                for (int i = 0; i < alliance.getNoGuilds(); i++) {
                    MapleGuild guild = Guild.getGuild(alliance.getGuildId(i));
                    if (guild == null) {
                        if (gid != alliance.getGuildId(i)) {
                            alliance.removeGuild(gid, false);
                        }
                        continue; //just skip
                    }
                    if (g_ == null || gid == alliance.getGuildId(i)) {
                        guild.changeARank(5);
                        guild.setAllianceId(0);
                        guild.broadcast(MaplePacketCreator.disbandAlliance(allianceid));
                    } else if (g_ != null) {
                        guild.broadcast(MaplePacketCreator.serverNotice(5, "[" + g_.getName() + "] Guild has left the alliance."));
                        guild.broadcast(MaplePacketCreator.changeGuildInAlliance(alliance, g_, false));
                        guild.broadcast(MaplePacketCreator.removeGuildFromAlliance(alliance, g_, expelled));
                    }

                }
            }

            if (gid == -1) {
                ALLIANCES_CACHED.remove(allianceid);
            }
        }

        public static List<MaplePacket> getAllianceInfo(int allianceid, boolean start) {
            List<MaplePacket> ret = new ArrayList<>();
            MapleGuildAlliance alliance = getAlliance(allianceid);
            if (alliance != null) {
                if (start) {
                    ret.add(MaplePacketCreator.getAllianceInfo(alliance));
                    ret.add(MaplePacketCreator.getGuildAlliance(alliance));
                }
                ret.add(MaplePacketCreator.getAllianceUpdate(alliance));
            }
            return ret;
        }

        public static void save() {
            LOGGER.debug("Saving alliances...");
            for (MapleGuildAlliance a : ALLIANCES_CACHED.values()) {
                a.saveToDb();
            }
        }
    }

    public static class Family {

        private static final Map<Integer, MapleFamily> FAMILY_CACHED = Maps.newConcurrentMap();

        public static void init() {
            LOGGER.info(">>> 初始化 [学院系统]");
            Stopwatch started = Stopwatch.createStarted();
            for (MapleFamily family : MapleFamily.loadAll()) {
                if (family.isProper()) {
                    FAMILY_CACHED.put(family.getId(), family);
                }
            }
            LOGGER.info("<<< [学院系统]初始化完毕，耗时：{}", started.stop());
        }

        public static MapleFamily getFamily(Integer id) {
            MapleFamily ret = FAMILY_CACHED.get(id);
            if (ret == null) {
                DFamily one = new QDFamily().id.eq(id).findOne();
                if (one == null) {
                    return null;
                }
                ret = new MapleFamily(one);
                FAMILY_CACHED.put(id, ret);
            }
            return ret;
        }

        public static void memberFamilyUpdate(MapleFamilyCharacter mfc, MapleCharacter mc) {
            MapleFamily f = getFamily(mfc.getFamilyId());
            if (f != null) {
                f.memberLevelJobUpdate(mc);
            }
        }

        public static void setFamilyMemberOnline(MapleFamilyCharacter mfc, boolean bOnline, int channel) {
            MapleFamily f = getFamily(mfc.getFamilyId());
            if (f != null) {
                f.setOnline(mfc.getId(), bOnline, channel);
            }
        }

        public static int setRep(int fid, int cid, int addrep, int oldLevel) {
            MapleFamily f = getFamily(fid);
            if (f != null) {
                return f.setRep(cid, addrep, oldLevel);
            }
            return 0;
        }

        public static void save() {
            LOGGER.debug("Saving families...");
            for (MapleFamily a : FAMILY_CACHED.values()) {
                a.writeToDB(false);
            }
        }

        public static void setFamily(int familyid, int seniorid, int junior1, int junior2, int currentrep, int totalrep, int cid) {
            int ch = Find.findChannel(cid);
            if (ch == -1) {
                // LOGGER.debug("ERROR: cannot find player in given channel");
                return;
            }
            MapleCharacter mc = getStorage(ch).getCharacterById(cid);
            if (mc == null) {
                return;
            }
            boolean bDifferent = mc.getFamilyId() != familyid || mc.getSeniorId() != seniorid || mc.getJunior1() != junior1 || mc.getJunior2() != junior2;
            mc.setFamily(familyid, seniorid, junior1, junior2);
            mc.setCurrentRep(currentrep);
            mc.setTotalRep(totalrep);
            if (bDifferent) {
                mc.saveFamilyStatus();
            }
        }

        public static void familyPacket(int gid, MaplePacket message, int cid) {
            MapleFamily f = getFamily(gid);
            if (f != null) {
                f.broadcast(message, -1, f.getMFC(cid).getPedigree());
            }
        }

        public static void disbandFamily(int gid) {
            MapleFamily g = getFamily(gid);
            if (g != null) {
                g.disbandFamily();
                FAMILY_CACHED.remove(gid);
            }
        }
    }

    public static void registerRespawn() {
        Timer.WORLD.register(new Respawn(), 3000); //divisible by 9000 if possible.
        //3000 good or bad? ive no idea >_>
        //buffs can also be done, but eh

    }

    public static class Respawn implements Runnable { //is putting it here a good idea?

        private int numTimes = 0;

        @Override
        public void run() {
            numTimes++;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleMap map : cserv.getMapFactory().getAllMaps()) { //iterating through each map o_x
                    handleMap(map, numTimes, map.getCharactersSize());
                }
                for (MapleMap map : cserv.getMapFactory().getAllInstanceMaps()) {
                    handleMap(map, numTimes, map.getCharactersSize());
                }
            }
        }
    }

    public static void handleMap(final MapleMap map, final int numTimes, final int size) {
        if (map.getItemsSize() > 0) {
            for (MapleMapItem item : map.getAllItemsThreadsafe()) {
                if (item.shouldExpire()) {
                    item.expire(map);
                } else if (item.shouldFFA()) {
                    item.setDropType((byte) 2);
                }
            }
        }
        if (map.characterSize() > 0) {
            if (map.canSpawn()) {
                map.respawn(false);
            }
            boolean hurt = map.canHurt();
            for (MapleCharacter chr : map.getCharactersThreadsafe()) {
                handleCooldowns(chr, numTimes, hurt);
            }
        }
        if (numTimes % 10 == 0 && (map.getId() == 220080001 && map.playerCount() == 0)) {
            ChannelServer.getInstance(map.getChannel()).getMapFactory().getMap(220080000).resetReactors();
        }
    }

    public static void scheduleRateDelay(final String type, long delay) {
        Timer.WORLD.schedule(new Runnable() {

            @Override
            public void run() {
                final String rate = type;

                if (rate.equals("经验")) {
                    for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                        cservs.setExpRate(1);
                    }
                } else if (rate.equals("爆率")) {
                    for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                        cservs.setDropRate(1);
                    }
                } else if (rate.equals("金币")) {
                    for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                        cservs.setMesoRate(1);
                    }
                } else if (rate.equalsIgnoreCase("boss爆率")) {
                    for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                        cservs.setBossDropRate(1);
                    }
                } else if (rate.equals("宠物经验")) {
//                    for (ChannelServer cservs : ChannelServer.getAllInstances()) {
//                        cservs.setPetExpRate(1);
//                    }
                }
                for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                    cservs.broadcastPacket(MaplePacketCreator.serverNotice(6, " 系统双倍活动已经结束。系统已成功自动切换为正常游戏模式！"));
                }
            }
        }, delay * 1000);
    }

    public static void handleCooldowns(final MapleCharacter chr, final int numTimes, final boolean hurt) { //is putting it here a good idea? expensive?
        final long now = System.currentTimeMillis();
        for (MapleCoolDownValueHolder m : chr.getCooldowns()) {
            if (m.startTime + m.length < now) {
                final int skil = m.skillId;
                chr.removeCooldown(skil);
                chr.getClient().getSession().write(MaplePacketCreator.skillCooldown(skil, 0));
            }
        }
        for (MapleDiseaseValueHolder m : chr.getAllDiseases()) {
            if (m.startTime + m.length < now) {
                chr.dispelDebuff(m.disease);
            }
        }
        if (numTimes % 20 == 0) { //we're parsing through the characters anyway (:
            for (MaplePet pet : chr.getPets()) {
                if (pet.getSummoned()) {
                    if (pet.getPetItemId() == 5000054 && pet.getSecondsLeft() > 0) {
                        pet.setSecondsLeft(pet.getSecondsLeft() - 1);
                        if (pet.getSecondsLeft() <= 0) {
                            chr.unequipPet(pet, true, true);
                            return;
                        }
                    }
                    int newFullness = pet.getFullness() - PetDataFactory.getHunger(pet.getPetItemId());
                    if (newFullness <= 5) {
                        pet.setFullness(15);
                        chr.unequipPet(pet, true, true);
                    } else {
                        pet.setFullness(newFullness);
                        chr.getClient().getSession().write(PetPacket.updatePet(pet, chr.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), true));
                    }
                }
            }
        }
        if (hurt && chr.isAlive()) {
            if (chr.getInventory(MapleInventoryType.EQUIPPED).findById(chr.getMap().getHPDecProtect()) == null) {
                if (chr.getMapId() == 749040100 && chr.getInventory(MapleInventoryType.CASH).findById(5451000) == null) { //minidungeon
                    chr.addHP(-chr.getMap().getHPDec());
                } else if (chr.getMapId() != 749040100) {
                    chr.addHP(-chr.getMap().getHPDec());
                }
            }
        }
    }
}
