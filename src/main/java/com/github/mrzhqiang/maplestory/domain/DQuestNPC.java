package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "questnpc")
public class DQuestNPC extends Model {

    @Id
    public Integer id;

    @Column(name = "npcid")
    public Integer npcid;

    @Column(name = "itemid")
    public Integer itemid;

    @Column(name = "sl")
    public Integer sl;

    @Column(name = "zt")
    public Integer zt;

    @Column(name = "name")
    public String name;

    @Column(name = "item")
    public Integer item;

    @Column(name = "itemsl")
    public Integer itemsl;

    @Column(name = "money")
    public Integer money;

}
