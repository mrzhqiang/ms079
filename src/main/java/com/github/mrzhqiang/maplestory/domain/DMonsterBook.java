package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "monsterbook")
public class DMonsterBook extends Model {

    @Id
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "charid")
    public DCharacter character;
    @NotNull
    @Column(name = "cardid")
    public Integer cardid;
    @Column(name = "level")
    public Integer level;

}
