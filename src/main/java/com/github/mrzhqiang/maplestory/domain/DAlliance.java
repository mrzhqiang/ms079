package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "alliances")
public class DAlliance extends Model {

    @Id
    public Integer id;
    @Column(name = "name", nullable = false)
    public String name;
    @Column(name = "leaderid", nullable = false)
    public Integer leaderid;
    @Column(name = "guild1", nullable = false)
    public Integer guild1;
    @Column(name = "guild2", nullable = false)
    public Integer guild2;
    @Column(name = "guild3", nullable = false)
    public Integer guild3;
    @Column(name = "guild4", nullable = false)
    public Integer guild4;
    @Column(name = "guild5", nullable = false)
    public Integer guild5;
    @Column(name = "rank1", nullable = false)
    public String rank1;
    @Column(name = "rank2", nullable = false)
    public String rank2;
    @Column(name = "rank3", nullable = false)
    public String rank3;
    @Column(name = "rank4", nullable = false)
    public String rank4;
    @Column(name = "rank5", nullable = false)
    public String rank5;
    @Column(name = "capacity", nullable = false)
    public Integer capacity;
    @Column(name = "notice", nullable = false)
    public String notice;

}
