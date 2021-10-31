package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wz_questactitemdata")
public class DWzQuestActItemData extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "itemid")
    public Integer itemid;
    @NotNull
    @Column(name = "count")
    public Integer count;
    @NotNull
    @Column(name = "period")
    public Integer period;
    @NotNull
    @Column(name = "gender")
    public Integer gender;
    @NotNull
    @Column(name = "job")
    public Integer job;
    @NotNull
    @Column(name = "jobEx")
    public Integer jobEx;
    @NotNull
    @Column(name = "prop")
    public Integer prop;
    @NotNull
    @Column(name = "uniqueid")
    public Integer uniqueid;

}
