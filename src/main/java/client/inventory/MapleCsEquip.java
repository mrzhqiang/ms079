package client.inventory;

import client.MapleClient;
import com.github.mrzhqiang.maplestory.domain.DCsEquipment;
import com.github.mrzhqiang.maplestory.domain.DCsItem;
import constants.GameConstants;
import server.MapleItemInformationProvider;
import server.Randomizer;
import tools.MaplePacketCreator;
import tools.Pair;

import java.io.Serializable;
import java.util.List;

public class MapleCsEquip extends MapleCsItem implements IEquip, Serializable {

    private int charmExp = 0;
    private int pvpDamage = 0;

    public DCsEquipment equipment;

    // super(id, position, (short) 1, (byte) 0);
    // super(id, position, (short) 1, flag);
    /*super(id, position, (int) 1, flag, uniqueid);*/
    public MapleCsEquip(DCsItem item) {
        super(item);
        this.equipment = item.getEquipment();
    }

    @Override
    public IItem copy() {
        MapleCsEquip ret = new MapleCsEquip(item);
        ret.itemLevel = this.itemLevel;
        return ret;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public int getUpgradeSlots() {
        return equipment.getUpgradeSlots();
    }

    @Override
    public int getStr() {
        return equipment.getStr();
    }

    @Override
    public int getDex() {
        return equipment.getDex();
    }

    @Override
    public int getInt() {
        return equipment.getIntelligence();
    }

    @Override
    public int getLuk() {
        return equipment.getLuk();
    }

    @Override
    public int getHp() {
        return equipment.getHp();
    }

    @Override
    public int getMp() {
        return equipment.getMp();
    }

    @Override
    public int getWatk() {
        return equipment.getWatk();
    }

    @Override
    public int getMatk() {
        return equipment.getMatk();
    }

    @Override
    public int getWdef() {
        return equipment.getWdef();
    }

    @Override
    public int getMdef() {
        return equipment.getMdef();
    }

    @Override
    public int getAcc() {
        return equipment.getAcc();
    }

    @Override
    public int getAvoid() {
        return equipment.getAvoid();
    }

    @Override
    public int getHands() {
        return equipment.getHands();
    }

    @Override
    public int getSpeed() {
        return equipment.getSpeed();
    }

    @Override
    public int getJump() {
        return equipment.getJump();
    }

    public void setStr(int str) {
        if (str < 0) {
            str = 0;
        }
        this.equipment.setStr(str);
    }

    public void setDex(int dex) {
        if (dex < 0) {
            dex = 0;
        }
        this.equipment.setDex(dex);
    }

    public void setInt(int _int) {
        if (_int < 0) {
            _int = 0;
        }
        this.equipment.setIntelligence(_int);
    }

    public void setLuk(int luk) {
        if (luk < 0) {
            luk = 0;
        }
        this.equipment.setLuk(luk);
    }

    public void setHp(int hp) {
        if (hp < 0) {
            hp = 0;
        }
        this.equipment.setHp(hp);
    }

    public void setMp(int mp) {
        if (mp < 0) {
            mp = 0;
        }
        this.equipment.setMp(mp);
    }

    public void setWatk(int watk) {
        if (watk < 0) {
            watk = 0;
        }
        this.equipment.setWatk(watk);
    }

    public void setMatk(int matk) {
        if (matk < 0) {
            matk = 0;
        }
        this.equipment.setMatk(matk);
    }

    public void setWdef(int wdef) {
        if (wdef < 0) {
            wdef = 0;
        }
        this.equipment.setWdef(wdef);
    }

    public void setMdef(int mdef) {
        if (mdef < 0) {
            mdef = 0;
        }
        this.equipment.setMdef(mdef);
    }

    public void setAcc(int acc) {
        if (acc < 0) {
            acc = 0;
        }
        this.equipment.setAcc(acc);
    }

    public void setAvoid(int avoid) {
        if (avoid < 0) {
            avoid = 0;
        }
        this.equipment.setAvoid(avoid);
    }

    public void setHands(int hands) {
        if (hands < 0) {
            hands = 0;
        }
        this.equipment.setHands(hands);
    }

    public void setSpeed(int speed) {
        if (speed < 0) {
            speed = 0;
        }
        this.equipment.setSpeed(speed);
    }

    public void setJump(int jump) {
        if (jump < 0) {
            jump = 0;
        }
        this.equipment.setJump(jump);
    }

    public void setUpgradeSlots(int upgradeSlots) {
        this.equipment.setUpgradeSlots(upgradeSlots);
    }

    @Override
    public int getLevel() {
        return equipment.getLevel();
    }

    public void setLevel(int level) {
        this.equipment.setLevel(level);
    }

    @Override
    public int getViciousHammer() {
        return equipment.getViciousHammer();
    }

    public void setViciousHammer(int ham) {
        equipment.setViciousHammer(ham);
    }

    @Override
    public int getItemEXP() {
        return equipment.getItemExp();
    }

    public void setItemEXP(int itemEXP) {
        if (itemEXP < 0) {
            itemEXP = 0;
        }
        this.equipment.setItemExp(itemEXP);
    }

    @Override
    public int getEquipExp() {
        if (equipment.getItemExp() <= 0) {
            return 0;
        }
        //aproximate value
        if (GameConstants.isWeapon(getItemId())) {
            return equipment.getItemExp() / IEquip.WEAPON_RATIO;
        } else {
            return equipment.getItemExp() / IEquip.ARMOR_RATIO;
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
        return this.equipment.getItemExp();
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
        return equipment.getDurability();
    }

    public void setDurability(int dur) {
        this.equipment.setDurability(dur);
    }

    @Override
    public int getEnhance() {
        return equipment.getEnhance();
    }

    public void setEnhance(int en) {
        this.equipment.setEnhance(en);
    }

    @Override
    public int getPotential1() {
        return equipment.getPotential1();
    }

    public void setPotential1(int en) {
        this.equipment.setPotential1(en);
    }

    @Override
    public int getPotential2() {
        return equipment.getPotential2();
    }

    public void setPotential2(int en) {
        this.equipment.setPotential2(en);
    }

    @Override
    public int getPotential3() {
        return equipment.getPotential3();
    }

    public void setPotential3(int en) {
        this.equipment.setPotential3(en);
    }

    @Override
    public int getState() {
        int pots = equipment.getPotential1() + equipment.getPotential2() + equipment.getPotential3();
        if (equipment.getPotential1() >= 30000 || equipment.getPotential2() >= 30000 || equipment.getPotential3() >= 30000) {
            return 7;
        } else if (equipment.getPotential1() >= 20000 || equipment.getPotential2() >= 20000 || equipment.getPotential3() >= 20000) {
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
        return equipment.getHpR();
    }

    public void setHpR(int hp) {
        this.equipment.setHpR(hp);
    }

    @Override
    public int getMpR() {
        return equipment.getMpR();
    }

    public void setMpR(int mp) {
        this.equipment.setMpR(mp);
    }

    public void gainItemLevel() {
        this.itemLevel = (int) (this.itemLevel + 1);
    }

    public void gainItemExp(MapleClient c, int gain, boolean timeless) {
        Integer itemExp = this.equipment.getItemExp();
        itemExp += gain;
        equipment.setItemExp(itemExp);
        int expNeeded = 0;
        if (timeless) {
            expNeeded = ExpTable.getTimelessItemExpNeededForLevel(this.itemLevel + 1);
        } else {
            expNeeded = ExpTable.getReverseItemExpNeededForLevel(this.itemLevel + 1);
        }
        if (this.equipment.getItemExp() >= expNeeded) {
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
                    equipment.setDex(equipment.getDex() + stat.getRight());
                    break;
                case "incSTR":
                    equipment.setStr(equipment.getStr() + stat.getRight());
                    break;
                case "incINT":
                    equipment.setIntelligence(equipment.getIntelligence() + stat.getRight());
                    break;
                case "incLUK":
                    equipment.setLuk(equipment.getLuk() + stat.getRight());
                    break;
                case "incMHP":
                    equipment.setHp(equipment.getHp() + stat.getRight());
                    break;
                case "incMMP":
                    equipment.setMp(equipment.getMp() + stat.getRight());
                    break;
                case "incPAD":
                    equipment.setWatk(equipment.getWatk() + stat.getRight());
                    break;
                case "incMAD":
                    equipment.setMatk(equipment.getMatk() + stat.getRight());
                    break;
                case "incPDD":
                    equipment.setWdef(equipment.getWdef() + stat.getRight());
                    break;
                case "incMDD":
                    equipment.setMdef(equipment.getMdef() + stat.getRight());
                    break;
                case "incEVA":
                    equipment.setAvoid(equipment.getAvoid() + stat.getRight());
                    break;
                case "incACC":
                    equipment.setAcc(equipment.getAcc() + stat.getRight());
                    break;
                case "incSpeed":
                    equipment.setSpeed(equipment.getSpeed() + stat.getRight());
                    break;
                case "incJump":
                    equipment.setJump(equipment.getJump() + stat.getRight());
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
        this.equipment.setItemLevel(gf);
    }

    @Override
    public int getEquipLevel() {
        return equipment.getItemLevel();
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
