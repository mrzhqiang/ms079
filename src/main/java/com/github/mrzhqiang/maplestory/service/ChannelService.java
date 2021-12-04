package com.github.mrzhqiang.maplestory.service;

import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import handling.channel.ChannelServer;
import handling.world.CharacterIdChannelPair;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public final class ChannelService {

    /**
     * 默认频道。
     * <p>
     * 刚登录还没有选择频道的时候，默认分配到此频道。
     */
    public static final int DEFAULT_CHANNEL = -1;
    /**
     * 现金商城频道。
     */
    public static final int CS_CHANNEL = -10;
    /**
     * 拍卖频道。
     * <p>
     * 目前好像已废弃，待确认。
     */
    public static final int MTS_CHANNEL = -20;

    private final Cache<Integer, Integer> idChannelCached;
    private final Cache<String, Integer> nameChannelCached;

    @Inject
    public ChannelService(ServerProperties properties) {
        CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder()
                .maximumSize(properties.getOnlineLimit());
        // 开启调试模式，才记录缓存的细节
        if (properties.isDebug()) {
            cacheBuilder.recordStats();
        }
        // build 方法返回的缓存容器，需要手动 put 缓存值
        this.idChannelCached = cacheBuilder.build();
        this.nameChannelCached = cacheBuilder.build();
    }

    /**
     * 初始化。
     * <p>
     * 虽然没有做任何事情，但保留这个方法，可以在后续重构中抽象出来 service 接口。
     */
    public void init() {
        // do nothing
    }

    public void addCached(Integer id, String name, Integer channel) {
        if (id == null || name == null) {
            return;
        }
        idChannelCached.put(id, channel);
        nameChannelCached.put(name.toLowerCase(), channel);
    }

    public Integer findById(Integer id) {
        if (id == null) {
            return DEFAULT_CHANNEL;
        }
        Integer channel = idChannelCached.getIfPresent(id);
        if (channel == null) {
            return DEFAULT_CHANNEL;
        }
        if (channel != CS_CHANNEL && channel != MTS_CHANNEL && !ChannelServer.container(channel)) {
            removeById(id);
            return DEFAULT_CHANNEL;
        }
        return channel;
    }

    public Integer findByName(String name) {
        if (name == null) {
            return DEFAULT_CHANNEL;
        }
        Integer ret = nameChannelCached.getIfPresent(name.toLowerCase());
        if (ret == null) {
            return DEFAULT_CHANNEL;
        }
        if (ret != CS_CHANNEL && ret != MTS_CHANNEL && !ChannelServer.container(ret)) {
            removeByName(name);
            return DEFAULT_CHANNEL;
        }
        return ret;
    }

    public void removeById(Integer id) {
        if (id == null) {
            return;
        }
        idChannelCached.invalidate(id);
    }

    public void removeByName(String name) {
        if (name == null) {
            return;
        }
        nameChannelCached.invalidate(name.toLowerCase());
    }

    public void removeCached(Integer id, String name) {
        removeById(id);
        removeByName(name);
    }

    public List<CharacterIdChannelPair> findBuddy(Collection<Integer> characterIds) {
        List<CharacterIdChannelPair> foundsChars = Lists.newArrayListWithCapacity(characterIds.size());
        for (Integer id : characterIds) {
            int channel = findById(id);
            if (channel > 0) {
                foundsChars.add(new CharacterIdChannelPair(id, channel));
            }
        }
        Collections.sort(foundsChars);
        return foundsChars;
    }

    public boolean isConnected(String name) {
        Integer channel = findByName(name);
        return channel != null && channel > 0;
    }
}
