package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "inventoryslot")
public class DInventorySlot {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "characterid")
    private Integer characterid;

    @Column(name = "equip")
    private Integer equip;

    @Column(name = "use")
    private Integer use;

    @Column(name = "setup")
    private Integer setup;

    @Column(name = "etc")
    private Integer etc;

    @Column(name = "cash")
    private Integer cash;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setEquip(Integer equip) {
        this.equip = equip;
    }

    public Integer getEquip() {
        return equip;
    }

    public void setUse(Integer use) {
        this.use = use;
    }

    public Integer getUse() {
        return use;
    }

    public void setSetup(Integer setup) {
        this.setup = setup;
    }

    public Integer getSetup() {
        return setup;
    }

    public void setEtc(Integer etc) {
        this.etc = etc;
    }

    public Integer getEtc() {
        return etc;
    }

    public void setCash(Integer cash) {
        this.cash = cash;
    }

    public Integer getCash() {
        return cash;
    }

    @Override
    public String toString() {
        return "DInventorySlot{" +
                "id=" + id + '\'' +
                "characterid=" + characterid + '\'' +
                "equip=" + equip + '\'' +
                "use=" + use + '\'' +
                "setup=" + setup + '\'' +
                "etc=" + etc + '\'' +
                "cash=" + cash + '\'' +
                '}';
    }
}
