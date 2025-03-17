package server;

import client.inventory.IItem;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import com.github.mrzhqiang.maplestory.domain.DMtsItemOther;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDMtsItemOther;
import constants.GameConstants;
import constants.ServerConstants;
import handling.MaplePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.Pair;
import tools.packet.MTSCSPacket;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MTSStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MTSStorage.class);
    //stores all carts all mts items, updates every hour

    private static final long serialVersionUID = 231541893513228L;

    private LocalDateTime lastUpdate = LocalDateTime.now();
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
        LOGGER.debug("Loading MTSStorage :::");
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

    public void addToBuyNow(MTSCart cart, IItem item, int price, int cid, String seller, LocalDateTime expiration) {
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

    private void loadBuyNow() {
        /*new QDMtsItems().tab.eq(1).findEach(it -> {
            if (!idToCart.containsKey(it.characterid)) {
                idToCart.put(it.characterid, new MTSCart(it.characterid));
            }
            Map<Integer, Pair<IItem, MapleInventoryType>> items = ItemLoader.MTS.loadItems(false, it.id);
            if (!items.isEmpty()) {
                for (Pair<IItem, MapleInventoryType> i : items.values()) {
                    buyNow.put(it.id, new MTSItemInfo(it.price, i.getLeft(), it.seller, it.id, it.characterid, it.expiration));
                }
            }
            // set last it item id
            packageId.set(it.id);
        });*/
    }

    public void saveBuyNow(boolean isShutDown) {
        if (this.end) {
            return;
        }
        this.end = isShutDown;
        if (isShutDown) {
            LOGGER.debug("Saving MTS...");
        }

        Map<Integer, ArrayList<IItem>> expire = new HashMap<>();
        List<Integer> toRemove = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        Map<Integer, ArrayList<Pair<IItem, MapleInventoryType>>> items = new HashMap<>();

        mutex.writeLock().lock(); //lock wL so rL will also be locked
        try {
            new QDMtsItemOther().tab.eq(1).delete();

            for (MTSItemInfo m : buyNow.values()) {
                if (now.isAfter(m.getEndingDate())) {
                    if (!expire.containsKey(m.getCharacterId())) {
                        expire.put(m.getCharacterId(), new ArrayList<>());
                    }
                    expire.get(m.getCharacterId()).add(m.getItem());
                    toRemove.add(m.getId());
                    items.put(m.getId(), null); //从 mtsitems 销毁。
                } else {
                    DMtsItemOther item = new DMtsItemOther();
                    item.setId(m.id);
                    item.setTab(1);
                    item.setPrice(m.price);
                    item.setCharacter(new QDCharacter().id.eq(m.getCharacterId()).findOne());
                    item.setExpiration(m.getEndingDate());
                    item.save();

                    if (!items.containsKey(m.getId())) {
                        items.put(m.getId(), new ArrayList<>());
                    }
                    items.get(m.getId()).add(new Pair<>(m.getItem(), GameConstants.getInventoryType(m.getItem().getItemId())));
                }
            }
            for (int i : toRemove) {
                buyNow.remove(i);
            }
        } finally {
            mutex.writeLock().unlock();
        }

        if (isShutDown) {
            LOGGER.debug("Saving MTS items...");
        }
        for (Entry<Integer, ArrayList<Pair<IItem, MapleInventoryType>>> ite : items.entrySet()) {
            ItemLoader.saveItems(ite.getValue(), null);
        }
        if (isShutDown) {
            LOGGER.debug("Saving MTS carts...");
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
        } finally {
            cart_mutex.writeLock().unlock();
        }
        lastUpdate = LocalDateTime.now();
    }

    public final void checkExpirations() {
        if ((Duration.between(lastUpdate, LocalDateTime.now()).toHours() > 1)) { //每隔一小时
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

    /**
     * todo 存储实体，直接保存，避免加锁。
     */
    public static class MTSItemInfo {

        private int price;
        private IItem item;
        private String seller;
        private int id; //packageid
        private int cid;
        private LocalDateTime date;

        public MTSItemInfo(int price, IItem item, String seller, int id, int cid, LocalDateTime date) {
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

        public LocalDateTime getEndingDate() {
            return date;
        }

        public String getSeller() {
            return seller;
        }
    }
}
