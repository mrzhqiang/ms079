package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wz_questactskilldata")
public class DWzQuestActSkillData extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "skillid")
    public Integer skillid;
    @NotNull
    @Column(name = "skillLevel")
    public Integer skillLevel;
    @NotNull
    @Column(name = "masterLevel")
    public Integer masterLevel;
    @NotNull
    @Column(name = "uniqueid")
    public Integer uniqueid;

}
