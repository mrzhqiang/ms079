package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "skills")
public class DSkill extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "skillid")
    public Integer skillid;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @NotNull
    @Column(name = "skilllevel")
    public Integer skilllevel;
    @NotNull
    @Column(name = "masterlevel")
    public Integer masterlevel;
    @NotNull
    @Column(name = "expiration")
    public Long expiration;

}
