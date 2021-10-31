package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "keymap")
public class DKeyMap extends Model {

    @Id
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @NotNull
    @Column(name = "key")
    public Integer key;
    @NotNull
    @Column(name = "type")
    public Integer type;
    @NotNull
    @Column(name = "action")
    public Integer action;

}
