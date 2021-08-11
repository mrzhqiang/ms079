/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package server.shops;

import client.inventory.IItem;
import client.inventory.ItemFlag;
import constants.GameConstants;
import client.MapleCharacter;
import client.MapleClient;
import server.MapleItemInformationProvider;
import handling.channel.ChannelServer;
import java.awt.Point;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import server.MapleInventoryManipulator;
import server.Timer.EtcTimer;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.MaplePacketCreator;
import tools.packet.PlayerShopPacket;

public class HiredMerchant extends AbstractPlayerStore {

    public ScheduledFuture<?> schedule;
    private List<String> blacklist;
    private int storeid;
    private long start;

    public HiredMerchant(MapleCharacter owner, int itemId, String desc) {
        super(owner, itemId, desc, "", 3);
        this.start = System.currentTimeMillis();
        this.blacklist = new LinkedList<String>();
        this.schedule = EtcTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                /*
                 * if ((HiredMerchant.this.getMCOwner() != null) &&
                 * (HiredMerchant.this.getMCOwner().getPlayerShop() ==
                 * HiredMerchant.this)) {
                 * HiredMerchant.this.getMCOwner().setPlayerShop(null); }
                 * HiredMerchant.this.removeAllVisitors(-1, -1);
                 */
                //      HiredMerchant.this.closeShop(true, true);
                closeShop(true, true);
            }
        }, 1000 * 60 * 60 * 24);
    }

    public byte getShopType() {
        return IMaplePlayerShop.HIRED_MERCHANT;
    }

    public final void setStoreid(final int storeid) {
        this.storeid = storeid;
    }

    public List<MaplePlayerShopItem> searchItem(final int itemSearch) {
        final List<MaplePlayerShopItem> itemz = new LinkedList<MaplePlayerShopItem>();
        for (MaplePlayerShopItem item : this.items) {
            if ((item.item.getItemId() == itemSearch) && (item.bundles > 0)) {
                itemz.add(item);
            }
        }
        return itemz;
    }

    @Override
    public void buy(MapleClient c, int item, short quantity) {
        MaplePlayerShopItem pItem = (MaplePlayerShopItem) this.items.get(item);
        IItem shopItem = pItem.item;
        IItem newItem = shopItem.copy();
        short perbundle = newItem.getQuantity();
        int theQuantity = pItem.price * quantity;
        newItem.setQuantity((short) (quantity * perbundle));

        byte flag = newItem.getFlag();

        if (ItemFlag.KARMA_EQ.check(flag)) {
            newItem.setFlag((byte) (flag - ItemFlag.KARMA_EQ.getValue()));
        } else if (ItemFlag.KARMA_USE.check(flag)) {
            newItem.setFlag((byte) (flag - ItemFlag.KARMA_USE.getValue()));
        }

        /*
         * if (MapleInventoryManipulator.checkSpace(c, newItem.getItemId(),
         * newItem.getQuantity(), newItem.getOwner()) &&
         * MapleInventoryManipulator.addFromDrop(c, newItem, false)) {
         * pItem.bundles -= quantity; // Number remaining in the store
         * bought.add(new BoughtItem(newItem.getItemId(), quantity, (pItem.price
         * * quantity), c.getPlayer().getName()));
         *
         * final int gainmeso = getMeso() + (pItem.price * quantity);
         * setMeso(gainmeso - GameConstants.EntrustedStoreTax(gainmeso));
         * c.getPlayer().gainMeso(-pItem.price * quantity, false); saveItems();
         * } else { c.getPlayer().dropMessage(1, "Your inventory is full.");
         * c.getSession().write(MaplePacketCreator.enableActions()); } }
         */
        /*
         * if (MapleInventoryManipulator.checkSpace(c, newItem.getItemId(),
         * newItem.getQuantity(), newItem.getOwner())) { final int gainmeso =
         * getMeso() + theQuantity -
         * GameConstants.EntrustedStoreTax(theQuantity); if (gainmeso > 0) {
         * setMeso(gainmeso); pItem.bundles -= quantity; // Number remaining in
         * the store MapleInventoryManipulator.addFromDrop(c, newItem, false);
         * bought.add(new BoughtItem(newItem.getItemId(), quantity, theQuantity,
         * c.getPlayer().getName())); c.getPlayer().gainMeso(-theQuantity,
         * false); saveItems(); MapleCharacter chr = getMCOwnerWorld(); if (chr
         * != null) { chr.dropMessage(-5, "物品 " +
         * MapleItemInformationProvider.getInstance().getName(newItem.getItemId())
         * + " (" + perbundle + ") x " + quantity + " 已從精靈商店賣出. 還剩下 " +
         * pItem.bundles + "個"); } } else { c.getPlayer().dropMessage(1,
         * "拍賣家有太多錢了.");
         * c.getSession().write(MaplePacketCreator.enableActions()); } } else {
         * c.getPlayer().dropMessage(1, "您的背包滿了.");
         * c.getSession().write(MaplePacketCreator.enableActions()); }
         */
        /*
         * if (c.getPlayer().getMeso() >= pItem.price * quantity) { if
         * (MapleInventoryManipulator.商店防止复制(c, newItem, false)) { pItem.bundles
         * -= quantity; // Number remaining in the store
         *
         * final int gainmeso = getMeso() + (pItem.price * quantity); if
         * (gainmeso > 0) { setMeso(gainmeso -
         * GameConstants.EntrustedStoreTax(gainmeso));
         * c.getPlayer().gainMeso(-pItem.price * quantity, false); } else {
         * c.getPlayer().dropMessage(1, "金币不足.");
         * c.getSession().write(MaplePacketCreator.enableActions()); } } else {
         * c.getPlayer().dropMessage(1, "背包已满" + "\r\n" + "请留1格以上位置" + "\r\n" +
         * "在进行购买物品" + "\r\n" + "防止非法复制");
         * c.getSession().write(MaplePacketCreator.enableActions()); //
         * c.getPlayer().dropMessage(1, "您的背包滿了."); } } else {
         * c.getPlayer().dropMessage(1, "金币不足");
         * c.getSession().write(MaplePacketCreator.enableActions()); }
         */
        if (!c.getPlayer().canHold(newItem.getItemId())) {
            c.getPlayer().dropMessage(1, "背包已满");
            c.sendPacket(MaplePacketCreator.enableActions());
            return;
        }
        if (MapleInventoryManipulator.checkSpace(c, newItem.getItemId(), newItem.getQuantity(), newItem.getOwner())) {
            int gainmeso = getMeso() + theQuantity - GameConstants.EntrustedStoreTax(theQuantity);
            if (gainmeso > 0) {
                setMeso(gainmeso);
                MaplePlayerShopItem tmp167_165 = pItem;
                tmp167_165.bundles = (short) (tmp167_165.bundles - quantity);
                MapleInventoryManipulator.addFromDrop(c, newItem, false);
                this.bought.add(new AbstractPlayerStore.BoughtItem(newItem.getItemId(), quantity, theQuantity, c.getPlayer().getName()));
                c.getPlayer().gainMeso(-theQuantity, false);
                saveItems();
                MapleCharacter chr = getMCOwnerWorld();
                String itemText = new StringBuilder().append(MapleItemInformationProvider.getInstance().getName(newItem.getItemId())).append(" (").append(perbundle).append(") x ").append(quantity).append(" 已经被卖出。 剩余数量: ").append(pItem.bundles).append(" 购买者: ").append(c.getPlayer().getName()).toString();
                if (chr != null) {
                    chr.dropMessage(-5, new StringBuilder().append("您雇佣商店里面的道具: ").append(itemText).toString());
                }

                System.out.println(new StringBuilder().append("[雇佣] ").append(chr != null ? chr.getName() : getOwnerName()).append(" 雇佣商店卖出: ").append(newItem.getItemId()).append(" - ").append(itemText).append(" 价格: ").append(theQuantity).toString());
            } else {
                c.getPlayer().dropMessage(1, "金币不足.");
                c.getSession().write(MaplePacketCreator.enableActions());
            }
        } else {
            c.getPlayer().dropMessage(1, "背包已满" + "\r\n" + "请留1格以上位置" + "\r\n" + "在进行购买物品" + "\r\n" + "防止非法复制");
            c.getSession().write(MaplePacketCreator.enableActions());
        }
    }

    @Override
    public void closeShop(boolean saveItems, boolean remove) {
        if (this.schedule != null) {
            this.schedule.cancel(false);
        }
        if (saveItems) {
            saveItems();
            //this.items.clear();
            items.clear();
        }
        if (remove) {
            ChannelServer.getInstance(this.channel).removeMerchant(this);
            getMap().broadcastMessage(PlayerShopPacket.destroyHiredMerchant(getOwnerId()));
        }
        getMap().removeMapObject(this);

        try {

            for (ChannelServer ch : ChannelServer.getAllInstances()) {
                MapleMap map = null;
                for (int i = 910000001; i <= 910000022; i++) {
                    map = ch.getMapFactory().getMap(i);
                    if (map != null) {
                        List<MapleMapObject> HMS = map.getMapObjectsInRange(new Point(0, 0), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.HIRED_MERCHANT));
                        for (MapleMapObject HM : HMS) {
                            HiredMerchant HMM = (HiredMerchant) HM;
                            if (HMM.getOwnerId() == getOwnerId()) {
                                map.removeMapObject(this);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }

        this.schedule = null;
    }

    public int getTimeLeft() {
        // return (int) ((System.currentTimeMillis() - start) / 1000);
        return (int) ((System.currentTimeMillis() - start) / 1000);
    }

    public final int getStoreId() {
        return storeid;
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.HIRED_MERCHANT;
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        if (isAvailable()) {
            client.getSession().write(PlayerShopPacket.destroyHiredMerchant(getOwnerId()));
        }
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        if (isAvailable()) {
            client.getSession().write(PlayerShopPacket.spawnHiredMerchant(this));
        }
    }

    public final boolean isInBlackList(final String bl) {
        return this.blacklist.contains(bl);
    }

    public final void addBlackList(final String bl) {
        this.blacklist.add(bl);
    }

    public final void removeBlackList(final String bl) {
        this.blacklist.remove(bl);
    }

    public final void sendBlackList(final MapleClient c) {
        c.getSession().write(PlayerShopPacket.MerchantBlackListView(this.blacklist));
    }

    public final void sendVisitor(final MapleClient c) {
        c.getSession().write(PlayerShopPacket.MerchantVisitorView(this.visitors));
    }
}
