package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "monsterbook")
public class DMonsterBook {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "charid", nullable = false)
    private Integer charid;

    @Column(name = "cardid", nullable = false)
    private Integer cardid;

    @Column(name = "level")
    private Integer level;

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

    public void setCardid(Integer cardid) {
        this.cardid = cardid;
    }

    public Integer getCardid() {
        return cardid;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "DMonsterBook{" +
                "id=" + id + '\'' +
                "charid=" + charid + '\'' +
                "cardid=" + cardid + '\'' +
                "level=" + level + '\'' +
                '}';
    }
}
