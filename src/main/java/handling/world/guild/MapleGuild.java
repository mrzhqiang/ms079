package handling.world.guild;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import com.github.mrzhqiang.maplestory.domain.DBbsThread;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DGuild;
import com.github.mrzhqiang.maplestory.domain.query.QDAlliance;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDGuild;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import handling.MaplePacket;
import handling.world.World;
import handling.world.guild.MapleBBSThread.MapleBBSReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.MaplePacketCreator;
import tools.StringUtil;
import tools.data.output.MaplePacketLittleEndianWriter;
import tools.packet.UIPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;


public final class MapleGuild implements java.io.Serializable {

    private static final long serialVersionUID = 6322150443228168192L;

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleGuild.class);

    private enum BCOP {
        NONE, DISBAND, EMBLEM_CHANGE
    }

    private static final MapleGuild EMPTY = new MapleGuild(null);

    // COW 写入时复制，避免多线程环境下，遍历时的修改导致并发异常或下标越界；
    // 其他线程安全的 List 需要在遍历时手动加锁，COW 不需要；
    // 但是遍历的同时，其他线程修改了 COW，则会产生新的数组，占据内存消耗，遍历的还是旧的数组。
    private final List<MapleGuildCharacter> members = Lists.newCopyOnWriteArrayList();

    private final String[] rankTitles = new String[5]; // 1 = 主, 2 = 小弟, 5 = 最低的成员
    private boolean bDirty = true;
    private int invitedid = 0;
    private final Map<Integer, MapleBBSThread> bbs = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock rL = lock.readLock(), wL = lock.writeLock();
    private boolean init = false;

    private final DGuild guild;
    private boolean proper = true;

    public MapleGuild(DGuild guild) {
        this.guild = guild;
        if (guild != null) {
            List<DCharacter> members = guild.getMembers();
            if (members == null || members.isEmpty()) {
                LOGGER.error("公会 {} 不存在成员，这是不可能的，除非公会解散了", guild.getName());
                proper = false;
                return;
            }
            boolean leaderCheck = false;
            for (DCharacter data : members) {
                if (data.equals(guild.getLeader())) {
                    leaderCheck = true;
                }
                this.members.add(new MapleGuildCharacter(data, -1, false));
            }
            if (!leaderCheck) {
                LOGGER.error("会长 " + guild.getLeader() + " 不在公会 " + guild.getId() + ". 不可能……公会解散了。");
                //  writeToDB(true);
                proper = false;
                return;
            }
            List<DBbsThread> threads = guild.getThreads();
            for (DBbsThread data : threads) {
                MapleBBSThread thread = new MapleBBSThread(data);
                bbs.put(data.getLocalThreadId(), thread);
            }
        }
    }

    public static List<MapleGuild> loadAll() {
        return new QDGuild().findStream().map(MapleGuild::new).collect(Collectors.toList());
    }

    public static MapleGuild empty() {
        return EMPTY;
    }

    public boolean isValid() {
        return guild != null && proper;
    }

    public void writeToDB(boolean bDisband) {
        if (!bDisband) {
            guild.setThreads(Collections.emptyList());
            guild.setReplies(Collections.emptyList());
            guild.save();
            for (MapleBBSThread bb : bbs.values()) {
                bb.entity.save();
                bb.replies.forEach((integer, mapleBBSReply) -> mapleBBSReply.reply.save());
            }
        } else {
            guild.getMembers().forEach(dCharacter -> {
                dCharacter.setGuildRank(5);
                dCharacter.setAllianceRank(5);
                dCharacter.save();
            });
            guild.setMembers(Collections.emptyList());
            guild.setThreads(Collections.emptyList());
            guild.setReplies(Collections.emptyList());

            MapleGuildAlliance alliance = World.Alliance.getAlliance(guild.getAlliance().getId());
            if (alliance != null) {
                alliance.removeGuild(guild.getId(), false);
            }

            guild.delete();

            broadcast(MaplePacketCreator.guildDisband(guild.getId()));
        }
    }

    public int getId() {
        return guild.getId();
    }

    public int getLeaderId() {
        return guild.getLeader().getId();
    }

    public MapleCharacter getLeader(MapleClient c) {
        return c.getChannelServer().getPlayerStorage().getCharacterById(guild.getLeader().getId());
    }

    public int getGP() {
        return guild.getGp();
    }

    public int getLogo() {
        return guild.getLogo();
    }

    public void setLogo(int l) {
        guild.setLogo(l);
    }

    public int getLogoColor() {
        return guild.getLogoColor();
    }

    public void setLogoColor(int c) {
        guild.setLogoColor(c);
    }

    public int getLogoBG() {
        return guild.getLogoBg();
    }

    public void setLogoBG(int bg) {
        guild.setLogoBg(bg);
    }

    public int getLogoBGColor() {
        return guild.getLogoBgColor();
    }

    public void setLogoBGColor(int c) {
        guild.setLogoBgColor(c);
    }

    public String getNotice() {
        return Strings.nullToEmpty(guild.getNotice());
    }

    public String getName() {
        return guild.getName();
    }

    public int getCapacity() {
        return guild.getCapacity();
    }

    public int getSignature() {
        return guild.getSignature();
    }

    public void broadcast(MaplePacket packet) {
        broadcast(packet, -1, BCOP.NONE);
    }

    public void broadcast(MaplePacket packet, int exception) {
        broadcast(packet, exception, BCOP.NONE);
    }

    // multi-purpose function that reaches every member of guild (except the character with exceptionId) in all channels with as little access to rmi as possible
    public void broadcast(MaplePacket packet, int exceptionId, BCOP bcop) {
        wL.lock();
        try {
            buildNotifications();
        } finally {
            wL.unlock();
        }

        rL.lock();
        try {
            for (MapleGuildCharacter mgc : members) {
                if (bcop == BCOP.DISBAND) {
                    if (mgc.isOnline()) {
                        World.Guild.setGuildAndRank(mgc.character.getId(), 0, 5, 5);
                    } else {
                        setOfflineGuildStatus(0, (byte) 5, (byte) 5, mgc.character.getId());
                    }
                } else if (mgc.isOnline() && mgc.character.getId() != exceptionId) {
                    if (bcop == BCOP.EMBLEM_CHANGE) {
                        World.Guild.changeEmblem(guild.getId(), mgc.character.getId(), new MapleGuildSummary(this));
                    } else {
                        World.Broadcast.sendGuildPacket(mgc.character.getId(), packet, exceptionId, guild.getId());
                    }
                }
            }
        } finally {
            rL.unlock();
        }

    }

    private void buildNotifications() {
        if (!bDirty) {
            return;
        }
        List<DCharacter> mem = new LinkedList<>();
        for (MapleGuildCharacter mgc : members) {
            if (!mgc.isOnline()) {
                continue;
            }
            if (mem.contains(mgc.character) || !mgc.character.getGuild().equals(guild)) {
                members.remove(mgc);
                continue;
            }
            mem.add(mgc.character);
        }
        bDirty = false;
    }

    public void updateMemberOnline(int cid, boolean online, int channel) {
        boolean bBroadcast = true;
        for (MapleGuildCharacter mgc : members) {
            if (mgc.character.getGuild().equals(guild) && mgc.character.getId() == cid) {
                if (mgc.isOnline() == online) {
                    bBroadcast = false;
                }
                mgc.setOnline(online);
                mgc.setChannel((byte) channel);
                break;
            }
        }
        if (bBroadcast) {
            broadcast(MaplePacketCreator.guildMemberOnline(guild.getId(), cid, online), cid);
            if (guild.getAlliance().getId() > 0) {
                World.Alliance.sendGuild(MaplePacketCreator.allianceMemberOnline(guild.getAlliance().getId(), guild.getId(), cid, online), guild.getId(), guild.getAlliance().getId());
            }
        }
        bDirty = true; // member formation has changed, update notifications
        init = true;
    }

    public void chat(String name, int cid, String msg) {
        broadcast(MaplePacketCreator.multiChat(name, msg, 2), cid);
    }

    public void allianceChat(String name, int cid, String msg) {
        broadcast(MaplePacketCreator.multiChat(name, msg, 3), cid);
    }

    public String getRankTitle(int rank) {
        return rankTitles[rank - 1];
    }

    public int getAllianceId() {
        //return alliance.getId();
        return guild.getAlliance().getId();
    }

    public int getInvitedId() {
        return this.invitedid;
    }

    public void setInvitedId(int iid) {
        this.invitedid = iid;
    }

    public void setAllianceId(int a) {
        guild.setAlliance(new QDAlliance().id.eq(a).findOne());
        guild.save();
    }

    // 创建公会的函数，如果成功则返回公会ID，否则返回0
    public static int createGuild(int leaderId, String name) {
        if (name == null || name.length() > 12) {
            return 0;
        }
        Optional<DGuild> oneOrEmpty = new QDGuild().name.eq(name).findOneOrEmpty();
        if (oneOrEmpty.isPresent()) {
            return oneOrEmpty.get().getId();
        }

        DGuild guild = new DGuild(new QDCharacter().id.eq(leaderId).findOne(), name);
        guild.setSignature(Math.toIntExact(System.currentTimeMillis() / 1000));
        guild.save();
        return guild.getId();
    }

    public boolean addMember(MapleGuildCharacter mgc) {
        // first of all, insert it into the members keeping alphabetical order of lowest ranks ;)
        wL.lock();
        try {
            if (members.size() >= guild.getCapacity()) {
                return false;
            }
            for (int i = members.size() - 1; i >= 0; i--) {
                if (members.get(i).character.getGuildRank() < 5 || members.get(i).character.getName().compareTo(mgc.character.getName()) < 0) {
                    members.add(i + 1, mgc);
                    bDirty = true;
                    break;
                }
            }
        } finally {
            wL.unlock();
        }
        gainGP(50);
        broadcast(MaplePacketCreator.newGuildMember(mgc));
        if (guild.getAlliance().getId() > 0) {
            World.Alliance.sendGuild(guild.getAlliance().getId());
        }
        return true;
    }

    public void memberLeave(MapleGuildCharacter mgc) {
        broadcast(MaplePacketCreator.memberLeft(mgc, false));
        gainGP(-50);
        wL.lock();
        try {
            bDirty = true;
            members.remove(mgc);
            if (mgc.isOnline()) {
                World.Guild.setGuildAndRank(mgc.character.getId(), 0, 5, 5);
            } else {
                setOfflineGuildStatus((short) 0, (byte) 5, (byte) 5, mgc.character.getId());
            }
            if (guild.getAlliance().getId() > 0) {
                World.Alliance.sendGuild(guild.getAlliance().getId());
            }
        } finally {
            wL.unlock();
        }
    }

    public void removeMember(MapleGuildCharacter initiator, String name, int cid) {
        wL.lock();
        try {
            for (MapleGuildCharacter mgc : members) {
                if (mgc.character.getId() == cid && initiator.character.getGuildRank() < mgc.character.getGuildRank()) {
                    broadcast(MaplePacketCreator.memberLeft(mgc, true));

                    bDirty = true;

                    gainGP(-50);
                    if (guild.getAlliance().getId() > 0) {
                        World.Alliance.sendGuild(guild.getAlliance().getId());
                    }
                    if (mgc.isOnline()) {
                        World.Guild.setGuildAndRank(cid, 0, 5, 5);
                    } else {
                        MapleCharacterUtil.sendNote(mgc.character.getName(), initiator.character.getName(), "You have been expelled from the guild.", 0);
                        setOfflineGuildStatus((short) 0, (byte) 5, (byte) 5, cid);
                    }
                    members.remove(mgc);
                    break;
                }
            }
        } finally {
            wL.unlock();
        }
    }

    public void changeARank() {
        changeARank(false);
    }

    public void changeARank(boolean leader) {
        for (MapleGuildCharacter mgc : members) {
            if (guild.getLeader().equals(mgc.character)) {
                changeARank(mgc.character.getId(), leader ? 1 : 2);
            } else {
                changeARank(mgc.character.getId(), 3);
            }
        }
    }

    public void changeARank(int newRank) {
        for (MapleGuildCharacter mgc : members) {
            changeARank(mgc.character.getId(), newRank);
        }
    }

    public void changeARank(int cid, int newRank) {
        if (guild.getAlliance().getId() <= 0) {
            return;
        }
        for (MapleGuildCharacter mgc : members) {
            if (cid == mgc.character.getId()) {
                if (mgc.isOnline()) {
                    World.Guild.setGuildAndRank(cid, guild.getId(), mgc.character.getGuildRank(), newRank);
                } else {
                    setOfflineGuildStatus(guild.getId(), mgc.character.getGuildRank(), (byte) newRank, cid);
                }
                mgc.character.setAllianceRank(newRank);
                //WorldRegistryImpl.getInstance().sendGuild(MaplePacketCreator.changeAllianceRank(allianceid, mgc), -1, allianceid);
                //WorldRegistryImpl.getInstance().sendGuild(MaplePacketCreator.updateAllianceRank(allianceid, mgc), -1, allianceid);
                World.Alliance.sendGuild(guild.getAlliance().getId());
                return;
            }
        }
        // it should never get to this point unless cid was incorrect o_O
        LOGGER.error("INFO: unable to find the correct id for changeRank({}, {})", cid, newRank);
    }

    public void changeRank(int cid, int newRank) {
        for (MapleGuildCharacter mgc : members) {
            if (cid == mgc.character.getId()) {
                if (mgc.isOnline()) {
                    World.Guild.setGuildAndRank(cid, guild.getId(), newRank, mgc.character.getAllianceRank());
                } else {
                    setOfflineGuildStatus(guild.getId(), (byte) newRank, mgc.character.getAllianceRank(), cid);
                }
                mgc.character.setGuildRank(newRank);
                broadcast(MaplePacketCreator.changeRank(mgc));
                return;
            }
        }
        // it should never get to this point unless cid was incorrect o_O
        LOGGER.error("INFO: unable to find the correct id for changeRank({}, {})", cid, newRank);
    }

    public void notice(String notice) {
        guild.setNotice(notice);
        broadcast(MaplePacketCreator.guildNotice(guild.getId(), notice));
    }

    public void updateMemberInfo(MapleGuildCharacter mgc) {
        for (MapleGuildCharacter member : members) {
            if (member.character.equals(mgc.character)) {
                int old_level = member.character.getLevel();
                int old_job = member.character.getJob();
                member.character.setJob(mgc.character.getJob());
                member.character.setLevel(mgc.character.getLevel());
                if (mgc.character.getLevel() > old_level) {
                    gainGP((mgc.character.getLevel() - old_level) * mgc.character.getLevel() / 10, false); //level 199->200 = 20 gp
                }
                if (old_level != mgc.character.getLevel()) {
                    // this.broadcast(MaplePacketCreator.sendLevelup(false, mgc.character.level, mgc.getName()), mgc.getId());
                }
                if (old_job != mgc.character.getJob()) {
                    //  this.broadcast(MaplePacketCreator.sendJobup(false, mgc.getJobId(), mgc.getName()), mgc.getId());
                }
                broadcast(MaplePacketCreator.guildMemberLevelJobUpdate(mgc));
                if (guild.getAlliance()!= null) {
                    World.Alliance.sendGuild(MaplePacketCreator.updateAlliance(mgc, guild.getAlliance().getId()), guild.getId(), guild.getAlliance().getId());
                }
                break;
            }
        }
    }

    public void changeRankTitle(String[] ranks) {
        System.arraycopy(ranks, 0, rankTitles, 0, 5);
        broadcast(MaplePacketCreator.rankTitleChange(guild.getId(), ranks));
    }

    public void disbandGuild() {
        writeToDB(true);
        broadcast(null, -1, BCOP.DISBAND);
    }

    public void setEmblem(int bg, int bgcolor, int logo, int logocolor) {
        broadcast(null, -1, BCOP.EMBLEM_CHANGE);

        if (guild != null) {
            guild.setLogo(logo);
            guild.setLogoColor(logocolor);
            guild.setLogoBg(bg);
            guild.setLogoBgColor(bgcolor);
            guild.save();
        }
    }

    public MapleGuildCharacter getMGC(int cid) {
        for (MapleGuildCharacter mgc : members) {
            if (mgc.character.getId() == cid) {
                return mgc;
            }
        }
        return null;
    }

    public boolean increaseCapacity() {
        if (guild.getCapacity() >= 100 || ((guild.getCapacity() + 5) > 100)) {
            return false;
        }
        guild.setCapacity(guild.getCapacity() + 5);
        broadcast(MaplePacketCreator.guildCapacityChange(this.guild.getId(), this.guild.getCapacity()));
        guild.save();
        return true;
    }

    public void gainGP(int amount) {
        gainGP(amount, true);
    }

    public void gainGP(int amount, boolean broadcast) {
        if (amount == 0) { //no change, no broadcast and no sql.
            return;
        }
        if (amount + guild.getGp() < 0) {
            amount = -guild.getGp();
        } //0 lowest
        guild.setGp(guild.getGp() + amount);
        broadcast(MaplePacketCreator.updateGP(guild.getId(), guild.getGp()));
        if (broadcast) {
            broadcast(UIPacket.getGPMsg(amount));
        }
        //  writeGPToDB();
    }

    public void addMemberData(MaplePacketLittleEndianWriter mplew) {
        mplew.write(members.size());

        for (MapleGuildCharacter mgc : members) {
            mplew.writeInt(mgc.character.getId());
        }
        for (MapleGuildCharacter mgc : members) {
            mplew.writeAsciiString(StringUtil.getRightPaddedStr(mgc.character.getName(), '\0', 13));
            mplew.writeInt(mgc.character.getJob());
            mplew.writeInt(mgc.character.getLevel());
            mplew.writeInt(mgc.character.getGuildRank());
            mplew.writeInt(mgc.isOnline() ? 1 : 0);
            mplew.writeInt(guild.getSignature());
            mplew.writeInt(mgc.character.getAllianceRank());
        }
    }

    // null indicates successful invitation being sent
    // keep in mind that this will be called by a handler most of the time
    // so this will be running mostly on a channel server, unlike the rest
    // of the class
    public static MapleGuildResponse sendInvite(MapleClient c, String targetName) {
        MapleCharacter mc = c.getChannelServer().getPlayerStorage().getCharacterByName(targetName);
        if (mc == null) {
            return MapleGuildResponse.NOT_IN_CHANNEL;
        }
        if (mc.character.getGuild().getId() > 0) {
            return MapleGuildResponse.ALREADY_IN_GUILD;
        }
        mc.getClient().getSession().write(MaplePacketCreator.guildInvite(c.getPlayer().character.getGuild().getId(),
                c.getPlayer().character.getName(), c.getPlayer().character.getLevel(), c.getPlayer().getJob()));
        return null;
    }

    public java.util.Collection<MapleGuildCharacter> getMembers() {
        return java.util.Collections.unmodifiableCollection(members);
    }

    public boolean isInit() {
        return init;
    }

    public List<MapleBBSThread> getBBS() {
        List<MapleBBSThread> ret = new ArrayList<>(bbs.values());
        ret.sort(new MapleBBSThread.ThreadComparator());
        return ret;
    }

    public int addBBSThread(String title, String text, int icon, boolean bNotice, int posterID) {
        int add = bbs.get(0) == null ? 1 : 0; //add 1 if no notice
        int ret = bNotice ? 0 : Math.max(1, bbs.size() + add);
        bbs.put(ret, new MapleBBSThread(ret, title, text, System.currentTimeMillis(), guild.getId(), posterID, icon));
        return ret;
    }

    public void editBBSThread(int localthreadid, String title, String text, int icon, int posterID, int guildRank) {
        MapleBBSThread thread = bbs.get(localthreadid);
        if (thread != null && (thread.ownerID == posterID || guildRank <= 2)) {
            bbs.put(localthreadid, new MapleBBSThread(localthreadid, title, text, System.currentTimeMillis(), guild.getId(), thread.ownerID, icon));
        }
    }

    public void deleteBBSThread(int localthreadid, int posterID, int guildRank) {
        MapleBBSThread thread = bbs.get(localthreadid);
        if (thread != null && (thread.ownerID == posterID || guildRank <= 2)) {
            bbs.remove(localthreadid);
        }
    }

    public void addBBSReply(int localthreadid, String text, int posterID) {
        MapleBBSThread thread = bbs.get(localthreadid);
        if (thread != null) {
            thread.replies.put(thread.replies.size(), new MapleBBSReply(thread.replies.size(), posterID, text, System.currentTimeMillis()));
        }
    }

    public void deleteBBSReply(int localthreadid, int replyid, int posterID, int guildRank) {
        MapleBBSThread thread = bbs.get(localthreadid);
        if (thread != null) {
            MapleBBSReply reply = thread.replies.get(replyid);
            if (reply != null && (reply.reply.getPoster().getId() == posterID || guildRank <= 2)) {
                thread.replies.remove(replyid);
            }
        }
    }

    public static void setOfflineGuildStatus(int guildid, int guildrank, int alliancerank, int cid) {
        DCharacter one = new QDCharacter().id.eq(cid).findOne();
        if (one != null) {
            one.setGuild(new QDGuild().id.eq(guildid).findOne());
            one.setGuildRank(guildrank);
            one.setAllianceRank(alliancerank);
            one.save();
        }
    }

    public int getPrefix(MapleCharacter chr) {
        return chr.getPrefix();
    }

    public static void levelRank(MapleClient c, int npcid) {
        List<DCharacter> characters = new QDCharacter().orderBy().level.desc().setMaxRows(100).findList();
        c.getSession().write(MaplePacketCreator.levelRank(npcid, characters));
    }

    public static void fameRank(MapleClient c, int npcid) {
        List<DCharacter> characters = new QDCharacter().orderBy().fame.desc().setMaxRows(100).findList();
        c.getSession().write(MaplePacketCreator.fameRank(npcid, characters));
    }

    public static void mesoRank(MapleClient c, int npcid) {
        List<DCharacter> characters = new QDCharacter().orderBy().meso.desc().setMaxRows(100).findList();
        c.getSession().write(MaplePacketCreator.mesoRank(npcid, characters));
    }

    public static void sgRank(MapleClient c, int npcid) {
        List<DCharacter> characters = new QDCharacter().orderBy().sg.desc().setMaxRows(100).findList();
        c.getSession().write(MaplePacketCreator.sgRank(npcid, characters));
    }

    public static void gpRank(MapleClient c, int npcid) {
        List<DGuild> guilds = new QDGuild().orderBy().gp.desc().setMaxRows(50).findList();
        c.getSession().write(MaplePacketCreator.gpRank(npcid, guilds));
    }

}
