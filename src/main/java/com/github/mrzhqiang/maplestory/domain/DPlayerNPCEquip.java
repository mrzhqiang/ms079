package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "playernpcs_equip")
public class DPlayerNPCEquip extends Model {

    @Id
    Integer id;
    @NotNull
    Integer npcId;
    @NotNull
    Integer equipId;
    @NotNull
    Integer equipPos;
    @NotNull
    Integer charId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNpcId() {
        return npcId;
    }

    public void setNpcId(Integer npcId) {
        this.npcId = npcId;
    }

    public Integer getEquipId() {
        return equipId;
    }

    public void setEquipId(Integer equipId) {
        this.equipId = equipId;
    }

    public Integer getEquipPos() {
        return equipPos;
    }

    public void setEquipPos(Integer equipPos) {
        this.equipPos = equipPos;
    }

    public Integer getCharId() {
        return charId;
    }

    public void setCharId(Integer charId) {
        this.charId = charId;
    }
}
