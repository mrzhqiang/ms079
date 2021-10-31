package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "questrequirements")
public class DQuestRequirement extends Model {

    @Id
    @Column(name = "questrequirementid")
    public Integer id;
    @NotNull
    @Column(name = "questid")
    public Integer questid;
    @NotNull
    @Column(name = "status")
    public Integer status;
    @NotNull
    @Column(name = "data")
    public byte[] data;

}
