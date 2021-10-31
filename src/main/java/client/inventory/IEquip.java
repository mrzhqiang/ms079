package client.inventory;

public interface IEquip extends IItem {

    enum ScrollResult {
        SUCCESS, FAIL, CURSE
    }

    int ARMOR_RATIO = 350000;
    int WEAPON_RATIO = 700000;

    int getUpgradeSlots();

    int getLevel();

    int getViciousHammer();

    int getItemEXP();

    int getExpPercentage();

    int getEquipLevel();

    int getEquipLevels();

    int getEquipExp();

    int getEquipExpForLevel();

    int getBaseLevel();

    int getStr();

    int getDex();

    int getInt();

    int getLuk();

    int getHp();

    int getMp();

    int getWatk();

    int getMatk();

    int getWdef();

    int getMdef();

    int getAcc();

    int getAvoid();

    int getHands();

    int getSpeed();

    int getJump();

    int getDurability();

    int getEnhance();

    int getState();

    int getPotential1();

    int getPotential2();

    int getPotential3();

    int getHpR();

    int getMpR();
}
