package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "character_slots")
public class DCharacterSlot extends Model {

    @Id
    Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "acc_id")
    DAccount account;
    @NotNull
    Integer worldId;
    @NotNull
    Integer charSlots;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DAccount getAccount() {
        return account;
    }

    public void setAccount(DAccount account) {
        this.account = account;
    }

    public Integer getWorldId() {
        return worldId;
    }

    public void setWorldId(Integer worldId) {
        this.worldId = worldId;
    }

    public Integer getCharSlots() {
        return charSlots;
    }

    public void setCharSlots(Integer charSlots) {
        this.charSlots = charSlots;
    }
}
