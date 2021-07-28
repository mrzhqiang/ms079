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
package client;

import constants.GameConstants;
import client.inventory.MapleInventoryType;
import client.inventory.IItem;
import client.inventory.Equip;
import client.inventory.IEquip;
import client.inventory.MapleWeaponType;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleStatEffect;
import server.StructPotentialItem;
import server.StructSetItem;
import server.StructSetItem.SetItem;
import tools.MaplePacketCreator;
import tools.data.output.MaplePacketLittleEndianWriter;

public class PlayerStats implements Serializable {

    private static final long serialVersionUID = -679541993413738569L;
    private transient WeakReference<MapleCharacter> chr;
    private Map<Integer, Integer> setHandling = new HashMap<Integer, Integer>();
    private List<Equip> durabilityHandling = new ArrayList<Equip>(), equipLevelHandling = new ArrayList<Equip>();
    private transient float shouldHealHP, shouldHealMP;
    public short str, dex, luk, int_, hp, maxhp, mp, maxmp;
    private transient short passive_sharpeye_percent, localmaxhp, localmaxmp;
    private transient byte passive_mastery, passive_sharpeye_rate;
    private transient int localstr, localdex, localluk, localint_;
    private transient int magic, watk, hands, accuracy;
    public transient boolean equippedWelcomeBackRing, equippedFairy, hasMeso, hasItem, hasVac, hasClone, hasPartyBonus, Berserk = false, isRecalc = false;
    public transient int equipmentBonusExp, expMod, dropMod, cashMod, levelBonus;
    public transient double expBuff, dropBuff, mesoBuff, cashBuff;
    //restore/recovery are separate variables because i dont know jack shit what it even does
    //same with incMesoProp/incRewardProp for now
    public transient double dam_r, bossdam_r;
    public transient int recoverHP, recoverMP, mpconReduce, incMesoProp, incRewardProp, DAMreflect, DAMreflect_rate, mpRestore,
            hpRecover, hpRecoverProp, mpRecover, mpRecoverProp, RecoveryUP, incAllskill;
    ;
    private transient float speedMod, jumpMod, localmaxbasedamage;
    // Elemental properties
    public transient int def, element_ice, element_fire, element_light, element_psn;
    public ReentrantLock lock = new ReentrantLock(); //we're getting concurrentmodificationexceptions, but would this slow things down?
    public int dot, dotTime, damX;
    private final Map<Integer, Integer> damageIncrease = new HashMap<>();

    public PlayerStats(final MapleCharacter chr) {
        // TODO, move str/dex/int etc here -_-
        this.chr = new WeakReference<MapleCharacter>(chr);
    }

    //POTENTIALS:
    //incMesoProp, incRewardProp
    public final void init() {
        recalcLocalStats();
        relocHeal();
    }

    public final short getStr() {
        return str;
    }

    public final short getDex() {
        return dex;
    }

    public final short getLuk() {
        return luk;
    }

    public final short getInt() {
        return int_;
    }

    public final void setStr(final short str) {
        this.str = str;
        recalcLocalStats();
    }

    public final void setDex(final short dex) {
        this.dex = dex;
        recalcLocalStats();
    }

    public final void setLuk(final short luk) {
        this.luk = luk;
        recalcLocalStats();
    }

    public final void setInt(final short int_) {
        this.int_ = int_;
        recalcLocalStats();
    }

    public final boolean setHp(final int newhp) {
        return setHp(newhp, false);
    }

    public final boolean setHp(int newhp, boolean silent) {
        final short oldHp = hp;
        int thp = newhp;
        if (thp < 0) {
            thp = 0;
        }
        if (thp > localmaxhp) {
            thp = localmaxhp;
        }
        this.hp = (short) thp;

        final MapleCharacter chra = chr.get();
        if (chra != null) {
            if (!silent) {
                chra.updatePartyMemberHP();
            }
            if (oldHp > hp && !chra.isAlive()) {
                chra.playerDead();
            }
        }
        return hp != oldHp;
    }

    public final boolean setMp(final int newmp) {
        final short oldMp = mp;
        int tmp = newmp;
        if (tmp < 0) {
            tmp = 0;
        }
        if (tmp > localmaxmp) {
            tmp = localmaxmp;
        }
        this.mp = (short) tmp;
        return mp != oldMp;
    }

    public final void setMaxHp(final short hp) {
        this.maxhp = hp;
        recalcLocalStats();
    }

    public final void setMaxMp(final short mp) {
        this.maxmp = mp;
        recalcLocalStats();
    }

    public final short getHp() {
        return hp;
    }

    public final short getMaxHp() {
        return maxhp;
    }

    public final short getMp() {
        return mp;
    }

    public final short getMaxMp() {
        return maxmp;
    }

    public final int getTotalDex() {
        return localdex;
    }

    public final int getTotalInt() {
        return localint_;
    }

    public final int getTotalStr() {
        return localstr;
    }

    public final int getTotalLuk() {
        return localluk;
    }

    public final int getTotalMagic() {
        return magic;
    }

    public final double getSpeedMod() {
        return speedMod;
    }

    public final double getJumpMod() {
        return jumpMod;
    }

    public final int getTotalWatk() {
        return watk;
    }

    public final short getCurrentMaxHp() {
        return localmaxhp;
    }

    public final short getCurrentMaxMp() {
        return localmaxmp;
    }

    public final int getHands() {
        return hands;
    }

    public final float getCurrentMaxBaseDamage() {
        return localmaxbasedamage;
    }

    public void recalcLocalStats() {
        recalcLocalStats(false);
    }

    public void recalcLocalStats(boolean first_login) {
        final MapleCharacter chra = chr.get();
        if (chra == null) {
            return;
        }
        lock.lock();
        try {
            if (isRecalc) {
                return;
            }
            isRecalc = true;
        } finally {
            lock.unlock();
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        int oldmaxhp = localmaxhp;
        int localmaxhp_ = getMaxHp();
        int localmaxmp_ = getMaxMp();
        localdex = getDex();
        localint_ = getInt();
        localstr = getStr();
        localluk = getLuk();
        int speed = 100;
        int jump = 100;
        int percent_hp = 0, percent_mp = 0, percent_str = 0, percent_dex = 0, percent_int = 0, percent_luk = 0, percent_acc = 0, percent_atk = 0, percent_matk = 0;
        int added_sharpeye_rate = 0, added_sharpeye_dmg = 0;
        magic = localint_;
        watk = 0;
        if (chra.getJob() == 500 || (chra.getJob() >= 520 && chra.getJob() <= 522)) {
            watk = 20; //bullet
        } else if (chra.getJob() == 400 || (chra.getJob() >= 410 && chra.getJob() <= 412) || (chra.getJob() >= 1400 && chra.getJob() <= 1412)) {
            watk = 30; //stars
        }
        StructPotentialItem pot;
        dam_r = 0.0;
        damX = 0;
        bossdam_r = 0.0;
        expBuff = 100.0;
        cashBuff = 100.0;
        dropBuff = 100.0;
        mesoBuff = 100.0;
        recoverHP = 0;
        recoverMP = 0;
        mpconReduce = 0;
        incMesoProp = 0;
        incRewardProp = 0;
        DAMreflect = 0;
        DAMreflect_rate = 0;
        hpRecover = 0;
        hpRecoverProp = 0;
        mpRecover = 0;
        mpRecoverProp = 0;
        mpRestore = 0;
        equippedWelcomeBackRing = false;
        equippedFairy = false;
        hasMeso = false;
        hasItem = false;
        hasPartyBonus = false;
        hasVac = false;
        hasClone = false;
        final boolean canEquipLevel = chra.getLevel() >= 120 && !GameConstants.isKOC(chra.getJob());
        equipmentBonusExp = 0;
        RecoveryUP = 0;
        dropMod = 1;
        expMod = 1;
        cashMod = 1;
        levelBonus = 0;
        incAllskill = 0;
        durabilityHandling.clear();
        equipLevelHandling.clear();
        setHandling.clear();
        element_fire = 100;
        element_ice = 100;
        element_light = 100;
        element_psn = 100;
        def = 100;
        dot = 0;
        dotTime = 0;
        damageIncrease.clear();
        for (IItem item : chra.getInventory(MapleInventoryType.EQUIPPED)) {
            final IEquip equip = (IEquip) item;

            if (equip.getPosition() == -11) {
                if (GameConstants.isMagicWeapon(equip.getItemId())) {
                    final Map<String, Integer> eqstat = MapleItemInformationProvider.getInstance().getEquipStats(equip.getItemId());

                    element_fire = eqstat.get("incRMAF");
                    element_ice = eqstat.get("incRMAI");
                    element_light = eqstat.get("incRMAL");
                    element_psn = eqstat.get("incRMAS");
                    def = eqstat.get("elemDefault");
                }
            }
            accuracy += equip.getAcc();
            localmaxhp_ += equip.getHp();
            localmaxmp_ += equip.getMp();
            localdex += equip.getDex();
            localint_ += equip.getInt();
            localstr += equip.getStr();
            localluk += equip.getLuk();
            magic += equip.getMatk() + equip.getInt();
            watk += equip.getWatk();
            speed += equip.getSpeed();
            jump += equip.getJump();

            switch (equip.getItemId()) {
                case 1112427: //cruel, gives crit + OHKO
                    added_sharpeye_rate += 5;
                    added_sharpeye_dmg += 20;
                    break;
                case 1112428: //critical, gives crit + OHKO
                    added_sharpeye_rate += 10;
                    added_sharpeye_dmg += 10;
                    break;
                case 1112429: //magical, gives crit + STUN
                    added_sharpeye_rate += 5;
                    added_sharpeye_dmg += 20;
                    break;
                case 1112127:
                    equippedWelcomeBackRing = true;
                    break;
                case 1122017:
                    equippedFairy = true;
                    break;
                case 1812000:
                    hasMeso = true;
                    break;
                case 1812001:
                    hasItem = true;
                    break;
                default:
                    for (int eb_bonus : GameConstants.Equipments_Bonus) {
                        if (equip.getItemId() == eb_bonus) {
                            equipmentBonusExp += GameConstants.Equipment_Bonus_EXP(eb_bonus);
                            break;
                        }
                    }
                    break;
            } //slow, poison, darkness, seal, freeze
            percent_hp += equip.getHpR();
            percent_mp += equip.getMpR();
            int set = ii.getSetItemID(equip.getItemId());
            if (set > 0) {
                int value = 1;
                if (setHandling.get(set) != null) {
                    value += setHandling.get(set);
                }
                setHandling.put(set, value); //id of Set, number of items to go with the set
            }
            if (equip.getState() > 1) {
                int[] potentials = {equip.getPotential1(), equip.getPotential2(), equip.getPotential3()};
                for (int i : potentials) {
                    if (i > 0) {
                        pot = ii.getPotentialInfo(i).get(ii.getReqLevel(equip.getItemId()) / 10);
                        if (pot != null) {
                            localstr += pot.incSTR;
                            localdex += pot.incDEX;
                            localint_ += pot.incINT;
                            localluk += pot.incLUK;
                            localmaxhp += pot.incMHP;
                            localmaxmp += pot.incMMP;
                            watk += pot.incPAD;
                            magic += pot.incINT + pot.incMAD;
                            speed += pot.incSpeed;
                            jump += pot.incJump;
                            accuracy += pot.incACC;
                            incAllskill += pot.incAllskill;
                            percent_hp += pot.incMHPr;
                            percent_mp += pot.incMMPr;
                            percent_str += pot.incSTRr;
                            percent_dex += pot.incDEXr;
                            percent_int += pot.incINTr;
                            percent_luk += pot.incLUKr;
                            percent_acc += pot.incACCr;
                            percent_atk += pot.incPADr;
                            percent_matk += pot.incMADr;
                            added_sharpeye_rate += pot.incCr;
                            added_sharpeye_dmg += pot.incCr;
                            if (!pot.boss) {
                                dam_r = (double) Math.max(pot.incDAMr, dam_r);
                            } else {
                                bossdam_r = (double) Math.max(pot.incDAMr, bossdam_r); //SET, not add
                            }
                            recoverHP += pot.RecoveryHP;
                            recoverMP += pot.RecoveryMP;
                            RecoveryUP += pot.RecoveryUP;
                            if (pot.HP > 0) {
                                hpRecover += pot.HP;
                                hpRecoverProp += pot.prop;
                            }
                            if (pot.MP > 0) {
                                mpRecover += pot.MP;
                                mpRecoverProp += pot.prop;
                            }
                            mpconReduce += pot.mpconReduce;
                            incMesoProp += pot.incMesoProp;
                            incRewardProp += pot.incRewardProp;
                            if (pot.DAMreflect > 0) {
                                DAMreflect += pot.DAMreflect;
                                DAMreflect_rate += pot.prop;
                            }
                            mpRestore += pot.mpRestore;
                            if (!first_login && pot.skillID > 0) {
                                chra.changeSkillLevel_Skip(SkillFactory.getSkill(getSkillByJob(pot.skillID, chra.getJob())), (byte) 1, (byte) 1);
                            }
                        }
                    }
                }
                if (equip.getDurability() > 0) {
                    durabilityHandling.add((Equip) equip);
                }
                if (canEquipLevel && GameConstants.getMaxLevel(equip.getItemId()) > 0 && (GameConstants.getStatFromWeapon(equip.getItemId()) == null ? (equip.getEquipLevels() <= GameConstants.getMaxLevel(equip.getItemId())) : (equip.getEquipLevels() < GameConstants.getMaxLevel(equip.getItemId())))) {
                    equipLevelHandling.add((Equip) equip);
                }
            }
        }
        final Iterator<Entry<Integer, Integer>> iter = setHandling.entrySet().iterator();
        while (iter.hasNext()) {
            final Entry<Integer, Integer> entry = iter.next();
            final StructSetItem set = ii.getSetItem(entry.getKey());
            if (set != null) {
                final Map<Integer, SetItem> itemz = set.getItems();
                for (Entry<Integer, SetItem> ent : itemz.entrySet()) {
                    if (ent.getKey() <= entry.getValue()) {
                        SetItem se = ent.getValue();
                        localstr += se.incSTR;
                        localdex += se.incDEX;
                        localint_ += se.incINT;
                        localluk += se.incLUK;
                        watk += se.incPAD;
                        magic += se.incINT + se.incMAD;
                        speed += se.incSpeed;
                        accuracy += se.incACC;
                        localmaxhp_ += se.incMHP;
                        localmaxmp_ += se.incMMP;
                    }
                }
            }
        }

        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        for (IItem item : chra.getInventory(MapleInventoryType.CASH)) {
            if (expMod < 3 && (item.getItemId() == 5211060 || item.getItemId() == 5211050 || item.getItemId() == 5211051 || item.getItemId() == 5211052 || item.getItemId() == 5211053 || item.getItemId() == 5211054)) {
                expMod = 3;//overwrite
            } else if (expMod == 1 && (item.getItemId() == 5210001 || item.getItemId() == 5210004 || item.getItemId() == 5211061 || item.getItemId() == 5211000 || item.getItemId() == 5211001 || item.getItemId() == 5211002 || item.getItemId() == 5211003 || item.getItemId() == 5211046 || item.getItemId() == 5211047 || item.getItemId() == 5211048 || item.getItemId() == 5211049)) {
                expMod = 2;
            } else if (expMod == 1 && (item.getItemId() == 5210005 || item.getItemId() == 5210000)) {
                if (day >= 2 && day <= 6) {
                    if (hour >= 18 || hour < 6) {
                        expMod = 2;
                    }
                } else if (day == 1 || day == 7) {
                    expMod = 2;
                }
            } else if (expMod == 1 && (item.getItemId() == 5210003 || item.getItemId() == 5210002)) {
                if (day >= 2 && day <= 6) {
                    if (hour < 18 && hour >= 6) {
                        expMod = 2;
                    }
                } else if (day == 1 || day == 7) {
                    expMod = 2;
                }
            } else if (expMod == 1 && item.getItemId() == 5210006 && (hour >= 22 || hour <= 2)) {
                expMod = 2;
            } else if (expMod == 1 && item.getItemId() == 5210007 && hour >= 2 && hour <= 6) {
                expMod = 2;
            } else if (expMod == 1 && item.getItemId() == 5210008 && hour >= 6 && hour <= 10) {
                expMod = 2;
            } else if (expMod == 1 && item.getItemId() == 5210009 && hour >= 10 && hour <= 14) {
                expMod = 2;
            } else if (expMod == 1 && item.getItemId() == 5210010 && hour >= 14 && hour <= 18) {
                expMod = 2;
            } else if (expMod == 1 && item.getItemId() == 5210011 && hour >= 18 && hour <= 22) {
                expMod = 2;
            }
            if (dropMod == 1) {
                if (item.getItemId() == 5360009 || item.getItemId() == 5360010 || item.getItemId() == 5360011 || item.getItemId() == 5360012 || item.getItemId() == 5360013 || item.getItemId() == 5360014 || item.getItemId() == 5360017 || item.getItemId() == 5360050 || item.getItemId() == 5360053 || item.getItemId() == 5360042 || item.getItemId() == 5360052 || item.getItemId() == 5360016 || item.getItemId() == 5360015) {
                    dropMod = 2;
                } else if (item.getItemId() == 5360000 && hour >= 0 && hour <= 6) {
                    dropMod = 2;
                } else if (item.getItemId() == 5360001 && hour >= 6 && hour <= 12) {
                    dropMod = 2;
                } else if (item.getItemId() == 5360002 && hour >= 12 && hour <= 18) {
                    dropMod = 2;
                } else if (item.getItemId() == 5360003 && hour >= 18 && hour <= 24) {
                    dropMod = 2;
                }
            }
            if (item.getItemId() == 5650000) {
                hasPartyBonus = true;
            } else if (item.getItemId() == 5590001) {
                levelBonus = 10;
            } else if (levelBonus == 0 && item.getItemId() == 5590000) {
                levelBonus = 5;
            }
        }
        for (IItem item : chra.getInventory(MapleInventoryType.ETC)) { //omfg;
            switch (item.getItemId()) {
                case 4030003:
                    hasVac = true;
                    break;
                case 4030004:
                    hasClone = true;
                    break;
                case 4030005:
                    cashMod = 2;
                    break;
            }
        }
        magic += chra.getSkillLevel(SkillFactory.getSkill(22000000));
        //dam_r += (chra.getJob() >= 430 && chra.getJob() <= 434 ? 70 : 0); //leniency on upper stab
        this.localstr += (percent_str * localstr) / 100f;
        this.localdex += (percent_dex * localdex) / 100f;
        final int before_ = localint_;
        this.localint_ += (percent_int * localint_) / 100f;
        this.magic += localint_ - before_;
        this.localluk += (percent_luk * localluk) / 100f;
        this.accuracy += (percent_acc * accuracy) / 100f;
        this.watk += (percent_atk * watk) / 100f;
        this.magic += (percent_matk * magic) / 100f; //or should this go before
        localmaxhp_ += (percent_hp * localmaxhp_) / 100f;
        localmaxmp_ += (percent_mp * localmaxmp_) / 100f;
        magic = Math.min(magic, 1999); //buffs can make it higher

        Integer buff = chra.getBuffedValue(MapleBuffStat.MAPLE_WARRIOR);
        if (buff != null) {
            final double d = buff.doubleValue() / 100.0;
            localstr += d * str; //base only
            localdex += d * dex;
            localluk += d * luk;

            final int before = localint_;
            localint_ += d * int_;
            magic += localint_ - before;
        }
        buff = chra.getBuffedValue(MapleBuffStat.ECHO_OF_HERO);
        if (buff != null) {
            final double d = buff.doubleValue() / 100.0;
            watk += (int) (watk * d);
            magic += (int) (magic * d);
        }
        buff = chra.getBuffedValue(MapleBuffStat.ARAN_COMBO);
        if (buff != null) {
            watk += buff.intValue() / 10;
        }
        buff = chra.getBuffedValue(MapleBuffStat.MAXHP);
        if (buff != null) {
            localmaxhp_ += (buff.doubleValue() / 100.0) * localmaxhp_;
        }
        buff = chra.getBuffedValue(MapleBuffStat.CONVERSION);
        if (buff != null) {
            localmaxhp_ += (buff.doubleValue() / 100.0) * localmaxhp_;
        }
        buff = chra.getBuffedValue(MapleBuffStat.MAXMP);
        if (buff != null) {
            localmaxmp_ += (buff.doubleValue() / 100.0) * localmaxmp_;
        }
        buff = chra.getBuffedValue(MapleBuffStat.MP_BUFF);
        if (buff != null) {
            localmaxmp_ += (buff.doubleValue() / 100.0) * localmaxmp_;
        }
        buff = chra.getBuffedValue(MapleBuffStat.ENHANCED_MAXHP);
        if (buff != null) {
            localmaxhp_ += buff.intValue();
        }
        buff = chra.getBuffedValue(MapleBuffStat.ENHANCED_MAXMP);
        if (buff != null) {
            localmaxmp_ += buff.intValue();
        }
        switch (chra.getJob()) {
            case 322: { // Crossbowman
                final ISkill expert = SkillFactory.getSkill(3220004);
                final int boostLevel = chra.getSkillLevel(expert);
                if (boostLevel > 0) {
                    watk += expert.getEffect(boostLevel).getX();
                }
                break;
            }
            case 312: { // Bowmaster
                final ISkill expert = SkillFactory.getSkill(3120005);
                final int boostLevel = chra.getSkillLevel(expert);
                if (boostLevel > 0) {
                    watk += expert.getEffect(boostLevel).getX();
                }
                break;
            }
            case 211:
            case 212: { // IL
                final ISkill amp = SkillFactory.getSkill(2110001);
                final int level = chra.getSkillLevel(amp);
                if (level > 0) {
                    dam_r *= amp.getEffect(level).getY() / 100.0;
                    bossdam_r *= amp.getEffect(level).getY() / 100.0;
                }
                break;
            }
            case 221:
            case 222: { // IL
                final ISkill amp = SkillFactory.getSkill(2210001);
                final int level = chra.getSkillLevel(amp);
                if (level > 0) {
                    dam_r *= amp.getEffect(level).getY() / 100.0;
                    bossdam_r *= amp.getEffect(level).getY() / 100.0;
                }
                break;
            }
            case 1211:
            case 1212: { // flame
                final ISkill amp = SkillFactory.getSkill(12110001);
                final int level = chra.getSkillLevel(amp);
                if (level > 0) {
                    dam_r *= amp.getEffect(level).getY() / 100.0;
                    bossdam_r *= amp.getEffect(level).getY() / 100.0;
                }
                break;
            }
            case 2215:
            case 2216:
            case 2217:
            case 2218: {
                final ISkill amp = SkillFactory.getSkill(22150000);
                final int level = chra.getSkillLevel(amp);
                if (level > 0) {
                    dam_r *= amp.getEffect(level).getY() / 100.0;
                    bossdam_r *= amp.getEffect(level).getY() / 100.0;
                }
                break;
            }
            case 2112: { // Aran
                final ISkill expert = SkillFactory.getSkill(21120001);
                final int boostLevel = chra.getSkillLevel(expert);
                if (boostLevel > 0) {
                    watk += expert.getEffect(boostLevel).getX();
                }
                break;
            }
        }
        final ISkill blessoffairy = SkillFactory.getSkill(GameConstants.getBOF_ForJob(chra.getJob()));
        final int boflevel = chra.getSkillLevel(blessoffairy);
        if (boflevel > 0) {
            watk += blessoffairy.getEffect(boflevel).getX();
            magic += blessoffairy.getEffect(boflevel).getY();
            accuracy += blessoffairy.getEffect(boflevel).getX();
        }
        buff = chra.getBuffedValue(MapleBuffStat.EXPRATE);
        if (buff != null) {
            if (chra.getBuffSource(MapleBuffStat.EXPRATE) == 0) {
                switch (chra.getMapId()) {

                }
            } else {
                expBuff *= buff.doubleValue() / 100.0;
            }
        }
        buff = chra.getBuffedValue(MapleBuffStat.DROP_RATE);
        if (buff != null) {
            if (chra.getBuffSource(MapleBuffStat.DROP_RATE) == 0) {
                switch (chra.getMapId()) {

                }
            } else {
                dropBuff *= buff.doubleValue() / 100.0;
            }
        }
        buff = chra.getBuffedValue(MapleBuffStat.ACASH_RATE);
        if (buff != null) {
            cashBuff *= buff.doubleValue() / 100.0;
        }
        buff = chra.getBuffedValue(MapleBuffStat.MESO_RATE);
        if (buff != null) {
            mesoBuff *= buff.doubleValue() / 100.0;
        }
        buff = chra.getBuffedValue(MapleBuffStat.MESOUP);
        if (buff != null) {
            mesoBuff *= buff.doubleValue() / 100.0;
        }
        buff = chra.getBuffedValue(MapleBuffStat.ACC);
        if (buff != null) {
            accuracy += buff.intValue();
        }
        buff = chra.getBuffedValue(MapleBuffStat.WATK);
        if (buff != null) {
            watk += buff.intValue();
        }
        buff = chra.getBuffedValue(MapleBuffStat.ENHANCED_WATK);
        if (buff != null) {
            watk += buff.intValue();
        }
        buff = chra.getBuffedValue(MapleBuffStat.MATK);
        if (buff != null) {
            magic += buff.intValue();
        }
        buff = chra.getBuffedValue(MapleBuffStat.速度);
        if (buff != null) {
            speed += buff.intValue();
        }
        buff = chra.getBuffedValue(MapleBuffStat.跳跃);
        if (buff != null) {
            jump += buff.intValue();
        }
        buff = chra.getBuffedValue(MapleBuffStat.疾驰移动);
        if (buff != null) {
            speed += buff.intValue();
        }
        buff = chra.getBuffedValue(MapleBuffStat.疾驰跳跃);
        if (buff != null) {
            jump += buff.intValue();
        }
        buff = chra.getBuffedValue(MapleBuffStat.DAMAGE_BUFF);
        if (buff != null) {
            dam_r += buff.doubleValue();
            bossdam_r += buff.doubleValue();
        }
        buff = chra.getBuffedSkill_Y(MapleBuffStat.FINAL_CUT);
        if (buff != null) {
            dam_r *= buff.doubleValue() / 100.0;
            bossdam_r *= buff.doubleValue() / 100.0;
        }
        buff = chra.getBuffedSkill_Y(MapleBuffStat.OWL_SPIRIT);
        if (buff != null) {
            dam_r *= buff.doubleValue() / 100.0;
            bossdam_r *= buff.doubleValue() / 100.0;
        }
        buff = chra.getBuffedValue(MapleBuffStat.BERSERK_FURY);
        if (buff != null) {
            dam_r *= 2.0;
            bossdam_r *= 2.0;
        }
        final ISkill bx = SkillFactory.getSkill(1320006);
        if (chra.getSkillLevel(bx) > 0) {
            dam_r *= bx.getEffect(chra.getSkillLevel(bx)).getDamage() / 100.0;
            bossdam_r *= bx.getEffect(chra.getSkillLevel(bx)).getDamage() / 100.0;
        }
        buff = chra.getBuffedValue(MapleBuffStat.PYRAMID_PQ);
        if (buff != null) {
            final MapleStatEffect eff = chra.getStatForBuff(MapleBuffStat.PYRAMID_PQ);
            dam_r *= eff.getBerserk() / 100.0;
            bossdam_r *= eff.getBerserk() / 100.0;
        }
        buff = chra.getBuffedValue(MapleBuffStat.属性攻击);
        if (buff != null) {
            final MapleStatEffect eff = chra.getStatForBuff(MapleBuffStat.属性攻击);
            dam_r *= eff.getDamage() / 100.0;
            bossdam_r *= eff.getDamage() / 100.0;
        }
        buff = chra.getBuffedValue(MapleBuffStat.LIGHTNING_CHARGE);
        if (buff != null) {
            final MapleStatEffect eff = chra.getStatForBuff(MapleBuffStat.LIGHTNING_CHARGE);
            dam_r *= eff.getDamage() / 100.0;
            bossdam_r *= eff.getDamage() / 100.0;
        }
        buff = chra.getBuffedSkill_X(MapleBuffStat.THORNS);
        if (buff != null) {
            added_sharpeye_rate += buff.intValue();
        }
        buff = chra.getBuffedSkill_Y(MapleBuffStat.THORNS);
        if (buff != null) {
            added_sharpeye_dmg += buff.intValue() - 100;
        }
        buff = chra.getBuffedSkill_X(MapleBuffStat.SHARP_EYES);
        if (buff != null) {
            added_sharpeye_rate += buff.intValue();
        }
        buff = chra.getBuffedSkill_Y(MapleBuffStat.SHARP_EYES);
        if (buff != null) {
            added_sharpeye_dmg += buff.intValue() - 100;
        }
        buff = chra.getBuffedValue(MapleBuffStat.CRITICAL_RATE_BUFF);
        if (buff != null) {
            added_sharpeye_rate += buff.intValue();
        }
        if (speed > 140) {
            speed = 140;
        }
        if (jump > 123) {
            jump = 123;
        }
        speedMod = speed / 100.0f;
        jumpMod = jump / 100.0f;
        Integer mount = chra.getBuffedValue(MapleBuffStat.MANA_REFLECTION);
        if (mount != null) {
            jumpMod = 1.23f;
            switch (mount.intValue()) {
                case 1:
                    speedMod = 1.5f;
                    break;
                case 2:
                    speedMod = 1.7f;
                    break;
                case 3:
                    speedMod = 1.8f;
                    break;
                default:
                    System.err.println("Unhandeled monster riding level, Speedmod = " + speedMod + "");
            }
        }
        hands = this.localdex + this.localint_ + this.localluk;

        localmaxhp = (short) Math.min(30000, Math.abs(Math.max(-30000, localmaxhp_)));
        localmaxmp = (short) Math.min(30000, Math.abs(Math.max(-30000, localmaxmp_)));

        CalcPassive_SharpEye(chra, added_sharpeye_rate, added_sharpeye_dmg);
        CalcPassive_Mastery(chra);
        if (first_login) {
            chra.silentEnforceMaxHpMp();
        } else {
            chra.enforceMaxHpMp();
        }

        localmaxbasedamage = calculateMaxBaseDamage(watk);
        if (oldmaxhp != 0 && oldmaxhp != localmaxhp) {
            chra.updatePartyMemberHP();
        }

        lock.lock();
        try {
            isRecalc = false;
        } finally {
            lock.unlock();
        }
    }

    public boolean checkEquipLevels(final MapleCharacter chr, int gain) {
        boolean changed = false;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        List<Equip> all = new ArrayList<Equip>(equipLevelHandling);
        for (Equip eq : all) {
            int lvlz = eq.getEquipLevels();
            eq.setItemEXP(eq.getItemEXP() + gain);

            if (eq.getEquipLevels() > lvlz) { //lvlup
                for (int i = eq.getEquipLevels() - lvlz; i > 0; i--) {
                    //now for the equipment increments...
                    final Map<Integer, Map<String, Integer>> inc = ii.getEquipIncrements(eq.getItemId());
                    if (inc != null && inc.containsKey(lvlz + i)) { //flair = 1
                        eq = ii.levelUpEquip(eq, inc.get(lvlz + i));
                    }
                    //UGH, skillz
                    if (GameConstants.getStatFromWeapon(eq.getItemId()) == null) {
                        final Map<Integer, List<Integer>> ins = ii.getEquipSkills(eq.getItemId());
                        if (ins != null && ins.containsKey(lvlz + i)) {
                            for (Integer z : ins.get(lvlz + i)) {
                                if (Math.random() < 0.1) { //10% chance dood
                                    final ISkill skil = SkillFactory.getSkill(z.intValue());
                                    if (skil != null && skil.canBeLearnedBy(chr.getJob()) && chr.getSkillLevel(skil) < chr.getMasterLevel(skil)) { //dont go over masterlevel :D
                                        chr.changeSkillLevel(skil, (byte) (chr.getSkillLevel(skil) + 1), chr.getMasterLevel(skil));
                                    }
                                }
                            }
                        }
                    }
                }
                changed = true;
            }
            chr.forceReAddItem(eq.copy(), MapleInventoryType.EQUIPPED);
        }
        if (changed) {
            chr.equipChanged();
            chr.getClient().getSession().write(MaplePacketCreator.showItemLevelupEffect());
            chr.getMap().broadcastMessage(chr, MaplePacketCreator.showForeignItemLevelupEffect(chr.getId()), false);
        }
        return changed;
    }

    public boolean checkEquipDurabilitys(final MapleCharacter chr, int gain) {
        for (Equip item : durabilityHandling) {
            item.setDurability(item.getDurability() + gain);
            if (item.getDurability() < 0) { //shouldnt be less than 0
                item.setDurability(0);
            }
        }
        List<Equip> all = new ArrayList<Equip>(durabilityHandling);
        for (Equip eqq : all) {
            if (eqq.getDurability() == 0) { //> 0 went to negative
                if (chr.getInventory(MapleInventoryType.EQUIP).isFull()) {
                    chr.getClient().getSession().write(MaplePacketCreator.getInventoryFull());
                    chr.getClient().getSession().write(MaplePacketCreator.getShowInventoryFull());
                    return false;
                }
                durabilityHandling.remove(eqq);
                final short pos = chr.getInventory(MapleInventoryType.EQUIP).getNextFreeSlot();
                MapleInventoryManipulator.unequip(chr.getClient(), eqq.getPosition(), pos);
                chr.getClient().getSession().write(MaplePacketCreator.updateSpecialItemUse(eqq, (byte) 1, pos));
            } else {
                chr.forceReAddItem(eqq.copy(), MapleInventoryType.EQUIPPED);
            }
        }
        return true;
    }

    private final void CalcPassive_Mastery(final MapleCharacter player) {
        if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11) == null) {
            passive_mastery = 0;
            return;
        }
        final int skil;
        switch (GameConstants.getWeaponType(player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11).getItemId())) {
            case BOW:
                skil = GameConstants.isKOC(player.getJob()) ? 13100000 : (GameConstants.isResist(player.getJob()) ? 33100000 : 3100000);
                break;
            case CLAW:
                skil = 4100000;
                break;
            case KATARA:
            case DAGGER:
                skil = player.getJob() >= 430 && player.getJob() <= 434 ? 4300000 : 4200000;
                break;
            case CROSSBOW:
                skil = 3200000;
                break;
            case AXE1H:
            case AXE2H:
                skil = 1100001;
                break;
            case SWORD1H:
            case SWORD2H:
                skil = GameConstants.isKOC(player.getJob()) ? 11100000 : (player.getJob() > 112 ? 1200000 : 1100000); //hero/pally
                break;
            case BLUNT1H:
            case BLUNT2H:
                skil = 1200001;
                break;
            case POLE_ARM:
                skil = GameConstants.isAran(player.getJob()) ? 21100000 : 1300001;
                break;
            case SPEAR:
                skil = 1300000;
                break;
            case KNUCKLE:
                skil = GameConstants.isKOC(player.getJob()) ? 15100001 : 5100001;
                break;
            case GUN:
                skil = GameConstants.isResist(player.getJob()) ? 35100000 : 5200000;
                break;
            case STAFF:
                skil = 32100006;
                break;
            default:
                passive_mastery = 0;
                return;

        }
        if (player.getSkillLevel(skil) <= 0) {
            passive_mastery = 0;
            return;
        }
        passive_mastery = (byte) ((player.getSkillLevel(skil) / 2) + (player.getSkillLevel(skil) % 2)); //after bb, simpler?
    }

    private final void CalcPassive_SharpEye(final MapleCharacter player, final int added_sharpeye_rate, final int added_sharpeye_dmg) {
        switch (player.getJob()) { // Apply passive Critical bonus
            case 410:
            case 411:
            case 412: { // Assasin/ Hermit / NL
                final ISkill critSkill = SkillFactory.getSkill(4100001);
                final int critlevel = player.getSkillLevel(critSkill);
                if (critlevel > 0) {
                    this.passive_sharpeye_percent = (short) (critSkill.getEffect(critlevel).getDamage() - 100 + added_sharpeye_dmg);
                    this.passive_sharpeye_rate = (byte) (critSkill.getEffect(critlevel).getProb() + added_sharpeye_rate);
                    return;
                }
                break;
            }
            case 1410:
            case 1411:
            case 1412: { // Night Walker
                final ISkill critSkill = SkillFactory.getSkill(14100001);
                final int critlevel = player.getSkillLevel(critSkill);
                if (critlevel > 0) {
                    this.passive_sharpeye_percent = (short) (critSkill.getEffect(critlevel).getDamage() - 100 + added_sharpeye_dmg);
                    this.passive_sharpeye_rate = (byte) (critSkill.getEffect(critlevel).getProb() + added_sharpeye_rate);
                    return;
                }
                break;
            }
            case 511:
            case 512: { // Buccaner, Viper
                final ISkill critSkill = SkillFactory.getSkill(5110000);
                final int critlevel = player.getSkillLevel(critSkill);
                if (critlevel > 0) {
                    this.passive_sharpeye_percent = (short) (critSkill.getEffect(critlevel).getDamage() - 100 + added_sharpeye_dmg);
                    this.passive_sharpeye_rate = (byte) (critSkill.getEffect(critlevel).getProb() + added_sharpeye_rate);
                    return;
                }
                break;
            }
            case 1511:
            case 1512: {
                final ISkill critSkill = SkillFactory.getSkill(15110000);
                final int critlevel = player.getSkillLevel(critSkill);
                if (critlevel > 0) {
                    this.passive_sharpeye_percent = (short) (critSkill.getEffect(critlevel).getDamage() - 100 + added_sharpeye_dmg);
                    this.passive_sharpeye_rate = (byte) (critSkill.getEffect(critlevel).getProb() + added_sharpeye_rate);
                    return;
                }
                break;
            }
            case 2111:
            case 2112: { // Aran, TODO : only applies when there's > 10 combo
                final ISkill critSkill = SkillFactory.getSkill(21110000);
                final int critlevel = player.getSkillLevel(critSkill);
                if (critlevel > 0) {
                    this.passive_sharpeye_percent = (short) ((critSkill.getEffect(critlevel).getX() * critSkill.getEffect(critlevel).getDamage()) + added_sharpeye_dmg);
                    this.passive_sharpeye_rate = (byte) ((critSkill.getEffect(critlevel).getX() * critSkill.getEffect(critlevel).getY()) + added_sharpeye_rate);
                    return;
                }
                break;
            }
            case 300:
            case 310:
            case 311:
            case 312:
            case 320:
            case 321:
            case 322: { // Bowman
                final ISkill critSkill = SkillFactory.getSkill(3000001);
                final int critlevel = player.getSkillLevel(critSkill);
                if (critlevel > 0) {
                    this.passive_sharpeye_percent = (short) (critSkill.getEffect(critlevel).getDamage() - 100 + added_sharpeye_dmg);
                    this.passive_sharpeye_rate = (byte) (critSkill.getEffect(critlevel).getProb() + added_sharpeye_rate);
                    return;
                }
                break;
            }
            case 1300:
            case 1310:
            case 1311:
            case 1312: { // Bowman
                final ISkill critSkill = SkillFactory.getSkill(13000000);
                final int critlevel = player.getSkillLevel(critSkill);
                if (critlevel > 0) {
                    this.passive_sharpeye_percent = (short) (critSkill.getEffect(critlevel).getDamage() - 100 + added_sharpeye_dmg);
                    this.passive_sharpeye_rate = (byte) (critSkill.getEffect(critlevel).getProb() + added_sharpeye_rate);
                    return;
                }
                break;
            }
            case 2214:
            case 2215:
            case 2216:
            case 2217:
            case 2218: { //Evan
                final ISkill critSkill = SkillFactory.getSkill(22140000);
                final int critlevel = player.getSkillLevel(critSkill);
                if (critlevel > 0) {
                    this.passive_sharpeye_percent = (short) (critSkill.getEffect(critlevel).getDamage() - 100 + added_sharpeye_dmg);
                    this.passive_sharpeye_rate = (byte) (critSkill.getEffect(critlevel).getProb() + added_sharpeye_rate);
                    return;
                }
                break;
            }
        }
        this.passive_sharpeye_percent = (short) added_sharpeye_dmg;
        this.passive_sharpeye_rate = (byte) added_sharpeye_rate;
    }

    public final short passive_sharpeye_percent() {
        return passive_sharpeye_percent;
    }

    public final byte passive_sharpeye_rate() {
        return passive_sharpeye_rate;
    }

    public final byte passive_mastery() {
        return passive_mastery; //* 5 + 10 for mastery %
    }

    public final float calculateMaxBaseDamage(final int watk) {
        final MapleCharacter chra = chr.get();
        if (chra == null) {
            return 0;
        }
        float maxbasedamage;
        if (watk == 0) {
            maxbasedamage = 1;
        } else {
            final IItem weapon_item = chra.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -11);
            final int job = chra.getJob();
            final MapleWeaponType weapon = weapon_item == null ? MapleWeaponType.NOT_A_WEAPON : GameConstants.getWeaponType(weapon_item.getItemId());
            int mainstat, secondarystat;

            switch (weapon) {
                case BOW:
                case CROSSBOW:
                    mainstat = localdex;
                    secondarystat = localstr;
                    break;
                case CLAW:
                case DAGGER:
                case KATARA:
                    if ((job >= 400 && job <= 434) || (job >= 1400 && job <= 1412)) {
                        mainstat = localluk;
                        secondarystat = localdex + localstr;
                    } else { // Non Thieves
                        mainstat = localstr;
                        secondarystat = localdex;
                    }
                    break;
                case KNUCKLE:
                    mainstat = localstr;
                    secondarystat = localdex;
                    break;
                case GUN:
                    mainstat = localdex;
                    secondarystat = localstr;
                    break;
                case NOT_A_WEAPON:
                    if ((job >= 500 && job <= 522) || (job >= 1500 && job <= 1512) || (job >= 3500 && job <= 3512)) {
                        mainstat = localstr;
                        secondarystat = localdex;
                    } else {
                        mainstat = 0;
                        secondarystat = 0;
                    }
                    break;
                default:
                    if ((job >= 200 && job <= 232) || (job >= 1200 && job <= 1211) && ((weapon == MapleWeaponType.WAND) || (weapon == MapleWeaponType.STAFF))) {
                        mainstat = this.localint_;
                        secondarystat = this.localluk;
                    } else {
                        mainstat = localstr;
                        secondarystat = localdex;
                    }
                    break;
            }
            maxbasedamage = ((weapon.getMaxDamageMultiplier() * mainstat) + secondarystat) * watk / 100;
        }
        return maxbasedamage;
    }

    public final float getHealHP() {
        return shouldHealHP;
    }

    public final float getHealMP() {
        return shouldHealMP;
    }

    public final void relocHeal() {
        final MapleCharacter chra = chr.get();
        if (chra == null) {
            return;
        }
        final int playerjob = chra.getJob();

        shouldHealHP = 10 + recoverHP; // Reset
        shouldHealMP = 3 + mpRestore + recoverMP; // i think

        if (GameConstants.isJobFamily(200, playerjob)) { // Improving MP recovery
            shouldHealMP += ((float) ((float) chra.getSkillLevel(SkillFactory.getSkill(2000000)) / 10) * chra.getLevel());

        } else if (GameConstants.isJobFamily(111, playerjob)) {
            final ISkill effect = SkillFactory.getSkill(1110000); // Improving MP Recovery
            final int lvl = chra.getSkillLevel(effect);
            if (lvl > 0) {
                shouldHealMP += effect.getEffect(lvl).getMp();
            }

        } else if (GameConstants.isJobFamily(121, playerjob)) {
            final ISkill effect = SkillFactory.getSkill(1210000); // Improving MP Recovery
            final int lvl = chra.getSkillLevel(effect);
            if (lvl > 0) {
                shouldHealMP += effect.getEffect(lvl).getMp();
            }

        } else if (GameConstants.isJobFamily(1111, playerjob)) {
            final ISkill effect = SkillFactory.getSkill(11110000); // Improving MP Recovery
            final int lvl = chra.getSkillLevel(effect);
            if (lvl > 0) {
                shouldHealMP += effect.getEffect(lvl).getMp();
            }

        } else if (GameConstants.isJobFamily(410, playerjob)) {
            final ISkill effect = SkillFactory.getSkill(4100002); // Endure
            final int lvl = chra.getSkillLevel(effect);
            if (lvl > 0) {
                shouldHealHP += effect.getEffect(lvl).getHp();
                shouldHealMP += effect.getEffect(lvl).getMp();
            }

        } else if (GameConstants.isJobFamily(420, playerjob)) {
            final ISkill effect = SkillFactory.getSkill(4200001); // Endure
            final int lvl = chra.getSkillLevel(effect);
            if (lvl > 0) {
                shouldHealHP += effect.getEffect(lvl).getHp();
                shouldHealMP += effect.getEffect(lvl).getMp();
            }
        }
        if (chra.isGM()) {
            shouldHealHP += 1000;
            shouldHealMP += 1000;
        }
        if (chra.getChair() != 0) { // Is sitting on a chair.
            shouldHealHP += 99; // Until the values of Chair heal has been fixed,
            shouldHealMP += 99; // MP is different here, if chair data MP = 0, heal + 1.5
        } else { // Because Heal isn't multipled when there's a chair :)
            final float recvRate = chra.getMap().getRecoveryRate();
            shouldHealHP *= recvRate;
            shouldHealMP *= recvRate;
        }
        shouldHealHP *= 2; // To avoid any problem with bathrobe / Sauna >.<
        shouldHealMP *= 2; // 1.5
    }

    public final void connectData(final MaplePacketLittleEndianWriter mplew) {
        mplew.writeShort(str); // str
        mplew.writeShort(dex); // dex
        mplew.writeShort(int_); // int
        mplew.writeShort(luk); // luk
        mplew.writeShort(hp); // hp
        mplew.writeShort(maxhp); // maxhp
        mplew.writeShort(mp); // mp
        mplew.writeShort(maxmp); // maxmp
    }

    public final int getSkillByJob(final int skillID, final int job) {
        if (GameConstants.isKOC(job)) {
            return skillID + 10000000;
        } else if (GameConstants.isAran(job)) {
            return skillID + 20000000;
        } else if (GameConstants.isEvan(job)) {
            return skillID + 20010000;
        } else if (GameConstants.isResist(job)) {
            return skillID + 30000000;
        }
        return skillID;
    }

    public final int getDamageIncrease(final int key) {
        if (damageIncrease.containsKey(key)) {
            return damageIncrease.get(key) + damX;
        }
        return damX;
    }

    public int getLimitBreak(MapleCharacter chra) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

        int limitBreak = 999999;
        Equip weapon = (Equip) chra.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
        if (weapon != null) {
            limitBreak = ii.getLimitBreak(weapon.getItemId());

            Equip subweapon = (Equip) chra.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10);
            if ((subweapon != null) && (GameConstants.is物理武器(subweapon.getItemId()))) {
                int subWeaponLB = ii.getLimitBreak(subweapon.getItemId());
                if (subWeaponLB > limitBreak) {
                    limitBreak = subWeaponLB;
                }
            }
        }
        return limitBreak;
    }
}
