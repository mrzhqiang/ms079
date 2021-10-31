package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wz_npcnamedata")
public class DWzNPCNameData extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "npc")
    public Integer npc;
    @NotNull
    @Column(name = "name")
    public String name;

}
