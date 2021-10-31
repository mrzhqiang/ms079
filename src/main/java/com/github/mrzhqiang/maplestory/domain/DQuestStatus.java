package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "queststatus")
public class DQuestStatus extends Model {

    @Id
    @Column(name = "queststatusid")
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;

    @NotNull
    @Column(name = "quest")
    public Integer quest;

    @NotNull
    @Column(name = "status")
    public Integer status;

    @NotNull
    @Column(name = "time")
    public Integer time;

    @NotNull
    @Column(name = "forfeited")
    public Integer forfeited;

    @Column(name = "customData")
    public String customData;

}
