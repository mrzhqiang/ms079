package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "reactordrops")
public class DReactorDrop {

    @Id
    @Column(name = "reactordropid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reactordropid;

    @Column(name = "reactorid", nullable = false)
    private Integer reactorid;

    @Column(name = "itemid", nullable = false)
    private Integer itemid;

    @Column(name = "chance", nullable = false)
    private Integer chance;

    @Column(name = "questid", nullable = false)
    private Integer questid;

    public void setReactordropid(Integer reactordropid) {
        this.reactordropid = reactordropid;
    }

    public Integer getReactordropid() {
        return reactordropid;
    }

    public void setReactorid(Integer reactorid) {
        this.reactorid = reactorid;
    }

    public Integer getReactorid() {
        return reactorid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setChance(Integer chance) {
        this.chance = chance;
    }

    public Integer getChance() {
        return chance;
    }

    public void setQuestid(Integer questid) {
        this.questid = questid;
    }

    public Integer getQuestid() {
        return questid;
    }

    @Override
    public String toString() {
        return "DReactorDrop{" +
                "reactordropid=" + reactordropid + '\'' +
                "reactorid=" + reactorid + '\'' +
                "itemid=" + itemid + '\'' +
                "chance=" + chance + '\'' +
                "questid=" + questid + '\'' +
                '}';
    }
}
