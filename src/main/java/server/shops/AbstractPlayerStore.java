package server.shops;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.IItem;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import com.github.mrzhqiang.maplestory.domain.DHiredMerch;
import com.github.mrzhqiang.maplestory.domain.query.QDHiredMerch;
import com.google.common.collect.Lists;
import constants.GameConstants;
import handling.MaplePacket;
import handling.channel.ChannelServer;
import handling.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.maps.AbstractMapleMapObject;
import server.maps.MapleMap;
import server.maps.MapleMapObjectType;
import tools.Pair;
import tools.packet.PlayerShopPacket;

import java.lang.ref.WeakReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractPlayerStore extends AbstractMapleMapObject implements IMaplePlayerShop {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPlayerStore.class);

    protected boolean open = false, available = false;
    protected String ownerName, des, pass;
    protected int ownerId, owneraccount, itemId, channel, map;
    protected AtomicInteger meso = new AtomicInteger(0);
    protected List<WeakReference<MapleCharacter>> chrs;
    protected List<String> visitors = new LinkedList<>();
    protected List<BoughtItem> bought = new LinkedList<>();
    protected List<MaplePlayerShopItem> items = new LinkedList<>();
    // private static final Logger log = Logger.getLogger(AbstractPlayerStore.class);

    public final DHiredMerch merch;

    public AbstractPlayerStore(MapleCharacter owner, int itemId, String desc, String pass, int slots) {
        this.merch = new DHiredMerch();
        merch.setCharacter(owner.character);
        merch.setAccount(owner.character.getAccount());
        merch.setMesos(0);
        merch.setMap(owner.getMapId());
        merch.setChannel(owner.getClient().getChannel());
        merch.setTime(LocalDateTime.now());
        this.setPosition(owner.getPosition());
        this.itemId = itemId;
        this.des = desc;
        this.pass = pass;
        this.chrs = Lists.newArrayListWithCapacity(slots);
        for (int i = 0; i < chrs.size(); i++) {
            chrs.add(new WeakReference<>(null));
        }
    }

    @Override
    public int getMaxSize() {
        return chrs.size() + 1;
    }

    @Override
    public int getSize() {
        return getFreeSlot() == -1 ? getMaxSize() : getFreeSlot();
    }

    @Override
    public void broadcastToVisitors(MaplePacket packet) {
        broadcastToVisitors(packet, true);
    }

    public void broadcastToVisitors(MaplePacket packet, boolean owner) {
        for (WeakReference<MapleCharacter> chr : chrs) {
            if (chr != null && chr.get() != null) {
                chr.get().getClient().getSession().write(packet);
            }
        }
        if (getShopType() != IMaplePlayerShop.HIRED_MERCHANT && owner && getMCOwner() != null) {
            getMCOwner().getClient().getSession().write(packet);
        }
    }

    public void broadcastToVisitors(MaplePacket packet, int exception) {
        for (WeakReference<MapleCharacter> chr : chrs) {
            if (chr != null && chr.get() != null && getVisitorSlot(chr.get()) != exception) {
                chr.get().getClient().getSession().write(packet);
            }
        }
        if (getShopType() != IMaplePlayerShop.HIRED_MERCHANT && getMCOwner() != null && exception != ownerId) {
            getMCOwner().getClient().getSession().write(packet);
        }
    }

    @Override
    public int getMeso() {
        return meso.get();
    }

    @Override
    public void setMeso(int meso) {
        this.meso.set(meso);
    }

    @Override
    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    public boolean saveItems() {
        if (getShopType() != IMaplePlayerShop.HIRED_MERCHANT) { //hired merch only
            return false;
        }

        new QDHiredMerch().account.id.eq(owneraccount).or().character.id.eq(ownerId).delete();

        merch.setMesos(meso.get());
        merch.setTime(LocalDateTime.now());
        merch.save();

        List<Pair<IItem, MapleInventoryType>> iters = new ArrayList<>();
        for (MaplePlayerShopItem pItems : items) {
            if ((pItems.item == null)
                    || (pItems.bundles <= 0)
                    || ((pItems.item.getQuantity() <= 0) && (!GameConstants.isRechargable(pItems.item.getItemId())))) {
                continue;
            }
            IItem item = pItems.item.copy();
            item.setQuantity((short) (item.getQuantity() * pItems.bundles));
            //item.setFlag((byte) (pItems.flag));
            iters.add(new Pair<>(item, GameConstants.getInventoryType(item.getItemId())));
        }
        // ItemLoader.HIRED_MERCHANT.saveItems(iters, this.ownerId);

        ItemLoader.saveItems(iters, null);
        return true;
    }

    public MapleCharacter getVisitor(int num) {
        return chrs.get(num).get();
    }

    @Override
    public void update() {
        if (isAvailable()) {
            if (getShopType() == IMaplePlayerShop.HIRED_MERCHANT) {
                getMap().broadcastMessage(PlayerShopPacket.updateHiredMerchant((HiredMerchant) this));
            } else if (getMCOwner() != null) {
                getMap().broadcastMessage(PlayerShopPacket.sendPlayerShopBox(getMCOwner()));
            }
        }
    }

    @Override
    public void addVisitor(MapleCharacter visitor) {
        int i = getFreeSlot();
        if (i > 0) {
            if (getShopType() >= 3) {
                broadcastToVisitors(PlayerShopPacket.getMiniGameNewVisitor(visitor, i, (MapleMiniGame) this));
            } else {
                broadcastToVisitors(PlayerShopPacket.shopVisitorAdd(visitor, i));
            }
            chrs.set(i - 1, new WeakReference<>(visitor));
            if (!isOwner(visitor)) {
                visitors.add(visitor.getName());
            }
            if (i == 3) {
                update();
            }
        }
    }

    @Override
    public void removeVisitor(MapleCharacter visitor) {
        final byte slot = getVisitorSlot(visitor);
        boolean shouldUpdate = getFreeSlot() == -1;
        if (slot > 0) {
            broadcastToVisitors(PlayerShopPacket.shopVisitorLeave(slot), slot);
            chrs.set(slot - 1, new WeakReference<>(null));
            if (shouldUpdate) {
                update();
            }
        }
    }

    @Override
    public byte getVisitorSlot(MapleCharacter visitor) {
        for (byte i = 0; i < chrs.size(); i++) {
            WeakReference<MapleCharacter> reference = chrs.get(i);
            if (reference != null) {
                MapleCharacter character = reference.get();
                if (character != null && character.getId() == visitor.getId()) {
                    return (byte) (i + 1);
                }
            }
        }
        if (visitor.getId() == ownerId) { //can visit own store in merch, otherwise not.
            return 0;
        }
        return -1;
    }

    @Override
    public void removeAllVisitors(int error, int type) {
        for (int i = 0; i < chrs.size(); i++) {
            MapleCharacter visitor = getVisitor(i);
            if (visitor != null) {
                if (type != -1) {
                    visitor.getClient().getSession().write(PlayerShopPacket.shopErrorMessage(error, type));
                }
                broadcastToVisitors(PlayerShopPacket.shopVisitorLeave(getVisitorSlot(visitor)), getVisitorSlot(visitor));
                visitor.setPlayerShop(null);
                chrs.set(i, new WeakReference<>(null));
            }
        }
        update();
    }

    @Override
    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public int getOwnerId() {
        return ownerId;
    }

    @Override
    public int getOwnerAccId() {
        return owneraccount;
    }

    @Override
    public String getDescription() {
        if (des == null) {
            return "";
        }
        return des;
    }

    @Override
    public List<Pair<Byte, MapleCharacter>> getVisitors() {
        List<Pair<Byte, MapleCharacter>> chrz = new LinkedList<Pair<Byte, MapleCharacter>>();
        for (byte i = 0; i < chrs.size(); i++) { //include owner or no
            WeakReference<MapleCharacter> reference = chrs.get(i);
            if (reference != null) {
                MapleCharacter character = reference.get();
                if (character != null) {
                    chrz.add(new Pair<>((byte) (i + 1), character));
                }
            }
        }
        return chrz;
    }

    @Override
    public List<MaplePlayerShopItem> getItems() {
        return items;
    }

    @Override
    public void addItem(MaplePlayerShopItem item) {
        //LOGGER.debug("Adding item ... 2");
        items.add(item);
    }

    @Override
    public boolean removeItem(int item) {
        return false;
    }

    @Override
    public void removeFromSlot(int slot) {
        items.remove(slot);
    }

    @Override
    public byte getFreeSlot() {
        for (byte i = 0; i < chrs.size(); i++) {
            if (chrs.get(i) == null || chrs.get(i).get() == null) {
                return (byte) (i + 1);
            }
        }
        return -1;
    }

    @Override
    public int getItemId() {
        return itemId;
    }

    @Override
    public boolean isOwner(MapleCharacter chr) {
        return chr.getId() == ownerId && chr.getName().equals(ownerName);
    }

    @Override
    public String getPassword() {
        if (pass == null) {
            return "";
        }
        return pass;
    }

    @Override
    public void sendDestroyData(MapleClient client) {
    }

    @Override
    public void sendSpawnData(MapleClient client) {
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.SHOP;
    }

    public MapleCharacter getMCOwner() {
        return getMap().getCharacterById(ownerId);
    }

    public MapleCharacter getMCOwnerWorld() {
        int ourChannel = World.Find.findChannel(ownerId);
        if (ourChannel <= 0) {
            return null;
        }
        return ChannelServer.getInstance(ourChannel).getPlayerStorage().getCharacterById(ownerId);
    }

    public MapleMap getMap() {
        return ChannelServer.getInstance(channel).getMapFactory().getMap(map);
    }

    @Override
    public int getGameType() {
        if (getShopType() == IMaplePlayerShop.HIRED_MERCHANT) { //hiredmerch
            return 5;
        } else if (getShopType() == IMaplePlayerShop.PLAYER_SHOP) { //shop lol
            return 4;
        } else if (getShopType() == IMaplePlayerShop.OMOK) { //omok
            return 1;
        } else if (getShopType() == IMaplePlayerShop.MATCH_CARD) { //matchcard
            return 2;
        }
        return 0;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void setAvailable(boolean b) {
        this.available = b;
    }

    @Override
    public List<BoughtItem> getBoughtItems() {
        return bought;
    }

    public static final class BoughtItem {

        public int id;
        public int quantity;
        public int totalPrice;
        public String buyer;

        public BoughtItem(final int id, final int quantity, final int totalPrice, final String buyer) {
            this.id = id;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
            this.buyer = buyer;
        }
    }
}
