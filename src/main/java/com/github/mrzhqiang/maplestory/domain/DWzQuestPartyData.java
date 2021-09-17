package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "wz_questpartydata")
public class DWzQuestPartyData {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "questid", nullable = false)
    private Integer questid;

    @Column(name = "rank", nullable = false)
    private String rank;

    @Column(name = "mode", nullable = false)
    private String mode;

    @Column(name = "property", nullable = false)
    private String property;

    @Column(name = "value", nullable = false)
    private Integer value;

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

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DWzQuestPartyData{" +
                "id=" + id + '\'' +
                "questid=" + questid + '\'' +
                "rank=" + rank + '\'' +
                "mode=" + mode + '\'' +
                "property=" + property + '\'' +
                "value=" + value + '\'' +
                '}';
    }
}
