/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package constants;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.IItem;
import client.inventory.MapleInventoryType;
import client.inventory.MapleWeaponType;
import client.status.MonsterStatus;
import handling.channel.handler.AttackInfo;
import java.util.List;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import server.MapleStatEffect;
import server.Randomizer;
import server.maps.MapleMapObjectType;

public class GameConstants {

    public static final String[] stats = {"tuc", "reqLevel", "reqJob", "reqSTR", "reqDEX", "reqINT", "reqLUK", "reqPOP", "cash", "cursed", "success", "setItemID", "equipTradeBlock", "durability", "randOption", "randStat", "masterLevel", "reqSkillLevel", "elemDefault", "incRMAS", "incRMAF", "incRMAI", "incRMAL", "canLevel", "skill", "charmEXP"};
    public static final List<MapleMapObjectType> rangedMapobjectTypes = Collections.unmodifiableList(Arrays.asList(
            MapleMapObjectType.ITEM,
            MapleMapObjectType.MONSTER,
            MapleMapObjectType.DOOR,
            MapleMapObjectType.REACTOR,
            MapleMapObjectType.SUMMON,
            MapleMapObjectType.NPC,
            MapleMapObjectType.LOVE,
            MapleMapObjectType.MIST));
    private static final int[] exp = {0, 15, 34, 57, 92, 135, 372, 560, 840, 1242, 1716,
        2360, 3216, 4200, 5460, 7050, 8840, 11040, 13716, 16680, 20216,
        24402, 28980, 34320, 40512, 47216, 54900, 63666, 73080, 83720, 95700,
        108480, 122760, 138666, 155540, 174216, 194832, 216600, 240500, 266682, 294216,
        324240, 356916, 391160, 428280, 468450, 510420, 555680, 604416, 655200, 709716, // 51等到這
        748608, 789631, 832902, 878545, 926689, 977471, 1031036, 1087536, 1147132, 1209994,
        1276301, 1346242, 1420016, 1497832, 1579913, 1666492, 1757815, 1854143, 1955750, 2062925, // 71等到這
        2175973, 2295216, 2410993, 2553663, 2693603, 2841212, 2996910, 3161140, 3334370, 3517093,
        3709829, 3913127, 4127566, 4353756, 4592341, 4844001, 5109452, 5389449, 5684790, 5996316,
        6324914, 6671519, 7037118, 7422752, 7829518, 8258575, 8711144, 9188514, 9692044, 10223168, // 101等到這
        10783397, 11374327, 11997640, 12655110, 13348610, 14080113, 14851703, 15665576, 16524049, 17429566,
        18384706, 19392187, 20454878, 21575805, 22758159, 24005306, 25320796, 26708375, 28171993, 29715818,//121等到這
        31344244, 33061908, 34873700, 36784778, 38800583, 40926854, 43169645, 45535341, 48030677, 50662758,//131等到這
        53439077, 56367538, 59456479, 62714694, 66151459, 69776558, 73600313, 77633610, 81887931, 86375389,//141等到這
        91108760, 96101520, 101367883, 106922842, 112782213, 118962678, 125481832, 132358236, 139611467, 147262175,//151等到這
        155332142, 163844343, 172823012, 182293713, 192283408, 202820538, 213935103, 225658746, 238024845, 251068606, //160
        264827165, 279339693, 294647508, 310794191, 327825712, 345790561, 364739883, 384727628, 405810702, 428049128, //170
        451506220, 476248760, 502347192, 529875818, 558913012, 589541445, 621848316, 655925603, 691870326, 729784819,
        769777027, 811960808, 856456260, 903390063, 952895838, 1005114529, 1060194805, 1118293480, 1179575962, 1244216724,
        1312399800, 1384319309, 1460180007, 1540197871, 1624600714, 1713628833, 1807535693, 1906588648, 2011069705, 2121276324};
    private static final int[] closeness = {0, 1, 3, 6, 14, 31, 60, 108, 181, 287, 434, 632, 891, 1224, 1642, 2161, 2793,
        3557, 4467, 5542, 6801, 8263, 9950, 11882, 14084, 16578, 19391, 22547, 26074,
        30000};
    private static final int[] mountexp = {0, 6, 25, 50, 105, 134, 196, 254, 263, 315, 367, 430, 543, 587, 679, 725, 897, 1146, 1394, 1701, 2247,
        2543, 2898, 3156, 3313, 3584, 3923, 4150, 4305, 4550};
    public static final int[] itemBlock = {2340000, 2049100, 4001129, 2040037, 2040006, 2040007, 2040303, 2040403, 2040506, 2040507, 2040603, 2040709, 2040710, 2040711, 2040806, 2040903, 2041024, 2041025, 2043003, 2043103, 2043203, 2043303, 2043703, 2043803, 2044003, 2044103, 2044203, 2044303, 2044403, 2044503, 2044603, 2044908, 2044815, 2044019, 2044703, 1004001, 4007008, 1004002, 5152053, 5150040};
    public static final int[] cashBlock = {5062000, 5650000, 5431000, 5431001, 5432000, 5450000, 5550000, 5550001, 5640000, 5530013, 5150039, 5150046, 5150054, 1812006, 5650000, 5222000, 5221001, 5220014, 5220015, 5420007, 5451000,
        5210000, 5210001, 5210002, 5210003, 5210004, 5210005, 5210006, 5210007, 5210008, 5210009, 5210010, 5210011, 5211000, 5211001, 5211002, 5211003, 5211004, 5211005, 5211006, 5211007, 5211008, 5211009, 5211010, 5211011, 5211012, 5211013, 5211014, 5211015, 5211016, 5211017, 5211018,
        5211019, 5211020, 5211021, 5211022, 5211023, 5211024, 5211025, 5211026, 5211027, 5211028, 5211029, 5211030, 5211031, 5211032, 5211033, 5211034, 5211035, 5211036, 5211037, 5211038, 5211039, 5211040, 5211041, 5211042, 5211043,
        5211044, 5211045, 5211046, 5211047, 5211048, 5211049, 5211050, 5211051, 5211052, 5211053, 5211054, 5211055, 5211056, 5211057, 5211058, 5211059, 5211060, 5211061,//2x exp
        5360000, 5360001, 5360002, 5360003, 5360004, 5360005, 5360006, 5360007, 5360008, 5360009, 5360010, 5360011, 5360012, 5360013, 5360014, 5360017, 5360050, 5211050, 5360042, 5360052, 5360053, 5360050, //2x drop
        1112810, 1112811, 5530013, 4001431, 4001432, 4032605,
        5270000, 5270001, 5270002, 5270003, 5270004, 5270005, 5270006, //2x meso
        9102328, 9102329, 9102330, 9102331, 9102332, 9102333}; //miracle cube and stuff
    public static final int OMOK_SCORE = 122200;
    public static final int MATCH_SCORE = 122210;
    public static final int[] blockedSkills = {4341003};
    public static final String MASTER = "%&HYGEomgLOL";
    public static final String[] RESERVED = {"Rental"};
    private static final int[] mobHpVal = {0, 15, 20, 25, 35, 50, 65, 80, 95, 110, 125, 150, 175, 200, 225, 250, 275, 300, 325, 350, 375, 405, 435, 465, 495, 525, 580, 650, 720, 790, 900, 990, 1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900, 2000, 2100, 2200, 2300, 2400, 2520, 2640, 2760, 2880, 3000, 3200, 3400, 3600, 3800, 4000, 4300, 4600, 4900, 5200, 5500, 5900, 6300, 6700, 7100, 7500, 8000, 8500, 9000, 9500, 10000, 11000, 12000, 13000, 14000, 15000, 17000, 19000, 21000, 23000, 25000, 27000, 29000, 31000, 33000, 35000, 37000, 39000, 41000, 43000, 45000, 47000, 49000, 51000, 53000, 55000, 57000, 59000, 61000, 63000, 65000, 67000, 69000, 71000, 73000, 75000, 77000, 79000, 81000, 83000, 85000, 89000, 91000, 93000, 95000, 97000, 99000, 101000, 103000, 105000, 107000, 109000, 111000, 113000, 115000, 118000, 120000, 125000, 130000, 135000, 140000, 145000, 150000, 155000, 160000, 165000, 170000, 175000, 180000, 185000, 190000, 195000, 200000, 205000, 210000, 215000, 220000, 225000, 230000, 235000, 240000, 250000, 260000, 270000, 280000, 290000, 300000, 310000, 320000, 330000, 340000, 350000, 360000, 370000, 380000, 390000, 400000, 410000, 420000, 430000, 440000, 450000, 460000, 470000, 480000, 490000, 500000, 510000, 520000, 530000, 550000, 570000, 590000, 610000, 630000, 650000, 670000, 690000, 710000, 730000, 750000, 770000, 790000, 810000, 830000, 850000, 870000, 890000, 910000};
    public static int 单机IP = 1;
    public static String 绑定IP = "www.66mxd.cn";//IP地址

    public static int getExpNeededForLevel(final int level) {
        if (level < 0 || level >= exp.length) {
            return Integer.MAX_VALUE;
        }
        return exp[level];
    }

    public static int getClosenessNeededForLevel(final int level) {
        return closeness[level - 1];
    }

    public static int getMountExpNeededForLevel(final int level) {
        return mountexp[level - 1];
    }

    public static int getBookLevel(final int level) {
        return (int) ((5 * level) * (level + 1));
    }

    public static int getTimelessRequiredEXP(final int level) {
        return 70 + (level * 10);
    }

    public static int getReverseRequiredEXP(final int level) {
        return 60 + (level * 5);
    }

    public static int maxViewRangeSq() {
        return 100000000; // 800 * 800
        // return 800000; // 800 * 800
    }

    public static boolean isJobFamily(final int baseJob, final int currentJob) {
        return currentJob >= baseJob && currentJob / 100 == baseJob / 100;
    }

    public static boolean isKOC(final int job) {
        return job >= 1000 && job < 2000;
    }

    public static boolean isEvan(final int job) {
        return job == 2001 || (job >= 2200 && job <= 2218);
    }

    public static boolean isAran(final int job) {
        return job >= 2000 && job <= 2112 && job != 2001;
    }

    public static boolean isResist(final int job) {
        return job >= 3000 && job <= 3512;
    }

    public static boolean isAdventurer(final int job) {
        return job >= 0 && job < 1000;
    }

    public static boolean isRecoveryIncSkill(final int id) {
        switch (id) {
            case 1110000:
            case 2000000:
            case 1210000:
            case 11110000:
            case 4100002:
            case 4200001:
                return true;
        }
        return false;
    }

    public static boolean isLinkedAranSkill(final int id) {
        return getLinkedAranSkill(id) != id;
    }

    public static int getLinkedAranSkill(final int id) {
        switch (id) {
            case 21110007:
            case 21110008:
                return 21110002;
            case 21120009:
            case 21120010:
                return 21120002;
            case 4321001:
                return 4321000;
            case 33101006:
            case 33101007:
                return 33101005;
            case 33101008:
                return 33101004;
            case 35101009:
            case 35101010:
                return 35100008;
            case 35111009:
            case 35111010:
                return 35111001;
        }
        return id;
    }

    public static int getBOF_ForJob(final int job) {
        if (isAdventurer(job)) {
            return 12;
        } else if (isKOC(job)) {
            return 10000012;
        } else if (isResist(job)) {
            return 30000012;
        } else if (isEvan(job)) {
            return 20010012;
        }
        return 20000012;
    }

    public static boolean isElementAmp_Skill(final int skill) {
        switch (skill) {
            case 2110001:
            case 2210001:
            case 12110001:
            case 22150000:
                return true;
        }
        return false;
    }

    public static int getMPEaterForJob(final int job) {
        switch (job) {
            case 210:
            case 211:
            case 212:
                return 2100000;
            case 220:
            case 221:
            case 222:
                return 2200000;
            case 230:
            case 231:
            case 232:
                return 2300000;
        }
        return 2100000; // Default, in case GM
    }

    public static int getJobShortValue(int job) {
        if (job >= 1000) {
            job -= (job / 1000) * 1000;
        }
        job /= 100;
        if (job == 4) { // For some reason dagger/ claw is 8.. IDK
            job *= 2;
        } else if (job == 3) {
            job += 1;
        } else if (job == 5) {
            job += 11; // 16
        }
        return job;
    }

    public static boolean isPyramidSkill(final int skill) {
        switch (skill) {
            case 1020:
            case 10001020:
            case 20001020:
            case 20011020:
            case 30001020:
                return true;
        }
        return false;
    }

    public static boolean isMulungSkill(final int skill) {
        switch (skill) {
            case 1009:
            case 1010:
            case 1011:
            case 10001009:
            case 10001010:
            case 10001011:
            case 20001009:
            case 20001010:
            case 20001011:
            case 20011009:
            case 20011010:
            case 20011011:
            case 30001009:
            case 30001010:
            case 30001011:
                return true;
        }
        return false;
    }

    public static boolean isThrowingStar(final int itemId) {
        return itemId / 10000 == 207;
    }

    public static boolean isBullet(final int itemId) {
        return itemId / 10000 == 233;
    }

    public static boolean isRechargable(final int itemId) {
        return isThrowingStar(itemId) || isBullet(itemId);
    }

    public static boolean isOverall(final int itemId) {
        return itemId / 10000 == 105;
    }

    public static boolean isPet(final int itemId) {
        return itemId / 10000 == 500;
    }

    public static boolean isArrowForCrossBow(final int itemId) {
        return itemId >= 2061000 && itemId < 2062000;
    }

    public static boolean isArrowForBow(final int itemId) {
        return itemId >= 2060000 && itemId < 2061000;
    }

    public static boolean isMagicWeapon(final int itemId) {
        final int s = itemId / 10000;
        return s == 137 || s == 138;
    }

    public static boolean isWeapon(final int itemId) {
        return itemId >= 1300000 && itemId < 1500000;
    }

    public static MapleInventoryType getInventoryType(final int itemId) {
        final byte type = (byte) (itemId / 1000000);
        if (type < 1 || type > 5) {
            return MapleInventoryType.UNDEFINED;
        }
        return MapleInventoryType.getByType(type);
    }

    public static MapleWeaponType getWeaponType(final int itemId) {
        int cat = itemId / 10000;
        cat = cat % 100;
        switch (cat) {
            case 30:
                return MapleWeaponType.SWORD1H;
            case 31:
                return MapleWeaponType.AXE1H;
            case 32:
                return MapleWeaponType.BLUNT1H;
            case 33:
                return MapleWeaponType.DAGGER;
            case 34:
                return MapleWeaponType.KATARA;
            case 37:
                return MapleWeaponType.WAND;
            case 38:
                return MapleWeaponType.STAFF;
            case 40:
                return MapleWeaponType.SWORD2H;
            case 41:
                return MapleWeaponType.AXE2H;
            case 42:
                return MapleWeaponType.BLUNT2H;
            case 43:
                return MapleWeaponType.SPEAR;
            case 44:
                return MapleWeaponType.POLE_ARM;
            case 45:
                return MapleWeaponType.BOW;
            case 46:
                return MapleWeaponType.CROSSBOW;
            case 47:
                return MapleWeaponType.CLAW;
            case 48:
                return MapleWeaponType.KNUCKLE;
            case 49:
                return MapleWeaponType.GUN;
        }
        return MapleWeaponType.NOT_A_WEAPON;
    }

    public static boolean isShield(final int itemId) {
        int cat = itemId / 10000;
        cat = cat % 100;
        return cat == 9;
    }

    public static boolean isEquip(final int itemId) {
        return itemId / 1000000 == 1;
    }

    public static boolean isCleanSlate(int itemId) {
        return itemId / 100 == 20490;
    }

    public static boolean isAccessoryScroll(int itemId) {
        return itemId / 100 == 20492;
    }

    public static boolean isChaosScroll(int itemId) {
        if (itemId >= 2049105 && itemId <= 2049110) {
            return false;
        }
        return itemId / 100 == 20491;
    }

    public static int getChaosNumber(int itemId) {
        return itemId == 2049116 ? 10 : 5;
    }

    public static boolean isEquipScroll(int scrollId) {
        return scrollId / 100 == 20493;
    }

    public static boolean isPotentialScroll(int scrollId) {
        return scrollId / 100 == 20494;
    }

    public static boolean isSpecialScroll(final int scrollId) {
        switch (scrollId) {
            case 2040727: // Spikes on show
            case 2041058: // Cape for Cold protection
                return true;
        }
        return false;
    }

    public static boolean isTwoHanded(final int itemId) {
        switch (getWeaponType(itemId)) {
            case AXE2H:
            case GUN:
            case KNUCKLE:
            case BLUNT2H:
            case BOW:
            case CLAW:
            case CROSSBOW:
            case POLE_ARM:
            case SPEAR:
            case SWORD2H:
                return true;
            default:
                return false;
        }
    }

    public static boolean isTownScroll(final int id) {
        return id >= 2030000 && id < 2040000;
    }

    public static boolean isUpgradeScroll(final int id) {
        return id >= 2040000 && id < 2050000;
    }

    public static boolean isGun(final int id) {
        return id >= 1492000 && id < 1500000;
    }

    public static boolean isUse(final int id) {
        return id >= 2000000 && id <= 2490000;
    }

    public static boolean isSummonSack(final int id) {
        return id / 10000 == 210;
    }

    public static boolean isMonsterCard(final int id) {
        return id / 10000 == 238;
    }

    public static boolean isSpecialCard(final int id) {
        return id / 1000 >= 2388;
    }

    public static int getCardShortId(final int id) {
        return id % 10000;
    }

    public static boolean isGem(final int id) {
        return id >= 4250000 && id <= 4251402;
    }

    public static boolean isOtherGem(final int id) {
        switch (id) {
            case 4001174:
            case 4001175:
            case 4001176:
            case 4001177:
            case 4001178:
            case 4001179:
            case 4001180:
            case 4001181:
            case 4001182:
            case 4001183:
            case 4001184:
            case 4001185:
            case 4001186:
            case 4031980:
            case 2041058:
            case 2040727:
            case 1032062:
            case 4032334:
            case 4032312:
            case 1142156:
            case 1142157:
                return true; //mostly quest items
        }
        return false;
    }

    public static boolean isCustomQuest(final int id) {
        return id > 99999;
    }

    public static int getTaxAmount(final int meso) {
        if (meso >= 100000000) {
            return (int) Math.round(0.06 * meso);
        } else if (meso >= 25000000) {
            return (int) Math.round(0.05 * meso);
        } else if (meso >= 10000000) {
            return (int) Math.round(0.04 * meso);
        } else if (meso >= 5000000) {
            return (int) Math.round(0.03 * meso);
        } else if (meso >= 1000000) {
            return (int) Math.round(0.018 * meso);
        } else if (meso >= 100000) {
            return (int) Math.round(0.008 * meso);
        }
        return 0;
    }

    public static int EntrustedStoreTax(final int meso) {
        if (meso >= 100000000) {
            return (int) Math.round(0.03 * meso);
        } else if (meso >= 25000000) {
            return (int) Math.round(0.025 * meso);
        } else if (meso >= 10000000) {
            return (int) Math.round(0.02 * meso);
        } else if (meso >= 5000000) {
            return (int) Math.round(0.015 * meso);
        } else if (meso >= 1000000) {
            return (int) Math.round(0.009 * meso);
        } else if (meso >= 100000) {
            return (int) Math.round(0.004 * meso);
        }
        return 0;
    }

    public static short getSummonAttackDelay(final int id) {
        switch (id) {
            case 15001004: // Lightning
            case 14001005: // Darkness
            case 13001004: // Storm
            case 12001004: // Flame
            case 11001004: // Soul
            case 3221005: // Freezer
            case 3211005: // Golden Eagle
            case 3121006: // Phoenix
            case 3111005: // Silver Hawk
            case 2321003: // Bahamut
            case 2311006: // Summon Dragon
            case 2221005: // Infrit
            case 2121005: // Elquines
                return 3030;
            case 5211001: // Octopus
            case 5211002: // Gaviota
            case 5220002: // Support Octopus
                return 1530;
            case 3211002: // Puppet
            case 3111002: // Puppet
            case 1321007: // Beholder
                return 0;
        }
        return 0;
    }

    public static short getAttackDelay(final int id) {
        switch (id) { // Assume it's faster(2)
            case 4321001: //tornado spin
                return 40; //reason being you can spam with final assaulter
            case 3121004: // Storm of Arrow
            case 33121009:
            case 13111002: // Storm of Arrow
            case 5221004: // Rapidfire
            case 4221001: //Assassinate?
            case 5201006: // Recoil shot/ Back stab shot
                return 120;
            case 13101005: // Storm Break
                return 360;
            case 5001003: // Double Fire
            case 2301002: // Heal
                return 390;
            case 5001001: // Straight/ Flash Fist
            case 15001001: // Straight/ Flash Fist
            case 1321003: // Rush
            case 1221007: // Rush
            case 1121006: // Rush
                return 450;
            case 5211004: // Flamethrower
            case 5211005: // Ice Splitter
            case 4201005: // Savage blow
                return 480;
            case 0: // Normal Attack, TODO delay for each weapon type
            case 5111002: // Energy Blast
            case 15101005: // Energy Blast
            case 1001004: // Power Strike
            case 11001002: // Power Strike
            case 1001005: // Slash Blast
            case 11001003: // Slash Blast
            case 1311005: // Sacrifice
                return 570;
            //case 2101004: // Fire Arrow
            case 12101002: // Fire Arrow
            case 2101005: // Poison Breath
            case 2121003: // Fire Demon
            case 2221003: // Ice Demon
            case 2121006: // Paralyze
            case 3111006: // Strafe
            case 311004: // Arrow Rain
            case 13111000: // Arrow Rain
            case 3111003: // Inferno
            case 3101005: // Arrow Bomb
            case 4001344: // Lucky Seven
            case 14001004: // Lucky seven
            case 4121007: // Triple Throw
            case 14111005: // Triple Throw
            case 4111004: // Shadow Meso
            case 4101005: // Drain
            case 4211004: // Band of Thieves
            case 4201004: // Steal
            case 4001334: // Double Stab
            case 5221007: // Battleship Cannon
            case 1211002: // Charged blow
            case 1311003: // Dragon Fury : Spear
            case 1311004: // Dragon Fury : Pole Arm
            case 3211006: // Strafe
            case 3211004: // Arrow Eruption
            case 3211003: // Blizzard Arrow
            case 3201005: // Iron Arrow
            case 3221001: // Piercing
            case 4111005: // Avenger
            case 14111002: // Avenger
            case 5201001: // Invisible shot
            case 5101004: // Corkscrew Blow
            case 15101003: // Corkscrew Blow
            case 1121008: // Brandish
            case 11111004: // Brandish
            case 1221009: // Blast
                return 600;
            case 5201004: // Blank Shot/ Fake shot
            case 5211000: // Burst Fire/ Triple Fire
            case 5001002: // Sommersault Kick
            case 15001002: // Sommersault Kick
            case 4221007: // Boomerang Stab
            case 1311001: // Spear Crusher, 16~30 pts = 810
            case 1311002: // PA Crusher, 16~30 pts = 810
            case 2221006: // Chain Lightning
                return 660;
            case 4121008: // Ninja Storm
            case 5211006: // Homing Beacon
            case 5221008: // Battleship Torpedo
            case 5101002: // Backspin Blow
            case 2001005: // Magic Claw
            case 12001003: // Magic Claw
            case 2001004: // Energy Bolt
            case 2301005: // Holy Arrow
            case 2121001: // Big Bang
            case 2221001: // Big Bang
            case 2321001: // Big Bang
            case 2321007: // Angel's Ray
            case 2201005: // Thunderbolt
            case 2201004: // Cold Beam
            case 2211002: // Ice Strike
            case 4211006: // Meso Explosion
            case 5121005: // Snatch
            case 12111006: // Fire Strike
            case 11101004: // Soul Blade
                return 750;
            case 15111007: // Shark Wave
            case 2111006: // Elemental Composition
            case 2211006: // Elemental Composition
                return 810;
            case 13111006: // Wind Piercing
            case 4211002: // Assaulter
            case 5101003: // Double Uppercut
            case 2111002: // Explosion
                return 900;
            case 5121003: // Energy Orb
            case 2311004: // Shining Ray
                return 930;
            case 13111007: // Wind Shot
                return 960;
            case 14101006: // Vampire
            case 4121003: // Showdown
            case 4221003: // Showdown
                return 1020;
            case 12101006: // Fire Pillar
                return 1050;
            case 5121001: // Dragon Strike
                return 1060;
            case 2211003: // Thunder Spear
            case 1311006: // Dragon Roar
                return 1140;
            case 11111006: // Soul Driver
                return 1230;
            case 12111005: // Flame Gear
                return 1260;
            case 2111003: // Poison Mist
                return 1320;
            case 5111006: // Shockwave
            case 15111003: // Shockwave
                return 1500;
            case 5121007: // Barrage
            case 15111004: // Barrage
                return 1830;
            case 5221003: // Ariel Strike
            case 5121004: // Demolition
                return 2160;
            case 2321008: // Genesis
                return 2700;
            case 2121007: // Meteor Shower
            case 10001011: // Meteo Shower
            case 2221007: // Blizzard
                return 3060;
        }
        // TODO delay for final attack, weapon type, swing,stab etc
        return 330; // Default usually
    }

    public static byte gachaponRareItem(final int id) {
        switch (id) {
            case 2340000: // White Scroll
            case 2049100: // Chaos Scroll
            case 2049000: // Reverse Scroll
            case 2049001: // Reverse Scroll
            case 2049002: // Reverse Scroll
            case 2040006: // Miracle
            case 2040007: // Miracle
            case 2040303: // Miracle
            case 2040403: // Miracle
            case 2040506: // Miracle
            case 2040507: // Miracle
            case 2040603: // Miracle
            case 2040709: // Miracle
            case 2040710: // Miracle
            case 2040711: // Miracle
            case 2040806: // Miracle
            case 2040903: // Miracle
            case 2041024: // Miracle
            case 2041025: // Miracle
            case 2043003: // Miracle
            case 2043103: // Miracle
            case 2043203: // Miracle
            case 2043303: // Miracle
            case 2043703: // Miracle
            case 2043803: // Miracle
            case 2044003: // Miracle
            case 2044103: // Miracle
            case 2044203: // Miracle
            case 2044303: // Miracle
            case 2044403: // Miracle
            case 2044503: // Miracle
            case 2044603: // Miracle
            case 2044908: // Miracle
            case 2044815: // Miracle
            case 2044019: // Miracle
            case 2044703: // Miracle
            case 1372039: // Elemental wand lvl 130
            case 1372040: // Elemental wand lvl 130
            case 1372041: // Elemental wand lvl 130
            case 1372042: // Elemental wand lvl 130
            case 1092049: // Dragon Khanjar
            case 1382037: // Blade Staff
                return 2;
            case 1102084: // Pink Gaia Cape
            case 1102041: // Pink Adventurer Cape
            case 1402044: // Pumpkin Lantern
            case 1082149: // Brown Work glove
            case 1102086: // Purple Gaia Cape
            case 1102042: // Purple Adventurer Cape

            case 3010065: // Pink Parasol
            case 3010064: // Brown Sand Bunny Cushion
            case 3010063: // Starry Moon Cushion
            case 3010068: // Teru Teru Chair
            case 3010054: // Baby Bear's Dream
            case 3012001: // Round the Campfire
            case 3012002: // Rubber Ducky Bath
            case 3010020: // Portable Meal Table
            case 3010041: // Skull Throne

            case 1082179: //yellow marker
                return 2;
            //1 = wedding msg o.o
        }
        return 0;
    }
    public final static int[] goldrewards = {
        2340000, 1, // white scroll
        2070018, 1, // balance fury 沒有的物品
        1402037, 1, // Rigbol Sword
        2290096, 1, // Maple Warrior 20
        2290049, 1, // Genesis 30
        2290041, 1, // Meteo 30
        2290047, 1, // Blizzard 30
        2290095, 1, // Smoke 30
        2290017, 1, // Enrage 30
        2290075, 1, // Snipe 30
        2290085, 1, // Triple Throw 30
        2290116, 1, // Areal Strike
        1302059, 3, // Dragon Carabella
        2049100, 1, // Chaos Scroll
        2340000, 1, // White Scroll
        1092049, 1, // Dragon Kanjar
        1102041, 1, // Pink Cape
        1432018, 3, // Sky Ski
        1022047, 3, // Owl Mask
        3010051, 1, // Chair
        3010020, 1, // Portable meal table
        2040914, 1, // Shield for Weapon Atk

        1432011, 3, // Fair Frozen
        1442020, 3, // HellSlayer
        1382035, 3, // Blue Marine
        1372010, 3, // Dimon Wand
        1332027, 3, // Varkit
        1302056, 3, // Sparta
        1402005, 3, // Bezerker
        1472053, 3, // Red Craven
        1462018, 3, // Casa Crow
        1452017, 3, // Metus
        1422013, 3, // Lemonite
        1322029, 3, // Ruin Hammer
        1412010, 3, // Colonian Axe

        1472051, 1, // Green Dragon Sleeve
        1482013, 1, // Emperor's Claw
        1492013, 1, // Dragon fire Revlover

        1382050, 1, // Blue Dragon Staff
        1382045, 1, // Fire Staff, Level 105
        1382047, 1, // Ice Staff, Level 105
        1382048, 1, // Thunder Staff
        1382046, 1, // Poison Staff

        1332032, 4, // Christmas Tree
        1482025, 3, // Flowery Tube

        4001011, 4, // Lupin Eraser
        4001010, 4, // Mushmom Eraser
        4001009, 4, // Stump Eraser

        2030008, 5, // Bottle, return scroll
        1442018, 3, // Frozen Tuna
        2040900, 4, // Shield for DEF
        2000005, 10, // Power Elixir
        2000004, 10, // Elixir
        4280000, 4}; // Gold Box
    public final static int[] silverrewards = {
        3010041, 1, // skull throne
        1002452, 3, // Starry Bandana
        1002455, 3, // Starry Bandana
        2290084, 1, // Triple Throw 20
        2290048, 1, // Genesis 20
        2290040, 1, // Meteo 20
        2290046, 1, // Blizzard 20
        2290074, 1, // Sniping 20
        2290064, 1, // Concentration 20
        2290094, 1, // Smoke 20
        2290022, 1, // Berserk 20
        2290056, 1, // Bow Expert 30
        2290066, 1, // xBow Expert 30
        2290020, 1, // Sanc 20
        1102082, 1, // Black Raggdey Cape
        1302049, 1, // Glowing Whip
        2340000, 1, // White Scroll
        1102041, 1, // Pink Cape
        1452019, 2, // White Nisrock
        4001116, 3, // Hexagon Pend
        4001012, 3, // Wraith Eraser
        1022060, 2, // Foxy Racoon Eye

        1432011, 3, // Fair Frozen
        1442020, 3, // HellSlayer
        1382035, 3, // Blue Marine
        1372010, 3, // Dimon Wand
        1332027, 3, // Varkit
        1302056, 3, // Sparta
        1402005, 3, // Bezerker
        1472053, 3, // Red Craven
        1462018, 3, // Casa Crow
        1452017, 3, // Metus
        1422013, 3, // Lemonite
        1322029, 3, // Ruin Hammer
        1412010, 3, // Colonian Axe

        1002587, 3, // Black Wisconsin
        1402044, 1, // Pumpkin lantern
        2101013, 4, // Summoning Showa boss
        1442046, 1, // Super Snowboard
        1422031, 1, // Blue Seal Cushion
        1332054, 3, // Lonzege Dagger
        1012056, 3, // Dog Nose
        1022047, 3, // Owl Mask
        3012002, 1, // Bathtub
        1442012, 3, // Sky snowboard
        1442018, 3, // Frozen Tuna
        1432010, 3, // Omega Spear
        1432036, 1, // Fishing Pole 沒用的物品
        2000005, 10, // Power Elixir
        2000004, 10, // Elixir
        4280001, 4}; // Silver Box
    public static int[] eventCommonReward = {
        0, 40,
        1, 10,
        //        5060003, 18,
        //        4170023, 18,
        4031019, 5,
        4280000, 3,
        4280001, 4,
        5490000, 3,
        5490001, 4
    };
    public static int[] eventUncommonReward = {
        2, 4,
        3, 4,
        5160000, 5,
        5160001, 5,
        5160002, 5,
        5160003, 5,
        5160004, 5,
        5160005, 5,
        5160006, 5,
        5160007, 5,
        5160008, 5,
        5160009, 5,
        5160010, 5,
        5160011, 5,
        5160012, 5,
        5160013, 5,
        5240017, 5,
        5240000, 5,
        4080000, 5,
        4080001, 5,
        4080002, 5,
        4080003, 5,
        4080004, 5,
        4080005, 5,
        4080006, 5,
        4080007, 5,
        4080008, 5,
        4080009, 5,
        4080010, 5,
        4080011, 5,
        4080100, 5,
        4031019, 5,
        5121003, 5,
        5150000, 5,
        5150001, 5,
        5150002, 1,
        5150003, 1,
        5150004, 1,
        5150005, 2,
        5150006, 2,
        5150007, 2,
        5150008, 2,
        5150009, 14,
        2022459, 5,
        2022460, 5,
        2022461, 5,
        2022462, 5,
        2022463, 5,
        2450000, 2,
        5152000, 5,
        5152001, 5
    };
    public static int[] eventRareReward = {
        4031019, 5,
        2049100, 5,
        //        1122017, 1,
        2049401, 10,
        2049301, 20,
        2049400, 3,
        2340000, 1,
        3010130, 5,
        3010131, 5,
        3010132, 5,
        3010133, 5,
        3010136, 5,
        3010116, 5,
        3010117, 5,
        3010118, 5,
        1112405, 1,
        1112413, 1,
        1112414, 1,
        //        1022097, 1,
        2040211, 1,
        2040212, 1,
        2049000, 2,
        2049001, 2,
        2049002, 2,
        2049003, 2,
        1012058, 2,
        1012059, 2,
        1012060, 2,
        1012061, 2
    };
    public static int[] eventSuperReward = {
        4031019, 5,
        2022121, 10,
        4031307, 50,
        3010127, 10,
        3010128, 10,
        3010137, 10,
        2049300, 10,
        1012139, 10,
        1012140, 10,
        1012141, 10
    };
    public static int[] fishingReward = {
        0, 90, // Meso
        1, 70, // EXP
        // 1302021, 5, // Pico Pico Hammer
        // 1072238, 1, // Voilet Snowshoe
        // 1072239, 1, // Yellow Snowshoe
        //2049100, 1, // Chaos Scroll
        // 2049301, 1, // Equip Enhancer Scroll
        // 2049401, 1, // Potential Scroll
        // 1302000, 3, // Sword
        // 1442011, 1, // Surfboard
        //  4000517, 8, // Golden Fish
        //  4000518, 25, // Golden Fish Egg
        4031627, 2, // White Bait (3cm)
        4031628, 1, // Sailfish (120cm)
        4031630, 1, // Carp (30cm)
        4031631, 1, // Salmon(150cm)
        4031632, 1, // Shovel
        4031633, 2, // Whitebait (3.6cm)
        4031634, 1, // Whitebait (5cm)
        4031635, 1, // Whitebait (6.5cm)
        4031636, 1, // Whitebait (10cm)
        4031637, 2, // Carp (53cm)
        4031638, 2, // Carp (60cm)
        4031639, 1, // Carp (100cm)
        4031640, 1, // Carp (113cm)
        4031641, 2, // Sailfish (128cm)
        4031642, 2, // Sailfish (131cm)
        4031643, 1, // Sailfish (140cm)
        4031644, 1, // Sailfish (148cm)
        4031645, 2, // Salmon (166cm)
        4031646, 2, // Salmon (183cm)
        4031647, 1, // Salmon (227cm)
        4031648, 1, // Salmon (288cm)
        4031629, 1 // Pot
    };

    public static boolean isDragonItem(int itemId) {
        switch (itemId) {
            case 1372032:
            case 1312031:
            case 1412026:
            case 1302059:
            case 1442045:
            case 1402036:
            case 1432038:
            case 1422028:
            case 1472051:
            case 1472052:
            case 1332049:
            case 1332050:
            case 1322052:
            case 1452044:
            case 1462039:
            case 1382036:
            case 1342010:
                return true;
            default:
                return false;
        }
    }

    public static boolean isReverseItem(int itemId) {
        switch (itemId) {
            case 1002790:
            case 1002791:
            case 1002792:
            case 1002793:
            case 1002794:
            case 1082239:
            case 1082240:
            case 1082241:
            case 1082242:
            case 1082243:
            case 1052160:
            case 1052161:
            case 1052162:
            case 1052163:
            case 1052164:
            case 1072361:
            case 1072362:
            case 1072363:
            case 1072364:
            case 1072365:

            case 1302086:
            case 1312038:
            case 1322061:
            case 1332075:
            case 1332076:
            case 1372045:
            case 1382059:
            case 1402047:
            case 1412034:
            case 1422038:
            case 1432049:
            case 1442067:
            case 1452059:
            case 1462051:
            case 1472071:
            case 1482024:
            case 1492025:

            case 1342012:
                return true;
            default:
                return false;
        }
    }

    public static boolean isTimelessItem(int itemId) {
        switch (itemId) {
            case 1032031: //shield earring, but technically
            case 1102172:
            case 1002776:
            case 1002777:
            case 1002778:
            case 1002779:
            case 1002780:
            case 1082234:
            case 1082235:
            case 1082236:
            case 1082237:
            case 1082238:
            case 1052155:
            case 1052156:
            case 1052157:
            case 1052158:
            case 1052159:
            case 1072355:
            case 1072356:
            case 1072357:
            case 1072358:
            case 1072359:
            case 1092057:
            case 1092058:
            case 1092059:

            case 1122011:
            case 1122012:

            case 1302081:
            case 1312037:
            case 1322060:
            case 1332073:
            case 1332074:
            case 1372044:
            case 1382057:
            case 1402046:
            case 1412033:
            case 1422037:
            case 1432047:
            case 1442063:
            case 1452057:
            case 1462050:
            case 1472068:
            case 1482023:
            case 1492023:
            case 1342011:
                return true;
            default:
                return false;
        }
    }

    public static boolean isRing(int itemId) {
        return itemId >= 1112000 && itemId < 1113000;
    }// 112xxxx - pendants, 113xxxx - belts

    //if only there was a way to find in wz files -.-
    public static boolean isEffectRing(int itemid) {

        return isFriendshipRing(itemid) || isCrushRing(itemid) || isMarriageRing(itemid);
    }

    public static boolean isFriendshipRing(int itemId) {
        switch (itemId) {
            case 1112800:
            case 1112801:
            case 1112802:
            //  case 1112804:
            case 1112810: //new
            case 1112811: //new, doesnt work in friendship?
            case 1112812: //new, im ASSUMING it's friendship cuz of itemID, not sure.
            case 1112015:
            case 1049000:

            case 1112816:
            case 1112817:

                return true;
        }
        return false;
    }

    public static boolean isCrushRing(int itemId) {
        switch (itemId) {
            case 1112001:
            case 1112002:
            case 1112003:
            case 1112005:
            case 1112006:
            case 1112007:
            case 1112012:
            case 1112015:
            //case 1112804:
            case 1048000:
            case 1048001:
            case 1048002:
                return true;
        }
        return false;
    }
    public static int[] Equipments_Bonus = {1122017};

    public static int Equipment_Bonus_EXP(final int itemid) { // TODO : Add Time for more exp increase
        switch (itemid) {
            case 1122017:
                return 30;
        }
        return 0;
    }
    public static int[] blockedMaps = {109050000, 280030000, 240060200, 280090000, 280030001, 240060201, 950101100, 950101010};
    //If you can think of more maps that could be exploitable via npc,block nao pliz!

    public static int getExpForLevel(int i, int itemId) {
        if (isReverseItem(itemId)) {
            return getReverseRequiredEXP(i);
        } else if (getMaxLevel(itemId) > 0) {
            return getTimelessRequiredEXP(i);
        }
        return 0;
    }

    public static int getMaxLevel(final int itemId) {
        if (isTimelessItem(itemId)) {
            return 5;
        } else if (isReverseItem(itemId)) {
            return 3;
        } else {
            switch (itemId) {
                case 1302109:
                case 1312041:
                case 1322067:
                case 1332083:
                case 1372048:
                case 1382064:
                case 1402055:
                case 1412037:
                case 1422041:
                case 1432052:
                case 1442073:
                case 1452064:
                case 1462058:
                case 1472079:
                case 1482035:

                case 1302108:
                case 1312040:
                case 1322066:
                case 1332082:
                case 1372047:
                case 1382063:
                case 1402054:
                case 1412036:
                case 1422040:
                case 1432051:
                case 1442072:
                case 1452063:
                case 1462057:
                case 1472078:
                case 1482036:
                    return 1;

                case 1072376:
                    return 2;
            }
        }
        return 0;
    }

    public static int getStatChance() {
        return 25;
    }

    public static MonsterStatus getStatFromWeapon(final int itemid) {
        switch (itemid) {
            case 1302109:
            case 1312041:
            case 1322067:
            case 1332083:
            case 1372048:
            case 1382064:
            case 1402055:
            case 1412037:
            case 1422041:
            case 1432052:
            case 1442073:
            case 1452064:
            case 1462058:
            case 1472079:
            case 1482035:
                return MonsterStatus.命中;
            case 1302108:
            case 1312040:
            case 1322066:
            case 1332082:
            case 1372047:
            case 1382063:
            case 1402054:
            case 1412036:
            case 1422040:
            case 1432051:
            case 1442072:
            case 1452063:
            case 1462057:
            case 1472078:
            case 1482036:
                return MonsterStatus.速度;
        }
        return null;
    }

    public static int getXForStat(MonsterStatus stat) {
        switch (stat) {
            case 命中:
                return -70;
            case 速度:
                return -50;
        }
        return 0;
    }

    public static int getSkillForStat(MonsterStatus stat) {
        switch (stat) {
            case 命中:
                return 3221006;
            case 速度:
                return 3121007;
        }
        return 0;
    }
    public final static int[] normalDrops = {
        4001009, //real
        4001010,
        4001011,
        4001012,
        4001013,
        4001014, //real
        4001021,
        4001038, //fake
        4001039,
        4001040,
        4001041,
        4001042,
        4001043, //fake
        4001038, //fake
        4001039,
        4001040,
        4001041,
        4001042,
        4001043, //fake
        4001038, //fake
        4001039,
        4001040,
        4001041,
        4001042,
        4001043, //fake
        4000164, //start
        2000000,
        2000003,
        2000004,
        2000005,
        4000019,
        4000000,
        4000016,
        4000006,
        2100121,
        4000029,
        4000064,
        5110000,
        4000306,
        4032181,
        4006001,
        4006000,
        2050004,
        3994102,
        3994103,
        3994104,
        3994105,
        2430007, //end
        4000164, //start
        2000000,
        2000003,
        2000004,
        2000005,
        4000019,
        4000000,
        4000016,
        4000006,
        2100121,
        4000029,
        4000064,
        5110000,
        4000306,
        4032181,
        4006001,
        4006000,
        2050004,
        3994102,
        3994103,
        3994104,
        3994105,
        2430007, //end
        4000164, //start
        2000000,
        2000003,
        2000004,
        2000005,
        4000019,
        4000000,
        4000016,
        4000006,
        2100121,
        4000029,
        4000064,
        5110000,
        4000306,
        4032181,
        4006001,
        4006000,
        2050004,
        3994102,
        3994103,
        3994104,
        3994105,
        2430007}; //end
    public final static int[] rareDrops = {
        2049100,
        2049301,
        2049401,
        2022326,
        2022193,
        2049000,
        2049001,
        2049002};
    public final static int[] superDrops = {
        2040804,
        2049400,
        2049100};

    public static int getSkillBook(final int job) {
        if (job >= 2210 && job <= 2218) {
            return job - 2209;
        }
        switch (job) {
            case 3210:
            case 3310:
            case 3510:
                return 1;
            case 3211:
            case 3311:
            case 3511:
                return 2;
            case 3212:
            case 3312:
            case 3512:
                return 3;
        }
        return 0;
    }

    public static int getSkillBookForSkill(final int skillid) {
        return getSkillBook(skillid / 10000);
    }

    public static int getMountItem(final int sourceid) {
        switch (sourceid) {
            case 5221006:
                return 1932000;
            case 33001001: //temp.
                return 1932015;
            case 35001002:
            case 35120000:
                return 1932016;
            case 1013:
            case 10001013:
            case 20001013:
            case 20011013:
            case 30001013:
            case 1046:
            case 10001046:
            case 20001046:
            case 20011046:
            case 30001046:
                return 1932001;
            case 1015:
            case 10001015:
            case 20001015:
            case 20011015:
            case 30001015:
            case 1048:
            case 10001048:
            case 20001048:
            case 20011048:
            case 30001048:
                return 1932002;
            case 1016:
            case 10001016:
            case 20001016:
            case 20011016:
            case 30001016:
            case 1017:
            case 1007:
            case 10001017:
            case 20001017:
            case 20011017:
            case 30001017:
            case 1027:
            case 10001027:
            case 20001027:
            case 20011027:
            case 30001027:
                return 1932007;
            case 1018:
            case 10001018:
            case 20001018:
            case 20011018:
            case 30001018:
                return 1932003;
            case 1019:
            case 10001019:
            case 20001019:
            case 20011019:
            case 30001019:
                return 1932005;
            case 1025:
            case 10001025:
            case 20001025:
            case 20011025:
            case 30001025:
                return 1932006;
            case 1028:
            case 10001028:
            case 20001028:
            case 20011028:
            case 30001028:
                return 1932008;
            case 1029:
            case 10001029:
            case 20001029:
            case 20011029:
            case 30001029:
                return 1932009;
            case 1030:
            case 10001030:
            case 20001030:
            case 20011030:
            case 30001030:
                return 1932011;
            case 1031:
            case 10001031:
            case 20001031:
            case 20011031:
            case 30001031:
                return 1932010;
            case 1034:
            case 10001034:
            case 20001034:
            case 20011034:
            case 30001034:
                return 1932014;
            case 1035:
            case 10001035:
            case 20001035:
            case 20011035:
            case 30001035:
                return 1932012;
            case 1036:
            case 10001036:
            case 20001036:
            case 20011036:
            case 30001036:
                return 1932017;
            case 1037:
            case 10001037:
            case 20001037:
            case 20011037:
            case 30001037:
                return 1932018;
            case 1038:
            case 10001038:
            case 20001038:
            case 20011038:
            case 30001038:
                return 1932019;
            case 1039:
            case 10001039:
            case 20001039:
            case 20011039:
            case 30001039:
                return 1932020;
            case 1040:
            case 10001040:
            case 20001040:
            case 20011040:
            case 30001040:
                return 1932021;
            case 1042:
            case 10001042:
            case 20001042:
            case 20011042:
            case 30001042:
                return 1932022;
            case 1044:
            case 10001044:
            case 20001044:
            case 20011044:
            case 30001044:
                return 1932023;
            case 1045:
            case 10001045:
            case 20001045:
            case 20011045:
            case 30001045:
                return 1932030; //wth? helicopter? i didnt see one, so we use hog
            case 1049:
            case 10001049:
            case 20001049:
            case 20011049:
            case 30001049:
                return 1932025;
            case 1050:
            case 10001050:
            case 20001050:
            case 20011050:
            case 30001050:
                return 1932004;
            case 1051:
            case 10001051:
            case 20001051:
            case 20011051:
            case 30001051:
                return 1932026;
            case 1052:
            case 10001052:
            case 20001052:
            case 20011052:
            case 30001052:
                return 1932027;
            case 1053:
            case 10001053:
            case 20001053:
            case 20011053:
            case 30001053:
                return 1932028;
            case 1054:
            case 10001054:
            case 20001054:
            case 20011054:
            case 30001054:
                return 1932029;
            case 1069:
            case 10001069:
            case 20001069:
            case 20011069:
            case 30001069:
                return 1932038;
            case 1096:
            case 10001096:
            case 20001096:
            case 20011096:
            case 30001096:
                return 1932045;
            case 1101:
            case 10001101:
            case 20001101:
            case 20011101:
            case 30001101:
                return 1932046;
            case 1102:
            case 10001102:
            case 20001102:
            case 20011102:
            case 30001102:
                return 1932047;
            default:
                return 0;
        }
    }

    public static boolean isKatara(int itemId) {
        return itemId / 10000 == 134;
    }

    public static boolean isDagger(int itemId) {
        return itemId / 10000 == 133;
    }

    public static boolean isApplicableSkill(int skil) {
        return skil < 40000000 && (skil % 10000 < 8000 || skil % 10000 > 8003); //no additional/decent skills
    }

    public static boolean isApplicableSkill_(int skil) { //not applicable to saving but is more of temporary
        return skil >= 90000000 || (skil % 10000 >= 8000 && skil % 10000 <= 8003);
    }

    public static boolean isTablet(int itemId) {
        return itemId / 1000 == 2047;
    }

    public static int getSuccessTablet(final int scrollId, final int level) {
        if (scrollId % 1000 / 100 == 2) { //2047_2_00 = armor, 2047_3_00 = accessory
            switch (level) {
                case 0:
                    return 70;
                case 1:
                    return 55;
                case 2:
                    return 43;
                case 3:
                    return 33;
                case 4:
                    return 26;
                case 5:
                    return 20;
                case 6:
                    return 16;
                case 7:
                    return 12;
                case 8:
                    return 10;
                default:
                    return 7;
            }
        } else if (scrollId % 1000 / 100 == 3) {
            switch (level) {
                case 0:
                    return 70;
                case 1:
                    return 35;
                case 2:
                    return 18;
                case 3:
                    return 12;
                default:
                    return 7;
            }
        } else {
            switch (level) {
                case 0:
                    return 70;
                case 1:
                    return 50; //-20
                case 2:
                    return 36; //-14
                case 3:
                    return 26; //-10
                case 4:
                    return 19; //-7
                case 5:
                    return 14; //-5
                case 6:
                    return 10; //-4
                default:
                    return 7;  //-3
            }
        }
    }

    public static int getCurseTablet(final int scrollId, final int level) {
        if (scrollId % 1000 / 100 == 2) { //2047_2_00 = armor, 2047_3_00 = accessory
            switch (level) {
                case 0:
                    return 10;
                case 1:
                    return 12;
                case 2:
                    return 16;
                case 3:
                    return 20;
                case 4:
                    return 26;
                case 5:
                    return 33;
                case 6:
                    return 43;
                case 7:
                    return 55;
                case 8:
                    return 70;
                default:
                    return 100;
            }
        } else if (scrollId % 1000 / 100 == 3) {
            switch (level) {
                case 0:
                    return 12;
                case 1:
                    return 18;
                case 2:
                    return 35;
                case 3:
                    return 70;
                default:
                    return 100;
            }
        } else {
            switch (level) {
                case 0:
                    return 10;
                case 1:
                    return 14; //+4
                case 2:
                    return 19; //+5
                case 3:
                    return 26; //+7
                case 4:
                    return 36; //+10
                case 5:
                    return 50; //+14
                case 6:
                    return 70; //+20
                default:
                    return 100;  //+30
            }
        }
    }

    public static boolean isAccessory(final int itemId) {
        return (itemId >= 1010000 && itemId < 1040000) || (itemId >= 1122000 && itemId < 1153000) || (itemId >= 1112000 && itemId < 1113000);
    }

    public static boolean potentialIDFits(final int potentialID, final int newstate, final int i) {
        //first line is always the best
        //but, sometimes it is possible to get second/third line as well
        //may seem like big chance, but it's not as it grabs random potential ID anyway
        if (newstate == 7) {
            return (i == 0 || Randomizer.nextInt(10) == 0 ? potentialID >= 30000 : potentialID >= 20000 && potentialID < 30000);
        } else if (newstate == 6) {
            return (i == 0 || Randomizer.nextInt(10) == 0 ? potentialID >= 20000 && potentialID < 30000 : potentialID >= 10000 && potentialID < 20000);
        } else if (newstate == 5) {
            return (i == 0 || Randomizer.nextInt(10) == 0 ? potentialID >= 10000 && potentialID < 20000 : potentialID < 10000);
        } else {
            return false;
        }
    }

    public static boolean optionTypeFits(final int optionType, final int itemId) {
        switch (optionType) {
            case 10: //weapon
                return isWeapon(itemId);
            case 11: //any armor
                return !isWeapon(itemId);
            case 20: //shield??????????
                return itemId / 10000 == 109; //just a gues
            case 21: //pet equip?????????
                return itemId / 10000 == 180; //???LOL
            case 40: //face accessory
                return isAccessory(itemId);
            case 51: //hat
                return itemId / 10000 == 100;
            case 52: //cape
                return itemId / 10000 == 110;
            case 53: //top/bottom/overall
                return itemId / 10000 == 104 || itemId / 10000 == 105 || itemId / 10000 == 106;
            case 54: //glove
                return itemId / 10000 == 108;
            case 55: //shoe
                return itemId / 10000 == 107;
            case 90:
                return false; //half this stuff doesnt even work
            default:
                return true;
        }
    }

    public static final boolean isMountItemAvailable(final int mountid, final int jobid) {
        if (jobid != 900 && mountid / 10000 == 190) {
            if (isKOC(jobid)) {
                if (mountid < 1902005 || mountid > 1902007) {
                    return false;
                }
            } else if (isAdventurer(jobid)) {
                if (mountid < 1902000 || mountid > 1902002) {
                    return false;
                }
            } else if (isAran(jobid)) {
                if (mountid < 1902015 || mountid > 1902018) {
                    return false;
                }
            } else if (isEvan(jobid)) {
                if (mountid < 1902040 || mountid > 1902042) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isEvanDragonItem(final int itemId) {
        return itemId >= 1940000 && itemId < 1980000; //194 = mask, 195 = pendant, 196 = wings, 197 = tail
    }

    public static boolean canScroll(final int itemId) {
        return itemId / 100000 != 19 && itemId / 100000 != 16; //no mech/taming/dragon
    }

    public static boolean canHammer(final int itemId) {
        switch (itemId) {
            case 1122000:
            case 1122076: //ht, chaos ht
                return false;
        }
        if (!canScroll(itemId)) {
            return false;
        }
        return true;
    }
    public static int[] owlItems = new int[]{
        1082002, // work gloves
        2070005,
        2070006,
        1022047,
        1102041,
        2044705,
        2340000, // white scroll
        2040017,
        1092030,
        2040804};

    public static int getMasterySkill(final int job) {
        if (job >= 1410 && job <= 1412) {
            return 14100000;
        } else if (job >= 410 && job <= 412) {
            return 4100000;
        } else if (job >= 520 && job <= 522) {
            return 5200000;
        }
        return 0;
    }

    public static int getExpRate_Below10(final int job) {
        if (GameConstants.isEvan(job)) {
            return 1;
        } else if (GameConstants.isAran(job) || GameConstants.isKOC(job)) {
            return 5;
        }
        return 1;
    }

    public static int getExpRate_Quest(final int level) {
        return 1;
    }

    public static String getCashBlockedMsg(final int id) {
        switch (id) {
            case 5062000:
                //cube
                return "该道具只能通过NPC购买.";
        }
        return "该道具禁止购买";
    }

    public static boolean isCustomReactItem(final int rid, final int iid, final int original) {
        if (rid == 2008006) { //orbis pq LOL
            return iid == (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + 4001055);
            //4001056 = sunday. 4001062 = saturday
        } else {
            return iid == original;
        }
    }

    public static int getJobNumber(int jobz) {
        int job = (jobz % 1000);
        if (job / 100 == 0) {
            return 0; //beginner
        } else if (job / 10 == 0) {
            return 1;
        } else {
            return 2 + (job % 10);
        }
    }

    public static boolean isForceRespawn(int mapid) {
        switch (mapid) {
            case 925100100: //crocs and stuff
                return true;
            default:
                return mapid / 100000 == 9800 && (mapid % 10 == 1 || mapid % 1000 == 100);
        }
    }

    public static int getCustomSpawnID(int summoner, int def) {
        switch (summoner) {
            case 9400589:
            case 9400748: //MV
                return 9400706; //jr
            default:
                return def;
        }
    }

    public static boolean canForfeit(int questid) {
        switch (questid) {
            case 20000:
            case 20010:
            case 20015: //cygnus quests
            case 20020:
                return false;
            default:
                return true;
        }
    }

    public static boolean isEventMap(final int mapid) {
        return (mapid >= 109010000 && mapid < 109050000) || (mapid > 109050001 && mapid < 109090000) || (mapid >= 809040000 && mapid <= 809040100);
    }

    public static boolean is豆豆装备(int itemId) {
        switch (itemId) {
            //帽子
            case 1123200:
            case 1002695://幽灵帽
            case 1002609://兔耳魔法帽
            case 1002665://西红柿帽
            case 1002985://豆箱帽子
            case 1002986://蝙蝠怪面具
            case 1002761://枫叶面具
            case 1002760://地球帽
            case 1002583://蝙蝠客头套
            case 1002543://板栗帽
            case 1002448://紫色头巾

            //衣服
            case 1052137://西红柿外套

            //盾牌
            case 1092051://啤酒杯盾牌

            //武器
            case 1702232://我的朋友金猎犬
            case 1702138://大火腿

            //坐骑
            case 1902031:
            case 1902032:
            case 1902033:
            case 1902034:
            case 1902035:
            case 1902036:
            case 1902037:
            //鞍子
            case 1912024:
            case 1912025:
            case 1912026:
            case 1912027:
            case 1912028:
            case 1912029:
            case 1912030:
                return true;
        }
        return false;
    }

    public static int getCustomReactItem(int rid, int original) {
        if (rid == 2008006) {
            return Calendar.getInstance().get(7) + 4001055;
        }

        return original;
    }

    public static boolean Summon_Skill_ID_550(int SkillID) {
        switch (SkillID) {
            case 3121006:// 火凤凰     550
                return true;
        }
        return false;
    }

    public static boolean Summon_Skill_ID_500(int SkillID) {
        switch (SkillID) {
            case 3221005:// 冰凤凰     500
            case 5220002:// 超级章鱼炮台     500
                return true;
        }
        return false;
    }

    public static boolean Summon_Skill_ID_450(int SkillID) {
        switch (SkillID) {
            case 5211002:// 海鸥空袭     450
                return true;
        }
        return false;
    }

    public static boolean Summon_Skill_ID_300(int SkillID) {
        switch (SkillID) {
            case 2221005:// 火魔兽       300
                return true;
        }
        return false;
    }

    public static boolean Summon_Skill_ID_270(int SkillID) {
        switch (SkillID) {
            case 2121005:// 冰破魔兽     270
                return true;
        }
        return false;
    }

    public static boolean Summon_Skill_ID_250(int SkillID) {
        switch (SkillID) {
            case 12111004:// 火魔兽	 250
                return true;
        }
        return false;
    }

    public static boolean Summon_Skill_ID_230(int SkillID) {
        switch (SkillID) {
            case 2321003:// 强化圣龙     230
                return true;
        }
        return false;
    }

    public static boolean Summon_Skill_ID_200(int SkillID) {
        switch (SkillID) {
            case 5211001:// 章鱼炮台     200
                return true;
        }
        return false;
    }

    public static boolean Summon_Skill_ID_150(int SkillID) {
        switch (SkillID) {
            case 2311006:// 圣龙召唤     150
                return true;
        }
        return false;
    }

    public static boolean Summon_Skill_ID_100(int SkillID) {
        switch (SkillID) {
            case 3111005://银鹰召唤     100
            case 3211005://金鹰召唤     100
                return true;
        }
        return false;
    }

    public static boolean Summon_Skill_ID_40(int SkillID) {
        switch (SkillID) {
            case 11001004:// 魂精灵  	 40
            case 12001004:// 炎精灵	 20
            case 13001004:// 风精灵	 40
            case 14001005:// 夜精灵	 40
            case 15001004:// 雷精灵	 40
                return true;
        }
        return false;
    }

    public static int Novice_Skill(int skill, MapleCharacter c, int damage) {
        switch (skill) {
            case 1000://新手 蜗牛壳
            case 10001000://新手 蜗牛壳
            case 20001000://战神  蜗牛壳
                if ((c.getStat().getCurrentMaxBaseDamage() <= damage / 13)) {
                    return 1;
                }
        }
        return damage;
    }

    public static boolean Novice_Skill(int skill) {
        switch (skill) {
            case 1000://新手 蜗牛壳
            case 10001000://新手 蜗牛壳
            case 20001000://战神  蜗牛壳
                return true;
        }
        return false;
    }

    public static boolean Ares_Skill_140(int skill) {//战神技能
        switch (skill) {
            case 21100002:
            case 21111005:
            case 21110006:
            case 21000002:

                return true;
        }
        return false;
    }

    public static boolean Ares_Skill_350(int skill) {//战神技能
        switch (skill) {
            case 21110003:
            case 21120002:
            case 21100001:
            case 21110002:

                return true;
        }
        return false;
    }

    public static boolean Ares_Skill_800(int skill) {//战神技能
        switch (skill) {
            case 21120005:
            case 21110004:
            case 21100004:

                return true;
        }
        return false;
    }

    public static boolean Ares_Skill_1500(int skill) {//战神技能
        switch (skill) {
            case 21120006:

                return true;
        }
        return false;
    }

    public static boolean Thief_Skill_270(int skill) {//奇袭者技能
        switch (skill) {
            case 15001001:
            case 15001002:
            case 15101006:
            case 15110000:
            case 15111004:
            case 15111006:
                return true;
        }
        return false;
    }

    public static boolean Thief_Skill_420(int skill) {//奇袭者技能
        switch (skill) {
            case 15111001:
            case 15101003:
            case 15111007:
                return true;
        }
        return false;
    }

    public static boolean Thief_Skill_650(int skill) {//奇袭者技能
        switch (skill) {
            case 15111003:
                return true;
        }
        return false;
    }

    public static boolean Night_Knight_Skill_220(int skill) {//夜行者技能
        switch (skill) {
            case 14001004:
            case 14100005:
            case 14101006:
            case 14111002:
            case 14111005:
            case 14111006:
                return true;
        }
        return false;
    }

    public static boolean Wind_Knight_Skill_160(int skill) {//风灵使者技能
        switch (skill) {
            case 13001003:
            case 13101005:
            case 13111000:
            case 13111001:
            case 13111002:
                return true;
        }
        return false;
    }

    public static boolean Wind_Knight_Skill_550(int skill) {//风灵使者技能
        switch (skill) {
            case 13111006:
            case 13111007:
                return true;
        }
        return false;
    }

    public static boolean Fire_Knight_Skill_140(int skill) {//魂骑士技能
        switch (skill) {
            case 12001003:
            case 12101002:
            case 12101006:
            case 12111005:
            case 12111006:
                return true;
        }
        return false;
    }

    public static boolean Fire_Knight_Skill_500(int skill) {//魂骑士技能
        switch (skill) {
            case 12111003:
                return true;
        }
        return false;
    }

    public static boolean Ghost_Knight_Skill_320(int skill) {//魂骑士技能
        switch (skill) {
            case 11001002:
            case 11001003:
            case 11101004:
            case 11111002:
            case 11111003:
            case 11111004:
            case 11111006:
                return true;
        }
        return false;
    }

    public static boolean Pirate_Skill_290(int skill) {//海盗技能
        switch (skill) {
            case 5001001:
            case 5001002:
            case 5001003:
            case 5101002:
            case 5101003:
            case 5201001:
            case 5201002:
            case 5201004:
            case 5201006:
            case 5210000:
            case 5211004:
            case 5211005:
            case 5121007:
            case 5221004:
                return true;
        }
        return false;
    }

    public static boolean Pirate_Skill_420(int skill) {//海盗技能
        switch (skill) {
            case 5221007:
            case 5211006:
            case 5121004:
            case 5101004:
                return true;
        }
        return false;
    }

    public static boolean Pirate_Skill_700(int skill) {//海盗技能
        switch (skill) {
            case 5111006:
            case 5220011:
            case 5121005:
                return true;
        }
        return false;
    }

    public static boolean Pirate_Skill_810(int skill) {//海盗技能
        switch (skill) {
            case 5221008:
            case 5121001:
                return true;
        }
        return false;
    }

    public static boolean Pirate_Skill_1200(int skill) {//海盗技能
        switch (skill) {
            case 5221003:
                return true;
        }
        return false;
    }

    public static boolean Thief_Skill_180(int skill) {//飞侠技能
        switch (skill) {
            case 4001334:
            case 4001344:
            case 4101005:
            case 4111004:
            case 4111005:
            case 4121007:
            case 4201004:
            case 4201005:
                return true;
        }
        return false;
    }

    public static boolean Thief_Skill_250(int skill) {//飞侠技能
        switch (skill) {
            case 4211004:
                return true;
        }
        return false;
    }

    public static boolean Thief_Skill_500(int skill) {//飞侠技能
        switch (skill) {
            case 4211002:
            case 4221007:
            case 4221001:
                return true;
        }
        return false;
    }

    public static boolean Bowman_Skill_180(int skill) {//弓箭手技能
        switch (skill) {
            case 3001005:
            case 3101005:
            case 3111003:
            case 3111004:
            case 3111006:
            case 3121003:
            case 3121004:
            case 3221003:
            case 3201005:
            case 3211003:
            case 3211004:
            case 3211006:
                return true;
        }
        return false;
    }

    public static boolean Bowman_Skill_260(int skill) {//弓箭手技能
        switch (skill) {
            case 3201003:
            case 3101003:
            case 3000001:
            case 3001004:
                return true;
        }
        return false;
    }

    public static boolean Bowman_Skill_850(int skill) {//弓箭手技能
        switch (skill) {
            case 3221001:
                return true;
        }
        return false;
    }

    public static boolean Magician_Skill_90(int skill) {//魔法师技能
        switch (skill) {
            case 2001004:
            case 2001005:
            case 2301005:
                return true;
        }
        return false;
    }

    public static boolean Magician_Skill_180(int skill) {//魔法师技能
        switch (skill) {
            case 2101004:
            case 2111002:
            case 2111006:
            case 2121003:
            case 2201004:
            case 2211003:
            case 2211006:
            case 2221003:
            case 2221006:
            case 2311004:
            case 2211002:
                return true;
        }
        return false;
    }

    public static boolean Magician_Skill_240(int skill) {//魔法师技能
        switch (skill) {
            case 2121006:
            case 2321007:
            case 2301002:
            case 2201005:
                return true;
        }
        return false;
    }

    public static boolean Magician_Skill_670(int skill) {//魔法师技能
        switch (skill) {
            case 2121001:
            case 2221001:
            case 2321001:
            case 2121007:
            case 2221007:
            case 2321008:
                return true;
        }
        return false;
    }

    public static boolean Warrior_Skill_900(int skill) {//战士技能
        switch (skill) {
            case 1221011:

                return true;
        }
        return false;
    }

    public static boolean Warrior_Skill_550(int skill) {//战士技能
        switch (skill) {
            case 1221009:

                return true;
        }
        return false;
    }

    public static boolean Warrior_Skill_450(int skill) {//战士技能
        switch (skill) {
            case 1001004:
            case 1001005:
            case 1100002:
            case 1100003:
            case 1111002:
            case 1111008:
            case 1121006:
            case 1121008:
            case 1121009:
            case 1211002:
            case 1221007:
            case 1311001:
            case 1311002:
            case 1311003:
            case 1311004:
            case 1311005:
            case 1311006:
            case 1321003:
                return true;
        }
        return false;
    }

    public static boolean Warrior_Skill_2000(int skill) {//战士技能
        switch (skill) {
            case 1111003:
            case 1111004:
            case 1111005:
            case 1111006:
                return true;
        }
        return false;
    }

    public static boolean No_Mob(int MobID) {
        switch (MobID) {
            case 9300028: //艾里葛斯
            case 8510000: //皮亚奴斯
            case 8510100: //嗜血单眼怪 
            case 8520000: //皮亚奴斯
            case 8800000: //扎昆1
            case 8800001: //扎昆2
            case 8800002: //扎昆 
            case 8800003: //扎昆手臂1 
            case 8800004: //扎昆手臂2 
            case 8800005: //扎昆手臂3 
            case 8800006: //扎昆手臂4 
            case 8800007: //扎昆手臂5 
            case 8800008: //扎昆手臂6 
            case 8800009: //扎昆手臂7 
            case 8800010: //扎昆手臂8 
            case 8810000: //暗黑龙王的左侧头颅 
            case 8810001: //暗黑龙王的右侧头颅 
            case 8810002: //暗黑龙王的头A 
            case 8810003: //暗黑龙王的头B 
            case 8810004: //暗黑龙王的头C 
            case 8810005: //暗黑龙王的左手 
            case 8810006: //暗黑龙王的右手 
            case 8810007: //暗黑龙王的翅膀 
            case 8810008: //暗黑龙王的腿 
            case 8810009: //暗黑龙王的尾巴 
            case 8810010: //死亡暗黑龙王的头A 
            case 8810011: //死亡暗黑龙王的头B 
            case 8810012: //死亡暗黑龙王的头C 
            case 8810013: //死亡暗黑龙王的左手 
            case 8810014: //死亡暗黑龙王的右手 
            case 8810015: //死亡暗黑龙王的翅膀 
            case 8810016: //死亡暗黑龙王的腿 
            case 8810017: //死亡暗黑龙王的尾巴 
            case 8810018: //暗黑龙王的灵魂 
            case 8500001: //帕普拉图斯的座钟 
            case 8500002: //帕普拉图斯 
            case 8220003: //大海兽  
            case 8220004: // 
            case 8220005: //玄冰独角兽 
            case 8220006: //雷卡 
            case 8820000: //的宠儿－品克缤 
            case 8820001: //的宠儿－品克缤 
            case 8820002: //神雕像 
            case 8820003: //贤者所罗门 
            case 8820004: //贤者莱克斯 
            case 8820005: //火鹰雕像 
            case 8820006: //冰鹰雕像 
            case 8820007: //比恩宝宝 
            case 8820008: //宝宝BOSS召唤用透明怪物 
            case 8820009: //t0透明怪物 
            case 8820010: //的宠儿－品克缤 
            case 8820011: //的宠儿－品克缤 
            case 8820012: //的宠儿－品克缤 
            case 8820013: //的宠儿－品克缤 
            case 8820014: //的宠儿－品克缤 
            case 8820015: //贤者所罗门 
            case 8820016: //贤者莱克斯 
            case 8820017: //火鹰雕像 
            case 8820018: //冰鹰雕像 
            case 8820019: //神雕像 
            case 8820020: //贤者所罗门 
            case 8820021: //贤者莱克斯
            case 9420520: //克雷塞尔 [1851574]
            case 9420521: //克雷塞尔 [1851613]
            case 9420522: //克雷塞尔 
            case 9420541:
            case 9420542:
            case 9420543:
            case 9420544:
            case 9420545:
            case 9420546:
            case 9420547:
            case 9420548:
            case 9420549:
            case 9420550:

                return true;
        }
        return false;
    }

    public static boolean 不检测技能(int SkillID) {
        switch (SkillID) {
            case 1321001:
            case 1121001:
            case 1221001:
            case 1111008:
            case 1121006:
            case 1221007:
            case 1321003:
            case 21100002:
            case 21100004:
            case 21110004:
                return true;
        }
        return false;
    }

    public static int getMonsterHP(int level) {
        if ((level < 0) || (level >= mobHpVal.length)) {
            return Integer.MAX_VALUE;
        }
        return mobHpVal[level];
    }

    public static boolean isMarriageRing(int itemId) {
        switch (itemId) {
            case 1112300:
            case 1112301:
            case 1112302:
            case 1112303:
            case 1112304:
            case 1112305:
            case 1112306:
            case 1112307:
            case 1112308:
            case 1112309:
            case 1112310:
            case 1112311:
            case 1112315:
            case 1112316:
            case 1112317:
            case 1112318:
            case 1112319:
            case 1112320:
            case 1112803:
            //   case 1112804:
            case 1112806:
            case 1112807:
            case 1112808:
            case 1112809:
            case 1112013:
                return true;
        }
        return false;
    }

    public static boolean isElseSkill(int id) {
        switch (id) {
            case 10001009:
            case 20001009:
            case 1009:
            case 1020:
            case 10001020:
            case 20001020:
            case 4211006:
                return true;
        }
        return false;
    }

    public static double getAttackRange(MapleCharacter chr, MapleStatEffect def, AttackInfo attack) {
        int rangeInc = 50;//chr.getStat().defRange;// 矪瞶环祘戮穨
        double base = 450.0;// 膀娄
        double defRange = ((base + rangeInc) * (base + rangeInc));// 膀娄絛瞅
        if (def != null) {
            // 璸衡絛瞅((maxX * maxX) + (maxY * maxY)) + (м絛瞅 * м絛瞅))
            //    defRange += def.getMaxDistanceSq() + (def.getRange() * def.getRange());
            if (getAttackRangeBySkill(attack) != 0) {// 钡﹚м絛瞅
                defRange = getAttackRangeBySkill(attack);
            }
        } else {// 炊硄ю阑
            defRange = getAttackRangeByWeapon(chr);// 眖猌竟莉絛瞅
        }
        return defRange;
    }

    private static double getAttackRangeBySkill(AttackInfo attack) {
        double defRange = 0;
        switch (attack.skill) {
            case 21120006:
                defRange = 800000.0;
                break;
            case 2121007:
            case 2221007:
            case 2321008:
                defRange = 750000.0;
                break;
            case 2221006:
            case 3101005:
            case 21101003:
                defRange = 600000.0;
                break;
            case 2111003:
                defRange = 400000.0;
                break;
            case 4001344:
            case 1121008:
                defRange = 350000.0;
                break;
            case 2211002:
                defRange = 300000.0;
                break;
            case 5110001:
            case 2311004:
            case 2211003:
            case 2001005:
                defRange = 250000.0;
                break;
            case 5221004:
            case 2321007:
                defRange = 200000.0;
                break;
            case 20001000:
            case 1000:
                defRange = 130000.0;
                break;
        }
        return defRange;
    }

    private static double getAttackRangeByWeapon(MapleCharacter chr) {
//        IItem weapon_item = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11);
//        MapleWeaponType weapon = GameConstants.getWeaponType(weapon_item.getItemId());
//        switch (weapon) {
//            case 簀:       // ベ
//                return 200000;
//            case 甅:     // 甅
//                return 250000;
//            case 簀:     // 簀
//            case └:       // └
//            case :       // 
//                return 180000;
//            default:
        return 100000;

    }

    public static short getSlotMax(int itemId) {
        switch (itemId) {
            case 4030003:
            case 4030004:
            case 4030005:
                return 1;
            case 4001168:
            case 4031306:
            case 4031307:
            case 3993000:
            case 3993002:
            case 3993003:
                return 100;
            case 5220010:
            case 5220013:
                return 1000;
            case 5220020:
                return 2000;
        }
        return 0;
    }

    public static short getStat(int itemId, int def) {
        switch (itemId) {
            //case 1002419:
            //    return 5;
            case 1002959:
                return 25;
            case 1142002:
                return 10;
            case 1122121:
                return 7;
        }
        return (short) def;
    }

    public static short getHpMp(int itemId, int def) {
        switch (itemId) {
            case 1122121:
                return 500;
            case 1142002:
            case 1002959:
                return 1000;
        }
        return (short) def;
    }

    public static short getATK(int itemId, int def) {
        switch (itemId) {
            case 1122121:
                return 3;
            case 1002959:
                return 4;
            case 1142002:
                return 9;
        }
        return (short) def;
    }

    public static short getDEF(int itemId, int def) {
        switch (itemId) {
            case 1122121:
                return 250;
            case 1002959:
                return 500;
        }
        return (short) def;
    }

    public static boolean is武陵道场(int mapId) {
        return mapId >= 925020100 && mapId <= 925023814;
    }

    public static int getLinkedAttackSkill(final int id) {
        switch (id) {
            case 21110007:
            case 21110008:
                return 21110002;
            case 21120009:
            case 21120010:
                return 21120002;
            case 4321001:
                return 4321000;
            case 33101006:
            case 33101007:
                return 33101005;
            case 33101008:
                return 33101004;
            case 35101009:
            case 35101010:
                return 35100008;
            case 35111009:
            case 35111010:
                return 35111001;
            case 35121013:
                return 35111004;
            case 35121011:
                return 35121009;
            case 32001007:
            case 32001008:
            case 32001009:
            case 32001010:
            case 32001011:
                return 32001001;
            case 5300007:
                return 5301001;
            case 5320011:
                return 5321004;
            case 23101007:
                return 23101001;
            case 23111010:
            case 23111009:
                return 23111008;
            case 31001006:
            case 31001007:
            case 31001008:
                return 31000004;
            case 30010183:
            case 30010184:
            case 30010186:
                return 30010110;
            case 5710012:
                return 5711002;
            case 31121010:
                return 31121000;
            case 5211015:
            case 5211016:
                return 5211011;
            case 24111008:
                return 24111006;
            case 24121010:
                return 24121003;
            case 5001008:
                return 5200010;
            case 5001009:
                return 5101004;
            case 41001005:
            case 41001004:
                return 41001000;
            case 41101009:
            case 41101008:
                return 41101000;
            case 41111012:
            case 41111011:
                return 41111000;
            case 41120013:
            case 41120012:
            case 41120011:
                return 41121000;
            case 42001006:
            case 42001005:
                return 42001000;
            case 42001007:
                return 42001002;
            case 42100010:
                return 42101001;
            //Zero:
            case 101000102:
                return 101000101;
            case 101000202:
                return 101000201;
            case 101100202:
                return 101100201;
            case 101110201:
                return 101110200;
            case 101110204:
                return 101110203;
            case 101120101:
                return 101120100;
            case 101120103:
                return 101120102;
            case 101120105:
            case 101120106:
                return 101120104;
            case 101120203:
                return 101120202;
            case 101120205:
            case 101120206:
                return 101120204;
            case 101120200:
                return 101121200;
            case 112000001:
            case 112000002:
                return 112000000;
            case 112120001:
            case 112120002:
            case 112120003:
                return 112120000;
        }
        return id;
    }

    public static int getFishingTime(boolean vip, boolean gm) {
        //return 20 * 1000;//钓鱼时间
        return gm ? 1000 : (vip ? 10000 : 20000);
    }

    public static boolean is战神教程技能(final int skill) {
        switch (skill) {
            case 20000014:// - 教程技能 - 教程技能
            case 20000015:// - 教程技能 - 教程技能
            case 20000016:// - 教程技能 - 教程技能
            case 20000017:// - 教程技能 - 教程技能
            case 20000018:// - 教程技能 - 教程技能
                return true;
        }
        return false;
    }


    public static int getMountS(int s) {
        // int s = Randomizer.nextInt(316);
        switch (s) {
            case 1:
                return 1932003;
            case 2:
                return 1932004;
            case 3:
                return 1932005;
            case 4:
                return 1932006;
            case 5:
                return 1932007;
            case 6:
                return 1932008;
            case 7:
                return 1932009;
            case 8:
                return 1932010;
            case 9:
                return 1932011;
            case 10:
                return 1932012;
            case 11:
                return 1932013;
            case 12:
                return 1932014;
            case 13:
                return 1932015;
            case 14:
                return 1932016;
            case 15:
                return 1932017;
            case 16:
                return 1932018;
            case 17:
                return 1932020;
            case 18:
                return 1932021;
            case 19:
                return 1932022;
            case 20:
                return 1932023;
            case 21:
                return 1932025;
            case 22:
                return 1932026;
            case 23:
                return 1932027;
            case 24:
                return 1932028;
            case 25:
                return 1932029;
            case 26:
                return 1932030;
            case 27:
                return 1932031;
            case 28:
                return 1932032;
            case 29:
                return 1932033;
            case 30:
                return 1932034;
            case 31:
                return 1932035;
            case 32:
                return 1932036;
            case 33:
                return 1932038;
            case 34:
                return 1932041;
            case 35:
                return 1932043;
            case 36:
                return 1932044;
            case 37:
                return 1932045;
            case 38:
                return 1932046;
            case 39:
                return 1932047;
            case 40:
                return 1932048;
            case 41:
                return 1932049;
            case 42:
                return 1932050;
            case 43:
                return 1932051;
            case 44:
                return 1932052;
            case 45:
                return 1932053;
            case 46:
                return 1932054;
            case 47:
                return 1932055;
            case 48:
                return 1932056;
            case 49:
                return 1932057;
            case 50:
                return 1932058;
            case 51:
                return 1932059;
            case 52:
                return 1932060;
            case 53:
                return 1932061;
            case 54:
                return 1932062;
            case 55:
                return 1932063;
            case 56:
                return 1932064;
            case 57:
                return 1932065;
            case 58:
                return 1932066;
            case 59:
                return 1932071;
            case 60:
                return 1932072;
            case 61:
                return 1932078;
            case 62:
                return 1932080;
            case 63:
                return 1932081;
            case 64:
                return 1932083;
            case 65:
                return 1932084;
            case 66:
                return 1932001;
            case 67:
                return 1932086;
            case 68:
                return 1932087;
            case 69:
                return 1932088;
            case 70:
                return 1932089;
            case 71:
                return 1932090;
            case 72:
                return 1932091;
            case 73:
                return 1932092;
            case 74:
                return 1932093;
            case 75:
                return 1932094;
            case 76:
                return 1932095;
            case 77:
                return 1932096;
            case 78:
                return 1932097;
            case 79:
                return 1932098;
            case 80:
                return 1932099;
            case 81:
                return 1932100;
            case 82:
                return 1932102;
            case 83:
                return 1932103;
            case 84:
                return 1932105;
            case 85:
                return 1932106;
            case 86:
                return 1932107;
            case 87:
                return 1932108;
            case 88:
                return 1932109;
            case 89:
                return 1932110;
            case 90:
                return 1932112;
            case 91:
                return 1932113;
            case 92:
                return 1932114;
            case 93:
                return 1932115;
            case 94:
                return 1932116;
            case 95:
                return 1932117;
            case 96:
                return 1932118;
            case 97:
                return 1932119;
            case 98:
                return 1932120;
            case 99:
                return 1932121;
            case 100:
                return 1932122;
            case 101:
                return 1932123;
            case 102:
                return 1932124;
            case 103:
                return 1932126;
            case 104:
                return 1932127;
            case 105:
                return 1932128;
            case 106:
                return 1932129;
            case 107:
                return 1932130;
            case 108:
                return 1932131;
            case 109:
                return 1932132;
            case 110:
                return 1932133;
            case 111:
                return 1932134;
            case 112:
                return 1932135;
            case 113:
                return 1932136;
            case 114:
                return 1932137;
            case 115:
                return 1932138;
            case 116:
                return 1932139;
            case 117:
                return 1932140;
            case 118:
                return 1932141;
            case 119:
                return 1932142;
            case 120:
                return 1932143;
            case 121:
                return 1932144;
            case 122:
                return 1932145;
            case 123:
                return 1932146;
            case 124:
                return 1932147;
            case 125:
                return 1932148;
            case 126:
                return 1932149;
            case 127:
                return 1932150;
            case 128:
                return 1932151;
            case 129:
                return 1932152;
            case 130:
                return 1932153;
            case 131:
                return 1932154;
            case 132:
                return 1932155;
            case 133:
                return 1932156;
            case 134:
                return 1932157;
            case 135:
                return 1932158;
            case 136:
                return 1932159;
            case 137:
                return 1932002;
            case 138:
                return 1932161;
            case 139:
                return 1932162;
            case 140:
                return 1932163;
            case 141:
                return 1932164;
            case 142:
                return 1932165;
            case 143:
                return 1932166;
            case 144:
                return 1932167;
            case 145:
                return 1932168;
            case 146:
                return 1932169;
            case 147:
                return 1932170;
            case 148:
                return 1932171;
            case 149:
                return 1932172;
            case 150:
                return 1932173;
            case 151:
                return 1932174;
            case 152:
                return 1932175;
            case 153:
                return 1932176;
            case 154:
                return 1932177;
            case 155:
                return 1932178;
            case 156:
                return 1932179;
            case 157:
                return 1932180;
            case 158:
                return 1932181;
            case 159:
                return 1932182;
            case 160:
                return 1932183;
            case 161:
                return 1932184;
            case 162:
                return 1932185;
            case 163:
                return 1932186;
            case 164:
                return 1932187;
            case 165:
                return 1932188;
            case 166:
                return 1932189;
            case 167:
                return 1932190;
            case 168:
                return 1932191;
            case 169:
                return 1932192;
            case 170:
                return 1932193;
            case 171:
                return 1932194;
            case 172:
                return 1932195;
            case 173:
                return 1932196;
            case 174:
                return 1932197;
            case 175:
                return 1932198;
            case 176:
                return 1932199;
            case 177:
                return 1932200;
            case 178:
                return 1932201;
            case 179:
                return 1932202;
            case 180:
                return 1932203;
            case 181:
                return 1932204;
            case 182:
                return 1932205;
            case 183:
                return 1932206;
            case 184:
                return 1932207;
            case 185:
                return 1932208;
            case 186:
                return 1932211;
            case 187:
                return 1932212;
            case 188:
                return 1932213;
            case 189:
                return 1932214;
            case 190:
                return 1932215;
            case 191:
                return 1932216;
            case 192:
                return 1932217;
            case 193:
                return 1932218;
            case 194:
                return 1932219;
            case 195:
                return 1932220;
            case 196:
                return 1932221;
            case 197:
                return 1932222;
            case 198:
                return 1932223;
            case 199:
                return 1932224;
            case 200:
                return 1932225;
            case 201:
                return 1932226;
            case 202:
                return 1932227;
            case 203:
                return 1932228;
            case 204:
                return 1932230;
            case 205:
                return 1932231;
            case 206:
                return 1932232;
            case 207:
                return 1932234;
            case 208:
                return 1932235;
            case 209:
                return 1932236;
            case 210:
                return 1932237;
            case 211:
                return 1932238;
            case 212:
                return 1932239;
            case 213:
                return 1932240;
            case 214:
                return 1932241;
            case 215:
                return 1932242;
            case 216:
                return 1932243;
            case 217:
                return 1932244;
            case 218:
                return 1932245;
            case 219:
                return 1932246;
            case 220:
                return 1932247;
            case 221:
                return 1932248;
            case 222:
                return 1932249;
            case 223:
                return 1932250;
            case 224:
                return 1932251;
            case 225:
                return 1932252;
            case 226:
                return 1932253;
            case 227:
                return 1932254;
            case 228:
                return 1932255;
            case 229:
                return 1932256;
            case 230:
                return 1932258;
            case 231:
                return 1932259;
            case 232:
                return 1932260;
            case 233:
                return 1932261;
            case 234:
                return 1932262;
            case 235:
                return 1932263;
            case 236:
                return 1932264;
            case 237:
                return 1932265;
            case 238:
                return 1932266;
            case 239:
                return 1932267;
            case 240:
                return 1932268;
            case 241:
                return 1932269;
            case 242:
                return 1932270;
            case 243:
                return 1932271;
            case 244:
                return 1932272;
            case 245:
                return 1932273;
            case 246:
                return 1932274;
            case 247:
                return 1932275;
            case 248:
                return 1932276;
            case 249:
                return 1932277;
            case 250:
                return 1932279;
            case 251:
                return 1932280;
            case 252:
                return 1932281;
            case 253:
                return 1932282;
            case 254:
                return 1932286;
            case 255:
                return 1932287;
            case 256:
                return 1932288;
            case 257:
                return 1932289;
            case 258:
                return 1932290;
            case 259:
                return 1932291;
            case 260:
                return 1932292;
            case 261:
                return 1932293;
            case 262:
                return 1932294;
            case 263:
                return 1932295;
            case 264:
                return 1932296;
            case 265:
                return 1932297;
            case 266:
                return 1932298;
            case 267:
                return 1932299;
            case 268:
                return 1932300;
            case 269:
                return 1932301;
            case 270:
                return 1932302;
            case 271:
                return 1932303;
            case 272:
                return 1932304;
            case 273:
                return 1932305;
            case 274:
                return 1932306;
            case 275:
                return 1932307;
            case 276:
                return 1932308;
            case 277:
                return 1932310;
            case 278:
                return 1932311;
            case 279:
                return 1932313;
            case 280:
                return 1932314;
            case 281:
                return 1932315;
            case 282:
                return 1932316;
            case 283:
                return 1932317;
            case 284:
                return 1932318;
            case 285:
                return 1932319;
            case 286:
                return 1932320;
            case 287:
                return 1932321;
            case 288:
                return 1932322;
            case 289:
                return 1932323;
            case 290:
                return 1932324;
            case 291:
                return 1932325;
            case 292:
                return 1932326;
            case 293:
                return 1932327;
            case 294:
                return 1932329;
            case 295:
                return 1932330;
            case 296:
                return 1932332;
            case 297:
                return 1932334;
            case 298:
                return 1932335;
            case 299:
                return 1932336;
            case 300:
                return 1932337;
            case 301:
                return 1932338;
            case 302:
                return 1932339;
            case 303:
                return 1932341;
            case 304:
                return 1932342;
            case 305:
                return 1932350;
            case 306:
                return 1932351;
            case 307:
                return 1932352;
            case 308:
                return 1932353;
            case 309:
                return 1932355;
            case 310:
                return 1932366;
            case 311:
                return 1939000;
            case 312:
                return 1939001;
            case 313:
                return 1939002;
            case 314:
                return 1939003;
            case 315:
                return 1939004;
            case 316:
                return 1939005;
            case 317:
                return 1932374;
            case 318:
                return 1932376;
            case 319:
                return 1932378;
            case 320:
                return 1939006;
            default:
                return 1930001;
        }

    }

    public static boolean is物理武器(int itemId) {
        if (itemId == 1342069) {
            return false;
        }
        return ((itemId >= 1300000) && (itemId < 1540000)) || (itemId / 1000 == 1212) || (itemId / 1000 == 1222) || (itemId / 1000 == 1232) || (itemId / 1000 == 1242) || (itemId / 1000 == 1252);
    }
}
