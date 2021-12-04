package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inventoryslot")
public class DInventorySlot extends Model {

    @Id
    @Column(name = "id")
    Integer id;
    @OneToOne
    @JoinColumn(name = "character_id")
    DCharacter character;
    Integer equip;
    @Column(name = "use_")
    Integer use;
    Integer setup;
    Integer etc;
    Integer cash;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DCharacter getCharacter() {
        return character;
    }

    public void setCharacter(DCharacter character) {
        this.character = character;
    }

    public Integer getEquip() {
        return equip;
    }

    public void setEquip(Integer equip) {
        this.equip = equip;
    }

    public Integer getUse() {
        return use;
    }

    public void setUse(Integer use) {
        this.use = use;
    }

    public Integer getSetup() {
        return setup;
    }

    public void setSetup(Integer setup) {
        this.setup = setup;
    }

    public Integer getEtc() {
        return etc;
    }

    public void setEtc(Integer etc) {
        this.etc = etc;
    }

    public Integer getCash() {
        return cash;
    }

    public void setCash(Integer cash) {
        this.cash = cash;
    }
}
