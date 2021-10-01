package client.inventory;


/**
 * 背包类型。
 */
public enum MapleInventoryType {

    UNDEFINED(0),
    EQUIP(1),// 装备
    USE(2),// 消耗
    SETUP(3),// 设置
    ETC(4),// 其他
    CASH(5),// 特殊
    EQUIPPED(-1); // 已装备

    private final byte type;

    MapleInventoryType(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }

    public short getBitfieldEncoding() {
        return (short) (2 << type);
    }

    public static MapleInventoryType getByType(byte type) {
        for (MapleInventoryType value : MapleInventoryType.values()) {
            if (value.getType() == type) {
                return value;
            }
        }
        return null;
    }

    public static MapleInventoryType getByWZName(String name) {
        switch (name) {
            case "Install":
                return SETUP;
            case "Consume":
                return USE;
            case "Etc":
                return ETC;
            case "Cash":
            case "Pet":
                return CASH;
            default:
                return UNDEFINED;
        }
    }
}
