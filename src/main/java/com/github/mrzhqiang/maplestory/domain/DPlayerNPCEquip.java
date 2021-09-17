package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "playernpcs_equip")
public class DPlayerNPCEquip {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "npcid", nullable = false)
    private Integer npcid;

    @Column(name = "equipid", nullable = false)
    private Integer equipid;

    @Column(name = "equippos", nullable = false)
    private Integer equippos;

    @Column(name = "charid", nullable = false)
    private Integer charid;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setNpcid(Integer npcid) {
        this.npcid = npcid;
    }

    public Integer getNpcid() {
        return npcid;
    }

    public void setEquipid(Integer equipid) {
        this.equipid = equipid;
    }

    public Integer getEquipid() {
        return equipid;
    }

    public void setEquippos(Integer equippos) {
        this.equippos = equippos;
    }

    public Integer getEquippos() {
        return equippos;
    }

    public void setCharid(Integer charid) {
        this.charid = charid;
    }

    public Integer getCharid() {
        return charid;
    }

    @Override
    public String toString() {
        return "DPlayerNPCEquip{" +
                "id=" + id + '\'' +
                "npcid=" + npcid + '\'' +
                "equipid=" + equipid + '\'' +
                "equippos=" + equippos + '\'' +
                "charid=" + charid + '\'' +
                '}';
    }
}
