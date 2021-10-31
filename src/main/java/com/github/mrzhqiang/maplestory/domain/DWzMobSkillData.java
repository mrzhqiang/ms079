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
    public Integer id;
    @NotNull
    @Column(name = "skillid")
    public Integer skillid;
    @NotNull
    @Column(name = "level")
    public Integer level;
    @NotNull
    @Column(name = "hp")
    public Integer hp;
    @NotNull
    @Column(name = "mpcon")
    public Integer mpcon;
    @NotNull
    @Column(name = "x")
    public Integer x;
    @NotNull
    @Column(name = "y")
    public Integer y;
    @NotNull
    @Column(name = "time")
    public Integer time;
    @NotNull
    @Column(name = "prop")
    public Integer prop;
    @NotNull
    @Column(name = "limit_")
    public Integer limit;
    @NotNull
    @Column(name = "spawneffect")
    public Integer spawneffect;
    @NotNull
    @Column(name = "interval_")
    public Integer interval;
    @NotNull
    @Column(name = "summons")
    public String summons;
    @NotNull
    @Column(name = "ltx")
    public Integer ltx;
    @NotNull
    @Column(name = "lty")
    public Integer lty;
    @NotNull
    @Column(name = "rbx")
    public Integer rbx;
    @NotNull
    @Column(name = "rby")
    public Integer rby;
    @NotNull
    @Column(name = "once")
    public Integer once;

}
