package client.inventory;

import client.MapleCharacter;
import com.google.common.collect.Maps;
import constants.GameConstants;
import server.MapleItemInformationProvider;
import tools.MaplePacketCreator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MapleInventory implements Iterable<IItem>, Serializable {

    private final AtomicInteger slotLimit;
    private final MapleInventoryType type;

    private final Map<Integer, IItem> inventory = Maps.newLinkedHashMap();

    /**
     * Creates a new instance of MapleInventory
     */
    public MapleInventory(MapleInventoryType type, int slotLimit) {
        this.slotLimit = new AtomicInteger(slotLimit);
        this.type = type;
    }

    public void addSlot(byte slot) {
        this.slotLimit.getAndAdd(slot);

        if (slotLimit.get() > 96) {
            slotLimit.set(96);
        }
    }

    public int getSlotLimit() {
        return slotLimit.intValue();
    }

    public void setSlotLimit(int slot) {
        if (slot > 96) {
            slot = 96;
        }
        slotLimit.set(slot);
    }

    /**
     * Returns the item with its slot id if it exists within the inventory,
     * otherwise null is returned
     */
    public IItem findById(int itemId) {
        for (IItem item : inventory.values()) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }
        return null;
    }

    public IItem findByUniqueId(int itemId) {
        for (IItem item : inventory.values()) {
            if (item.getUniqueId() == itemId) {
                return item;
            }
        }
        return null;
    }

    public int countById(int itemId) {
        int possesed = 0;
        for (IItem item : inventory.values()) {
            if (item.getItemId() == itemId) {
                possesed += item.getQuantity();
            }
        }
        return possesed;
    }

    public List<IItem> listById(int itemId) {
        List<IItem> ret = new ArrayList<>();
        for (IItem item : inventory.values()) {
            if (item.getItemId() == itemId) {
                ret.add(item);
            }
        }
        // the linkedhashmap does impose insert order as returned order but we can not guarantee that this is still the
        // correct order - blargh, we could empty the map and reinsert in the correct order after each inventory
        // addition, or we could use an array/list, it's only 255 entries anyway...
        if (ret.size() > 1) {
            Collections.sort(ret);
        }
        return ret;
    }

    public Collection<IItem> list() {
        return inventory.values();
    }

    /**
     * Adds the item to the inventory and returns the assigned slot id
     */
    public int addItem(IItem item) {
        int slotId = getNextFreeSlot();
        if (slotId < 0) {
            return -1;
        }
        inventory.put(slotId, item);
        item.setPosition(slotId);
        return slotId;
    }

    public void addFromDB(IItem item) {
        if (item.getPosition() < 0 && !type.equals(MapleInventoryType.EQUIPPED)) {
            // 这会导致很多卡住问题，直到我们完成位置检查
            return;
        }
        inventory.put(item.getPosition(), item);
    }

    public boolean move2(int sSlot, int dSlot, int slotMax) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        IItem source = this.inventory.get(sSlot);
        IItem target = this.inventory.get(dSlot);
        if (source == null) {
            throw new InventoryException("Trying to move empty slot");
        }
        if (target == null) {
            source.setPosition(dSlot);
            this.inventory.put(dSlot, source);
            this.inventory.remove(sSlot);
        } else if ((target.getItemId() == source.getItemId()) && (!GameConstants.isThrowingStar(source.getItemId())) && (!GameConstants.isBullet(source.getItemId()))) {
            if (this.type.getType() == MapleInventoryType.EQUIP.getType()) {
                swap(target, source);
            }
            if (source.getQuantity() + target.getQuantity() > slotMax) {
                short rest = (short) (source.getQuantity() + target.getQuantity() - slotMax);
                if (rest + slotMax != source.getQuantity() + target.getQuantity()) {
                    return false;
                }
                source.setQuantity(rest);
                target.setQuantity(slotMax);
            } else {
                target.setQuantity((short) (source.getQuantity() + target.getQuantity()));
                this.inventory.remove(sSlot);
            }
        } else {
            swap(target, source);
        }
        return true;
    }

    public void move(int sSlot, int dSlot, int slotMax) {
        if (dSlot > slotLimit.get()) {
            return;
        }
        Item source = (Item) inventory.get(sSlot);
        Item target = (Item) inventory.get(dSlot);
        if (source == null) {
            throw new InventoryException("Trying to move empty slot");
        }
        if (target == null) {
            source.setPosition(dSlot);
            inventory.put(dSlot, source);
            inventory.remove(sSlot);
        } else if (target.getItemId() == source.getItemId() && !GameConstants.isThrowingStar(source.getItemId()) && !GameConstants.isBullet(source.getItemId()) && target.getOwner().equals(source.getOwner()) && target.getExpiration() == source.getExpiration()) {
            if (type.getType() == MapleInventoryType.EQUIP.getType() || type.getType() == MapleInventoryType.CASH.getType()) {
                swap(target, source);
            } else if (source.getQuantity() + target.getQuantity() > slotMax) {
                source.setQuantity((short) ((source.getQuantity() + target.getQuantity()) - slotMax));
                target.setQuantity(slotMax);
            } else {
                target.setQuantity((short) (source.getQuantity() + target.getQuantity()));
                inventory.remove(sSlot);
            }
        } else {
            swap(target, source);
        }
    }

    private void swap(IItem source, IItem target) {
        inventory.remove(source.getPosition());
        inventory.remove(target.getPosition());
        int swapPos = source.getPosition();
        source.setPosition(target.getPosition());
        target.setPosition(swapPos);
        inventory.put(source.getPosition(), source);
        inventory.put(target.getPosition(), target);
    }

    public IItem getItem(int slot) {
        return inventory.get(slot);
    }

    public void removeItem(int slot) {
        removeItem(slot, 1, false);
    }

    public void removeItem(int slot, int quantity, boolean allowZero) {
        removeItem(slot, quantity, allowZero, null);
    }

    public void removeItem(int slot, int quantity, boolean allowZero, MapleCharacter chr) {
        IItem item = inventory.get(slot);
        if (item == null) { // TODO is it ok not to throw an exception here?
            return;
        }
        item.setQuantity((short) (item.getQuantity() - quantity));
        if (item.getQuantity() < 0) {
            item.setQuantity((short) 0);
        }
        if (item.getQuantity() == 0 && !allowZero) {
            removeSlot(slot);
        }
        if (chr != null) {
            chr.getClient().sendPacket(MaplePacketCreator.modifyInventory(false, new ModifyInventory(ModifyInventory.Types.REMOVE, item)));
            chr.dropMessage(5, "期限道具[" + MapleItemInformationProvider.getInstance().getName(item.getItemId()) + "]已经过期");
        }
    }

    public void removeSlot(int slot) {
        inventory.remove(slot);
    }

    public boolean isFull() {
        return inventory.size() >= slotLimit.get();
    }

    public boolean isFull(int margin) {
        return inventory.size() + margin >= slotLimit.get();
    }

    /**
     * Returns the next empty slot id, -1 if the inventory is full
     */
    public int getNextFreeSlot() {
        if (isFull()) {
            return -1;
        }
        for (int i = 1; i <= slotLimit.get(); i++) {
            if (!inventory.containsKey(i)) {
                return i;
            }
        }
        return -1;
    }

    public int getNumFreeSlot() {
        if (isFull()) {
            return 0;
        }
        int free = 0;
        for (int i = 1; i <= slotLimit.get(); i++) {
            if (!inventory.containsKey(i)) {
                free++;
            }
        }
        return free;
    }

    public MapleInventoryType getType() {
        return type;
    }

    @Override
    public Iterator<IItem> iterator() {
        return Collections.unmodifiableCollection(inventory.values()).iterator();
    }
    
}
