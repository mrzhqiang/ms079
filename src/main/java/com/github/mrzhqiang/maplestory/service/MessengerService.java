package com.github.mrzhqiang.maplestory.service;

import client.MapleCharacter;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import handling.channel.ChannelServer;
import handling.world.MapleMessenger;
import handling.world.MapleMessengerCharacter;
import handling.world.World;
import tools.MaplePacketCreator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public final class MessengerService {

    private final AtomicInteger runningMessengerId = new AtomicInteger();

    private final Cache<Integer, MapleMessenger> cached;
    private final ChannelService channelService;

    @Inject
    public MessengerService(ServerProperties properties, ChannelService channelService) {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder()
                .maximumSize(100000);
        if (properties.isDebug()) {
            builder.recordStats();
        }
        this.cached = builder.build();
        this.channelService = channelService;
    }

    public void init() {
        runningMessengerId.set(1);
    }

    public MapleMessenger createMessenger(MapleMessengerCharacter chrfor) {
        int messengerid = runningMessengerId.getAndIncrement();
        MapleMessenger messenger = new MapleMessenger(messengerid, chrfor);
        cached.put(messenger.getId(), messenger);
        return messenger;
    }

    public void declineChat(String target, String namefrom) {
        int ch = World.Find.findChannel(target);
        if (ch > 0) {
            ChannelServer cs = ChannelServer.getInstance(ch);
            MapleCharacter chr = cs.getPlayerStorage().getCharacterByName(target);
            if (chr != null) {
                MapleMessenger messenger = chr.getMessenger();
                if (messenger != null) {
                    chr.getClient().getSession().write(MaplePacketCreator.messengerNote(namefrom, 5, 0));
                }
            }
        }
    }

    public MapleMessenger getMessenger(int messengerid) {
        return cached.getIfPresent(messengerid);
    }

    public void leaveMessenger(int messengerid, MapleMessengerCharacter target) {
        MapleMessenger messenger = getMessenger(messengerid);
        if (messenger == null) {
            throw new IllegalArgumentException("No messenger with the specified messengerid exists");
        }
        int position = messenger.getPositionByName(target.getName());
        messenger.removeMember(target);

        for (MapleMessengerCharacter mmc : messenger.getMembers()) {
            if (mmc != null) {
                int ch = World.Find.findChannel(mmc.getId());
                if (ch > 0) {
                    MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(mmc.getName());
                    if (chr != null) {
                        chr.getClient().getSession().write(MaplePacketCreator.removeMessengerPlayer(position));
                    }
                }
            }
        }
    }

    public void silentLeaveMessenger(int messengerid, MapleMessengerCharacter target) {
        MapleMessenger messenger = getMessenger(messengerid);
        if (messenger == null) {
            throw new IllegalArgumentException("No messenger with the specified messengerid exists");
        }
        messenger.silentRemoveMember(target);
    }

    public void silentJoinMessenger(int messengerid, MapleMessengerCharacter target) {
        MapleMessenger messenger = getMessenger(messengerid);
        if (messenger == null) {
            throw new IllegalArgumentException("No messenger with the specified messengerid exists");
        }
        messenger.silentAddMember(target);
    }

    public void updateMessenger(int messengerid, String namefrom, int fromchannel) {
        MapleMessenger messenger = getMessenger(messengerid);
        int position = messenger.getPositionByName(namefrom);

        for (MapleMessengerCharacter messengerchar : messenger.getMembers()) {
            if (messengerchar != null && !messengerchar.getName().equals(namefrom)) {
                int ch = World.Find.findChannel(messengerchar.getName());
                if (ch > 0) {
                    MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                    if (chr != null) {
                        MapleCharacter from = ChannelServer.getInstance(fromchannel).getPlayerStorage().getCharacterByName(namefrom);
                        chr.getClient().getSession().write(MaplePacketCreator.updateMessengerPlayer(namefrom, from, position, fromchannel - 1));
                    }
                }
            }
        }
    }

    public void joinMessenger(int messengerid, MapleMessengerCharacter target, String from, int fromchannel) {
        MapleMessenger messenger = getMessenger(messengerid);
        if (messenger == null) {
            throw new IllegalArgumentException("No messenger with the specified messengerid exists");
        }
        messenger.addMember(target);
        int position = messenger.getPositionByName(target.getName());
        for (MapleMessengerCharacter messengerchar : messenger.getMembers()) {
            if (messengerchar != null) {
                int mposition = messenger.getPositionByName(messengerchar.getName());
                int ch = World.Find.findChannel(messengerchar.getName());
                if (ch > 0) {
                    MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                    if (chr != null) {
                        if (!messengerchar.getName().equals(from)) {
                            MapleCharacter fromCh = ChannelServer.getInstance(fromchannel).getPlayerStorage().getCharacterByName(from);
                            chr.getClient().getSession().write(MaplePacketCreator.addMessengerPlayer(from, fromCh, position, fromchannel - 1));
                            fromCh.getClient().getSession().write(MaplePacketCreator.addMessengerPlayer(chr.getName(), chr, mposition, messengerchar.getChannel() - 1));
                        } else {
                            chr.getClient().getSession().write(MaplePacketCreator.joinMessenger(mposition));
                        }
                    }
                }
            }
        }
    }

    public void messengerChat(int messengerid, String chattext, String namefrom) {
        MapleMessenger messenger = getMessenger(messengerid);
        if (messenger == null) {
            throw new IllegalArgumentException("No messenger with the specified messengerid exists");
        }

        for (MapleMessengerCharacter messengerchar : messenger.getMembers()) {
            if (messengerchar != null && !messengerchar.getName().equals(namefrom)) {
                int ch = World.Find.findChannel(messengerchar.getName());
                if (ch > 0) {
                    MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                    if (chr != null) {

                        chr.getClient().getSession().write(MaplePacketCreator.messengerChat(chattext));
                    }
                }
            } //Whisp Monitor Code
            else if (messengerchar != null) {
                int ch = World.Find.findChannel(messengerchar.getName());
                if (ch > 0) {
                    MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(messengerchar.getName());
                }
            }
            //
        }
    }

    public void messengerInvite(String sender, int messengerid, String target, int fromchannel, boolean gm) {
        if (channelService.isConnected(target)) {
            int ch = World.Find.findChannel(target);
            if (ch > 0) {
                MapleCharacter from = ChannelServer.getInstance(fromchannel).getPlayerStorage().getCharacterByName(sender);
                MapleCharacter targeter = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(target);
                if (targeter != null && targeter.getMessenger() == null) {
                    if (!targeter.isGM() || gm) {
                        targeter.getClient().getSession().write(MaplePacketCreator.messengerInvite(sender, messengerid));
                        from.getClient().getSession().write(MaplePacketCreator.messengerNote(target, 4, 1));
                    } else {
                        from.getClient().getSession().write(MaplePacketCreator.messengerNote(target, 4, 0));
                    }
                } else {
                    from.getClient().getSession().write(MaplePacketCreator.messengerChat(sender + " : " + target + " is already using Maple Messenger"));
                }
            }
        }

    }
}
