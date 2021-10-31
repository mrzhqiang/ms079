package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "savedlocations")
public class DSavedLocation extends Model {

    @Id
    public Integer id;
    @NotNull
    @OneToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @NotNull
    @Column(name = "locationtype")
    public Integer locationtype;
    @NotNull
    @Column(name = "map")
    public Integer map;

}
