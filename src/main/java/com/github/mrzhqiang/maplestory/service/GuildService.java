package com.github.mrzhqiang.maplestory.service;

import client.MapleCharacter;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DGuild;
import com.github.mrzhqiang.maplestory.domain.query.QDGuild;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import handling.MaplePacket;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.PlayerStorage;
import handling.world.World;
import handling.world.guild.MapleBBSThread;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildCharacter;
import handling.world.guild.MapleGuildSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.MaplePacketCreator;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Singleton
public final class GuildService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuildService.class);

    private static final int MAX_GUILD_SIZE = 1000;
    private static final int MAX_GUILD_NAME_LENGTH = 12;
    private static final Duration DURATION = Duration.ofMinutes(30);

    private final QDGuild qdGuild;
    private final ServerProperties properties;
    private final ChannelService channelService;

    private final LoadingCache<Integer, MapleGuild> idGuildCached;
    private final LoadingCache<String, MapleGuild> nameGuildCached;

    @Inject
    public GuildService(QDGuild qdGuild, ServerProperties properties, ChannelService channelService) {
        this.qdGuild = qdGuild;
        this.properties = properties;
        this.channelService = channelService;
        // fixme 确定一个合适的最大数量和过期时间，以节省内存
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
                .maximumSize(MAX_GUILD_SIZE)
                .expireAfterAccess(DURATION);
        if (properties.isDebug()) {
            cacheBuilder.recordStats();
        }
        this.idGuildCached = cacheBuilder.build(getIdLoader());
        this.nameGuildCached = cacheBuilder.build(getNameLoader());
    }

    @Nonnull
    private CacheLoader<Integer, MapleGuild> getIdLoader() {
        // 通过加载器构建，当缓存不存在时，自动从加载器加载指定内容
        return new CacheLoader<Integer, MapleGuild>() {
            @Override
            public MapleGuild load(@Nonnull Integer id) {
                // 防止缓存穿透，这里我们返回 empty 常量值
                return qdGuild.id.eq(id).findOneOrEmpty().map(MapleGuild::new).orElse(MapleGuild.empty());
            }
        };
    }

    @Nonnull
    private CacheLoader<String, MapleGuild> getNameLoader() {
        return new CacheLoader<String, MapleGuild>() {
            @Override
            public MapleGuild load(@Nonnull String name) {
                return qdGuild.name.eq(name).findOneOrEmpty().map(MapleGuild::new).orElse(MapleGuild.empty());
            }
        };
    }

    public void init() {
        LOGGER.info(">>> 准备初始化 [家族系统]");
        Stopwatch stopwatch = Stopwatch.createStarted();
        // 注意，这里不使用 findStream
        // 1. 不想在这里使用 try-resource 语句
        // 2. 结果不会很大，所以不需要迭代方式查询
        qdGuild.findList().stream().map(MapleGuild::new).filter(MapleGuild::isValid).forEach(this::addCached);
        LOGGER.info("<<< [家族系统] 初始化完毕，共加载 {} 个家族，耗时：{}", idGuildCached.size(), stopwatch.stop());
    }

    public void addCached(MapleGuild guild) {
        idGuildCached.put(guild.getId(), guild);
        nameGuildCached.put(guild.getName(), guild);
    }

    public Optional<DGuild> createBy(DCharacter leader, String name) {
        Preconditions.checkNotNull(leader, "leader == null");
        return Optional.ofNullable(name)
                .filter(this::validName)
                .filter(this::notExists)
                .map(it -> create(leader, it));
    }

    public boolean validName(String name) {
        return name.length() <= MAX_GUILD_NAME_LENGTH;
    }

    public boolean notExists(String name) {
        return !exists(name);
    }

    public boolean exists(String name) {
        return qdGuild.name.eq(name).exists();
    }

    private DGuild create(DCharacter leader, String name) {
        DGuild guild = new DGuild(leader, name);
        // Math.toIntExact(System.currentTimeMillis() / 1000)
        guild.setSignature(Math.toIntExact(Instant.now().getEpochSecond()));
        // 必须持久化到数据库，防止未来的某个时间段创建家族出错
        guild.save();
        if (properties.isDebug()) {
            LOGGER.debug("正准备创建家族：{}", guild);
        }
        // 这里不操作缓存，因为创建操作还未完成
        return guild;
    }

    public Optional<MapleGuild> findById(Integer id) {
        // 原版本的设计：找到家族，同步 id 和 name 到缓存
        // 这里移除了这个设计，因为我们有 CacheLoader
        return Optional.ofNullable(id)
                // 此方法会自动触发 CacheLoader 去获取缓存值，因此不会返回 null 值
                .map(idGuildCached::getUnchecked)
                // 但有可能返回 empty 值
                .filter(MapleGuild::isValid);
    }

    public Optional<MapleGuild> findByName(String name) {
        return Optional.ofNullable(name)
                .map(nameGuildCached::getUnchecked)
                .filter(MapleGuild::isValid);
    }

    public Optional<MapleGuild> findBy(MapleCharacter character) {
        return findById(character.getGuildId());
    }

    public boolean addMember(MapleGuildCharacter character) {
        Integer id = character.character.getGuild().getId();
        return findById(id).map(it -> it.addMember(character)).orElse(false);
    }

    public void removeMember(MapleGuildCharacter initiator, String name, int cid) {
        Integer id = initiator.character.getGuild().getId();
        findById(id).ifPresent(it -> it.removeMember(initiator, name, cid));
    }

    public void updateMemberOnline(MapleGuildCharacter character, boolean online, int channel) {
        Integer id = character.character.getGuild().getId();
        findById(id).ifPresent(it -> it.updateMemberOnline(character.character.getId(), online, channel));
    }

    public void updateMemberInfo(MapleGuildCharacter mc) {
        Integer id = mc.character.getGuild().getId();
        findById(id).ifPresent(it -> it.updateMemberInfo(mc));
    }

    public void memberLeave(MapleGuildCharacter character) {
        Integer id = character.character.getGuild().getId();
        findById(id).ifPresent(it -> it.memberLeave(character));
    }

    public void broadcast(int gid, MaplePacket message) {
        findById(gid).ifPresent(it -> it.broadcast(message));
    }

    public void chat(int gid, String name, int cid, String msg) {
        findById(gid).ifPresent(it -> it.chat(name, cid, msg));
    }

    public void notice(int gid, String notice) {
        findById(gid).ifPresent(it -> it.notice(notice));
    }

    public void changeRank(int gid, int cid, int newRank) {
        findById(gid).ifPresent(it -> it.changeRank(cid, newRank));
    }

    public void changeRankTitle(int gid, String[] ranks) {
        findById(gid).ifPresent(it -> it.changeRankTitle(ranks));
    }

    public void setEmblem(int gid, short bg, byte bgcolor, short logo, byte logocolor) {
        findById(gid).ifPresent(it -> it.setEmblem(bg, bgcolor, logo, logocolor));
    }

    public void disband(int gid) {
        findById(gid).ifPresent(MapleGuild::disbandGuild);
        idGuildCached.invalidate(gid);
    }

    public void delete(int guildId, int characterId) {
        //ensure it's loaded on world server
        //setGuildMemberOnline(mc, false, -1);
        findById(guildId).ifPresent(it -> {
            MapleGuildCharacter character = it.getMGC(characterId);
            if (character != null) {
                if (character.character.getGuildRank() > 1) {
                    it.memberLeave(character);
                } else {
                    it.disbandGuild();
                }
            }
        });
    }

    public boolean increaseCapacity(int gid) {
        return findById(gid).map(MapleGuild::increaseCapacity).orElse(false);
    }

    public void gainGP(int gid, int amount) {
        findById(gid).ifPresent(it -> it.gainGP(amount));
    }

    public int getGP(int gid) {
        return findById(gid).map(MapleGuild::getGP).orElse(0);
    }

    public int getInvitedId(int gid) {
        return findById(gid).map(MapleGuild::getInvitedId).orElse(0);
    }

    public void setInvitedId(int gid, int inviteId) {
        findById(gid).ifPresent(it -> it.setInvitedId(inviteId));
    }

    public int getGuildLeader(String guildName) {
        return findByName(guildName).map(MapleGuild::getLeaderId).orElse(0);
    }

    public void save() {
        if (properties.isDebug()) {
            LOGGER.debug("准备保存所有家族数据");
        }
        for (MapleGuild guild : idGuildCached.asMap().values()) {
            guild.writeToDB(false);
        }
    }

    public List<MapleBBSThread> getBBS(int gid) {
        return findById(gid)
                .map(MapleGuild::getBBS)
                .orElse(Collections.emptyList());
    }

    public int addBBSThread(int guildid, String title, String text, int icon, boolean bNotice, int posterID) {
        return findById(guildid)
                .map(it -> it.addBBSThread(title, text, icon, bNotice, posterID))
                .orElse(-1);
    }

    public void editBBSThread(int guildid, int localthreadid, String title, String text, int icon, int posterID, int guildRank) {
        findById(guildid).ifPresent(it -> it.editBBSThread(localthreadid, title, text, icon, posterID, guildRank));
    }

    public void deleteBBSThread(int guildid, int localthreadid, int posterID, int guildRank) {
        findById(guildid).ifPresent(it -> it.deleteBBSThread(localthreadid, posterID, guildRank));
    }

    public void addBBSReply(int guildid, int localthreadid, String text, int posterID) {
        findById(guildid).ifPresent(it -> it.addBBSReply(localthreadid, text, posterID));
    }

    public void deleteBBSReply(int guildid, int localthreadid, int replyid, int posterID, int guildRank) {
        findById(guildid).ifPresent(it -> it.deleteBBSReply(localthreadid, replyid, posterID, guildRank));
    }

    public void changeEmblem(int gid, int affectedPlayers, MapleGuildSummary mgs) {
        short logoBG = mgs.getLogoBG();
        byte logoBGColor = mgs.getLogoBGColor();
        short logo = mgs.getLogo();
        byte logoColor = mgs.getLogoColor();
        World.Broadcast.sendGuildPacket(affectedPlayers,
                MaplePacketCreator.guildEmblemChange(gid, logoBG, logoBGColor, logo, logoColor),
                -1, gid);
        setGuildAndRank(affectedPlayers, -1, -1, -1); //重生玩家
    }

    public void setGuildAndRank(int cid, int guildid, int rank, int alliancerank) {
        int channelId = channelService.findById(cid);
        if (channelId == ChannelService.DEFAULT_CHANNEL) {
            if (properties.isDebug()) {
                LOGGER.debug("错误：无法在给定的频道中找到玩家 {}，可能已下线", cid);
            }
            return;
        }

        PlayerStorage storage;
        if (channelId == ChannelService.MTS_CHANNEL) {
            storage = CashShopServer.getPlayerStorageMTS();
        } else if (channelId == ChannelService.CS_CHANNEL) {
            storage = CashShopServer.getPlayerStorage();
        } else {
            storage = ChannelServer.getInstance(channelId).getPlayerStorage();
        }

        MapleCharacter character = storage.getCharacterById(cid);
        if (character == null) {
            return;
        }
        boolean bDifferentGuild;
        if (guildid == -1 && rank == -1) { //just need a respawn
            bDifferentGuild = true;
        } else {
            bDifferentGuild = guildid != character.getGuildId();
            character.setGuildId(guildid);
            character.setGuildRank((byte) rank);
            character.setAllianceRank((byte) alliancerank);
            character.saveGuildStatus();
        }
        if (bDifferentGuild && channelId > 0) {
            character.getMap().broadcastMessage(character, MaplePacketCreator.removePlayerFromMap(cid), false);
            character.getMap().broadcastMessage(character, MaplePacketCreator.spawnPlayerMapobject(character), false);
        }
    }

}
