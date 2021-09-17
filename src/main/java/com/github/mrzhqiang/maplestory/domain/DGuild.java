package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.DbForeignKey;
import io.ebean.annotation.Length;

import javax.persistence.*;

@Entity
@Table(name = "guilds")
public class DGuild {

    @Id
    public Integer guildid;
    @OneToOne
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "leader")
    public DCharacter leader;
    @Column(name = "GP", nullable = false)
    public Integer GP;
    @Column(name = "logo")
    public Integer logo;
    @Column(name = "logoColor", nullable = false)
    public Integer logoColor;
    @Length(45)
    @Column(name = "name", nullable = false)
    public String name;
    @Length(45)
    @Column(name = "rank1title", nullable = false)
    public String rank1title;
    @Length(45)
    @Column(name = "rank2title", nullable = false)
    public String rank2title;
    @Length(45)
    @Column(name = "rank3title", nullable = false)
    public String rank3title;
    @Length(45)
    @Column(name = "rank4title", nullable = false)
    public String rank4title;
    @Length(45)
    @Column(name = "rank5title", nullable = false)
    public String rank5title;
    @Column(name = "capacity", nullable = false)
    public Integer capacity;
    @Column(name = "logoBG")
    public Integer logoBG;
    @Column(name = "logoBGColor", nullable = false)
    public Integer logoBGColor;
    @Length(101)
    @Column(name = "notice")
    public String notice;
    @Column(name = "signature", nullable = false)
    public Integer signature;
    @Column(name = "alliance", nullable = false)
    public Integer alliance;

}
