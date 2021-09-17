package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "questmonster")
public class DQuestMonster {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "questid")
    private Integer questid;

    @Column(name = "monsterid")
    private Integer monsterid;

    @Column(name = "zt", nullable = false)
    private Integer zt;

    @Column(name = "charid")
    private Integer charid;

    @Column(name = "name")
    private String name;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setQuestid(Integer questid) {
        this.questid = questid;
    }

    public Integer getQuestid() {
        return questid;
    }

    public void setMonsterid(Integer monsterid) {
        this.monsterid = monsterid;
    }

    public Integer getMonsterid() {
        return monsterid;
    }

    public void setZt(Integer zt) {
        this.zt = zt;
    }

    public Integer getZt() {
        return zt;
    }

    public void setCharid(Integer charid) {
        this.charid = charid;
    }

    public Integer getCharid() {
        return charid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DQuestMonster{" +
                "id=" + id + '\'' +
                "questid=" + questid + '\'' +
                "monsterid=" + monsterid + '\'' +
                "zt=" + zt + '\'' +
                "charid=" + charid + '\'' +
                "name=" + name + '\'' +
                '}';
    }
}
