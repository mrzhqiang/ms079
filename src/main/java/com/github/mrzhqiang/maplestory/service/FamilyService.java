package com.github.mrzhqiang.maplestory.service;

import client.MapleCharacter;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.github.mrzhqiang.maplestory.domain.DFamily;
import com.github.mrzhqiang.maplestory.domain.query.QDFamily;
import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import handling.MaplePacket;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.PlayerStorage;
import handling.world.World;
import handling.world.family.MapleFamily;
import handling.world.family.MapleFamilyCharacter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;

@Singleton
public final class FamilyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FamilyService.class);

    private final LoadingCache<Integer, MapleFamily> cached;

    @Inject
    public FamilyService(ServerProperties properties) {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(Duration.ofMinutes(30));
        if (properties.isDebug()) {
            builder.recordStats();
        }
        this.cached = builder.build(loader());
    }

    private CacheLoader<Integer, MapleFamily> loader() {
        return new CacheLoader<Integer, MapleFamily>() {
            @Override
            public MapleFamily load(@Nonnull Integer id) {
                return null;
            }
        };
    }

    public void init() {
        LOGGER.info(">>> 初始化 [学院系统]");
        Stopwatch started = Stopwatch.createStarted();
        for (MapleFamily family : MapleFamily.loadAll()) {
            if (family.isProper()) {
                cached.put(family.getId(), family);
            }
        }
        LOGGER.info("<<< [学院系统]初始化完毕，耗时：{}", started.stop());
    }

    public MapleFamily getFamily(Integer id) {
        MapleFamily ret = cached.getUnchecked(id);
        if (ret == null) {
            DFamily one = new QDFamily().id.eq(id).findOne();
            if (one == null) {
                return null;
            }
            ret = new MapleFamily(one);
            cached.put(id, ret);
        }
        return ret;
    }

    public void memberFamilyUpdate(MapleFamilyCharacter mfc, MapleCharacter mc) {
        MapleFamily f = getFamily(mfc.getFamilyId());
        if (f != null) {
            f.memberLevelJobUpdate(mc);
        }
    }

    public void setFamilyMemberOnline(MapleFamilyCharacter mfc, boolean bOnline, int channel) {
        MapleFamily f = getFamily(mfc.getFamilyId());
        if (f != null) {
            f.setOnline(mfc.getId(), bOnline, channel);
        }
    }

    public int setRep(int fid, int cid, int addrep, int oldLevel) {
        MapleFamily f = getFamily(fid);
        if (f != null) {
            return f.setRep(cid, addrep, oldLevel);
        }
        return 0;
    }

    public void save() {
        LOGGER.debug("保存学院...");
        cached.asMap().forEach((key, value) -> value.writeToDB(false));
    }

    public void setFamily(int familyid, int seniorid, int junior1, int junior2, int currentrep, int totalrep, int cid) {
        int ch = World.Find.findChannel(cid);
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

    public void familyPacket(int gid, MaplePacket message, int cid) {
        MapleFamily f = getFamily(gid);
        if (f != null) {
            f.broadcast(message, -1, f.getMFC(cid).getPedigree());
        }
    }

    public void disbandFamily(int gid) {
        MapleFamily g = getFamily(gid);
        if (g != null) {
            g.disbandFamily();
            cached.invalidate(gid);
        }
    }

    public static PlayerStorage getStorage(int channel) {
        if (channel == -20) {
            return CashShopServer.getPlayerStorageMTS();
        } else if (channel == -10) {
            return CashShopServer.getPlayerStorage();
        }
        return ChannelServer.getInstance(channel).getPlayerStorage();
    }

}
