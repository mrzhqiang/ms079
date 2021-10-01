package client.inventory;

public interface IItem extends Comparable<IItem> {

    byte getType();

    short getPosition();

    byte getFlag();

    boolean getLocked();

    short getQuantity();

    String getOwner();

    String getGMLog();

    int getItemId();

    MaplePet getPet();

    int getUniqueId();

    IItem copy();

    long getExpiration();

    void setFlag(byte flag);

    void setLocked(byte flag);
    
    void setUniqueId(int id);

    void setPosition(short position);

    void setExpiration(long expire);

    void setOwner(String owner);

    void setGMLog(String message);

    void setQuantity(short quantity);

    void setGiftFrom(String gf);

    void setEquipLevel(byte j);

    byte getEquipLevel();

    String getGiftFrom();

    MapleRing getRing();
}
