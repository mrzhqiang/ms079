package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pets")
public class DPet extends Model {

    @Id
    Integer id;
    String name;
    @NotNull
    Integer level;
    @NotNull
    Integer closeness;
    @NotNull
    Integer fullness;
    @NotNull
    Integer seconds;
    @NotNull
    Integer flags;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCloseness() {
        return closeness;
    }

    public void setCloseness(Integer closeness) {
        this.closeness = closeness;
    }

    public Integer getFullness() {
        return fullness;
    }

    public void setFullness(Integer fullness) {
        this.fullness = fullness;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }
}
