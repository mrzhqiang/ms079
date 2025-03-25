package client.inventory;

import com.github.mrzhqiang.maplestory.domain.DPet;
import com.github.mrzhqiang.maplestory.domain.query.QDPet;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import server.MapleItemInformationProvider;
import server.movement.AbsoluteLifeMovement;
import server.movement.LifeMovement;
import server.movement.LifeMovementFragment;

import java.io.Serializable;
import java.util.List;

public class MaplePet implements Serializable {

    public enum PetFlag {

        ITEM_PICKUP(0x01, 5190000, 5191000),
        EXPAND_PICKUP(0x02, 5190002, 5191002), //idk
        AUTO_PICKUP(0x04, 5190003, 5191003), //idk
        UNPICKABLE(0x08, 5190005, -1), //not coded
        LEFTOVER_PICKUP(0x10, 5190004, 5191004), //idk
        HP_CHARGE(0x20, 5190001, 5191001),
        MP_CHARGE(0x40, 5190006, -1),
        PET_BUFF(0x80, -1, -1), //idk
        PET_DRAW(0x100, 5190007, -1), //nfs
        PET_DIALOGUE(0x200, 5190008, -1); //nfs

        private int i, item, remove;

        PetFlag(int i, int item, int remove) {
            this.i = i;
            this.item = item;
            this.remove = remove;
        }

        public int getValue() {
            return i;
        }

        public boolean check(int flag) {
            return (flag & i) == i;
        }

        public static PetFlag getByAddId(int itemId) {
            for (PetFlag flag : PetFlag.values()) {
                if (flag.item == itemId) {
                    return flag;
                }
            }
            return null;
        }

        public static PetFlag getByDelId(int itemId) {
            for (PetFlag flag : PetFlag.values()) {
                if (flag.remove == itemId) {
                    return flag;
                }
            }
            return null;
        }
    }

    private static long serialVersionUID = 9179541993413738569L;

    private DPet pet;

    private int Fh = 0, stance = 0, uniqueid, petitemid;
    private Vector pos;
    private int summoned = 0;
    private int inventorypos = 0;
    private boolean changed = false;

    private MaplePet(int petitemid, int uniqueid) {
        this.petitemid = petitemid;
        this.uniqueid = uniqueid;
    }

    private MaplePet(int petitemid, int uniqueid, int inventorypos) {
        this.petitemid = petitemid;
        this.uniqueid = uniqueid;
        this.inventorypos = inventorypos;
    }

    private MaplePet(int petitemid, DPet pet, int inventorypos) {
        this.petitemid = petitemid;
        this.pet = pet;
        this.uniqueid = pet.getId();
        this.inventorypos = inventorypos;
    }

    public static MaplePet loadFromDb(int itemid, int petid, int inventorypos) {
        DPet one = new QDPet().id.eq(petid).findOne();
        if (one == null) {
            return null;
        }
        MaplePet ret = new MaplePet(itemid, one, inventorypos);
        ret.changed = false;
        return ret;
    }

    public void saveToDb() {
        if (!changed) {
            return;
        }

        pet.save();
        changed = false;
    }

    public static MaplePet createPet(int itemid, int uniqueid) {
        return createPet(itemid, MapleItemInformationProvider.getInstance().getName(itemid), 1, 0, 100, uniqueid, itemid == 5000054 ? 18000 : 0);
    }

    public static MaplePet createPet(int itemid, String name, int level, int closeness, int fullness, int uniqueid, int secondsLeft) {
        if (uniqueid <= -1) { //wah
            uniqueid = MapleInventoryIdentifier.getInstance();
        }
        int ret1 = MapleItemInformationProvider.getInstance().getPetFlagInfo(itemid);

        DPet dPet = new DPet();
        dPet.setId(uniqueid);
        dPet.setName(name);
        dPet.setLevel(level);
        dPet.setCloseness(closeness);
        dPet.setFullness(fullness);
        dPet.setSeconds(secondsLeft);
        dPet.setFlags(ret1);
        dPet.save();
        return new MaplePet(itemid, dPet, uniqueid);
    }

    public String getName() {
        return pet.getName();
    }

    public void setName(String name) {
        this.pet.setName(name);
        this.changed = true;
    }

    public boolean getSummoned() {
        return summoned > 0;
    }

    public int getSummonedValue() {
        return summoned;
    }

    public void setSummoned(int summoned) {
        this.summoned = (int) summoned;
    }

    public int getInventoryPosition() {
        return inventorypos;
    }

    public void setInventoryPosition(int inventorypos) {
        this.inventorypos = inventorypos;
    }

    public int getUniqueId() {
        return uniqueid;
    }

    public int getCloseness() {
        return pet.getCloseness();
    }

    public void setCloseness(int closeness) {
        if (closeness >= 2147483647 || closeness <= 0) {
            closeness = 1;
        }
        this.pet.setCloseness(closeness);
        this.changed = true;
    }

    public int getLevel() {
        return pet.getLevel();
    }

    public void setLevel(int level) {
        this.pet.setLevel(level);
        this.changed = true;
    }

    public int getFullness() {
        return pet.getFullness();
    }

    public void setFullness(int fullness) {
        this.pet.setFullness(fullness);
        this.changed = true;
    }

    public int getFlags() {
        return pet.getFlags();
    }

    public void setFlags(int fffh) {
        this.pet.setFlags(fffh);
        this.changed = true;
    }

    public int getFh() {
        return Fh;
    }

    public void setFh(int Fh) {
        this.Fh = Fh;
    }

    public Vector getPos() {
        return pos;
    }

    public void setPos(Vector pos) {
        this.pos = pos;
    }

    public int getStance() {
        return stance;
    }

    public void setStance(int stance) {
        this.stance = stance;
    }

    public int getPetItemId() {
        return petitemid;
    }

    public boolean canConsume(int itemId) {
        MapleItemInformationProvider mii = MapleItemInformationProvider.getInstance();
        for (int petId : mii.petsCanConsume(itemId)) {
            if (petId == petitemid) {
                return true;
            }
        }
        return false;
    }

    public void updatePosition(List<LifeMovementFragment> movement) {
        for (LifeMovementFragment move : movement) {
            if (move instanceof LifeMovement) {
                if (move instanceof AbsoluteLifeMovement) {
                    setPos(((LifeMovement) move).getPosition());
                }
                setStance(((LifeMovement) move).getNewstate());
            }
        }
    }

    public int getSecondsLeft() {
        return pet.getSeconds();
    }

    public void setSecondsLeft(int sl) {
        this.pet.setSeconds(sl);
        this.changed = true;
    }
}
