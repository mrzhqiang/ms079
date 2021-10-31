package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "alliances")
public class DAlliance extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "name")
    public String name;
    @NotNull
    @OneToOne
    @JoinColumn(name = "leaderid")
    public DCharacter leader;
    @NotNull
    @OneToOne
    @JoinColumn(name = "guild1")
    public DGuild guild1;
    @NotNull
    @OneToOne
    @JoinColumn(name = "guild2")
    public DGuild guild2;
    @NotNull
    @OneToOne
    @JoinColumn(name = "guild3")
    public DGuild guild3;
    @NotNull
    @OneToOne
    @JoinColumn(name = "guild4")
    public DGuild guild4;
    @NotNull
    @OneToOne
    @JoinColumn(name = "guild5")
    public DGuild guild5;
    @NotNull
    @Column(name = "rank1")
    public String rank1;
    @NotNull
    @Column(name = "rank2")
    public String rank2;
    @NotNull
    @Column(name = "rank3")
    public String rank3;
    @NotNull
    @Column(name = "rank4")
    public String rank4;
    @NotNull
    @Column(name = "rank5")
    public String rank5;
    @NotNull
    @Column(name = "capacity")
    public Integer capacity;
    @NotNull
    @Column(name = "notice")
    public String notice;

}
