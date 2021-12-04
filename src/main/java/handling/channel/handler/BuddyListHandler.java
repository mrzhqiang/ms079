package handling.channel.handler;

import client.BuddyEntry;
import client.BuddyList;
import client.BuddyList.BuddyAddResult;
import client.BuddyList.BuddyOperation;
import client.CharacterNameAndId;
import client.MapleCharacter;
import client.MapleClient;
import com.github.mrzhqiang.maplestory.domain.DBuddy;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDBuddy;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.google.common.collect.Lists;
import handling.channel.ChannelServer;
import handling.world.World;
import io.ebean.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

import java.util.Optional;

import static client.BuddyList.BuddyOperation.ADDED;
import static client.BuddyList.BuddyOperation.DELETED;

public class BuddyListHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuddyListHandler.class);

    private static final class CharacterIdNameBuddyCapacity extends CharacterNameAndId {

        public CharacterIdNameBuddyCapacity(DCharacter character, String group) {
            super(character, group);
        }

        public int getBuddyCapacity() {
            return character.getBuddyCapacity();
        }
    }

    private static final void nextPendingRequest(final MapleClient c) {
        BuddyEntry pendingBuddyRequest = c.getPlayer().getBuddylist().pollPendingRequest();
        if (pendingBuddyRequest != null) {
            c.getSession().write(MaplePacketCreator.requestBuddylistAdd(pendingBuddyRequest.character));
        }
    }

    private static CharacterIdNameBuddyCapacity getCharacterIdAndNameFromDatabase(String name, String group) {
        DCharacter one = new QDCharacter().name.like(name).findOne();
        CharacterIdNameBuddyCapacity ret = null;
        if (one != null) {
            if (one.getGm() == 0) {
                ret = new CharacterIdNameBuddyCapacity(one, group);
            }
        }
        return ret;
    }

    public static void BuddyOperation(SeekableLittleEndianAccessor slea, MapleClient c) {
         /* CharacterNameAndId pendingBuddyRequest = c.getPlayer().getBuddylist().pollPendingRequest();
        if (pendingBuddyRequest != null) {
            c.getSession().write(MaplePacketCreator.requestBuddylistAdd(pendingBuddyRequest.getId(), pendingBuddyRequest.getName()));
        }*/

        int mode = slea.readByte();
        BuddyList buddylist = c.getPlayer().getBuddylist();

        if (mode == 1) { // add
            String addName = slea.readMapleAsciiString();
            String groupName = slea.readMapleAsciiString();
            BuddyEntry ble = buddylist.get(addName);

            if (addName.length() > 13 || groupName.length() > 16) {
                return;
            }
            if (ble != null && (ble.getGroup().equals(groupName) || !ble.isVisible())) {
                c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 11));
            } else if (ble != null && ble.isVisible()) {
                ble.setGroup(groupName);
                c.getSession().write(MaplePacketCreator.updateBuddylist(buddylist.getBuddies()));
                c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 13));
            } else if (buddylist.isFull()) {
                c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 11));
            } else {
                CharacterIdNameBuddyCapacity charWithId = null;
                int channel = World.Find.findChannel(addName);
                MapleCharacter otherChar = null;
                if (channel > 0) {
                    otherChar = ChannelServer.getInstance(channel).getPlayerStorage().getCharacterByName(addName);
                    if (!otherChar.isGM() || c.getPlayer().isGM()) {
                        charWithId = new CharacterIdNameBuddyCapacity(otherChar.character, groupName);
                    }
                } else {
                    charWithId = getCharacterIdAndNameFromDatabase(addName, groupName);
                }

                if (charWithId != null) {
                    BuddyAddResult buddyAddResult = null;
                    if (channel > 0) {
                        buddyAddResult = World.Buddy.requestBuddyAdd(addName, c.getChannel(), c.getPlayer().character);
                    } else {
                        int count = new QDBuddy().owner.eq(charWithId.character).pending.eq(false).findCount();
                        if (count >= charWithId.getBuddyCapacity()) {
                            buddyAddResult = BuddyAddResult.BUDDYLIST_FULL;
                        }

                        // SELECT pending FROM buddies WHERE characterid = ? AND buddyid = ?
                        Optional<DBuddy> optional = new QDBuddy().owner.eq(charWithId.character).buddies.id.eq(c.getPlayer().getId()).findOneOrEmpty();
                        if (optional.isPresent()) {
                            buddyAddResult = BuddyAddResult.ALREADY_ON_LIST;
                        }
                    }
                    if (buddyAddResult == BuddyAddResult.BUDDYLIST_FULL) {
                        c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 12));
                    } else {
                        int displayChannel = -1;
                        int otherCid = charWithId.getId();
                        if (buddyAddResult == BuddyAddResult.ALREADY_ON_LIST && channel > 0) {
                            displayChannel = channel;
                            notifyRemoteChannel(c, channel, otherCid, groupName, ADDED);
                        } else if (buddyAddResult != BuddyAddResult.ALREADY_ON_LIST && channel > 0) {
                            DBuddy buddy = new DBuddy();
                            buddy.setOwner(charWithId.character);
                            buddy.setBuddies(Lists.newArrayList(DB.reference(DCharacter.class, c.getPlayer().getId())));
                            buddy.setGroupName(groupName);
                            buddy.setPending(true);
                            buddy.save();
                        }
                        buddylist.put(new BuddyEntry(charWithId.character, groupName, displayChannel, true));
                        c.getSession().write(MaplePacketCreator.updateBuddylist(buddylist.getBuddies()));
                    }
                } else {
                    c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 15));
                }
            }
            nextPendingRequest(c);
        } else if (mode == 2) { // accept buddy
            int otherCid = slea.readInt();
            if (!buddylist.isFull()) {
                int channel = World.Find.findChannel(otherCid);
                DCharacter one;
                if (channel < 0) {
                    one = new QDCharacter().id.eq(otherCid).findOne();
                } else {
                    MapleCharacter otherChar = ChannelServer.getInstance(channel).getPlayerStorage().getCharacterById(otherCid);
                    one = otherChar.character;
                }
                if (one != null) {
                    buddylist.put(new BuddyEntry(one, "ㄤ", channel, true));
                    c.getSession().write(MaplePacketCreator.updateBuddylist(buddylist.getBuddies()));
                    notifyRemoteChannel(c, channel, otherCid, "ㄤ", ADDED);
                }
            } else {
                c.getSession().write(MaplePacketCreator.buddylistMessage((byte) 11));
            }
            nextPendingRequest(c);
        } else if (mode == 3) { // delete
            final int otherCid = slea.readInt();
            final BuddyEntry blz = buddylist.get(otherCid);
            if (blz != null && blz.isVisible()) {
                notifyRemoteChannel(c, World.Find.findChannel(otherCid), otherCid, blz.getGroup(), DELETED);
            }
            buddylist.remove(otherCid);
            c.getSession().write(MaplePacketCreator.updateBuddylist(c.getPlayer().getBuddylist().getBuddies()));
            nextPendingRequest(c);
        } else {
            LOGGER.debug("Unknown buddylist: " + slea);
        }
    }

    private static void notifyRemoteChannel(MapleClient c, int remoteChannel, int otherCid, String group, BuddyOperation operation) {
        MapleCharacter player = c.getPlayer();

        if (remoteChannel > 0) {
            World.Buddy.buddyChanged(otherCid, player.character, c.getChannel(), operation, group);
        }
    }
}
