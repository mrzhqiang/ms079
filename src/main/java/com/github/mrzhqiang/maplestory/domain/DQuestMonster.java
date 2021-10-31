package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "questmonster")
public class DQuestMonster extends Model {

    @Id
    public Integer id;
    @Column(name = "questid")
    public Integer questid;
    @Column(name = "monsterid")
    public Integer monsterid;
    @NotNull
    @Column(name = "zt")
    public Integer zt;
    @Column(name = "charid")
    public Integer charid;
    @Column(name = "name")
    public String name;

}
