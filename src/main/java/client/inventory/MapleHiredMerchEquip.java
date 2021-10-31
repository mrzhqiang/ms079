package client.inventory;

import client.MapleClient;
import com.github.mrzhqiang.maplestory.domain.DHiredMerchEquipment;
import com.github.mrzhqiang.maplestory.domain.DHiredMerchItem;
import constants.GameConstants;
import server.MapleItemInformationProvider;
import server.Randomizer;
import tools.MaplePacketCreator;
import tools.Pair;

import java.io.Serializable;
import java.util.List;

public class MapleHiredMerchEquip extends MapleHiredMerchItem implements IEquip, Serializable {

    private int charmExp = 0;
    private int pvpDamage = 0;

    public DHiredMerchEquipment equipment;

    // super(id, position, (short) 1, (byte) 0);
    public MapleHiredMerchEquip(DHiredMerchItem item) {
        super(item);
        this.equipment = item.equipment;
    }

    @Override
    public IItem copy() {
        MapleHiredMerchEquip equip = new MapleHiredMerchEquip(item);
        equip.itemLevel = itemLevel;
        return equip;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public int getUpgradeSlots() {
        return equipment.upgradeslots;
    }

    @Override
    public int getStr() {
        return equipment.str;
    }

    @Override
    public int getDex() {
        return equipment.dex;
    }

    @Override
    public int getInt() {
        return equipment.intField;
    }

    @Override
    public int getLuk() {
        return equipment.luk;
    }

    @Override
    public int getHp() {
        return equipment.hp;
    }

    @Override
    public int getMp() {
        return equipment.mp;
    }

    @Override
    public int getWatk() {
        return equipment.watk;
    }

    @Override
    public int getMatk() {
        return equipment.matk;
    }

    @Override
    public int getWdef() {
        return equipment.wdef;
    }

    @Override
    public int getMdef() {
        return equipment.mdef;
    }

    @Override
    public int getAcc() {
        return equipment.acc;
    }

    @Override
    public int getAvoid() {
        return equipment.avoid;
    }

    @Override
    public int getHands() {
        return equipment.hands;
    }

    @Override
    public int getSpeed() {
        return equipment.speed;
    }

    @Override
    public int getJump() {
        return equipment.jump;
    }

    public void setStr(int str) {
        if (str < 0) {
            str = 0;
        }
        this.equipment.str = str;
    }

    public void setDex(int dex) {
        if (dex < 0) {
            dex = 0;
        }
        this.equipment.dex = dex;
    }

    public void setInt(int _int) {
        if (_int < 0) {
            _int = 0;
        }
        this.equipment.intField = _int;
    }

    public void setLuk(int luk) {
        if (luk < 0) {
            luk = 0;
        }
        this.equipment.luk = luk;
    }

    public void setHp(int hp) {
        if (hp < 0) {
            hp = 0;
        }
        this.equipment.hp = hp;
    }

    public void setMp(int mp) {
        if (mp < 0) {
            mp = 0;
        }
        this.equipment.mp = mp;
    }

    public void setWatk(int watk) {
        if (watk < 0) {
            watk = 0;
        }
        this.equipment.watk = watk;
    }

    public void setMatk(int matk) {
        if (matk < 0) {
            matk = 0;
        }
        this.equipment.matk = matk;
    }

    public void setWdef(int wdef) {
        if (wdef < 0) {
            wdef = 0;
        }
        this.equipment.wdef = wdef;
    }

    public void setMdef(int mdef) {
        if (mdef < 0) {
            mdef = 0;
        }
        this.equipment.mdef = mdef;
    }

    public void setAcc(int acc) {
        if (acc < 0) {
            acc = 0;
        }
        this.equipment.acc = acc;
    }

    public void setAvoid(int avoid) {
        if (avoid < 0) {
            avoid = 0;
        }
        this.equipment.avoid = avoid;
    }

    public void setHands(int hands) {
        if (hands < 0) {
            hands = 0;
        }
        this.equipment.hands = hands;
    }

    public void setSpeed(int speed) {
        if (speed < 0) {
            speed = 0;
        }
        this.equipment.speed = speed;
    }

    public void setJump(int jump) {
        if (jump < 0) {
            jump = 0;
        }
        this.equipment.jump = jump;
    }

    public void setUpgradeSlots(int upgradeSlots) {
        this.equipment.upgradeslots = upgradeSlots;
    }

    @Override
    public int getLevel() {
        return equipment.level;
    }

    public void setLevel(int level) {
        this.equipment.level = level;
    }

    @Override
    public int getViciousHammer() {
        return equipment.viciousHammer;
    }

    public void setViciousHammer(int ham) {
        equipment.viciousHammer = ham;
    }

    @Override
    public int getItemEXP() {
        return equipment.itemEXP;
    }

    public void setItemEXP(int itemEXP) {
        if (itemEXP < 0) {
            itemEXP = 0;
        }
        this.equipment.itemEXP = itemEXP;
    }

    @Override
    public int getEquipExp() {
        if (equipment.itemEXP <= 0) {
            return 0;
        }
        //aproximate value
        if (GameConstants.isWeapon(getItemId())) {
            return equipment.itemEXP / IEquip.WEAPON_RATIO;
        } else {
            return equipment.itemEXP / IEquip.ARMOR_RATIO;
        }
    }

    @Override
    public int getEquipExpForLevel() {
        if (getEquipExp() <= 0) {
            return 0;
        }
        int expz = getEquipExp();
        for (int i = getBaseLevel(); i <= GameConstants.getMaxLevel(getItemId()); i++) {
            if (expz >= GameConstants.getExpForLevel(i, getItemId())) {
                expz -= GameConstants.getExpForLevel(i, getItemId());
            } else { //for 0, dont continue;
                break;
            }
        }
        return expz;
    }

    /**
     * @return
     */
    @Override
    public int getExpPercentage() {
        return this.equipment.itemEXP;
    }

    /*@Override
    public int getExpPercentage() {
        if (getEquipLevels() < getBaseLevel() || getEquipLevels() > GameConstants.getMaxLevel(getItemId()) || GameConstants.getExpForLevel(getEquipLevels(), getItemId()) <= 0) {
            return 0;
        }
        return getEquipExpForLevel() * 100 / GameConstants.getExpForLevel(getEquipLevels(), getItemId());
    }*/
    public int getEquipLevels() {
        if (GameConstants.getMaxLevel(getItemId()) <= 0) {
            return 0;
        } else if (getEquipExp() <= 0) {
            return getBaseLevel();
        }
        int levelz = getBaseLevel();
        int expz = getEquipExp();
        for (int i = levelz; (GameConstants.getStatFromWeapon(getItemId()) == null ? (i <= GameConstants.getMaxLevel(getItemId())) : (i < GameConstants.getMaxLevel(getItemId()))); i++) {
            if (expz >= GameConstants.getExpForLevel(i, getItemId())) {
                levelz++;
                expz -= GameConstants.getExpForLevel(i, getItemId());
            } else { //for 0, dont continue;
                break;
            }
        }
        return levelz;
    }

    @Override
    public int getBaseLevel() {
        return (GameConstants.getStatFromWeapon(getItemId()) == null ? 1 : 0);
    }

    @Override
    public void setQuantity(int quantity) {
        if (quantity < 0 || quantity > 1) {
            throw new RuntimeException("Setting the quantity to " + quantity + " on an equip (itemid: " + getItemId() + ")");
        }
        super.setQuantity(quantity);
    }

    @Override
    public int getDurability() {
        return equipment.durability;
    }

    public void setDurability(int dur) {
        this.equipment.durability = dur;
    }

    @Override
    public int getEnhance() {
        return equipment.enhance;
    }

    public void setEnhance(int en) {
        this.equipment.enhance = en;
    }

    @Override
    public int getPotential1() {
        return equipment.potential1;
    }

    public void setPotential1(int en) {
        this.equipment.potential1 = en;
    }

    @Override
    public int getPotential2() {
        return equipment.potential2;
    }

    public void setPotential2(int en) {
        this.equipment.potential2 = en;
    }

    @Override
    public int getPotential3() {
        return equipment.potential3;
    }

    public void setPotential3(int en) {
        this.equipment.potential3 = en;
    }

    @Override
    public int getState() {
        int pots = equipment.potential1 + equipment.potential2 + equipment.potential3;
        if (equipment.potential1 >= 30000 || equipment.potential2 >= 30000 || equipment.potential3 >= 30000) {
            return 7;
        } else if (equipment.potential1 >= 20000 || equipment.potential2 >= 20000 || equipment.potential3 >= 20000) {
            return 6;
        } else if (pots >= 1) {
            return 5;
        } else if (pots < 0) {
            return 1;
        }
        return 0;
    }

    public void resetPotential() { //equip first receive
        //0.04% chance unique, 4% chance epic, else rare
        int rank = Randomizer.nextInt(100) < 4 ? (Randomizer.nextInt(100) < 4 ? -7 : -6) : -5;
        setPotential1((int) rank);
        setPotential2((int) (Randomizer.nextInt(10) == 1 ? rank : 0)); //1/10 chance of 3 line
        setPotential3((int) 0); //just set it theoretically
    }

    public void renewPotential() {
        //4% chance upgrade
        int rank = Randomizer.nextInt(100) < 4 && getState() != 7 ? -(getState() + 1) : -(getState());
        setPotential1((int) rank);
        setPotential2((int) (getPotential3() > 0 ? rank : 0)); //1/10 chance of 3 line
        setPotential3((int) 0); //just set it theoretically
    }

    @Override
    public int getHpR() {
        return equipment.hpR;
    }

    public void setHpR(int hp) {
        this.equipment.hpR = hp;
    }

    @Override
    public int getMpR() {
        return equipment.mpR;
    }

    public void setMpR(int mp) {
        this.equipment.mpR = mp;
    }

    public void gainItemLevel() {
        this.itemLevel = (int) (this.itemLevel + 1);
    }

    public void gainItemExp(MapleClient c, int gain, boolean timeless) {
        this.equipment.itemEXP += gain;
        int expNeeded = 0;
        if (timeless) {
            expNeeded = ExpTable.getTimelessItemExpNeededForLevel(this.itemLevel + 1);
        } else {
            expNeeded = ExpTable.getReverseItemExpNeededForLevel(this.itemLevel + 1);
        }
        if (this.equipment.itemEXP >= expNeeded) {
            // gainItemLevel();
            gainItemLevel(c, timeless);
            //gainLevel();
            c.getSession().write(MaplePacketCreator.showItemLevelup());
        }
    }

    public void gainItemLevel(MapleClient c, boolean timeless) {
        List<Pair<String, Integer>> stats = MapleItemInformationProvider.getInstance().getItemLevelupStats(getItemId(), itemLevel, timeless);
        for (Pair<String, Integer> stat : stats) {
            switch (stat.getLeft()) {
                case "incDEX":
                    equipment.dex += stat.getRight();
                    break;
                case "incSTR":
                    equipment.str += stat.getRight();
                    break;
                case "incINT":
                    equipment.intField += stat.getRight();
                    break;
                case "incLUK":
                    equipment.luk += stat.getRight();
                    break;
                case "incMHP":
                    equipment.hp += stat.getRight();
                    break;
                case "incMMP":
                    equipment.mp += stat.getRight();
                    break;
                case "incPAD":
                    equipment.watk += stat.getRight();
                    break;
                case "incMAD":
                    equipment.matk += stat.getRight();
                    break;
                case "incPDD":
                    equipment.wdef += stat.getRight();
                    break;
                case "incMDD":
                    equipment.mdef += stat.getRight();
                    break;
                case "incEVA":
                    equipment.avoid += stat.getRight();
                    break;
                case "incACC":
                    equipment.acc += stat.getRight();
                    break;
                case "incSpeed":
                    equipment.speed += stat.getRight();
                    break;
                case "incJump":
                    equipment.jump += stat.getRight();
                    break;
            }
        }
        this.itemLevel++;
        c.getPlayer().getClient().getSession().write(MaplePacketCreator.showEquipmentLevelUp());
        c.getPlayer().getClient().getSession().write(MaplePacketCreator.updateSpecialItemUse(this, getType()));
        c.getPlayer().getClient().getSession().write(MaplePacketCreator.getCharInfo(c.getPlayer()));
        /*  c.getPlayer().getMap().removePlayer(c.getPlayer());
            c.getPlayer().getMap().addPlayer(c.getPlayer());*/
        //c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.showForeignEffect(c.getPlayer().getId(), 17));
        // c.getPlayer().forceUpdateItem(MapleInventoryType.EQUIPPED, this);
    }

    @Override
    public void setEquipLevel(int gf) {
        this.equipment.itemlevel = gf;
    }

    @Override
    public int getEquipLevel() {
        return equipment.itemlevel;
    }

    public int getCharmEXP() {
        return charmExp;
    }

    public void setCharmEXP(int s) {
        charmExp = s;
    }

    public int getPVPDamage() {
        return pvpDamage;
    }

    public void setPVPDamage(int p) {
        pvpDamage = p;
    }
}
