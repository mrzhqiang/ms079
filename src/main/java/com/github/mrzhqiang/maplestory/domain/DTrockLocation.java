package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "trocklocations")
public class DTrockLocation extends Model {

    @Id
    @Column(name = "trockid")
    public Integer id;
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @Column(name = "mapid")
    public Integer mapid;

}
