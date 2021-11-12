package handling.world.guild;

import com.github.mrzhqiang.maplestory.domain.DAlliance;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DGuild;
import com.github.mrzhqiang.maplestory.domain.query.QDAlliance;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDGuild;
import handling.MaplePacket;
import handling.world.World;
import io.ebean.DB;
import tools.MaplePacketCreator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class MapleGuildAlliance {

    private enum GAOp {
        NONE, DISBAND, NEW_GUILD
    }

    public static final int CHANGE_CAPACITY_COST = 10000000;

    private final DAlliance entity;

    private MapleGuildAlliance(DAlliance entity) {
        this.entity = entity;
    }

    public static List<MapleGuildAlliance> loadAll() {
        return new QDAlliance()
                .findStream()
                .map(MapleGuildAlliance::new)
                .collect(Collectors.toList());
    }

    @Nullable
    public static MapleGuildAlliance findById(int id) {
        DAlliance alliance = new QDAlliance().id.eq(id).findOne();
        if (alliance != null && alliance.id > 0) {
            return new MapleGuildAlliance(alliance);
        }
        return null;
    }

    @Nullable
    public static MapleGuildAlliance createAndSave(int leaderId, String name, int guild1, int guild2) {
        if (name.length() > 12) {
            return null;
        }

        boolean exists = new QDAlliance().name.eq(name).exists();
        if (exists) {
            return null;
        }

        DAlliance alliance = new DAlliance();
        alliance.name = name;
        alliance.leader = new QDCharacter().id.eq(leaderId).findOne();
        alliance.guild1 = new QDGuild().id.eq(guild1).findOne();
        alliance.guild2 = new QDGuild().id.eq(guild2).findOne();
        alliance.save();
        return new MapleGuildAlliance(alliance);
    }

    public int getNoGuilds() {
        int ret = 0;
        if (entity.guild1 != null && entity.guild1.id > 0) {
            ret += 1;
        }
        if (entity.guild2 != null && entity.guild2.id > 0) {
            ret += 1;
        }
        if (entity.guild3 != null && entity.guild3.id > 0) {
            ret += 1;
        }
        if (entity.guild4 != null && entity.guild4.id > 0) {
            ret += 1;
        }
        if (entity.guild5 != null && entity.guild5.id > 0) {
            ret += 1;
        }
        return ret;
    }

    public boolean deleteAlliance() {
        // todo 级联操作
        resetCharacters(entity.guild1);
        resetCharacters(entity.guild2);
        resetCharacters(entity.guild3);
        resetCharacters(entity.guild4);
        resetCharacters(entity.guild5);
        return entity.delete();
    }

    private void resetCharacters(DGuild guild) {
        if (guild != null) {
            new QDCharacter().guild.eq(guild)
                    .asUpdate()
                    .set("allianceRank", 5)
                    .update();
        }
    }

    public void broadcast(MaplePacket packet) {
        broadcast(packet, -1, GAOp.NONE, false);
    }

    public void broadcast(MaplePacket packet, int exception) {
        broadcast(packet, exception, GAOp.NONE, false);
    }

    public void broadcast(MaplePacket packet, int exceptionId, GAOp op, boolean expelled) {
        int allianceId = entity.id;
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
        entity.save();
    }

    public void setRank(String[] ranks) {
        if (ranks != null) {
            if (ranks.length > 0) {
                entity.rank1 = ranks[0];
            }
            if (ranks.length > 1) {
                entity.rank2 = ranks[1];
            }
            if (ranks.length > 2) {
                entity.rank3 = ranks[2];
            }
            if (ranks.length > 3) {
                entity.rank4 = ranks[3];
            }
            if (ranks.length > 4) {
                entity.rank5 = ranks[4];
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
        return new String[]{entity.rank1, entity.rank2, entity.rank3, entity.rank4, entity.rank5};
    }

    public int[] getGuilds() {
        return new int[]{entity.guild1.id, entity.guild2.id, entity.guild3.id, entity.guild4.id, entity.guild5.id};
    }

    public String getNotice() {
        return entity.notice;
    }

    public void setNotice(String newNotice) {
        entity.notice = newNotice;
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
        return entity.id;
    }

    public String getName() {
        return entity.name;
    }

    public int getCapacity() {
        return entity.capacity;
    }

    public boolean setCapacity() {
        if (entity.capacity >= 5) {
            return false;
        }
        entity.capacity += 1;
        broadcast(MaplePacketCreator.getAllianceUpdate(this));
        saveToDb();
        return true;
    }

    public boolean addGuild(int guildid) {
        int guilds = getNoGuilds();
        if (guilds >= getCapacity()) {
            return false;
        }
        DGuild one = new QDGuild().id.eq(guildid).findOne();
        switch (guilds) {
            case 1:
                entity.guild1 = one;
                break;
            case 2:
                entity.guild2 = one;
                break;
            case 3:
                entity.guild3 = one;
                break;
            case 4:
                entity.guild4 = one;
                break;
            case 5:
                entity.guild5 = one;
                break;
        }
        saveToDb();
        broadcast(null, guildid, GAOp.NEW_GUILD, false);
        return true;
    }

    public boolean removeGuild(int guildid, boolean expelled) {
        int[] guilds = getGuilds();
        int noGuilds = getNoGuilds();
        for (int i = 0; i < noGuilds; i++) {
            if (guilds[i] == guildid) {
                broadcast(null, guildid, GAOp.DISBAND, expelled);
                if (i == 0) {
                    entity.guild1 = null;
                    return disband();
                }
                if (i == 1) {
                    entity.guild1 = entity.guild2;
                    entity.guild2 = null;
                }
                if (i == 2) {
                    entity.guild1 = entity.guild2;
                    entity.guild2 = entity.guild3;
                    entity.guild3 = null;
                }
                if (i == 3) {
                    entity.guild1 = entity.guild2;
                    entity.guild2 = entity.guild3;
                    entity.guild3 = entity.guild4;
                    entity.guild4 = null;
                }
                if (i == 4) {
                    entity.guild5 = null;
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
        return entity.leader.id;
    }

    public boolean setLeaderId(int c) {
        DCharacter one = new QDCharacter().id.eq(c).findOne();
        if (one== null || one.equals(entity.leader)) {
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
                MapleGuildCharacter newLead = guild.getMGC(one.id);
                MapleGuildCharacter oldLead = guild.getMGC(entity.leader.id);
                if (newLead != null && oldLead != null) { //same guild
                    return false;
                } else if (newLead != null && newLead.character.guildRank == 1 && newLead.character.allianceRank == 2) { //guild1 should always be leader so no worries about g being -1
                    guild.changeARank(c, 1);
                    g = i;
                    leaderName = newLead.character.name;
                } else if (oldLead != null && oldLead.character.guildRank == 1 && oldLead.character.allianceRank == 1) {
                    guild.changeARank(entity.leader.id, 2);
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
        entity.leader = one;
        saveToDb();
        return true;
    }

    public boolean changeAllianceRank(int cid, int change) {
        DCharacter one = new QDCharacter().id.eq(cid).findOne();
        if (one== null || one.equals(entity.leader) || change < 0 || change > 1) {
            return false;
        }
        int[] guilds = getGuilds();
        for (int i = 0; i < getNoGuilds(); i++) {
            MapleGuild guild = World.Guild.getGuild(guilds[i]);
            if (guild != null) {
                MapleGuildCharacter chr = guild.getMGC(cid);
                if (chr != null && chr.character.allianceRank > 2) {
                    if ((change == 0 && chr.character.allianceRank >= 5) || (change == 1 && chr.character.allianceRank <= 3)) {
                        return false;
                    }
                    guild.changeARank(cid, chr.character.allianceRank + (change == 0 ? 1 : -1));
                    return true;
                }
            }
        }
        return false;
    }
}
