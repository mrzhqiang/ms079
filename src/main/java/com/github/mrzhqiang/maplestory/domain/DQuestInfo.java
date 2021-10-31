package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "questinfo")
public class DQuestInfo extends Model {

    @Id
    @Column(name = "questinfoid")
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @NotNull
    @Column(name = "quest")
    public Integer quest;
    @Column(name = "customData")
    public String customData;

}
