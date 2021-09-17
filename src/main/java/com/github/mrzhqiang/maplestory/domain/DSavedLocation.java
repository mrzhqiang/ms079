package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "savedlocations")
public class DSavedLocation {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "characterid", nullable = false)
    private Integer characterid;

    @Column(name = "locationtype", nullable = false)
    private Integer locationtype;

    @Column(name = "map", nullable = false)
    private Integer map;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setLocationtype(Integer locationtype) {
        this.locationtype = locationtype;
    }

    public Integer getLocationtype() {
        return locationtype;
    }

    public void setMap(Integer map) {
        this.map = map;
    }

    public Integer getMap() {
        return map;
    }

    @Override
    public String toString() {
        return "DSavedLocation{" +
                "id=" + id + '\'' +
                "characterid=" + characterid + '\'' +
                "locationtype=" + locationtype + '\'' +
                "map=" + map + '\'' +
                '}';
    }
}
