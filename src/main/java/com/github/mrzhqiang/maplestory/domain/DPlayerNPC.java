package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "playernpcs")
public class DPlayerNPC extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "name")
    public String name;
    @NotNull
    @Column(name = "hair")
    public Integer hair;
    @NotNull
    @Column(name = "face")
    public Integer face;
    @NotNull
    @Column(name = "skin")
    public Integer skin;
    @NotNull
    @Column(name = "x")
    public Integer x;
    @NotNull
    @Column(name = "y")
    public Integer y;
    @NotNull
    @Column(name = "map")
    public Integer map;
    @NotNull
    @Column(name = "charid")
    public Integer charid;
    @NotNull
    @Column(name = "scriptid")
    public Integer scriptid;
    @NotNull
    @Column(name = "foothold")
    public Integer foothold;
    @NotNull
    @Column(name = "dir")
    public Integer dir;
    @NotNull
    @Column(name = "gender")
    public Integer gender;

    @Column(name = "pets")
    public String pets;

}
