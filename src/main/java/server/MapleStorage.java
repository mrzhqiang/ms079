package server;

import client.MapleClient;
import client.inventory.IItem;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import com.github.mrzhqiang.maplestory.domain.DStorage;
import com.github.mrzhqiang.maplestory.domain.query.QDStorage;
import constants.GameConstants;
import database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.MaplePacketCreator;
import tools.Pair;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class MapleStorage implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleStorage.class);

    private static final long serialVersionUID = 9179541993413738569L;
    private int id;
    private int accountId;
    private List<IItem> items;
    private int meso;
    private int slots;
    private boolean changed = false;
    private Map<MapleInventoryType, List<IItem>> typeItems = new EnumMap<>(MapleInventoryType.class);

    private MapleStorage(int id, int slots, int meso, int accountId) {
        this.id = id;
        this.slots = slots;
        this.items = new LinkedList<>();
        this.meso = meso;
        this.accountId = accountId;
    }

    public static int create(int id) {
        DStorage storage = new DStorage();
        storage.setAccountid(id);
        storage.setSlots(4);
        storage.setMeso(0);
        storage.save();
        return storage.getStorageid();
    }

    public static MapleStorage loadStorage(int id) {
        MapleStorage ret;
        int storeId;
        Optional<DStorage> optional = new QDStorage().accountid.eq(id).findOneOrEmpty();
        if (optional.isPresent()) {
            DStorage storage = optional.get();
            storeId = storage.getStorageid();
            ret = new MapleStorage(storeId, storage.getSlots(), storage.getMeso(), id);

            for (Pair<IItem, MapleInventoryType> mit : ItemLoader.STORAGE.loadItems(false, id).values()) {
                ret.items.add(mit.getLeft());
            }
        } else {
            storeId = create(id);
            ret = new MapleStorage(storeId, (byte) 4, 0, id);
        }
        return ret;
    }

    public void saveToDB() {
        if (!changed) {
            return;
        }
        try {
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement("UPDATE storages SET slots = ?, meso = ? WHERE storageid = ?");
            ps.setInt(1, slots);
            ps.setInt(2, meso);
            ps.setInt(3, id);
            ps.executeUpdate();
            ps.close();

            List<Pair<IItem, MapleInventoryType>> listing = new ArrayList<>();
            for (final IItem item : items) {
                listing.add(new Pair<>(item, GameConstants.getInventoryType(item.getItemId())));
            }
            ItemLoader.STORAGE.saveItems(listing, accountId);
        } catch (SQLException ex) {
            LOGGER.error("Error saving storage" + ex);
        }
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
        Collections.sort(items, (o1, o2) -> {
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
        c.getSession().write(MaplePacketCreator.getStorage(npcId, (byte) slots, items, meso));
    }

    public void sendStored(MapleClient c, MapleInventoryType type) {
        c.getSession().write(MaplePacketCreator.storeStorage((byte) slots, type, typeItems.get(type)));
    }

    public void sendTakenOut(MapleClient c, MapleInventoryType type) {
        c.getSession().write(MaplePacketCreator.takeOutStorage((byte) slots, type, typeItems.get(type)));
    }

    public int getMeso() {
        return meso;
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
        this.meso = meso;
    }

    public void sendMeso(MapleClient c) {
        c.getSession().write(MaplePacketCreator.mesoStorage((byte) slots, meso));
    }

    public boolean isFull() {
        return items.size() >= slots;
    }

    public int getSlots() {
        return slots;
    }

    public void increaseSlots(byte gain) {
        changed = true;
        this.slots += gain;
    }

    public void setSlots(byte set) {
        changed = true;
        this.slots = set;
    }

    public void close() {
        typeItems.clear();
    }
}
