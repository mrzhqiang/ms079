package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;

import javax.persistence.*;

@Entity
@Table(name = "mountdata")
public class DMountData extends Model {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "characterid")
    private Integer characterid;

    @Column(name = "Level", nullable = false)
    private Integer level;

    @Column(name = "Exp", nullable = false)
    private Integer exp;

    @Column(name = "Fatigue", nullable = false)
    private Integer fatigue;

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

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Integer getExp() {
        return exp;
    }

    public void setFatigue(Integer fatigue) {
        this.fatigue = fatigue;
    }

    public Integer getFatigue() {
        return fatigue;
    }

    @Override
    public String toString() {
        return "DMountData{" +
                "id=" + id + '\'' +
                "characterid=" + characterid + '\'' +
                "level=" + level + '\'' +
                "exp=" + exp + '\'' +
                "fatigue=" + fatigue + '\'' +
                '}';
    }
}
