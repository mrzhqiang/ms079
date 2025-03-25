package client.inventory;

import javax.annotation.Nullable;

public enum InventoryItemType {
    INVENTORY(0),
    STORAGE(1),
    CASHSHOP_EXPLORER(2),
    CASHSHOP_CYGNUS(3),
    CASHSHOP_ARAN(4),
    HIRED_MERCHANT(5),
    DUEY(6),
    CASHSHOP_EVAN(7),
    MTS(8),
    MTS_TRANSFER(9),
    CASHSHOP_DB(10),
    CASHSHOP_RESIST(11),
    ;

    private final int type;
    @Nullable
    public static InventoryItemType getByType(int type) {
        for (InventoryItemType value : InventoryItemType.values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }

    InventoryItemType(int type) {
        this.type = type;
    }
}
