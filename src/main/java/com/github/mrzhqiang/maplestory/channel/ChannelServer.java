package com.github.mrzhqiang.maplestory.channel;

import com.github.mrzhqiang.maplestory.api.RunnableServer;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.Map;

public final class ChannelServer implements RunnableServer {

    private final Map<Integer, MapleChannel> channelMap = Maps.newConcurrentMap();

    private final ServerProperties properties;

    private final int count;

    public ChannelServer(ServerProperties properties) {
        this.properties = properties;
        int channelCount = properties.getChannelCount();
        if (channelCount > 10) {
            channelCount = 10;
        }
        this.count = channelCount;
    }

    @Override
    public void init() {
        for (int i = 0; i < count; i++) {
            channelMap.put(i + 1, MapleChannel.of());
        }
    }

    @Override
    public void run() {

    }

    @Override
    public void close() throws IOException {

    }
}
