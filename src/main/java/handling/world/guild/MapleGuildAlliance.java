package handling.world.guild;

import com.github.mrzhqiang.maplestory.domain.DAlliance;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DGuild;
import com.github.mrzhqiang.maplestory.domain.query.QDAlliance;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDGuild;
import handling.MaplePacket;
import handling.world.World;
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
        return new QDAlliance().findList().stream()
                .map(MapleGuildAlliance::new)
                .collect(Collectors.toList());
    }

    @Nullable
    public static MapleGuildAlliance findById(int id) {
        DAlliance alliance = new QDAlliance().id.eq(id).findOne();
        if (alliance != null) {
            return new MapleGuildAlliance(alliance);
        }
        return null;
    }

    @Nullable
    public static MapleGuildAlliance createAndSave(int leaderId, String name, int guild1, int guild2) {
        if (name == null || name.length() > 12) {
            return null;
        }

        boolean exists = new QDAlliance().name.eq(name).exists();
        if (exists) {
            return null;
        }

        DAlliance alliance = new DAlliance();
        alliance.setName(name);
        alliance.setLeader(new QDCharacter().id.eq(leaderId).findOne());
        alliance.setGuild1(new QDGuild().id.eq(guild1).findOne());
        alliance.setGuild2(new QDGuild().id.eq(guild2).findOne());
        alliance.save();
        return new MapleGuildAlliance(alliance);
    }

    public int getNoGuilds() {
        int ret = 0;
        if (entity.getGuild1() != null) {
            ret += 1;
        }
        if (entity.getGuild2() != null) {
            ret += 1;
        }
        if (entity.getGuild3() != null) {
            ret += 1;
        }
        if (entity.getGuild4() != null) {
            ret += 1;
        }
        if (entity.getGuild5() != null) {
            ret += 1;
        }
        return ret;
    }

    public boolean deleteAlliance() {
        // todo 级联操作
        resetCharacters(entity.getGuild1());
        resetCharacters(entity.getGuild2());
        resetCharacters(entity.getGuild3());
        resetCharacters(entity.getGuild4());
        resetCharacters(entity.getGuild5());
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
        int allianceId = entity.getId();
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
                entity.setRank1(ranks[0]);
            }
            if (ranks.length > 1) {
                entity.setRank2(ranks[1]);
            }
            if (ranks.length > 2) {
                entity.setRank3(ranks[2]);
            }
            if (ranks.length > 3) {
                entity.setRank4(ranks[3]);
            }
            if (ranks.length > 4) {
                entity.setRank5(ranks[4]);
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
        return new String[]{entity.getRank1(), entity.getRank2(), entity.getRank3(), entity.getRank4(), entity.getRank5()};
    }

    public int[] getGuilds() {
        return new int[]{entity.getGuild1().getId(), entity.getGuild2().getId(), entity.getGuild3().getId(), entity.getGuild4().getId(), entity.getGuild5().getId()};
    }

    public String getNotice() {
        return entity.getNotice();
    }

    public void setNotice(String newNotice) {
        entity.setNotice(newNotice);
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
        return entity.getId();
    }

    public String getName() {
        return entity.getName();
    }

    public int getCapacity() {
        return entity.getCapacity();
    }

    public boolean setCapacity() {
        if (entity.getCapacity() >= 5) {
            return false;
        }
        entity.setCapacity(entity.getCapacity() + 1);
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
                entity.setGuild1(one);
                break;
            case 2:
                entity.setGuild2(one);
                break;
            case 3:
                entity.setGuild3(one);
                break;
            case 4:
                entity.setGuild4(one);
                break;
            case 5:
                entity.setGuild5(one);
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
                    entity.setGuild1(null);
                    return disband();
                }
                if (i == 1) {
                    entity.setGuild1(entity.getGuild2());
                    entity.setGuild2(null);
                }
                if (i == 2) {
                    entity.setGuild1(entity.getGuild2());
                    entity.setGuild2(entity.getGuild3());
                    entity.setGuild3(null);
                }
                if (i == 3) {
                    entity.setGuild1(entity.getGuild2());
                    entity.setGuild2(entity.getGuild3());
                    entity.setGuild3(entity.getGuild4());
                    entity.setGuild4(null);
                }
                if (i == 4) {
                    entity.setGuild5(null);
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
        return entity.getLeader().getId();
    }

    public boolean setLeaderId(int c) {
        DCharacter one = new QDCharacter().id.eq(c).findOne();
        if (one == null || one.equals(entity.getLeader())) {
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
                MapleGuildCharacter newLead = guild.getMGC(one.getId());
                MapleGuildCharacter oldLead = guild.getMGC(entity.getLeader().getId());
                if (newLead != null && oldLead != null) { //same guild
                    return false;
                } else if (newLead != null && newLead.character.getGuildRank() == 1 && newLead.character.getAllianceRank() == 2) { //guild1 should always be leader so no worries about g being -1
                    guild.changeARank(c, 1);
                    g = i;
                    leaderName = newLead.character.getName();
                } else if (oldLead != null && oldLead.character.getGuildRank() == 1 && oldLead.character.getAllianceRank() == 1) {
                    guild.changeARank(entity.getLeader().getId(), 2);
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
        entity.setLeader(one);
        saveToDb();
        return true;
    }

    public boolean changeAllianceRank(int cid, int change) {
        DCharacter one = new QDCharacter().id.eq(cid).findOne();
        if (one == null || one.equals(entity.getLeader()) || change < 0 || change > 1) {
            return false;
        }
        int[] guilds = getGuilds();
        for (int i = 0; i < getNoGuilds(); i++) {
            MapleGuild guild = World.Guild.getGuild(guilds[i]);
            if (guild != null) {
                MapleGuildCharacter chr = guild.getMGC(cid);
                if (chr != null && chr.character.getAllianceRank() > 2) {
                    if ((change == 0 && chr.character.getAllianceRank() >= 5) || (change == 1 && chr.character.getAllianceRank() <= 3)) {
                        return false;
                    }
                    guild.changeARank(cid, chr.character.getAllianceRank() + (change == 0 ? 1 : -1));
                    return true;
                }
            }
        }
        return false;
    }
}
