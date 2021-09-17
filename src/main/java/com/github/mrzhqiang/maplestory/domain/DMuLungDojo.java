package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "mulungdojo")
public class DMuLungDojo {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "charid", nullable = false)
    private Integer charid;

    @Column(name = "stage", nullable = false)
    private Integer stage;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCharid(Integer charid) {
        this.charid = charid;
    }

    public Integer getCharid() {
        return charid;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public Integer getStage() {
        return stage;
    }

    @Override
    public String toString() {
        return "DMuLungDojo{" +
                "id=" + id + '\'' +
                "charid=" + charid + '\'' +
                "stage=" + stage + '\'' +
                '}';
    }
}
