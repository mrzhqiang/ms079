package com.github.mrzhqiang.maplestory.service;

import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.github.mrzhqiang.maplestory.domain.query.QDAlliance;
import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import handling.MaplePacket;
import handling.world.World;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildAlliance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.MaplePacketCreator;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public final class AllianceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllianceService.class);

    private final LoadingCache<Integer, MapleGuildAlliance> cached;

    @Inject
    public AllianceService(ServerProperties properties) {
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(Duration.ofMinutes(30));
        if (properties.isDebug()) {
            cacheBuilder.recordStats();
        }
        this.cached = cacheBuilder.build(loader());
    }

    private CacheLoader<Integer, MapleGuildAlliance> loader() {
        return new CacheLoader<Integer, MapleGuildAlliance>() {
            @Override
            public MapleGuildAlliance load(@Nonnull Integer key) {
                return null;
            }
        };
    }

    public void init() {
        LOGGER.info(">>> 初始化 [家族联盟系统]");
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (MapleGuildAlliance alliance : MapleGuildAlliance.loadAll()) {
            cached.put(alliance.getId(), alliance);
        }
        LOGGER.info("<<< [家族联盟系统] 初始化完毕，耗时：{}", stopwatch.stop());
    }

    public MapleGuildAlliance getAlliance(Integer allianceId) {
        MapleGuildAlliance ret = cached.getUnchecked(allianceId);
        if (ret != null) {
            return ret;
        }

        ret = MapleGuildAlliance.findById(allianceId);
        if (ret != null) {
            cached.put(allianceId, ret);
            return ret;
        }
        return null;
    }

    public int getAllianceLeader(int allianceid) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        if (alliance != null) {
            return alliance.getLeaderId();
        }
        return 0;
    }

    public void updateAllianceRanks(int allianceid, String[] ranks) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        if (alliance != null) {
            alliance.setRank(ranks);
        }
    }

    public void updateAllianceNotice(int allianceid, String notice) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        if (alliance != null) {
            alliance.setNotice(notice);
        }
    }

    public boolean canInvite(int allianceid) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        if (alliance != null) {
            return alliance.getCapacity() > alliance.getNoGuilds();
        }
        return false;
    }

    public boolean changeAllianceLeader(int allianceid, int cid) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        if (alliance != null) {
            return alliance.setLeaderId(cid);
        }
        return false;
    }

    public boolean changeAllianceRank(int allianceid, int cid, int change) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        if (alliance != null) {
            return alliance.changeAllianceRank(cid, change);
        }
        return false;
    }

    public boolean changeAllianceCapacity(int allianceid) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        if (alliance != null) {
            return alliance.setCapacity();
        }
        return false;
    }

    public boolean disbandAlliance(int allianceid) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        if (alliance != null) {
            return alliance.disband();
        }
        return false;
    }

    public boolean addGuildToAlliance(int allianceid, int gid) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        if (alliance != null) {
            return alliance.addGuild(gid);
        }
        return false;
    }

    public boolean removeGuildFromAlliance(int allianceid, int gid, boolean expelled) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        if (alliance != null) {
            return alliance.removeGuild(gid, expelled);
        }
        return false;
    }

    public void sendGuild(int allianceid) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        if (alliance != null) {
            sendGuild(MaplePacketCreator.getAllianceUpdate(alliance), -1, allianceid);
            sendGuild(MaplePacketCreator.getGuildAlliance(alliance), -1, allianceid);
        }
    }

    public void sendGuild(MaplePacket packet, int exceptionId, int allianceid) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        if (alliance != null) {
            for (int i = 0; i < alliance.getNoGuilds(); i++) {
                int gid = alliance.getGuildId(i);
                if (gid > 0 && gid != exceptionId) {
                    World.Guild.guildPacket(gid, packet);
                }
            }
        }
    }

    public boolean createAlliance(String alliancename, int cid, int cid2, int gid, int gid2) {
        MapleGuildAlliance alliance = MapleGuildAlliance.createAndSave(cid, alliancename, gid, gid2);
        if (alliance == null) {
            return false;
        }

        MapleGuild guild = World.Guild.getGuild(gid);
        MapleGuild guild2 = World.Guild.getGuild(gid2);
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

    public void allianceChat(int gid, String name, int cid, String msg) {
        MapleGuild guild = World.Guild.getGuild(gid);
        if (guild != null) {
            MapleGuildAlliance alliance = getAlliance(guild.getAllianceId());
            if (alliance != null) {
                for (int i = 0; i < alliance.getNoGuilds(); i++) {
                    MapleGuild mapleGuild = World.Guild.getGuild(alliance.getGuildId(i));
                    if (mapleGuild != null) {
                        mapleGuild.allianceChat(name, cid, msg);
                    }
                }
            }
        }
    }

    public void setNewAlliance(int gid, int allianceid) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        MapleGuild guild = World.Guild.getGuild(gid);
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
                    MapleGuild g_ = World.Guild.getGuild(alliance.getGuildId(i));
                    if (g_ != null) {
                        g_.broadcast(MaplePacketCreator.addGuildToAlliance(alliance, guild));
                        g_.broadcast(MaplePacketCreator.changeGuildInAlliance(alliance, guild, true));
                    }
                }
            }
        }
    }

    public void setOldAlliance(int gid, boolean expelled, int allianceid) {
        MapleGuildAlliance alliance = getAlliance(allianceid);
        MapleGuild g_ = World.Guild.getGuild(gid);
        if (alliance != null) {
            for (int i = 0; i < alliance.getNoGuilds(); i++) {
                MapleGuild guild = World.Guild.getGuild(alliance.getGuildId(i));
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
            cached.invalidate(allianceid);
        }
    }

    public List<MaplePacket> getAllianceInfo(int allianceid, boolean start) {
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

    public void save() {
        LOGGER.debug("Saving alliances...");
        cached.asMap().forEach((key, value) -> value.saveToDb());
    }
}
