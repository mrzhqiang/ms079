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
    public Integer id;
    @NotNull
    @Column(name = "npcid")
    public Integer npcid;
    @NotNull
    @Column(name = "equipid")
    public Integer equipid;
    @NotNull
    @Column(name = "equippos")
    public Integer equippos;
    @NotNull
    @Column(name = "charid")
    public Integer charid;

}
