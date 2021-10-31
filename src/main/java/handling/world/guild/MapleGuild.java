package handling.world.guild;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import com.github.mrzhqiang.maplestory.domain.DBbsThread;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DGuild;
import com.github.mrzhqiang.maplestory.domain.query.QDBbsThread;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDGuild;
import handling.MaplePacket;
import handling.world.World;
import handling.world.guild.MapleBBSThread.MapleBBSReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.MaplePacketCreator;
import tools.StringUtil;
import tools.data.output.MaplePacketLittleEndianWriter;
import tools.packet.UIPacket;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public final class MapleGuild implements java.io.Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleGuild.class);

    private enum BCOp {
        NONE, DISBAND, EMBELMCHANGE
    }

    public static final long serialVersionUID = 6322150443228168192L;

    // COW 写入时复制，避免多线程环境下，遍历时的修改导致并发异常或下标越界；
    // 其他线程安全的 List 需要在遍历时手动加锁，COW 不需要；
    // 但是遍历的同时，其他线程修改了 COW，则会产生新的数组，占据内存消耗，遍历的还是旧的数组。
    private final List<MapleGuildCharacter> members = new CopyOnWriteArrayList<>();

    private final String[] rankTitles = new String[5]; // 1 = master, 2 = jr, 5 = the lowest member
    private boolean bDirty = true, proper = true;
    private int allianceid = 0, invitedid = 0;
    private final Map<Integer, MapleBBSThread> bbs = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock rL = lock.readLock(), wL = lock.writeLock();
    private boolean init = false;

    private final DGuild guild;

    public MapleGuild(DGuild guild) {
        this.guild = guild;
        List<DCharacter> datas = new QDCharacter()
                .guild.eq(guild)
                .orderBy()
                .guildRank.asc()
                .name.asc()
                .findList();
        if (datas.isEmpty()) {
            LOGGER.error("No members in guild " + guild.id + ".  Impossible... guild is disbanding");
            proper = false;
            return;
        }
        boolean leaderCheck = false;
        for (DCharacter data : datas) {
            if (data.equals(guild.leader)) {
                leaderCheck = true;
            }
            members.add(new MapleGuildCharacter(data, -1, false));
        }

        if (!leaderCheck) {
            LOGGER.error("Leader " + guild.leader + " isn't in guild " + guild.id + ".  Impossible... guild is disbanding.");
            //  writeToDB(true);
            proper = false;
            return;
        }

        List<DBbsThread> threads = new QDBbsThread().guild.eq(guild).orderBy().localthreadid.desc().findList();
        for (DBbsThread data : threads) {
            MapleBBSThread thread = new MapleBBSThread(data);
            bbs.put(thread.localthreadID, thread);
        }
    }

    public boolean isProper() {
        return proper;
    }

    public static List<MapleGuild> loadAll() {
        return new QDGuild().findStream().map(MapleGuild::new).collect(Collectors.toList());
    }

    public void writeToDB(boolean bDisband) {
        if (!bDisband) {
            guild.threads = Collections.emptyList();
            guild.replies = Collections.emptyList();
            guild.save();
            for (MapleBBSThread bb : bbs.values()) {
                bb.thread.save();
                bb.replies.forEach((integer, mapleBBSReply) -> mapleBBSReply.reply.save());
            }
        } else {
            guild.characters.forEach(dCharacter -> {
                dCharacter.guildRank = 5;
                dCharacter.allianceRank = 5;
                dCharacter.save();
            });
            guild.characters = Collections.emptyList();
            guild.threads = Collections.emptyList();
            guild.replies = Collections.emptyList();

            MapleGuildAlliance alliance = World.Alliance.getAlliance(guild.alliance);
            if (alliance != null) {
                alliance.removeGuild(guild.id, false);
            }

            guild.delete();

            broadcast(MaplePacketCreator.guildDisband(guild.id));
        }
    }

    public int getId() {
        return guild.id;
    }

    public int getLeaderId() {
        return guild.leader.id;
    }

    public MapleCharacter getLeader(MapleClient c) {
        return c.getChannelServer().getPlayerStorage().getCharacterById(guild.leader.id);
    }

    public int getGP() {
        return guild.GP;
    }

    public int getLogo() {
        return guild.logo;
    }

    public void setLogo(int l) {
        guild.logo = l;
    }

    public int getLogoColor() {
        return guild.logoColor;
    }

    public void setLogoColor(int c) {
        guild.logoColor = c;
    }

    public int getLogoBG() {
        return guild.logoBG;
    }

    public void setLogoBG(int bg) {
        guild.logoBG = bg;
    }

    public int getLogoBGColor() {
        return guild.logoBGColor;
    }

    public void setLogoBGColor(int c) {
        guild.logoBGColor = c;
    }

    public String getNotice() {
        if (guild.notice == null) {
            return "";
        }
        return guild.notice;
    }

    public String getName() {
        return guild.name;
    }

    public int getCapacity() {
        return guild.capacity;
    }

    public int getSignature() {
        return guild.signature;
    }

    public void broadcast(MaplePacket packet) {
        broadcast(packet, -1, BCOp.NONE);
    }

    public void broadcast(MaplePacket packet, int exception) {
        broadcast(packet, exception, BCOp.NONE);
    }

    // multi-purpose function that reaches every member of guild (except the character with exceptionId) in all channels with as little access to rmi as possible
    public void broadcast(MaplePacket packet, int exceptionId, BCOp bcop) {
        wL.lock();
        try {
            buildNotifications();
        } finally {
            wL.unlock();
        }

        rL.lock();
        try {
            for (MapleGuildCharacter mgc : members) {
                if (bcop == BCOp.DISBAND) {
                    if (mgc.isOnline()) {
                        World.Guild.setGuildAndRank(mgc.character.id, 0, 5, 5);
                    } else {
                        setOfflineGuildStatus(0, (byte) 5, (byte) 5, mgc.character.id);
                    }
                } else if (mgc.isOnline() && mgc.character.id != exceptionId) {
                    if (bcop == BCOp.EMBELMCHANGE) {
                        World.Guild.changeEmblem(guild.id, mgc.character.id, new MapleGuildSummary(this));
                    } else {
                        World.Broadcast.sendGuildPacket(mgc.character.id, packet, exceptionId, guild.id);
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
            if (mem.contains(mgc.character) || !mgc.character.guild.equals(guild)) {
                members.remove(mgc);
                continue;
            }
            mem.add(mgc.character);
        }
        bDirty = false;
    }

    public void setOnline(int cid, boolean online, int channel) {
        boolean bBroadcast = true;
        for (MapleGuildCharacter mgc : members) {
            if (mgc.character.guild.equals(guild) && mgc.character.id == cid) {
                if (mgc.isOnline() == online) {
                    bBroadcast = false;
                }
                mgc.setOnline(online);
                mgc.setChannel((byte) channel);
                break;
            }
        }
        if (bBroadcast) {
            broadcast(MaplePacketCreator.guildMemberOnline(guild.id, cid, online), cid);
            if (allianceid > 0) {
                World.Alliance.sendGuild(MaplePacketCreator.allianceMemberOnline(allianceid, guild.id, cid, online), guild.id, allianceid);
            }
        }
        bDirty = true; // member formation has changed, update notifications
        init = true;
    }

    public void guildChat(String name, int cid, String msg) {
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
        return guild.alliance;
    }

    public int getInvitedId() {
        return this.invitedid;
    }

    public void setInvitedId(int iid) {
        this.invitedid = iid;
    }

    public void setAllianceId(int a) {
        guild.alliance = a;
        guild.save();
    }

    // function to create guild, returns the guild id if successful, 0 if not
    public static int createGuild(int leaderId, String name) {
        if (name == null || name.length() > 12) {
            return 0;
        }
        Optional<DGuild> oneOrEmpty = new QDGuild().name.eq(name).findOneOrEmpty();
        if (oneOrEmpty.isPresent()) {
            return oneOrEmpty.get().id;
        }

        DGuild guild = new DGuild();
        guild.name = name;
        guild.leader = new QDCharacter().id.eq(leaderId).findOne();
        guild.signature = Math.toIntExact(System.currentTimeMillis() / 1000);
        guild.save();
        return guild.id;
    }

    public int addGuildMember(MapleGuildCharacter mgc) {
        // first of all, insert it into the members keeping alphabetical order of lowest ranks ;)
        wL.lock();
        try {
            if (members.size() >= guild.capacity) {
                return 0;
            }
            for (int i = members.size() - 1; i >= 0; i--) {
                if (members.get(i).character.guildRank < 5 || members.get(i).character.name.compareTo(mgc.character.name) < 0) {
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
        if (allianceid > 0) {
            World.Alliance.sendGuild(allianceid);
        }
        return 1;
    }

    public void leaveGuild(MapleGuildCharacter mgc) {
        broadcast(MaplePacketCreator.memberLeft(mgc, false));
        gainGP(-50);
        wL.lock();
        try {
            bDirty = true;
            members.remove(mgc);
            if (mgc.isOnline()) {
                World.Guild.setGuildAndRank(mgc.character.id, 0, 5, 5);
            } else {
                setOfflineGuildStatus((short) 0, (byte) 5, (byte) 5, mgc.character.id);
            }
            if (allianceid > 0) {
                World.Alliance.sendGuild(allianceid);
            }
        } finally {
            wL.unlock();
        }
    }

    public void expelMember(MapleGuildCharacter initiator, String name, int cid) {
        wL.lock();
        try {
            for (MapleGuildCharacter mgc : members) {
                if (mgc.character.id == cid && initiator.character.guildRank < mgc.character.guildRank) {
                    broadcast(MaplePacketCreator.memberLeft(mgc, true));

                    bDirty = true;

                    gainGP(-50);
                    if (allianceid > 0) {
                        World.Alliance.sendGuild(allianceid);
                    }
                    if (mgc.isOnline()) {
                        World.Guild.setGuildAndRank(cid, 0, 5, 5);
                    } else {
                        MapleCharacterUtil.sendNote(mgc.character.name, initiator.character.name, "You have been expelled from the guild.", 0);
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
            if (guild.leader.equals(mgc.character)) {
                changeARank(mgc.character.id, leader ? 1 : 2);
            } else {
                changeARank(mgc.character.id, 3);
            }
        }
    }

    public void changeARank(int newRank) {
        for (MapleGuildCharacter mgc : members) {
            changeARank(mgc.character.id, newRank);
        }
    }

    public void changeARank(int cid, int newRank) {
        if (allianceid <= 0) {
            return;
        }
        for (MapleGuildCharacter mgc : members) {
            if (cid == mgc.character.id) {
                if (mgc.isOnline()) {
                    World.Guild.setGuildAndRank(cid, guild.id, mgc.character.guildRank, newRank);
                } else {
                    setOfflineGuildStatus(guild.id, mgc.character.guildRank, (byte) newRank, cid);
                }
                mgc.character.allianceRank = newRank;
                //WorldRegistryImpl.getInstance().sendGuild(MaplePacketCreator.changeAllianceRank(allianceid, mgc), -1, allianceid);
                //WorldRegistryImpl.getInstance().sendGuild(MaplePacketCreator.updateAllianceRank(allianceid, mgc), -1, allianceid);
                World.Alliance.sendGuild(allianceid);
                return;
            }
        }
        // it should never get to this point unless cid was incorrect o_O
        LOGGER.error("INFO: unable to find the correct id for changeRank({" + cid + "}, {" + newRank + "})");
    }

    public void changeRank(int cid, int newRank) {
        for (MapleGuildCharacter mgc : members) {
            if (cid == mgc.character.id) {
                if (mgc.isOnline()) {
                    World.Guild.setGuildAndRank(cid, guild.id, newRank, mgc.character.allianceRank);
                } else {
                    setOfflineGuildStatus(guild.id, (byte) newRank, mgc.character.allianceRank, cid);
                }
                mgc.character.guildRank = newRank;
                broadcast(MaplePacketCreator.changeRank(mgc));
                return;
            }
        }
        // it should never get to this point unless cid was incorrect o_O
        LOGGER.error("INFO: unable to find the correct id for changeRank({" + cid + "}, {" + newRank + "})");
    }

    public void setGuildNotice(String notice) {
        guild.notice = notice;
        broadcast(MaplePacketCreator.guildNotice(guild.id, notice));
    }

    public void memberLevelJobUpdate(MapleGuildCharacter mgc) {
        for (MapleGuildCharacter member : members) {
            if (member.character.equals(mgc.character)) {
                int old_level = member.character.level;
                int old_job = member.character.job;
                member.character.job = (mgc.character.job);
                member.character.level = mgc.character.level;
                if (mgc.character.level > old_level) {
                    gainGP((mgc.character.level - old_level) * mgc.character.level / 10, false); //level 199->200 = 20 gp
                }
                if (old_level != mgc.character.level) {
                    // this.broadcast(MaplePacketCreator.sendLevelup(false, mgc.character.level, mgc.getName()), mgc.getId());
                }
                if (old_job != mgc.character.job) {
                    //  this.broadcast(MaplePacketCreator.sendJobup(false, mgc.getJobId(), mgc.getName()), mgc.getId());
                }
                broadcast(MaplePacketCreator.guildMemberLevelJobUpdate(mgc));
                if (allianceid > 0) {
                    World.Alliance.sendGuild(MaplePacketCreator.updateAlliance(mgc, allianceid), guild.id, allianceid);
                }
                break;
            }
        }
    }

    public void changeRankTitle(String[] ranks) {
        System.arraycopy(ranks, 0, rankTitles, 0, 5);
        broadcast(MaplePacketCreator.rankTitleChange(guild.id, ranks));
    }

    public void disbandGuild() {
        writeToDB(true);
        broadcast(null, -1, BCOp.DISBAND);
    }

    public void setGuildEmblem(int bg, int bgcolor, int logo, int logocolor) {
        broadcast(null, -1, BCOp.EMBELMCHANGE);

        if (guild != null) {
            guild.logo = logo;
            guild.logoColor = logocolor;
            guild.logoBG = bg;
            guild.logoBGColor = bgcolor;
            guild.save();
        }
    }

    public MapleGuildCharacter getMGC(int cid) {
        for (MapleGuildCharacter mgc : members) {
            if (mgc.character.id == cid) {
                return mgc;
            }
        }
        return null;
    }

    public boolean increaseCapacity() {
        if (guild.capacity >= 100 || ((guild.capacity + 5) > 100)) {
            return false;
        }
        guild.capacity += 5;
        broadcast(MaplePacketCreator.guildCapacityChange(this.guild.id, this.guild.capacity));
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
        if (amount + guild.GP < 0) {
            amount = -guild.GP;
        } //0 lowest
        guild.GP += amount;
        broadcast(MaplePacketCreator.updateGP(guild.id, guild.GP));
        if (broadcast) {
            broadcast(UIPacket.getGPMsg(amount));
        }
        //  writeGPToDB();
    }

    public void addMemberData(MaplePacketLittleEndianWriter mplew) {
        mplew.write(members.size());

        for (MapleGuildCharacter mgc : members) {
            mplew.writeInt(mgc.character.id);
        }
        for (MapleGuildCharacter mgc : members) {
            mplew.writeAsciiString(StringUtil.getRightPaddedStr(mgc.character.name, '\0', 13));
            mplew.writeInt(mgc.character.job);
            mplew.writeInt(mgc.character.level);
            mplew.writeInt(mgc.character.guildRank);
            mplew.writeInt(mgc.isOnline() ? 1 : 0);
            mplew.writeInt(guild.signature);
            mplew.writeInt(mgc.character.allianceRank);
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
        if (mc.character.guild.id > 0) {
            return MapleGuildResponse.ALREADY_IN_GUILD;
        }
        mc.getClient().getSession().write(MaplePacketCreator.guildInvite(c.getPlayer().character.guild.id, c.getPlayer().character.name, c.getPlayer().character.level, c.getPlayer().getJob()));
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
        bbs.put(ret, new MapleBBSThread(ret, title, text, System.currentTimeMillis(), guild.id, posterID, icon));
        return ret;
    }

    public void editBBSThread(int localthreadid, String title, String text, int icon, int posterID, int guildRank) {
        MapleBBSThread thread = bbs.get(localthreadid);
        if (thread != null && (thread.ownerID == posterID || guildRank <= 2)) {
            bbs.put(localthreadid, new MapleBBSThread(localthreadid, title, text, System.currentTimeMillis(), guild.id, thread.ownerID, icon));
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
            if (reply != null && (reply.reply.postercid == posterID || guildRank <= 2)) {
                thread.replies.remove(replyid);
            }
        }
    }

    public static void setOfflineGuildStatus(int guildid, int guildrank, int alliancerank, int cid) {
        DCharacter one = new QDCharacter().id.eq(cid).findOne();
        if (one != null) {
            one.guild = new QDGuild().id.eq(guildid).findOne();
            one.guildRank = guildrank;
            one.allianceRank = alliancerank;
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
        List<DGuild> guilds = new QDGuild().orderBy().GP.desc().setMaxRows(50).findList();
        c.getSession().write(MaplePacketCreator.gpRank(npcid, guilds));
    }

}
