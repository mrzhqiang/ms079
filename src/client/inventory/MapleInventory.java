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
package client.inventory;

import client.MapleCharacter;
import constants.GameConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;
import server.MapleItemInformationProvider;
import tools.MaplePacketCreator;

public class MapleInventory implements Iterable<IItem>, Serializable {

    private Map<Short, IItem> inventory;
    private byte slotLimit = 0;
    private MapleInventoryType type;

    /**
     * Creates a new instance of MapleInventory
     */
    public MapleInventory(MapleInventoryType type, byte slotLimit) {
        this.inventory = new LinkedHashMap<Short, IItem>();
        this.slotLimit = slotLimit;
        this.type = type;
    }

    public void addSlot(byte slot) {
        this.slotLimit += slot;

        if (slotLimit > 96) {
            slotLimit = 96;
        }
    }

    public byte getSlotLimit() {
        return slotLimit;
    }

    public void setSlotLimit(byte slot) {
        if (slot > 96) {
            slot = 96;
        }
        slotLimit = slot;
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
        List<IItem> ret = new ArrayList<IItem>();
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
    public short addItem(IItem item) {
        short slotId = getNextFreeSlot();
        if (slotId < 0) {
            return -1;
        }
        inventory.put(slotId, item);
        item.setPosition(slotId);
        return slotId;
    }

    public void addFromDB(IItem item) {
        if (item.getPosition() < 0 && !type.equals(MapleInventoryType.EQUIPPED)) {
            // This causes a lot of stuck problem, until we are done with position checking
            return;
        }
        inventory.put(item.getPosition(), item);
    }

    public boolean move2(byte sSlot, byte dSlot, short slotMax) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        IItem source = (Item) this.inventory.get(Byte.valueOf(sSlot));
        IItem target = (Item) this.inventory.get(Byte.valueOf(dSlot));
        if (source == null) {
            throw new InventoryException("Trying to move empty slot");
        }
        if (target == null) {
            source.setPosition(dSlot);
            this.inventory.put(Short.valueOf(dSlot), source);
            this.inventory.remove(Byte.valueOf(sSlot));
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
                this.inventory.remove(Byte.valueOf(sSlot));
            }
        } else {
            swap(target, source);
        }
        return true;
    }

    public void move(short sSlot, short dSlot, short slotMax) {
        if (dSlot > slotLimit) {
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
        short swapPos = source.getPosition();
        source.setPosition(target.getPosition());
        target.setPosition(swapPos);
        inventory.put(source.getPosition(), source);
        inventory.put(target.getPosition(), target);
    }

    public IItem getItem(short slot) {
        return inventory.get(slot);
    }

    public void removeItem(short slot) {
        removeItem(slot, (short) 1, false);
    }

    public void removeItem(short slot, short quantity, boolean allowZero) {
        removeItem(slot, quantity, allowZero, null);
    }

    public void removeItem(short slot, short quantity, boolean allowZero, MapleCharacter chr) {
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

    public void removeSlot(short slot) {
        inventory.remove(slot);
    }

    public boolean isFull() {
        return inventory.size() >= slotLimit;
    }

    public boolean isFull(int margin) {
        return inventory.size() + margin >= slotLimit;
    }

    /**
     * Returns the next empty slot id, -1 if the inventory is full
     */
    public short getNextFreeSlot() {
        if (isFull()) {
            return -1;
        }
        for (short i = 1; i <= slotLimit; i++) {
            if (!inventory.keySet().contains(i)) {
                return i;
            }
        }
        return -1;
    }

    public short getNumFreeSlot() {
        if (isFull()) {
            return 0;
        }
        byte free = 0;
        for (short i = 1; i <= slotLimit; i++) {
            if (!inventory.keySet().contains(i)) {
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
