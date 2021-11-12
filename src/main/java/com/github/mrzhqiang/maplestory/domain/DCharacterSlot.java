package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "character_slots")
public class DCharacterSlot extends Model {

    @Id
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "accid")
    public DAccount account;
    @NotNull
    @Column(name = "worldid")
    public Integer worldid;
    @NotNull
    @Column(name = "charslots")
    public Integer charslots;

}
