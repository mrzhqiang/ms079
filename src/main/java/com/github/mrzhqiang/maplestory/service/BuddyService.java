package com.github.mrzhqiang.maplestory.service;

import client.BuddyEntry;
import client.BuddyList;
import client.MapleCharacter;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import handling.channel.ChannelServer;
import handling.world.World;
import tools.MaplePacketCreator;

import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public final class BuddyService {

    public void buddyChat(int[] recipientCharacterIds, int cidFrom, String nameFrom, String chattext) {
        for (int characterId : recipientCharacterIds) {
            int ch = World.Find.findChannel(characterId);
            if (ch > 0) {
                MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(characterId);
                if (chr != null && chr.getBuddylist().containsVisible(cidFrom)) {
                    chr.getClient().getSession().write(MaplePacketCreator.multiChat(nameFrom, chattext, 0));
                }
            }
        }
    }

    private void updateBuddies(int characterId, int channel, Collection<Integer> buddies, boolean offline, int gmLevel, boolean isHidden) {
        for (Integer buddy : buddies) {
            int ch = World.Find.findChannel(buddy);
            if (ch > 0) {
                MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(buddy);
                if (chr != null) {
                    BuddyEntry ble = chr.getBuddylist().get(characterId);
                    if (ble != null && ble.isVisible()) {
                        int mcChannel;
                        if (offline || (isHidden && chr.getGMLevel() < gmLevel)) {
                            ble.setChannel(-1);
                            mcChannel = -1;
                        } else {
                            ble.setChannel(channel);
                            mcChannel = channel - 1;
                        }
                        chr.getBuddylist().put(ble);
                        chr.getClient().sendPacket(MaplePacketCreator.updateBuddyChannel(ble.getCharacterId(), mcChannel));
                    }
                }
            }
        }
    }

    public void buddyChanged(int cid, DCharacter character, int channel, BuddyList.BuddyOperation operation, String group) {
        int ch = World.Find.findChannel(cid);
        if (ch > 0) {
            final MapleCharacter addChar = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterById(cid);
            if (addChar != null) {
                final BuddyList buddylist = addChar.getBuddylist();
                switch (operation) {
                    case ADDED:
                        if (buddylist.contains(character.getId())) {
                            buddylist.put(new BuddyEntry(character, group, channel, true));
                            addChar.getClient().getSession().write(MaplePacketCreator.updateBuddyChannel(character.getId(), channel - 1));
                        }
                        break;
                    case DELETED:
                        if (buddylist.contains(character.getId())) {
                            buddylist.put(new BuddyEntry(character, group, -1, buddylist.get(character.getId()).isVisible()));
                            addChar.getClient().getSession().write(MaplePacketCreator.updateBuddyChannel(character.getId(), -1));
                        }
                        break;
                }
            }
        }
    }

    public BuddyList.BuddyAddResult requestBuddyAdd(String addName, int channelFrom, DCharacter character) {
        int ch = World.Find.findChannel(character.getId());
        if (ch > 0) {
            MapleCharacter addChar = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(addName);
            if (addChar != null) {
                final BuddyList buddylist = addChar.getBuddylist();
                if (buddylist.isFull()) {
                    return BuddyList.BuddyAddResult.BUDDYLIST_FULL;
                }
                if (!buddylist.contains(character.getId())) {
                    buddylist.addBuddyRequest(addChar.getClient(), channelFrom, character);
                } else if (buddylist.containsVisible(character.getId())) {
                    return BuddyList.BuddyAddResult.ALREADY_ON_LIST;
                }
            }
        }
        return BuddyList.BuddyAddResult.OK;
    }

    public void loggedOn(String name, int characterId, int channel, Collection<Integer> buddies, int gmLevel, boolean isHidden) {
        updateBuddies(characterId, channel, buddies, false, gmLevel, isHidden);
    }

    public void loggedOff(String name, int characterId, int channel, Collection<Integer> buddies, int gmLevel, boolean isHidden) {
        updateBuddies(characterId, channel, buddies, true, gmLevel, isHidden);
    }

}
