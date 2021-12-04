package com.github.mrzhqiang.maplestory.service;

import client.MapleCharacter;
import handling.MaplePacket;
import handling.channel.ChannelServer;
import handling.world.World;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public final class BroadcastService {

    public void broadcastSmega(byte[] message) {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            cs.broadcastSmega(message);
        }
    }

    public void broadcastGMMessage(byte[] message) {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            cs.broadcastGMMessage(message);
        }
    }

    public void broadcastMessage(byte[] message) {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            cs.broadcastMessage(message);
        }
    }

    public void sendPacket(List<Integer> targetIds, MaplePacket packet, int exception) {
        MapleCharacter c;
        for (int i : targetIds) {
            if (i == exception) {
                continue;
            }
            int ch = World.Find.findChannel(i);
            if (ch < 0) {
                continue;
            }
            c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(i);
            if (c != null) {
                c.getClient().getSession().write(packet);
            }
        }
    }

    public void sendGuildPacket(int targetIds, MaplePacket packet, int exception, int guildid) {
        if (targetIds == exception) {
            return;
        }
        int ch = World.Find.findChannel(targetIds);
        if (ch < 0) {
            return;
        }
        final MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(targetIds);
        if (c != null && c.getGuildId() == guildid) {
            c.getClient().getSession().write(packet);
        }
    }

    public void sendFamilyPacket(int targetIds, MaplePacket packet, int exception, int guildid) {
        if (targetIds == exception) {
            return;
        }
        int ch = World.Find.findChannel(targetIds);
        if (ch < 0) {
            return;
        }
        final MapleCharacter c = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(targetIds);
        if (c != null && c.getFamilyId() == guildid) {
            c.getClient().getSession().write(packet);
        }
    }

    public void broadcastMessage(MaplePacket serverNotice) {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            cs.broadcastMessage(serverNotice);
        }
    }

}
