package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "character_slots")
public class DCharacterSlot {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "accid", nullable = false)
    private Integer accid;

    @Column(name = "worldid", nullable = false)
    private Integer worldid;

    @Column(name = "charslots", nullable = false)
    private Integer charslots;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setAccid(Integer accid) {
        this.accid = accid;
    }

    public Integer getAccid() {
        return accid;
    }

    public void setWorldid(Integer worldid) {
        this.worldid = worldid;
    }

    public Integer getWorldid() {
        return worldid;
    }

    public void setCharslots(Integer charslots) {
        this.charslots = charslots;
    }

    public Integer getCharslots() {
        return charslots;
    }

    @Override
    public String toString() {
        return "DCharacterSlot{" +
                "id=" + id + '\'' +
                "accid=" + accid + '\'' +
                "worldid=" + worldid + '\'' +
                "charslots=" + charslots + '\'' +
                '}';
    }
}
