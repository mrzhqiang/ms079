/*
 This file is part of the ZeroFusion MapleStory Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 ZeroFusion organized by "RMZero213" <RMZero213@hotmail.com>

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
package server;

import constants.GameConstants;
import client.inventory.IItem;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import java.sql.Connection;
import database.DatabaseConnection;
import handling.MaplePacket;
import constants.ServerConstants;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import tools.Pair;
import tools.packet.MTSCSPacket;

public class MTSStorage {
    //stores all carts all mts items, updates every hour

    private static final long serialVersionUID = 231541893513228L;
    private long lastUpdate = System.currentTimeMillis();
    private final Map<Integer, MTSCart> idToCart;
    private final AtomicInteger packageId;
    private final Map<Integer, MTSItemInfo> buyNow; //packageid to mtsiteminfo
    private static MTSStorage instance;
    private boolean end = false;
    private ReentrantReadWriteLock mutex;
    private ReentrantReadWriteLock cart_mutex;
    //mts_cart is just characterid, itemid
    //mts_items is id/packageid, tab(byte), price, characterid, seller, expiration

    public MTSStorage() {
        System.out.println("Loading MTSStorage :::");
        idToCart = new LinkedHashMap<Integer, MTSCart>();
        buyNow = new LinkedHashMap<Integer, MTSItemInfo>();
        packageId = new AtomicInteger(1);
        mutex = new ReentrantReadWriteLock();
        cart_mutex = new ReentrantReadWriteLock();
    }

    public static final MTSStorage getInstance() {
        return instance;
    }

    public static final void load() {

        if (instance == null) {
            instance = new MTSStorage();
            instance.loadBuyNow();
        }

    }

    public final boolean check(final int packageid) {
        return getSingleItem(packageid) != null;
    }

    public final boolean checkCart(final int packageid, final int charID) {
        final MTSItemInfo item = getSingleItem(packageid);
        return item != null && item.getCharacterId() != charID;
    }

    public final MTSItemInfo getSingleItem(final int packageid) {
        mutex.readLock().lock();
        try {
            return buyNow.get(packageid);
        } finally {
            mutex.readLock().unlock();
        }
    }

    public final void addToBuyNow(final MTSCart cart, final IItem item, final int price, final int cid, final String seller, final long expiration) {
        final int id;
        mutex.writeLock().lock();
        try {
            id = packageId.incrementAndGet();
            buyNow.put(id, new MTSItemInfo(price, item, seller, id, cid, expiration));
        } finally {
            mutex.writeLock().unlock();
        }
        cart.addToNotYetSold(id);
    }

    public final boolean removeFromBuyNow(final int id, final int cidBought, final boolean check) {
        IItem item = null;
        mutex.writeLock().lock();
        try {
            if (buyNow.containsKey(id)) {
                final MTSItemInfo r = buyNow.get(id);
                if (!check || r.getCharacterId() == cidBought) {
                    item = r.getItem();
                    buyNow.remove(id);
                }
            }
        } finally {
            mutex.writeLock().unlock();
        }
        if (item != null) {
            cart_mutex.readLock().lock();
            try {
                for (Entry<Integer, MTSCart> c : idToCart.entrySet()) {
                    c.getValue().removeFromCart(id);
                    c.getValue().removeFromNotYetSold(id);
                    if (c.getKey() == cidBought) {
                        c.getValue().addToInventory(item);
                    }
                }
            } finally {
                cart_mutex.readLock().unlock();
            }
        }
        return item != null;
    }

    private final void loadBuyNow() {
        int lastPackage = 0;
        int cId;
        Map<Integer, Pair<IItem, MapleInventoryType>> items;
        final Connection con = DatabaseConnection.getConnection();
        try {
            final PreparedStatement ps = con.prepareStatement("SELECT * FROM mts_items WHERE tab = 1");
            final ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lastPackage = rs.getInt("id");
                cId = rs.getInt("characterid");
                if (!idToCart.containsKey(cId)) {
                    idToCart.put(cId, new MTSCart(cId));
                }
                items = ItemLoader.MTS.loadItems(false, lastPackage);
                if (items != null && items.size() > 0) {
                    for (Pair<IItem, MapleInventoryType> i : items.values()) {
                        buyNow.put(lastPackage, new MTSItemInfo(rs.getInt("price"), i.getLeft(), rs.getString("seller"), lastPackage, cId, rs.getLong("expiration")));
                    }
                }
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        packageId.set(lastPackage);
    }

    public final void saveBuyNow(boolean isShutDown) {
        if (this.end) {
            return;
        }
        this.end = isShutDown;
        if (isShutDown) {
            System.out.println("Saving MTS...");
        }
        final Map<Integer, ArrayList<IItem>> expire = new HashMap<Integer, ArrayList<IItem>>();
        final List<Integer> toRemove = new ArrayList<Integer>();
        final long now = System.currentTimeMillis();
        final Map<Integer, ArrayList<Pair<IItem, MapleInventoryType>>> items = new HashMap<Integer, ArrayList<Pair<IItem, MapleInventoryType>>>();
        final Connection con = DatabaseConnection.getConnection();
        mutex.writeLock().lock(); //lock wL so rL will also be locked
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM mts_items WHERE tab = 1");
            ps.execute();
            ps.close();
            ps = con.prepareStatement("INSERT INTO mts_items VALUES (?, ?, ?, ?, ?, ?)");
            for (MTSItemInfo m : buyNow.values()) {
                if (now > m.getEndingDate()) {
                    if (!expire.containsKey(m.getCharacterId())) {
                        expire.put(m.getCharacterId(), new ArrayList<IItem>());
                    }
                    expire.get(m.getCharacterId()).add(m.getItem());
                    toRemove.add(m.getId());
                    items.put(m.getId(), null); //destroy from the mtsitems.
                } else {
                    ps.setInt(1, m.getId());
                    ps.setByte(2, (byte) 1);
                    ps.setInt(3, m.getPrice());
                    ps.setInt(4, m.getCharacterId());
                    ps.setString(5, m.getSeller());
                    ps.setLong(6, m.getEndingDate());
                    ps.executeUpdate();
                    if (!items.containsKey(m.getId())) {
                        items.put(m.getId(), new ArrayList<Pair<IItem, MapleInventoryType>>());
                    }
                    items.get(m.getId()).add(new Pair<IItem, MapleInventoryType>(m.getItem(), GameConstants.getInventoryType(m.getItem().getItemId())));
                }
            }
            for (int i : toRemove) {
                buyNow.remove(i);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mutex.writeLock().unlock();
        }
        if (isShutDown) {
            System.out.println("Saving MTS items...");
        }
        try {
            for (Entry<Integer, ArrayList<Pair<IItem, MapleInventoryType>>> ite : items.entrySet()) {
                ItemLoader.MTS.saveItems(ite.getValue(), ite.getKey());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (isShutDown) {
            System.out.println("Saving MTS carts...");
        }
        cart_mutex.writeLock().lock();
        try {
            for (Entry<Integer, MTSCart> c : idToCart.entrySet()) {
                for (int i : toRemove) {
                    c.getValue().removeFromCart(i);
                    c.getValue().removeFromNotYetSold(i);
                }
                if (expire.containsKey(c.getKey())) {
                    for (IItem item : expire.get(c.getKey())) {
                        c.getValue().addToInventory(item);
                    }
                }
                c.getValue().save();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            cart_mutex.writeLock().unlock();
        }
        lastUpdate = System.currentTimeMillis();
    }

    public final void checkExpirations() {
        if ((System.currentTimeMillis() - lastUpdate) > 3600000) { //every hour
            saveBuyNow(false);
        }
    }

    public final MTSCart getCart(final int characterId) {
        MTSCart ret;
        cart_mutex.readLock().lock();
        try {
            ret = idToCart.get(characterId);
        } finally {
            cart_mutex.readLock().unlock();
        }
        if (ret == null) {
            cart_mutex.writeLock().lock();
            try {
                ret = new MTSCart(characterId);
                idToCart.put(characterId, ret);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                cart_mutex.writeLock().unlock();
            }
        }
        return ret;
    }

    public final MaplePacket getCurrentMTS(final MTSCart cart) {
        mutex.readLock().lock();
        try {
            if (cart.getTab() == 1) { //buyNow
                return MTSCSPacket.sendMTS(getBuyNow(cart.getType(), cart.getPage()), cart.getTab(), cart.getType(), cart.getPage(), buyNow.size() / 16 + (buyNow.size() % 16 > 0 ? 1 : 0));
            } else if (cart.getTab() == 4) {
                return MTSCSPacket.sendMTS(getCartItems(cart), cart.getTab(), cart.getType(), cart.getPage(), 0);
            } else {
                return MTSCSPacket.sendMTS(new ArrayList<MTSItemInfo>(), cart.getTab(), cart.getType(), cart.getPage(), 0);
            }
        } finally {
            mutex.readLock().unlock();
        }
    }

    public final MaplePacket getCurrentNotYetSold(final MTSCart cart) {
        mutex.readLock().lock();
        try {
            final List<MTSItemInfo> nys = new ArrayList<MTSItemInfo>();
            MTSItemInfo r;
            final List<Integer> nyss = new ArrayList<Integer>(cart.getNotYetSold());
            for (int i : nyss) {
                r = buyNow.get(i);
                if (r == null) {
                    cart.removeFromNotYetSold(i);
                } else {
                    nys.add(r);
                }
            }
            return MTSCSPacket.getNotYetSoldInv(nys);
        } finally {
            mutex.readLock().unlock();
        }
    }

    public final MaplePacket getCurrentTransfer(final MTSCart cart, final boolean changed) {
        return MTSCSPacket.getTransferInventory(cart.getInventory(), changed);
    }

    private final List<MTSItemInfo> getBuyNow(final int type, int page) {
        //page * 16 = FIRST item thats displayed
        final int size = buyNow.size() / 16 + (buyNow.size() % 16 > 0 ? 1 : 0);
        final List<MTSItemInfo> ret = new ArrayList<MTSItemInfo>();
        final List<MTSItemInfo> rett = new ArrayList<MTSItemInfo>(buyNow.values());
        if (page > size) {
            page = 0;
        }
        MTSItemInfo r;
        for (int i = page * 16; i < page * 16 + 16; i++) {
            if (buyNow.size() >= i + 1) {
                r = rett.get(i); //by index
                if (r != null && (type == 0 || GameConstants.getInventoryType(r.getItem().getItemId()).getType() == type)) {
                    ret.add(r);
                }
            } else {
                break;
            }
        }
        return ret;
    }

    private final List<MTSItemInfo> getCartItems(final MTSCart cart) {
        final List<MTSItemInfo> ret = new ArrayList<MTSItemInfo>();
        MTSItemInfo r;
        final List<Integer> cartt = new ArrayList<Integer>(cart.getCart());
        for (int i : cartt) { //by packageid
            r = buyNow.get(i);
            if (r == null) {
                cart.removeFromCart(i);
            } else if (cart.getType() == 0 || GameConstants.getInventoryType(r.getItem().getItemId()).getType() == cart.getType()) {
                ret.add(r);
            }
        }
        return ret;
    }

    public static class MTSItemInfo {

        private int price;
        private IItem item;
        private String seller;
        private int id; //packageid
        private int cid;
        private long date;

        public MTSItemInfo(int price, IItem item, String seller, int id, int cid, long date) {
            this.item = item;
            this.price = price;
            this.seller = seller;
            this.id = id;
            this.cid = cid;
            this.date = date;
        }

        public IItem getItem() {
            return item;
        }

        public int getPrice() {
            return price;
        }

        public int getRealPrice() {
            return price + getTaxes();
        }

        public int getTaxes() {
            return ServerConstants.MTS_BASE + (int) (price * ServerConstants.MTS_TAX / 100);
        }

        public int getId() {
            return id;
        }

        public int getCharacterId() {
            return cid;
        }

        public long getEndingDate() {
            return date;
        }

        public String getSeller() {
            return seller;
        }
    }
}
