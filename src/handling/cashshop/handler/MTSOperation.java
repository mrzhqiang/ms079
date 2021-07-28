package handling.cashshop.handler;

import client.inventory.Equip;
import constants.GameConstants;
import client.inventory.IItem;
import client.MapleClient;
import client.inventory.MapleInventoryType;
import constants.ServerConstants;
import java.util.Calendar;
import server.MTSCart;
import server.MTSStorage;
import server.MTSStorage.MTSItemInfo;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import tools.data.input.SeekableLittleEndianAccessor;
import tools.packet.MTSCSPacket;

public class MTSOperation {

    public static void MTSOperation(final SeekableLittleEndianAccessor slea, final MapleClient c) {
        final MTSCart cart = MTSStorage.getInstance().getCart(c.getPlayer().getId());
        //System.out.println(slea.toString());
        if (slea.available() <= 0) {
            doMTSPackets(cart, c);
            return;
        }
        final byte op = slea.readByte();
        if (op == 1) { //put up for sale
            final byte invType = slea.readByte(); //1 = equip 2 = everything else
            if (invType != 1 && invType != 2) { //pet?
                c.getSession().write(MTSCSPacket.getMTSFailSell());
                doMTSPackets(cart, c);
                return;
            }
            final int itemid = slea.readInt(); //itemid
            if (slea.readByte() != 0) {
                c.getSession().write(MTSCSPacket.getMTSFailSell());
                doMTSPackets(cart, c);
                return;//we don't like uniqueIDs
            }
            slea.skip(8); //expiration, don't matter
            short stars = 1, quantity = 1;
            byte slot = 0;
            if (invType == 1) {
                slea.skip(32);
            } else {
                stars = slea.readShort(); //the entire quantity of the item
            }
            slea.readMapleAsciiString();//owner
            //again? =/
            if (invType == 1) {
                slea.skip(32);
//                slea.skip(48);
//                slot = (byte) slea.readInt();
//                slea.skip(4); //skip the quantity int, equips are always 1
            } else {
                slea.readShort(); //flag
                if (GameConstants.isThrowingStar(itemid) || GameConstants.isBullet(itemid)) {
                    slea.skip(8);//recharge ID thing
                }
                slot = (byte) slea.readInt();
                if (GameConstants.isThrowingStar(itemid) || GameConstants.isBullet(itemid)) {
                    quantity = stars; //this is due to stars you need to use the entire quantity, not specified
                    slea.skip(4); //so just skip the quantity int
                } else {
                    quantity = (short) slea.readInt(); //specified quantity
                }
            }
            final int price = slea.readInt();
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            final MapleInventoryType type = GameConstants.getInventoryType(itemid);
            final IItem item = c.getPlayer().getInventory(type).getItem(slot).copy();
            if (ii.isCash(itemid) || quantity <= 0 || item == null || item.getQuantity() <= 0 || item.getItemId() != itemid || item.getUniqueId() > 0 || item.getQuantity() < quantity || price < ServerConstants.MIN_MTS || c.getPlayer().getMeso() < ServerConstants.MTS_MESO || cart.getNotYetSold().size() >= 10 || ii.isDropRestricted(itemid) || ii.isAccountShared(itemid) || item.getExpiration() > -1 || item.getFlag() > 0) {
                c.getSession().write(MTSCSPacket.getMTSFailSell());
                doMTSPackets(cart, c);
                return;
            }
            if (type == MapleInventoryType.EQUIP) {
                final Equip eq = (Equip) item;
                if (eq.getState() > 0 || eq.getEnhance() > 0 || eq.getDurability() > -1) {
                    c.getSession().write(MTSCSPacket.getMTSFailSell());
                    doMTSPackets(cart, c);
                    return;
                }
            }
            if (quantity >= 50 && GameConstants.isUpgradeScroll(item.getItemId())) {
                c.setMonitored(true); //hack check
            }
            final long expiration = (System.currentTimeMillis() + (7L * 24 * 60 * 60 * 1000));
            item.setQuantity(quantity);
            MTSStorage.getInstance().addToBuyNow(cart, item, price, c.getPlayer().getId(), c.getPlayer().getName(), expiration);
            MapleInventoryManipulator.removeFromSlot(c, type, slot, quantity, false);
            c.getPlayer().gainMeso(-ServerConstants.MTS_MESO, false);
            c.getSession().write(MTSCSPacket.getMTSConfirmSell());
        } else if (op == 4) { //change page/tab
            cart.changeInfo(slea.readInt(), slea.readInt(), slea.readInt());
        } else if (op == 7) { //cancel sale
            if (!MTSStorage.getInstance().removeFromBuyNow(slea.readInt(), c.getPlayer().getId(), true)) {
                c.getSession().write(MTSCSPacket.getMTSFailCancel());
            } else {
                c.getSession().write(MTSCSPacket.getMTSConfirmCancel());
                sendMTSPackets(cart, c, true);
                return;
            }
        } else if (op == 8) { //transfer item
            final int id = Integer.MAX_VALUE - slea.readInt(); //fake id
            if (id >= cart.getInventory().size()) {
                c.getPlayer().dropMessage(1, "Please try it again later.");
                sendMTSPackets(cart, c, true);
                return;
            }
            final IItem item = cart.getInventory().get(id); //by index
            //System.out.println("NumItems: " + cart.getInventory().size() + ", ID: " + id + ", ItemExists?: " + (item != null));
            if (item != null && item.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
                IItem item_ = item.copy();
                short pos = MapleInventoryManipulator.addbyItem(c, item_, true);
                if (pos >= 0) {
                    if (item_.getPet() != null) {
                        item_.getPet().setInventoryPosition(pos);
                        c.getPlayer().addPet(item_.getPet());
                    }
                    cart.removeFromInventory(item);
                    c.getSession().write(MTSCSPacket.getMTSConfirmTransfer(item_.getQuantity(), pos)); //IF this is actually pos and pos
                    sendMTSPackets(cart, c, true);
                    return;
                } else {
                    //System.out.println("addByItem is less than 0");
                    c.getSession().write(MTSCSPacket.getMTSFailBuy());
                }
            } else {
                //System.out.println("CheckSpace return false");
                c.getSession().write(MTSCSPacket.getMTSFailBuy());
            }
        } else if (op == 9) { //add to cart
            final int id = slea.readInt();
            if (MTSStorage.getInstance().checkCart(id, c.getPlayer().getId()) && cart.addToCart(id)) {
                c.getSession().write(MTSCSPacket.addToCartMessage(false, false));
            } else {
                c.getSession().write(MTSCSPacket.addToCartMessage(true, false));
            }
        } else if (op == 10) { //delete from cart
            final int id = slea.readInt();
            if (cart.getCart().contains(id)) {
                cart.removeFromCart(id);
                c.getSession().write(MTSCSPacket.addToCartMessage(false, true));
            } else {
                c.getSession().write(MTSCSPacket.addToCartMessage(true, true));
            }
        } else if (op == 16 || op == 17) { //buyNow, buy from cart
            final MTSItemInfo mts = MTSStorage.getInstance().getSingleItem(slea.readInt());
            if (mts != null && mts.getCharacterId() != c.getPlayer().getId()) {
                if (c.getPlayer().getCSPoints(1) > mts.getRealPrice()) {
                    if (MTSStorage.getInstance().removeFromBuyNow(mts.getId(), c.getPlayer().getId(), false)) {
                        c.getPlayer().modifyCSPoints(1, -mts.getRealPrice(), false);
                        MTSStorage.getInstance().getCart(mts.getCharacterId()).increaseOwedNX(mts.getPrice());
                        c.getSession().write(MTSCSPacket.getMTSConfirmBuy());
                        sendMTSPackets(cart, c, true);
                        return;
                    } else {
                        c.getSession().write(MTSCSPacket.getMTSFailBuy());
                    }
                } else {
                    c.getSession().write(MTSCSPacket.getMTSFailBuy());
                }
            } else {
                c.getSession().write(MTSCSPacket.getMTSFailBuy());
            }
        } else if (c.getPlayer().isAdmin()) {
            //System.out.println("New MTS Op " + op + ", \n" + slea.toString());
        }
        doMTSPackets(cart, c);
    }

    public static void MTSUpdate(final MTSCart cart, final MapleClient c) {
        c.getPlayer().modifyCSPoints(1, MTSStorage.getInstance().getCart(c.getPlayer().getId()).getSetOwedNX(), false);
        c.getSession().write(MTSCSPacket.getMTSWantedListingOver(0, 0));
        doMTSPackets(cart, c);
    }

    private static void doMTSPackets(final MTSCart cart, final MapleClient c) {
        sendMTSPackets(cart, c, false);
    }

    private static void sendMTSPackets(final MTSCart cart, final MapleClient c, final boolean changed) {
        c.getSession().write(MTSStorage.getInstance().getCurrentMTS(cart));
        c.getSession().write(MTSStorage.getInstance().getCurrentNotYetSold(cart));
        c.getSession().write(MTSStorage.getInstance().getCurrentTransfer(cart, changed));
        c.getSession().write(MTSCSPacket.showMTSCash(c.getPlayer()));
        c.getSession().write(MTSCSPacket.enableCSUse());
        MTSStorage.getInstance().checkExpirations();
    }
}
