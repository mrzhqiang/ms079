package server;

import client.MapleClient;
import client.inventory.IItem;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import com.github.mrzhqiang.maplestory.domain.DStorage;
import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import com.github.mrzhqiang.maplestory.domain.query.QDStorage;
import constants.GameConstants;
import tools.MaplePacketCreator;
import tools.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MapleStorage implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;

    private final DStorage storage;
    private final List<IItem> items;

    private boolean changed = false;
    private final Map<MapleInventoryType, List<IItem>> typeItems = new EnumMap<>(MapleInventoryType.class);

    private MapleStorage(DStorage storage) {
        this.storage = storage;
        this.items = new LinkedList<>();
    }

    public static DStorage create(int id) {
        DStorage storage = new DStorage();
        storage.account = new QDAccount().id.eq(id).findOne();
        storage.slots = (4);
        storage.meso = (0);
        storage.save();
        return storage;
    }

    public static MapleStorage loadStorage(int id) {
        MapleStorage ret;
        Optional<DStorage> optional = new QDStorage().account.id.eq(id).findOneOrEmpty();
        if (optional.isPresent()) {
            DStorage storage = optional.get();
            ret = new MapleStorage(storage);

            for (Pair<IItem, MapleInventoryType> mit : ItemLoader.loadItems(1, false, id).values()) {
                ret.items.add(mit.getLeft());
            }
        } else {
            ret = new MapleStorage(create(id));
        }
        return ret;
    }

    public void saveToDB() {
        if (!changed) {
            return;
        }

        storage.save();

        List<Pair<IItem, MapleInventoryType>> listing = new ArrayList<>();
        for (IItem item : items) {
            listing.add(new Pair<>(item, GameConstants.getInventoryType(item.getItemId())));
        }
        ItemLoader.saveItems(listing);
    }

    public IItem takeOut(byte slot) {
        if (slot >= items.size() || slot < 0) {
            return null;
        }
        changed = true;
        IItem ret = items.remove(slot);
        MapleInventoryType type = GameConstants.getInventoryType(ret.getItemId());
        typeItems.put(type, new ArrayList<>(filterItems(type)));
        return ret;
    }

    public void store(IItem item) {
        changed = true;
        items.add(item);
        MapleInventoryType type = GameConstants.getInventoryType(item.getItemId());
        typeItems.put(type, new ArrayList<>(filterItems(type)));
    }

    public List<IItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    private List<IItem> filterItems(MapleInventoryType type) {
        List<IItem> ret = new LinkedList<>();

        for (IItem item : items) {
            if (GameConstants.getInventoryType(item.getItemId()) == type) {
                ret.add(item);
            }
        }
        return ret;
    }

    public byte getSlot(MapleInventoryType type, byte slot) {
        // MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        byte ret = 0;
        final List<IItem> it = typeItems.get(type);
        if (slot >= it.size() || slot < 0) {
            return -1;
        }
        for (IItem item : items) {
            if (item == it.get(slot)) {
                return ret;
            }
            ret++;
        }
        return -1;
    }

    public void sendStorage(MapleClient c, int npcId) {
        // sort by inventorytype to avoid confusion
        items.sort((o1, o2) -> {
            if (GameConstants.getInventoryType(o1.getItemId()).getType() < GameConstants.getInventoryType(o2.getItemId()).getType()) {
                return -1;
            } else if (GameConstants.getInventoryType(o1.getItemId()) == GameConstants.getInventoryType(o2.getItemId())) {
                return 0;
            } else {
                return 1;
            }
        });
        for (MapleInventoryType type : MapleInventoryType.values()) {
            typeItems.put(type, new ArrayList<>(items));
        }
        c.getSession().write(MaplePacketCreator.getStorage(npcId, storage.slots, items, storage.meso));
    }

    public void sendStored(MapleClient c, MapleInventoryType type) {
        c.getSession().write(MaplePacketCreator.storeStorage(storage.slots, type, typeItems.get(type)));
    }

    public void sendTakenOut(MapleClient c, MapleInventoryType type) {
        c.getSession().write(MaplePacketCreator.takeOutStorage(storage.slots, type, typeItems.get(type)));
    }

    public int getMeso() {
        return storage.meso;
    }

    public IItem findById(int itemId) {
        for (IItem item : items) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }
        return null;
    }

    public void setMeso(int meso) {
        if (meso < 0) {
            return;
        }
        changed = true;
        this.storage.meso = meso;
    }

    public void sendMeso(MapleClient c) {
        c.getSession().write(MaplePacketCreator.mesoStorage(storage.slots, storage.meso));
    }

    public boolean isFull() {
        return items.size() >= storage.slots;
    }

    public int getSlots() {
        return storage.slots;
    }

    public void increaseSlots(int gain) {
        changed = true;
        this.storage.slots += gain;
    }

    public void setSlots(int set) {
        changed = true;
        this.storage.slots = set;
    }

    public void close() {
        typeItems.clear();
    }
}
