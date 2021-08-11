package client.inventory;

import constants.GameConstants;

public class ModifyInventory {
    
    public static class Types {
        public static final int ADD = 0;
        public static final int UPDATE = 1;
        public static final int MOVE = 2;
        public static final int REMOVE = 3;
    }

    private final int mode;
    private IItem item;
    private short oldPos;

    public ModifyInventory(final int mode, final IItem item) {
        this.mode = mode;
        this.item = item.copy();
    }

    public ModifyInventory(final int mode, final IItem item, final short oldPos) {
        this.mode = mode;
        this.item = item.copy();
        this.oldPos = oldPos;
    }

    public final int getMode() {
        return mode;
    }

    public final int getInventoryType() {
        return GameConstants.getInventoryType(item.getItemId()).getType();
    }

    public final short getPosition() {
        return item.getPosition();
    }

    public final short getOldPosition() {
        return oldPos;
    }

    public final short getQuantity() {
        return item.getQuantity();
    }

    public final IItem getItem() {
        return item;
    }

    public final void clear() {
        this.item = null;
    }
}
