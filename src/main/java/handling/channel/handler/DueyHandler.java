package handling.channel.handler;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.inventory.IItem;
import client.inventory.ItemFlag;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import com.github.mrzhqiang.maplestory.domain.DDueyPackage;
import com.github.mrzhqiang.maplestory.domain.query.QDDueyPackage;
import constants.GameConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.MapleDueyActions;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.data.input.SeekableLittleEndianAccessor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DueyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DueyHandler.class);

    /*
     * 19 = Successful
     * 18 = One-of-a-kind Item is already in Reciever's delivery
     * 17 = The Character is unable to recieve the parcel
     * 15 = Same account
     * 14 = Name does not exist
     */
    public static final void DueyOperation(final SeekableLittleEndianAccessor slea, final MapleClient c) {

        final byte operation = slea.readByte();

        switch (operation) {
            case 1: { // Start Duey, 13 digit AS
                final String AS13Digit = slea.readMapleAsciiString();
                //		int unk = slea.readInt(); // Theres an int here, value = 1
                //  9 = error
                final int conv = c.getPlayer().getConversation();

                if (conv == 2) { // Duey
                    c.getSession().write(MaplePacketCreator.sendDuey((byte) 10, loadItems(c.getPlayer())));
                }
                break;
            }
            case 3: { // Send Item
                if (c.getPlayer().getConversation() != 2) {
                    return;
                }
                final byte inventId = slea.readByte();
                final short itemPos = slea.readShort();
                final short amount = slea.readShort();
                final int mesos = slea.readInt();
                final String recipient = slea.readMapleAsciiString();
                boolean quickdelivery = slea.readByte() > 0;

                final int finalcost = mesos + GameConstants.getTaxAmount(mesos) + (quickdelivery ? 0 : 5000);

                if (mesos >= 0 && mesos <= 100000000 && c.getPlayer().getMeso() >= finalcost) {
                    final int accid = MapleCharacterUtil.getIdByName(recipient);
                    if (accid != -1) {
                        if (accid != c.getAccID()) {
                            boolean recipientOn = false;
                            MapleClient rClient = null;
                            /*        try {
                             int channel = c.getChannelServer().getWorldInterface().find(recipient);
                             if (channel > -1) {
                             recipientOn = true;
                             ChannelServer rcserv = ChannelServer.getInstance(channel);
                             rClient = rcserv.getPlayerStorage().getCharacterByName(recipient).getClient();
                             }
                             } catch (RemoteException re) {
                             c.getChannelServer().reconnectWorld();
                             }*/

                            if (inventId > 0) {
                                final MapleInventoryType inv = MapleInventoryType.getByType(inventId);
                                final IItem item = c.getPlayer().getInventory(inv).getItem((byte) itemPos);
                                if (item == null) {
                                    c.getSession().write(MaplePacketCreator.sendDuey((byte) 17, null)); // Unsuccessfull
                                    return;
                                }
                                final int flag = item.getFlag();
                                if (ItemFlag.UNTRADEABLE.check(flag) || ItemFlag.LOCK.check(flag)) {
                                    c.getSession().write(MaplePacketCreator.enableActions());
                                    return;
                                }
                                if (c.getPlayer().getItemQuantity(item.getItemId(), false) >= amount) {
                                    final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                                    if (!ii.isDropRestricted(item.getItemId()) && !ii.isAccountShared(item.getItemId())) {
                                        if (addItemToDB(item, amount, mesos, c.getPlayer().getName(), accid, recipientOn)) {
                                            if (GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId())) {
                                                MapleInventoryManipulator.removeFromSlot(c, inv, (byte) itemPos, item.getQuantity(), true);
                                            } else {
                                                MapleInventoryManipulator.removeFromSlot(c, inv, (byte) itemPos, amount, true, false);
                                            }
                                            c.getPlayer().gainMeso(-finalcost, false);
                                            c.getSession().write(MaplePacketCreator.sendDuey((byte) 19, null)); // Successfull
                                        } else {
                                            c.getSession().write(MaplePacketCreator.sendDuey((byte) 17, null)); // Unsuccessful
                                        }
                                    } else {
                                        c.getSession().write(MaplePacketCreator.sendDuey((byte) 17, null)); // Unsuccessfull
                                    }
                                } else {
                                    c.getSession().write(MaplePacketCreator.sendDuey((byte) 17, null)); // Unsuccessfull
                                }
                            } else {
                                if (addMesoToDB(mesos, c.getPlayer().getName(), accid, recipientOn)) {
                                    c.getPlayer().gainMeso(-finalcost, false);

                                    c.getSession().write(MaplePacketCreator.sendDuey((byte) 19, null)); // Successfull
                                } else {
                                    c.getSession().write(MaplePacketCreator.sendDuey((byte) 17, null)); // Unsuccessfull
                                }
                            }
                            //                            if (recipientOn && rClient != null) {
                            //                              rClient.getSession().write(MaplePacketCreator.sendDueyMSG(Actions.PACKAGE_MSG.getCode()));
                            //                        }
                        } else {
                            c.getSession().write(MaplePacketCreator.sendDuey((byte) 15, null)); // Same acc error
                        }
                    } else {
                        c.getSession().write(MaplePacketCreator.sendDuey((byte) 14, null)); // Name does not exist
                    }
                } else {
                    c.getSession().write(MaplePacketCreator.sendDuey((byte) 12, null)); // Not enough mesos
                }
                break;
            }
            case 5: { // Recieve Package
                if (c.getPlayer().getConversation() != 2) {
                    return;
                }
                final int packageid = slea.readInt();
                //LOGGER.debug("Item attempted : " + packageid);
                final MapleDueyActions dp = loadSingleItem(packageid, c.getPlayer().getId());
                if (dp == null) {
                    return;
                }
                if (dp.getItem() != null && !MapleInventoryManipulator.checkSpace(c, dp.getItem().getItemId(), dp.getItem().getQuantity(), dp.getItem().getOwner())) {
                    c.getSession().write(MaplePacketCreator.sendDuey((byte) 16, null)); // Not enough Space
                    return;
                } else if (dp.getMesos() < 0 || (dp.getMesos() + c.getPlayer().getMeso()) < 0) {
                    c.getSession().write(MaplePacketCreator.sendDuey((byte) 17, null)); // Unsuccessfull
                    return;
                }
                removeItemFromDB(packageid, c.getPlayer().getId()); // Remove first
                //LOGGER.debug("Item removed : " + packageid);
                if (dp.getItem() != null) {
                    MapleInventoryManipulator.addFromDrop(c, dp.getItem(), false);
                }
                if (dp.getMesos() != 0) {
                    c.getPlayer().gainMeso(dp.getMesos(), false);
                }
                c.getSession().write(MaplePacketCreator.removeItemFromDuey(false, packageid));
                break;
            }
            case 6: { // Remove package
                if (c.getPlayer().getConversation() != 2) {
                    return;
                }
                final int packageid = slea.readInt();
                removeItemFromDB(packageid, c.getPlayer().getId());
                c.getSession().write(MaplePacketCreator.removeItemFromDuey(true, packageid));
                break;
            }
            case 8: { // Close Duey
                c.getPlayer().setConversation(0);
                break;
            }
            default: {
                LOGGER.debug("Unhandled Duey operation : " + slea.toString());
                break;
            }
        }

    }

    private static boolean addMesoToDB(int mesos, String sName, int recipientID, boolean isOn) {
        DDueyPackage aPackage = new DDueyPackage();
        aPackage.setReceiverId(recipientID);
        aPackage.setSenderName(sName);
        aPackage.setMesos(mesos);
        aPackage.setTimestamp(LocalDateTime.now());
        aPackage.setChecked(isOn);
        aPackage.setType(3);
        aPackage.save();
        return true;
    }

    private static boolean addItemToDB(IItem item, int quantity, int mesos, String sName, int recipientID, boolean isOn) {
        DDueyPackage aPackage = new DDueyPackage();
        aPackage.setReceiverId(recipientID);
        aPackage.setSenderName(sName);
        aPackage.setMesos(mesos);
        aPackage.setTimestamp(LocalDateTime.now());
        aPackage.setChecked(isOn);
        aPackage.setType(item.getType());
        aPackage.save();
        //TODO
        ItemLoader.saveItems(Collections.singletonList(new Pair<>(item, GameConstants.getInventoryType(item.getItemId()))), null);
        return true;
    }

    public static List<MapleDueyActions> loadItems(MapleCharacter chr) {
        return new QDDueyPackage().receiverId.eq(chr.getId()).findStream()
                .map(data -> {
                    MapleDueyActions dueypack = getItemByPID(data.getId());
                    dueypack.setSender(data.getSenderName());
                    dueypack.setMesos(data.getMesos());
                    dueypack.setSentTime(data.getTimestamp());
                    return dueypack;
                })
                .collect(Collectors.toList());
    }

    public static MapleDueyActions loadSingleItem(int packageid, int charid) {
        return new QDDueyPackage().id.eq(packageid).receiverId.eq(charid).findOneOrEmpty()
                .map(data -> {
                    MapleDueyActions dueypack = getItemByPID(data.getId());
                    dueypack.setSender(data.getSenderName());
                    dueypack.setMesos(data.getMesos());
                    dueypack.setSentTime(data.getTimestamp());
                    return dueypack;
                }).orElse(null);
    }

    public static void reciveMsg(MapleClient c, int recipientId) {
        DDueyPackage one = new QDDueyPackage().receiverId.eq(recipientId).findOne();
        if (one != null) {
            one.setChecked(true);
            one.save();
        }
    }

    private static void removeItemFromDB(int packageid, int charid) {
        new QDDueyPackage().id.eq(packageid).receiverId.eq(charid).delete();
    }

    private static MapleDueyActions getItemByPID(int packageid) {
        try {
            Map<Integer, Pair<IItem, MapleInventoryType>> iter = ItemLoader.loadItems(6,false, packageid);
            if (iter != null && iter.size() > 0) {
                for (Pair<IItem, MapleInventoryType> i : iter.values()) {
                    return new MapleDueyActions(packageid, i.getLeft());
                }
            }
        } catch (Exception se) {
            se.printStackTrace();
        }
        return new MapleDueyActions(packageid);
    }
}
