package handling.world.guild;

import com.github.mrzhqiang.maplestory.domain.DAlliance;
import database.DatabaseConnection;
import handling.MaplePacket;
import handling.world.World;
import io.ebean.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.MaplePacketCreator;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class MapleGuildAlliance {

    private enum GAOp {
        NONE, DISBAND, NEW_GUILD
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleGuildAlliance.class);

    public static final int CHANGE_CAPACITY_COST = 10000000;

    private final DAlliance alliance;

    private MapleGuildAlliance(DAlliance alliance) {
        this.alliance = alliance;
    }

    public static List<MapleGuildAlliance> loadAll() {
        return DB.find(DAlliance.class).findList().stream()
                .map(MapleGuildAlliance::new)
                .collect(Collectors.toList());
    }

    @Nullable
    public static MapleGuildAlliance findById(int id) {
        DAlliance alliance = DB.find(DAlliance.class, id);
        if (alliance != null && alliance.id > 0) {
            return new MapleGuildAlliance(alliance);
        }
        return null;
    }

    public int getNoGuilds() {
        int ret = 0;
        if (alliance.guild1 != null && alliance.guild1 > 0) {
            ret += 1;
        }
        if (alliance.guild2 != null && alliance.guild2 > 0) {
            ret += 1;
        }
        if (alliance.guild3 != null && alliance.guild3 > 0) {
            ret += 1;
        }
        if (alliance.guild4 != null && alliance.guild4 > 0) {
            ret += 1;
        }
        if (alliance.guild5 != null && alliance.guild5 > 0) {
            ret += 1;
        }
        return ret;
    }

    public static MapleGuildAlliance createAndSave(int leaderId, String name, int guild1, int guild2) {
        if (name.length() > 12) {
            return null;
        }

        boolean exists = DB.find(DAlliance.class).setParameter("name", name).exists();
        if (exists) {
            return null;
        }

        DAlliance alliance = new DAlliance();
        alliance.name = name;
        alliance.guild1 = guild1;
        alliance.guild2 = guild2;
        alliance.leaderid = leaderId;
        alliance.save();
        return new MapleGuildAlliance(alliance);
    }

    public boolean deleteAlliance() {
        try {
            Connection con = DatabaseConnection.getConnection();
            // todo 级联操作
            resetCharacters(con, alliance.guild1);
            resetCharacters(con, alliance.guild2);
            resetCharacters(con, alliance.guild3);
            resetCharacters(con, alliance.guild4);
            resetCharacters(con, alliance.guild5);
        } catch (SQLException e) {
            LOGGER.error("删除联盟失败", e);
            return false;
        }
        return alliance.delete();
    }

    private void resetCharacters(Connection con, Integer guildId) throws SQLException {
        if (guildId != null && guildId > 0) {
            PreparedStatement ps = con.prepareStatement("UPDATE characters SET alliancerank = 5 WHERE guildid = ?");
            ps.setInt(1, guildId);
            ps.execute();
            ps.close();
        }
    }

    public void broadcast(MaplePacket packet) {
        broadcast(packet, -1, GAOp.NONE, false);
    }

    public void broadcast(MaplePacket packet, int exception) {
        broadcast(packet, exception, GAOp.NONE, false);
    }

    public void broadcast(MaplePacket packet, int exceptionId, GAOp op, boolean expelled) {
        int allianceId = alliance.id;
        if (op == GAOp.DISBAND) {
            World.Alliance.setOldAlliance(exceptionId, expelled, allianceId); //-1 = alliance gone, exceptionId = guild left/expelled
        } else if (op == GAOp.NEW_GUILD) {
            World.Alliance.setNewAlliance(exceptionId, allianceId); //exceptionId = guild that just joined
        } else {
            World.Alliance.sendGuild(packet, exceptionId, allianceId); //exceptionId = guild to broadcast to only
        }
    }

    public boolean disband() {
        boolean ret = deleteAlliance();
        if (ret) {
            broadcast(null, -1, GAOp.DISBAND, false);
        }
        return ret;
    }

    public void saveToDb() {
        alliance.save();
    }

    public void setRank(String[] ranks) {
        if (ranks != null) {
            if (ranks.length > 0) {
                alliance.rank1 = ranks[0];
            }
            if (ranks.length > 1) {
                alliance.rank2 = ranks[1];
            }
            if (ranks.length > 2) {
                alliance.rank3 = ranks[2];
            }
            if (ranks.length > 3) {
                alliance.rank4 = ranks[3];
            }
            if (ranks.length > 4) {
                alliance.rank5 = ranks[4];
            }
        }
        broadcast(MaplePacketCreator.getAllianceUpdate(this));
        saveToDb();
    }

    public String getRank(int rank) {
        String[] ranks = getRanks();
        if (rank < 0 || rank > ranks.length) {
            return null;
        }
        return ranks[rank];
    }

    public String[] getRanks() {
        return new String[]{alliance.rank1, alliance.rank2, alliance.rank3, alliance.rank4, alliance.rank5};
    }

    public int[] getGuilds() {
        return new int[]{alliance.guild1, alliance.guild2, alliance.guild3, alliance.guild4, alliance.guild5};
    }

    public String getNotice() {
        return alliance.notice;
    }

    public void setNotice(String newNotice) {
        alliance.notice = newNotice;
        broadcast(MaplePacketCreator.getAllianceUpdate(this));
        broadcast(MaplePacketCreator.serverNotice(5, "联盟公告事项: " + newNotice));
        saveToDb();
    }

    public int getGuildId(int i) {
        int[] guilds = getGuilds();
        if (i < 0 || i >= guilds.length) {
            return 0;
        }
        return guilds[i];
    }

    public int getId() {
        return alliance.id;
    }

    public String getName() {
        return alliance.name;
    }

    public int getCapacity() {
        return alliance.capacity;
    }

    public boolean setCapacity() {
        if (alliance.capacity >= 5) {
            return false;
        }
        alliance.capacity += 1;
        broadcast(MaplePacketCreator.getAllianceUpdate(this));
        saveToDb();
        return true;
    }

    public boolean addGuild(int guildid) {
        int guilds = getNoGuilds();
        if (guilds >= getCapacity()) {
            return false;
        }
        switch (guilds) {
            case 1:
                alliance.guild1 = guildid;
                break;
            case 2:
                alliance.guild2 = guildid;
                break;
            case 3:
                alliance.guild3 = guildid;
                break;
            case 4:
                alliance.guild4 = guildid;
                break;
            case 5:
                alliance.guild5 = guildid;
                break;
        }
        saveToDb();
        broadcast(null, guildid, GAOp.NEW_GUILD, false);
        return true;
    }

    public boolean removeGuild(final int guildid, final boolean expelled) {
        int[] guilds = getGuilds();
        int noGuilds = getNoGuilds();
        for (int i = 0; i < noGuilds; i++) {
            if (guilds[i] == guildid) {
                broadcast(null, guildid, GAOp.DISBAND, expelled);
                if (i == 0) {
                    alliance.guild1 = -1;
                    return disband();
                }
                if (i == 1) {
                    alliance.guild1 = alliance.guild2;
                    alliance.guild2 = -1;
                }
                if (i == 2) {
                    alliance.guild1 = alliance.guild2;
                    alliance.guild2 = alliance.guild3;
                    alliance.guild3 = -1;
                }
                if (i == 3) {
                    alliance.guild1 = alliance.guild2;
                    alliance.guild2 = alliance.guild3;
                    alliance.guild3 = alliance.guild4;
                    alliance.guild4 = -1;
                }
                if (i == 4) {
                    alliance.guild5 = -1;
                }
                broadcast(MaplePacketCreator.getAllianceUpdate(this));
                broadcast(MaplePacketCreator.getGuildAlliance(this));
                saveToDb();
                return true;
            }
        }
        return false;
    }

    public int getLeaderId() {
        return alliance.leaderid;
    }

    public boolean setLeaderId(int c) {
        if (alliance.leaderid == c) {
            return false;
        }

        //MapleCharacter cb = c;
        //re-arrange the guilds so guild1 is always the leader guild
        int g = -1; //this shall be leader
        String leaderName = null;

        int[] guilds = getGuilds();
        int noGuilds = getNoGuilds();
        for (int i = 0; i < noGuilds; i++) {
            MapleGuild guild = World.Guild.getGuild(guilds[i]);
            if (guild != null) {
                MapleGuildCharacter newLead = guild.getMGC(c);
                MapleGuildCharacter oldLead = guild.getMGC(alliance.leaderid);
                if (newLead != null && oldLead != null) { //same guild
                    return false;
                } else if (newLead != null && newLead.getGuildRank() == 1 && newLead.getAllianceRank() == 2) { //guild1 should always be leader so no worries about g being -1
                    guild.changeARank(c, 1);
                    g = i;
                    leaderName = newLead.getName();
                } else if (oldLead != null && oldLead.getGuildRank() == 1 && oldLead.getAllianceRank() == 1) {
                    guild.changeARank(alliance.leaderid, 2);
                } else if (oldLead != null || newLead != null) {
                    return false;
                }
            }
        }
        if (g == -1) {
            return false; //nothing was done
        }
        int oldGuild = guilds[g];
        guilds[g] = guilds[0];
        guilds[0] = oldGuild;
        if (leaderName != null) {
            broadcast(MaplePacketCreator.serverNotice(5, leaderName + " 已经成为联盟的领袖。"));
        }
        broadcast(MaplePacketCreator.changeAllianceLeader(getId(), getLeaderId(), c));
        broadcast(MaplePacketCreator.updateAllianceLeader(getId(), getLeaderId(), c));
        broadcast(MaplePacketCreator.getAllianceUpdate(this));
        broadcast(MaplePacketCreator.getGuildAlliance(this));
        alliance.leaderid = c;
        saveToDb();
        return true;
    }

    public boolean changeAllianceRank(int cid, int change) {
        if (alliance.leaderid == cid || change < 0 || change > 1) {
            return false;
        }
        int[] guilds = getGuilds();
        for (int i = 0; i < getNoGuilds(); i++) {
            MapleGuild guild = World.Guild.getGuild(guilds[i]);
            if (guild != null) {
                MapleGuildCharacter chr = guild.getMGC(cid);
                if (chr != null && chr.getAllianceRank() > 2) {
                    if ((change == 0 && chr.getAllianceRank() >= 5) || (change == 1 && chr.getAllianceRank() <= 3)) {
                        return false;
                    }
                    guild.changeARank(cid, chr.getAllianceRank() + (change == 0 ? 1 : -1));
                    return true;
                }
            }
        }
        return false;
    }
}
