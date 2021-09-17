package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "inventoryequipment")
public class DInventoryEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventoryequipmentid", nullable = false)
    private Integer inventoryequipmentid;

    @Column(name = "inventoryitemid", nullable = false)
    private Integer inventoryitemid;

    @Column(name = "upgradeslots", nullable = false)
    private Integer upgradeslots;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "str", nullable = false)
    private Integer str;

    @Column(name = "dex", nullable = false)
    private Integer dex;

    @Column(name = "int", nullable = false)
    private Integer intField;

    @Column(name = "luk", nullable = false)
    private Integer luk;

    @Column(name = "hp", nullable = false)
    private Integer hp;

    @Column(name = "mp", nullable = false)
    private Integer mp;

    @Column(name = "watk", nullable = false)
    private Integer watk;

    @Column(name = "matk", nullable = false)
    private Integer matk;

    @Column(name = "wdef", nullable = false)
    private Integer wdef;

    @Column(name = "mdef", nullable = false)
    private Integer mdef;

    @Column(name = "acc", nullable = false)
    private Integer acc;

    @Column(name = "avoid", nullable = false)
    private Integer avoid;

    @Column(name = "hands", nullable = false)
    private Integer hands;

    @Column(name = "speed", nullable = false)
    private Integer speed;

    @Column(name = "jump", nullable = false)
    private Integer jump;

    @Column(name = "ViciousHammer", nullable = false)
    private Integer viciousHammer;

    @Column(name = "itemEXP", nullable = false)
    private Integer itemEXP;

    @Column(name = "durability", nullable = false)
    private Integer durability;

    @Column(name = "enhance", nullable = false)
    private Integer enhance;

    @Column(name = "potential1", nullable = false)
    private Integer potential1;

    @Column(name = "potential2", nullable = false)
    private Integer potential2;

    @Column(name = "potential3", nullable = false)
    private Integer potential3;

    @Column(name = "hpR", nullable = false)
    private Integer hpR;

    @Column(name = "mpR", nullable = false)
    private Integer mpR;

    @Column(name = "itemlevel", nullable = false)
    private Integer itemlevel;

    public void setInventoryequipmentid(Integer inventoryequipmentid) {
        this.inventoryequipmentid = inventoryequipmentid;
    }

    public Integer getInventoryequipmentid() {
        return inventoryequipmentid;
    }

    public void setInventoryitemid(Integer inventoryitemid) {
        this.inventoryitemid = inventoryitemid;
    }

    public Integer getInventoryitemid() {
        return inventoryitemid;
    }

    public void setUpgradeslots(Integer upgradeslots) {
        this.upgradeslots = upgradeslots;
    }

    public Integer getUpgradeslots() {
        return upgradeslots;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public void setStr(Integer str) {
        this.str = str;
    }

    public Integer getStr() {
        return str;
    }

    public void setDex(Integer dex) {
        this.dex = dex;
    }

    public Integer getDex() {
        return dex;
    }

    public void setIntField(Integer intField) {
        this.intField = intField;
    }

    public Integer getIntField() {
        return intField;
    }

    public void setLuk(Integer luk) {
        this.luk = luk;
    }

    public Integer getLuk() {
        return luk;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getHp() {
        return hp;
    }

    public void setMp(Integer mp) {
        this.mp = mp;
    }

    public Integer getMp() {
        return mp;
    }

    public void setWatk(Integer watk) {
        this.watk = watk;
    }

    public Integer getWatk() {
        return watk;
    }

    public void setMatk(Integer matk) {
        this.matk = matk;
    }

    public Integer getMatk() {
        return matk;
    }

    public void setWdef(Integer wdef) {
        this.wdef = wdef;
    }

    public Integer getWdef() {
        return wdef;
    }

    public void setMdef(Integer mdef) {
        this.mdef = mdef;
    }

    public Integer getMdef() {
        return mdef;
    }

    public void setAcc(Integer acc) {
        this.acc = acc;
    }

    public Integer getAcc() {
        return acc;
    }

    public void setAvoid(Integer avoid) {
        this.avoid = avoid;
    }

    public Integer getAvoid() {
        return avoid;
    }

    public void setHands(Integer hands) {
        this.hands = hands;
    }

    public Integer getHands() {
        return hands;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setJump(Integer jump) {
        this.jump = jump;
    }

    public Integer getJump() {
        return jump;
    }

    public void setViciousHammer(Integer viciousHammer) {
        this.viciousHammer = viciousHammer;
    }

    public Integer getViciousHammer() {
        return viciousHammer;
    }

    public void setItemEXP(Integer itemEXP) {
        this.itemEXP = itemEXP;
    }

    public Integer getItemEXP() {
        return itemEXP;
    }

    public void setDurability(Integer durability) {
        this.durability = durability;
    }

    public Integer getDurability() {
        return durability;
    }

    public void setEnhance(Integer enhance) {
        this.enhance = enhance;
    }

    public Integer getEnhance() {
        return enhance;
    }

    public void setPotential1(Integer potential1) {
        this.potential1 = potential1;
    }

    public Integer getPotential1() {
        return potential1;
    }

    public void setPotential2(Integer potential2) {
        this.potential2 = potential2;
    }

    public Integer getPotential2() {
        return potential2;
    }

    public void setPotential3(Integer potential3) {
        this.potential3 = potential3;
    }

    public Integer getPotential3() {
        return potential3;
    }

    public void setHpR(Integer hpR) {
        this.hpR = hpR;
    }

    public Integer getHpR() {
        return hpR;
    }

    public void setMpR(Integer mpR) {
        this.mpR = mpR;
    }

    public Integer getMpR() {
        return mpR;
    }

    public void setItemlevel(Integer itemlevel) {
        this.itemlevel = itemlevel;
    }

    public Integer getItemlevel() {
        return itemlevel;
    }

    @Override
    public String toString() {
        return "DInventoryEquipment{" +
                "inventoryequipmentid=" + inventoryequipmentid + '\'' +
                "inventoryitemid=" + inventoryitemid + '\'' +
                "upgradeslots=" + upgradeslots + '\'' +
                "level=" + level + '\'' +
                "str=" + str + '\'' +
                "dex=" + dex + '\'' +
                "intField=" + intField + '\'' +
                "luk=" + luk + '\'' +
                "hp=" + hp + '\'' +
                "mp=" + mp + '\'' +
                "watk=" + watk + '\'' +
                "matk=" + matk + '\'' +
                "wdef=" + wdef + '\'' +
                "mdef=" + mdef + '\'' +
                "acc=" + acc + '\'' +
                "avoid=" + avoid + '\'' +
                "hands=" + hands + '\'' +
                "speed=" + speed + '\'' +
                "jump=" + jump + '\'' +
                "viciousHammer=" + viciousHammer + '\'' +
                "itemEXP=" + itemEXP + '\'' +
                "durability=" + durability + '\'' +
                "enhance=" + enhance + '\'' +
                "potential1=" + potential1 + '\'' +
                "potential2=" + potential2 + '\'' +
                "potential3=" + potential3 + '\'' +
                "hpR=" + hpR + '\'' +
                "mpR=" + mpR + '\'' +
                "itemlevel=" + itemlevel + '\'' +
                '}';
    }
}
