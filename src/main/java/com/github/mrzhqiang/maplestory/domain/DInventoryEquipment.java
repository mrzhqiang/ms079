package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "inventoryequipment")
public class DInventoryEquipment extends Model {

    @Id
    Integer id;
    @NotNull
    @OneToOne
    @JoinColumn(name = "inventory_item_id", referencedColumnName = "id")
    DInventoryItem item;
    @NotNull
    Integer upgradeSlots = 0;
    @NotNull
    Integer level = 0;
    @NotNull
    Integer str = 0;
    @NotNull
    Integer dex = 0;
    @NotNull
    @Column(name = "intelligence")
    Integer intelligence = 0;
    @NotNull
    Integer luk = 0;
    @NotNull
    Integer hp = 0;
    @NotNull
    Integer mp = 0;
    @NotNull
    Integer watk = 0;
    @NotNull
    Integer matk = 0;
    @NotNull
    Integer wdef = 0;
    @NotNull
    Integer mdef = 0;
    @NotNull
    Integer acc = 0;
    @NotNull
    Integer avoid = 0;
    @NotNull
    Integer hands = 0;
    @NotNull
    Integer speed = 0;
    @NotNull
    Integer jump = 0;
    @NotNull
    Integer viciousHammer = 0;
    @NotNull
    Integer itemExp = 0;
    @NotNull
    Integer durability = 0;
    @NotNull
    Integer enhance = 0;
    @NotNull
    Integer potential1 = 0;
    @NotNull
    Integer potential2 = 0;
    @NotNull
    Integer potential3 = 0;
    @NotNull
    Integer hpR = 0;
    @NotNull
    Integer mpR = 0;
    @NotNull
    Integer itemLevel = 1;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DInventoryItem getItem() {
        return item;
    }

    public void setItem(DInventoryItem item) {
        this.item = item;
    }

    public Integer getUpgradeSlots() {
        return upgradeSlots;
    }

    public void setUpgradeSlots(Integer upgradeSlots) {
        this.upgradeSlots = upgradeSlots;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStr() {
        return str;
    }

    public void setStr(Integer str) {
        this.str = str;
    }

    public Integer getDex() {
        return dex;
    }

    public void setDex(Integer dex) {
        this.dex = dex;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getLuk() {
        return luk;
    }

    public void setLuk(Integer luk) {
        this.luk = luk;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getMp() {
        return mp;
    }

    public void setMp(Integer mp) {
        this.mp = mp;
    }

    public Integer getWatk() {
        return watk;
    }

    public void setWatk(Integer watk) {
        this.watk = watk;
    }

    public Integer getMatk() {
        return matk;
    }

    public void setMatk(Integer matk) {
        this.matk = matk;
    }

    public Integer getWdef() {
        return wdef;
    }

    public void setWdef(Integer wdef) {
        this.wdef = wdef;
    }

    public Integer getMdef() {
        return mdef;
    }

    public void setMdef(Integer mdef) {
        this.mdef = mdef;
    }

    public Integer getAcc() {
        return acc;
    }

    public void setAcc(Integer acc) {
        this.acc = acc;
    }

    public Integer getAvoid() {
        return avoid;
    }

    public void setAvoid(Integer avoid) {
        this.avoid = avoid;
    }

    public Integer getHands() {
        return hands;
    }

    public void setHands(Integer hands) {
        this.hands = hands;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getJump() {
        return jump;
    }

    public void setJump(Integer jump) {
        this.jump = jump;
    }

    public Integer getViciousHammer() {
        return viciousHammer;
    }

    public void setViciousHammer(Integer viciousHammer) {
        this.viciousHammer = viciousHammer;
    }

    public Integer getItemExp() {
        return itemExp;
    }

    public void setItemExp(Integer itemExp) {
        this.itemExp = itemExp;
    }

    public Integer getDurability() {
        return durability;
    }

    public void setDurability(Integer durability) {
        this.durability = durability;
    }

    public Integer getEnhance() {
        return enhance;
    }

    public void setEnhance(Integer enhance) {
        this.enhance = enhance;
    }

    public Integer getPotential1() {
        return potential1;
    }

    public void setPotential1(Integer potential1) {
        this.potential1 = potential1;
    }

    public Integer getPotential2() {
        return potential2;
    }

    public void setPotential2(Integer potential2) {
        this.potential2 = potential2;
    }

    public Integer getPotential3() {
        return potential3;
    }

    public void setPotential3(Integer potential3) {
        this.potential3 = potential3;
    }

    public Integer getHpR() {
        return hpR;
    }

    public void setHpR(Integer hpR) {
        this.hpR = hpR;
    }

    public Integer getMpR() {
        return mpR;
    }

    public void setMpR(Integer mpR) {
        this.mpR = mpR;
    }

    public Integer getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(Integer itemLevel) {
        this.itemLevel = itemLevel;
    }
}
