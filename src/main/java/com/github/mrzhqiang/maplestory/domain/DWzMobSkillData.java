package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wz_mobskilldata")
public class DWzMobSkillData extends Model {

    @Id
    Integer id;
    @NotNull
    Integer skillId;
    @NotNull
    Integer level;
    @NotNull
    Integer hp;
    @NotNull
    Integer mpCon;
    @NotNull
    Integer x;
    @NotNull
    Integer y;
    @NotNull
    Integer time;
    @NotNull
    Integer prop;
    @NotNull
    @Column(name = "limit_")
    Integer limit;
    @NotNull
    Integer spawnEffect;
    @NotNull
    @Column(name = "interval_")
    Integer interval;
    @NotNull
    String summons;
    @NotNull
    Integer ltx;
    @NotNull
    Integer lty;
    @NotNull
    Integer rbx;
    @NotNull
    Integer rby;
    @NotNull
    Integer once;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getMpCon() {
        return mpCon;
    }

    public void setMpCon(Integer mpCon) {
        this.mpCon = mpCon;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getProp() {
        return prop;
    }

    public void setProp(Integer prop) {
        this.prop = prop;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getSpawnEffect() {
        return spawnEffect;
    }

    public void setSpawnEffect(Integer spawnEffect) {
        this.spawnEffect = spawnEffect;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getSummons() {
        return summons;
    }

    public void setSummons(String summons) {
        this.summons = summons;
    }

    public Integer getLtx() {
        return ltx;
    }

    public void setLtx(Integer ltx) {
        this.ltx = ltx;
    }

    public Integer getLty() {
        return lty;
    }

    public void setLty(Integer lty) {
        this.lty = lty;
    }

    public Integer getRbx() {
        return rbx;
    }

    public void setRbx(Integer rbx) {
        this.rbx = rbx;
    }

    public Integer getRby() {
        return rby;
    }

    public void setRby(Integer rby) {
        this.rby = rby;
    }

    public Integer getOnce() {
        return once;
    }

    public void setOnce(Integer once) {
        this.once = once;
    }
}
