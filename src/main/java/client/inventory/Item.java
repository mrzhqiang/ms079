package client.inventory;

import com.github.mrzhqiang.maplestory.domain.DInventoryItem;
import constants.GameConstants;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Item implements IItem, Serializable {

    protected MaplePet pet = null;
    protected MapleRing ring = null;
    protected int itemLevel;

    public DInventoryItem item;

    public Item(DInventoryItem item) {
        this.item = item;
        this.itemLevel = 1;
    }

    public Item(int id, int position, int quantity) {
        this.item = new DInventoryItem();
        this.item.setItemId(id);
        this.item.setPosition(position);
        this.item.setQuantity(quantity);
    }

    public Item(int id, int position, int quantity, int flag) {
        this.item = new DInventoryItem();
        this.item.setItemId(id);
        this.item.setPosition(position);
        this.item.setQuantity(quantity);
        this.item.setFlag(flag);
    }

    public Item(int id, int position, int quantity, int flag, int uniqueid) {
        this.item = new DInventoryItem();
        this.item.setItemId(id);
        this.item.setPosition(position);
        this.item.setQuantity(quantity);
        this.item.setFlag(flag);
        this.item.setUniqueId(uniqueid);
    }

    public IItem copy() {
        Item ret = new Item(item);
        ret.pet = pet;
        return ret;
    }

    public void setPosition(int position) {
        this.item.setPosition(position);

        if (pet != null) {
            pet.setInventoryPosition(position);
        }
    }

    public void setQuantity(int quantity) {
        this.item.setQuantity(quantity);
    }

    @Override
    public int getItemId() {
        return item.getItemId();
    }

    @Override
    public int getPosition() {
        return item.getPosition();
    }

    @Override
    public int getFlag() {
        return item.getFlag();
    }

    public boolean getLocked() {
        return item.getFlag() == ItemFlag.LOCK.getValue();
    }

    @Override
    public int getQuantity() {
        return item.getQuantity();
    }

    @Override
    public int getType() {
        return 2; // An Item
    }

    @Override
    public String getOwner() {
        return item.getOwner();
    }

    public void setOwner(String owner) {
        this.item.setOwner(owner);
    }

    public void setFlag(int flag) {
        this.item.setFlag(flag);
    }

    public void setLocked(int flag) {
        if (flag == 1) {
            setFlag(ItemFlag.LOCK.getValue());
        } else if (flag == 0) {
            setFlag(getFlag() - ItemFlag.LOCK.getValue());
        }
    }

    @Override
    public long getExpiration() {
        return item.getExpireDate();
    }

    public void setExpiration(long expire) {
        this.item.setExpireDate(expire);
    }

    public void setExpiration(LocalDateTime expiration) {
        this.item.setExpireDate(expiration != null ? expiration.toInstant(ZoneOffset.UTC).toEpochMilli() : -1);
    }

    @Override
    public String getGMLog() {
        return item.getGmLog();
    }

    @Override
    public void setGMLog(String message) {
        this.item.setGmLog(message);
    }

    @Override
    public int getUniqueId() {
        return item.getUniqueId();
    }

    @Override
    public void setUniqueId(int id) {
        this.item.setUniqueId(id);
    }

    public MaplePet getPet() {
        return pet;
    }

    public void setPet(MaplePet pet) {
        this.pet = pet;
    }

    @Override
    public void setGiftFrom(String gf) {
        this.item.setSender(gf);
    }

    @Override
    public String getGiftFrom() {
        return item.getSender();
    }

    @Override
    public void setEquipLevel(int gf) {
        this.itemLevel = gf;
    }

    @Override
    public int getEquipLevel() {
        return itemLevel;
    }

    @Override
    public int compareTo(IItem other) {
        return Integer.compare(Math.abs(item.getPosition()), Math.abs(other.getPosition()));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IItem)) {
            return false;
        }
        IItem ite = (IItem) obj;
        return item.getUniqueId() == ite.getUniqueId()
                && item.getItemId() == ite.getItemId()
                && item.getQuantity() == ite.getQuantity()
                && Math.abs(item.getPosition()) == Math.abs(ite.getPosition());
    }

    @Override
    public String toString() {
        return "Item: " + item.getItemId() + " quantity: " + item.getQuantity();
    }

    @Override
    public MapleRing getRing() {
        if (!GameConstants.isEffectRing(item.getItemId()) || getUniqueId() <= 0) {
            return null;
        }
        if (ring == null) {
            ring = MapleRing.loadFromDb(getUniqueId(), item.getPosition() < 0);
        }
        return ring;
    }

    public void setRing(MapleRing ring) {
        this.ring = ring;
    }
}
