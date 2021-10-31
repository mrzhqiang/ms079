package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reactordrops")
public class DReactorDrop extends Model {

    @Id
    @Column(name = "reactordropid")
    public Integer id;
    @NotNull
    @Column(name = "reactorid")
    public Integer reactorid;
    @NotNull
    @Column(name = "itemid")
    public Integer itemid;
    @NotNull
    @Column(name = "chance")
    public Integer chance;
    @NotNull
    @Column(name = "questid")
    public Integer questid;

}
