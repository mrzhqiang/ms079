package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "regrocklocations")
public class DRegrockLocation {

    @Id
    @Column(name = "trockid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer trockid;

    @Column(name = "characterid")
    private Integer characterid;

    @Column(name = "mapid")
    private Integer mapid;

    public void setTrockid(Integer trockid) {
        this.trockid = trockid;
    }

    public Integer getTrockid() {
        return trockid;
    }

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setMapid(Integer mapid) {
        this.mapid = mapid;
    }

    public Integer getMapid() {
        return mapid;
    }

    @Override
    public String toString() {
        return "DRegrockLocation{" +
                "trockid=" + trockid + '\'' +
                "characterid=" + characterid + '\'' +
                "mapid=" + mapid + '\'' +
                '}';
    }
}
