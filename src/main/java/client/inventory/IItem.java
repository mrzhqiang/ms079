package client.inventory;

public interface IItem extends Comparable<IItem> {

    int getType();

    int getPosition();

    int getFlag();

    boolean getLocked();

    int getQuantity();

    String getOwner();

    String getGMLog();

    int getItemId();

    MaplePet getPet();

    int getUniqueId();

    IItem copy();

    long getExpiration();

    void setFlag(int flag);

    void setLocked(int flag);
    
    void setUniqueId(int id);

    void setPosition(int position);

    void setExpiration(long expire);

    void setOwner(String owner);

    void setGMLog(String message);

    void setQuantity(int quantity);

    void setGiftFrom(String gf);

    void setEquipLevel(int j);

    int getEquipLevel();

    String getGiftFrom();

    MapleRing getRing();
}
