package client.inventory;

import com.github.mrzhqiang.maplestory.domain.DCsItem;
import constants.GameConstants;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class MapleCsItem implements IItem, Serializable {

    protected MaplePet pet = null;
    protected MapleRing ring = null;
    protected int itemLevel;

    public final DCsItem item;

    public MapleCsItem(DCsItem item) {
        this.item = item;
        this.itemLevel = 1;
    }

    public IItem copy() {
        MapleCsItem ret = new MapleCsItem(item);
        ret.pet = pet;
        return ret;
    }

    public void setPosition(int position) {
        this.item.position = position;

        if (pet != null) {
            pet.setInventoryPosition(position);
        }
    }

    public void setQuantity(int quantity) {
        this.item.quantity = quantity;
    }

    @Override
    public int getItemId() {
        return item.itemid;
    }

    @Override
    public int getPosition() {
        return item.position;
    }

    @Override
    public int getFlag() {
        return item.flag;
    }

    public boolean getLocked() {
        return item.flag == ItemFlag.LOCK.getValue();
    }

    @Override
    public int getQuantity() {
        return item.quantity;
    }

    @Override
    public int getType() {
        return 2; // An Item
    }

    @Override
    public String getOwner() {
        return item.owner;
    }

    public void setOwner(String owner) {
        this.item.owner = owner;
    }

    public void setFlag(int flag) {
        this.item.flag = flag;
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
        return item.expiredate.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public void setExpiration(long expire) {
        this.item.expiredate = LocalDateTime.ofInstant(Instant.ofEpochMilli(expire), ZoneOffset.UTC);
    }

    public void setExpiration(LocalDateTime expiration) {
        this.item.expiredate = expiration;
    }

    @Override
    public String getGMLog() {
        return item.gmLog;
    }

    @Override
    public void setGMLog(String message) {
        this.item.gmLog = message;
    }

    @Override
    public int getUniqueId() {
        return item.uniqueid;
    }

    @Override
    public void setUniqueId(int id) {
        this.item.uniqueid = id;
    }

    public MaplePet getPet() {
        return pet;
    }

    public void setPet(MaplePet pet) {
        this.pet = pet;
    }

    @Override
    public void setGiftFrom(String gf) {
        this.item.sender = gf;
    }

    @Override
    public String getGiftFrom() {
        return item.sender;
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
        return Integer.compare(Math.abs(item.position), Math.abs(other.getPosition()));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IItem)) {
            return false;
        }
        IItem ite = (IItem) obj;
        return item.uniqueid == ite.getUniqueId()
                && item.itemid == ite.getItemId()
                && item.quantity == ite.getQuantity()
                && Math.abs(item.position) == Math.abs(ite.getPosition());
    }

    @Override
    public String toString() {
        return "Item: " + item.itemid + " quantity: " + item.quantity;
    }

    @Override
    public MapleRing getRing() {
        if (!GameConstants.isEffectRing(item.itemid) || getUniqueId() <= 0) {
            return null;
        }
        if (ring == null) {
            ring = MapleRing.loadFromDb(getUniqueId(), item.position < 0);
        }
        return ring;
    }

    public void setRing(MapleRing ring) {
        this.ring = ring;
    }
}
