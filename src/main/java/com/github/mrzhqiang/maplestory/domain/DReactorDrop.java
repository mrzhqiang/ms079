package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reactordrops")
public class DReactorDrop extends Model {

    @Id
    Integer id;
    @NotNull
    Integer reactorId;
    @NotNull
    Integer itemId;
    @NotNull
    Integer chance;
    @NotNull
    @Column(name = "questid")
    Integer questId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReactorId() {
        return reactorId;
    }

    public void setReactorId(Integer reactorId) {
        this.reactorId = reactorId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getChance() {
        return chance;
    }

    public void setChance(Integer chance) {
        this.chance = chance;
    }

    public Integer getQuestId() {
        return questId;
    }

    public void setQuestId(Integer questId) {
        this.questId = questId;
    }
}
