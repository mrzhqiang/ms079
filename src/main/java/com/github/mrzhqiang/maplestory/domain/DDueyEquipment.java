package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "dueyequipment")
public class DDueyEquipment extends Model {

    @Id
    @Column(name = "inventoryequipmentid")
    public Integer id;
    @NotNull
    @OneToOne
    @JoinColumn(name = "inventoryitemid")
    public DDueyItem item;
    @NotNull
    @Column(name = "upgradeslots")
    public Integer upgradeslots;
    @NotNull
    @Column(name = "level")
    public Integer level;
    @NotNull
    @Column(name = "str")
    public Integer str;
    @NotNull
    @Column(name = "dex")
    public Integer dex;
    @NotNull
    @Column(name = "int")
    public Integer intField;
    @NotNull
    @Column(name = "luk")
    public Integer luk;
    @NotNull
    @Column(name = "hp")
    public Integer hp;
    @NotNull
    @Column(name = "mp")
    public Integer mp;
    @NotNull
    @Column(name = "watk")
    public Integer watk;
    @NotNull
    @Column(name = "matk")
    public Integer matk;
    @NotNull
    @Column(name = "wdef")
    public Integer wdef;
    @NotNull
    @Column(name = "mdef")
    public Integer mdef;
    @NotNull
    @Column(name = "acc")
    public Integer acc;
    @NotNull
    @Column(name = "avoid")
    public Integer avoid;
    @NotNull
    @Column(name = "hands")
    public Integer hands;
    @NotNull
    @Column(name = "speed")
    public Integer speed;
    @NotNull
    @Column(name = "jump")
    public Integer jump;
    @NotNull
    @Column(name = "ViciousHammer")
    public Integer viciousHammer;
    @NotNull
    @Column(name = "itemEXP")
    public Integer itemEXP;
    @NotNull
    @Column(name = "durability")
    public Integer durability;
    @NotNull
    @Column(name = "enhance")
    public Integer enhance;
    @NotNull
    @Column(name = "potential1")
    public Integer potential1;
    @NotNull
    @Column(name = "potential2")
    public Integer potential2;
    @NotNull
    @Column(name = "potential3")
    public Integer potential3;
    @NotNull
    @Column(name = "hpR")
    public Integer hpR;
    @NotNull
    @Column(name = "mpR")
    public Integer mpR;

}
