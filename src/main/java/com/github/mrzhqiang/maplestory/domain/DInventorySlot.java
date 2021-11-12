package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "inventoryslot")
public class DInventorySlot extends Model {

    @Id
    @Column(name = "id")
    public Integer id;
    @OneToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @Column(name = "equip")
    public Integer equip;
    @Column(name = "use_")
    public Integer use;
    @Column(name = "setup")
    public Integer setup;
    @Column(name = "etc")
    public Integer etc;
    @Column(name = "cash")
    public Integer cash;

}
