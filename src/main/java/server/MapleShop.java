package server;

import client.MapleClient;
import client.SkillFactory;
import client.inventory.IItem;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import com.github.mrzhqiang.maplestory.domain.DShop;
import com.github.mrzhqiang.maplestory.domain.query.QDShop;
import com.github.mrzhqiang.maplestory.domain.query.QDShopItem;
import constants.GameConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.life.MapleLifeFactory;
import server.life.MapleNPC;
import tools.MaplePacketCreator;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MapleShop {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleShop.class);

    private static final Set<Integer> rechargeableItems = new LinkedHashSet<>();
    private int id;
    private int npcId;
    private List<MapleShopItem> items;

    static {
        for (int i = 2070000; i <= 2070021; i++) {
            rechargeableItems.add(i);
        }
        for (int i = 2070023; i <= 2070026; i++) {
            rechargeableItems.add(i);
        }
        rechargeableItems.remove(2070014);
        rechargeableItems.remove(2070015);
        rechargeableItems.remove(2070016);
        rechargeableItems.remove(2070017);
        rechargeableItems.remove(2070018);
        rechargeableItems.remove(2070019);
        rechargeableItems.remove(2070020);
        rechargeableItems.remove(2070021);
        rechargeableItems.remove(2070023);
        rechargeableItems.remove(2070024);
        rechargeableItems.remove(2070025);
        rechargeableItems.remove(2070026);

        for (int i = 2330000; i <= 2330006; i++) {
            rechargeableItems.add(i);
        }
        rechargeableItems.add(2331000);
        rechargeableItems.add(2332000);
        /*
         * rechargeableItems.add(2070000); rechargeableItems.add(2070001);
         * rechargeableItems.add(2070002); rechargeableItems.add(2070003);
         * rechargeableItems.add(2070004); rechargeableItems.add(2070005);
         * rechargeableItems.add(2070006); rechargeableItems.add(2070007);
         * rechargeableItems.add(2070008); rechargeableItems.add(2070009);
         * rechargeableItems.add(2070010); rechargeableItems.add(2070011);
         * rechargeableItems.add(2070012); rechargeableItems.add(2070013); //
         * rechargeableItems.add(2070014); // Doesn't Exist [Devil Rain] //
         * rechargeableItems.add(2070015); // Beginner Star //
         * rechargeableItems.add(2070016); //	rechargeableItems.add(2070017); //
         * Doesn't Exist // rechargeableItems.add(2070018); // Balanced Fury
         * rechargeableItems.add(2070019); // Magic Throwing Star
         *
         * rechargeableItems.add(2330000); rechargeableItems.add(2330001);
         * rechargeableItems.add(2330002); rechargeableItems.add(2330003);
         * rechargeableItems.add(2330004); rechargeableItems.add(2330005); //
         * rechargeableItems.add(2330006); // Beginner Bullet
         * rechargeableItems.add(2330007);
         *
         * rechargeableItems.add(2331000); // Capsules
         * rechargeableItems.add(2332000); // Capsules
         */
    }

    /**
     * Creates a new instance of MapleShop
     */
    private MapleShop(int id, int npcId) {
        this.id = id;
        this.npcId = npcId;
        items = new LinkedList<MapleShopItem>();
    }

    public void addItem(MapleShopItem item) {
        items.add(item);
    }

    public void sendShop(MapleClient c) {
        MapleNPC npc = MapleLifeFactory.getNPC(getNpcId());
        if (npc == null || npc.getName().equals("MISSINGNO")) {
            c.getPlayer().dropMessage(1, "商店" + id + "找不到此代码为" + getNpcId() + "的Npc");
            return;
        } else {
            if (c.getPlayer().isAdmin()) {
                c.getPlayer().dropMessage("您已建立与商店" + id + "的连接");
            }
        }
        c.getPlayer().setShop(this);
        c.getSession().write(MaplePacketCreator.getNPCShop(c, getNpcId(), items));
    }

    public void buy(MapleClient c, int itemId, short quantity) {
        if (quantity <= 0) {
            AutobanManager.getInstance().addPoints(c, 1000, 0, "Buying " + quantity + " " + itemId);
            return;
        }
        if (!GameConstants.isMountItemAvailable(itemId, c.getPlayer().getJob())) {
            c.getPlayer().dropMessage(1, "You may not buy this item.");
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        MapleShopItem item = findById(itemId);
        if (item != null && item.getPrice() > 0) {
            final int price = GameConstants.isRechargable(itemId) ? item.getPrice() : (item.getPrice() * quantity);
            if (price >= 0 && c.getPlayer().getMeso() >= price) {
                if (MapleInventoryManipulator.checkSpace(c, itemId, quantity, "")) {
                    c.getPlayer().gainMeso(-price, false);
                    if (GameConstants.isPet(itemId)) {
                        MapleInventoryManipulator.addById(c, itemId, quantity, "", MaplePet.createPet(itemId, MapleInventoryIdentifier.getInstance()), -1, (byte) 0);
                    } else {
                        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

                        if (GameConstants.isRechargable(itemId)) {
                            quantity = ii.getSlotMax(c, item.getItemId());
                        }

                        MapleInventoryManipulator.addById(c, itemId, quantity, (byte) 0);
                    }
                } else {
                    c.getPlayer().dropMessage(1, "Your Inventory is full");
                }
                c.getSession().write(MaplePacketCreator.confirmShopTransaction((byte) 0));
            }
        } /*
         * else if (item != null && quantity == 1 &&
         * c.getPlayer().haveItem(item.getReqItem(), item.getReqItemQ(), false,
         * true)) { if (MapleInventoryManipulator.checkSpace(c, itemId,
         * quantity, "")) { MapleInventoryManipulator.removeById(c,
         * GameConstants.getInventoryType(item.getReqItem()), item.getReqItem(),
         * item.getReqItemQ(), false, false); if (GameConstants.isPet(itemId)) {
         * MapleInventoryManipulator.addById(c, itemId, quantity, "",
         * MaplePet.createPet(itemId, MapleInventoryIdentifier.getInstance()),
         * -1); } else { MapleItemInformationProvider ii =
         * MapleItemInformationProvider.getInstance();
         *
         * if (GameConstants.isRechargable(itemId)) { quantity =
         * ii.getSlotMax(c, item.getItemId()); }
         * MapleInventoryManipulator.addById(c, itemId, quantity); } } else {
         * c.getPlayer().dropMessage(1, "Your Inventory is full"); }
         * c.getSession().write(MaplePacketCreator.confirmShopTransaction((byte)
         * 0)); }
         */
    }

    public void sell(MapleClient c, MapleInventoryType type, int slot, int quantity) {
        if (quantity == 0xFFFF || quantity == 0) {
            quantity = 1;
        }
        IItem item = c.getPlayer().getInventory(type).getItem(slot);
        if (item == null) {
            return;
        }

        if (GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId())) {
            quantity = item.getQuantity();
        }
        if (quantity < 0) {
            AutobanManager.getInstance().addPoints(c, 1000, 0, "Selling " + quantity + " " + item.getItemId() + " (" + type.name() + "/" + slot + ")");
            return;
        }
        int iQuant = item.getQuantity();
        if (iQuant == 0xFFFF) {
            iQuant = 1;
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.cantSell(item.getItemId())) {
            return;
        }
        if (quantity <= iQuant && iQuant > 0) {
            MapleInventoryManipulator.removeFromSlot(c, type, slot, quantity, false);
            double price;
            if (GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId())) {
                price = ii.getWholePrice(item.getItemId()) / (double) ii.getSlotMax(c, item.getItemId());
            } else {
                price = ii.getPrice(item.getItemId());
            }
            final int recvMesos = (int) Math.max(Math.ceil(price * quantity), 0);
            if (price != -1.0 && recvMesos > 0) {
                c.getPlayer().gainMeso(recvMesos, false);
            }
            c.getSession().write(MaplePacketCreator.confirmShopTransaction((byte) 0x8));
        }
    }

    public void recharge(final MapleClient c, final byte slot) {
        final IItem item = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);

        if (item == null || (!GameConstants.isThrowingStar(item.getItemId()) && !GameConstants.isBullet(item.getItemId()))) {
            return;
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        short slotMax = ii.getSlotMax(c, item.getItemId());
        final int skill = GameConstants.getMasterySkill(c.getPlayer().getJob());

        if (skill != 0) {
            slotMax += c.getPlayer().getSkillLevel(SkillFactory.getSkill(skill)) * 10;
        }
        if (item.getQuantity() < slotMax) {
            final int price = (int) Math.round(ii.getPrice(item.getItemId()) * (slotMax - item.getQuantity()));
            if (c.getPlayer().getMeso() >= price) {
                item.setQuantity(slotMax);
                c.getSession().write(MaplePacketCreator.updateInventorySlot(MapleInventoryType.USE, item, false));
                c.getPlayer().gainMeso(-price, false, true, false);
                c.getSession().write(MaplePacketCreator.confirmShopTransaction((byte) 0x8));
            }
        }
    }

    protected MapleShopItem findById(int itemId) {
        for (MapleShopItem item : items) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }
        return null;
    }

    public static MapleShop createFromDB(int id, boolean isShopId) {
        DShop one;
        if (isShopId) {
            one = new QDShop().id.eq(id).findOne();
        } else {
            one = new QDShop().npcid.eq(id).findOne();
        }

        if (one == null) {
            return null;
        }

        List<Integer> recharges = new ArrayList<>(rechargeableItems);

        MapleShop ret = new MapleShop(one.id, one.npcid);

        new QDShopItem().shopid.eq(one.id).order().position.asc().findStream()
                .map(it -> {
                    MapleShopItem starItem;
                    if (GameConstants.isThrowingStar(it.itemid) || GameConstants.isBullet(it.itemid)) {
                        if (rechargeableItems.contains(it.itemid)) {
                            recharges.remove(it.itemid);
                        }
                        starItem = new MapleShopItem((short) 1, it.itemid, it.price);
                    } else {
                        starItem = new MapleShopItem((short) 1000, it.itemid, it.price);
                    }
                    return starItem;
                })
                .forEach(ret::addItem);
        for (Integer recharge : recharges) {
            ret.addItem(new MapleShopItem((short) 1000, recharge, 0));
        }
        return ret;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getId() {
        return id;
    }
}
