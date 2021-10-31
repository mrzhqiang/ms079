package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "mountdata")
public class DMountData extends Model {

    @Id
    public Integer id;
    @OneToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @NotNull
    @Column(name = "Level")
    public Integer level;
    @NotNull
    @Column(name = "Exp")
    public Integer exp;
    @NotNull
    @Column(name = "Fatigue")
    public Integer fatigue;

}
