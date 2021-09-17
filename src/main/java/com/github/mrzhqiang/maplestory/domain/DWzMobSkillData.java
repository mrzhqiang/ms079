package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "wz_mobskilldata")
public class DWzMobSkillData {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "skillid", nullable = false)
    private Integer skillid;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "hp", nullable = false)
    private Integer hp;

    @Column(name = "mpcon", nullable = false)
    private Integer mpcon;

    @Column(name = "x", nullable = false)
    private Integer x;

    @Column(name = "y", nullable = false)
    private Integer y;

    @Column(name = "time", nullable = false)
    private Integer time;

    @Column(name = "prop", nullable = false)
    private Integer prop;

//    @Column(name = "limit", nullable = false)
    @Column(name = "limit_", nullable = false)
    private Integer limit;

    @Column(name = "spawneffect", nullable = false)
    private Integer spawneffect;

//    @Column(name = "interval", nullable = false)
    @Column(name = "interval_", nullable = false)
    private Integer interval;

    @Column(name = "summons", nullable = false)
    private String summons;

    @Column(name = "ltx", nullable = false)
    private Integer ltx;

    @Column(name = "lty", nullable = false)
    private Integer lty;

    @Column(name = "rbx", nullable = false)
    private Integer rbx;

    @Column(name = "rby", nullable = false)
    private Integer rby;

    @Column(name = "once", nullable = false)
    private Integer once;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setSkillid(Integer skillid) {
        this.skillid = skillid;
    }

    public Integer getSkillid() {
        return skillid;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getHp() {
        return hp;
    }

    public void setMpcon(Integer mpcon) {
        this.mpcon = mpcon;
    }

    public Integer getMpcon() {
        return mpcon;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getX() {
        return x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getY() {
        return y;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getTime() {
        return time;
    }

    public void setProp(Integer prop) {
        this.prop = prop;
    }

    public Integer getProp() {
        return prop;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setSpawneffect(Integer spawneffect) {
        this.spawneffect = spawneffect;
    }

    public Integer getSpawneffect() {
        return spawneffect;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setSummons(String summons) {
        this.summons = summons;
    }

    public String getSummons() {
        return summons;
    }

    public void setLtx(Integer ltx) {
        this.ltx = ltx;
    }

    public Integer getLtx() {
        return ltx;
    }

    public void setLty(Integer lty) {
        this.lty = lty;
    }

    public Integer getLty() {
        return lty;
    }

    public void setRbx(Integer rbx) {
        this.rbx = rbx;
    }

    public Integer getRbx() {
        return rbx;
    }

    public void setRby(Integer rby) {
        this.rby = rby;
    }

    public Integer getRby() {
        return rby;
    }

    public void setOnce(Integer once) {
        this.once = once;
    }

    public Integer getOnce() {
        return once;
    }

    @Override
    public String toString() {
        return "DWzMobSkillData{" +
                "id=" + id + '\'' +
                "skillid=" + skillid + '\'' +
                "level=" + level + '\'' +
                "hp=" + hp + '\'' +
                "mpcon=" + mpcon + '\'' +
                "x=" + x + '\'' +
                "y=" + y + '\'' +
                "time=" + time + '\'' +
                "prop=" + prop + '\'' +
                "limit=" + limit + '\'' +
                "spawneffect=" + spawneffect + '\'' +
                "interval=" + interval + '\'' +
                "summons=" + summons + '\'' +
                "ltx=" + ltx + '\'' +
                "lty=" + lty + '\'' +
                "rbx=" + rbx + '\'' +
                "rby=" + rby + '\'' +
                "once=" + once + '\'' +
                '}';
    }
}
