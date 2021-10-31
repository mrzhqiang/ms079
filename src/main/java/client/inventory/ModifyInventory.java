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
    private int oldPos;

    public ModifyInventory(final int mode, final IItem item) {
        this.mode = mode;
        this.item = item.copy();
    }

    public ModifyInventory(final int mode, final IItem item, final int oldPos) {
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

    public final int getPosition() {
        return item.getPosition();
    }

    public final int getOldPosition() {
        return oldPos;
    }

    public final int getQuantity() {
        return item.getQuantity();
    }

    public final IItem getItem() {
        return item;
    }

    public final void clear() {
        this.item = null;
    }
}
