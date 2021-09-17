package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "queststatus")
public class DQuestStatus {

    @Id
    @Column(name = "queststatusid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer queststatusid;

    @Column(name = "characterid", nullable = false)
    private Integer characterid;

    @Column(name = "quest", nullable = false)
    private Integer quest;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "time", nullable = false)
    private Integer time;

    @Column(name = "forfeited", nullable = false)
    private Integer forfeited;

    @Column(name = "customData")
    private String customData;

    public void setQueststatusid(Integer queststatusid) {
        this.queststatusid = queststatusid;
    }

    public Integer getQueststatusid() {
        return queststatusid;
    }

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setQuest(Integer quest) {
        this.quest = quest;
    }

    public Integer getQuest() {
        return quest;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getTime() {
        return time;
    }

    public void setForfeited(Integer forfeited) {
        this.forfeited = forfeited;
    }

    public Integer getForfeited() {
        return forfeited;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public String getCustomData() {
        return customData;
    }

    @Override
    public String toString() {
        return "DQuestStatus{" +
                "queststatusid=" + queststatusid + '\'' +
                "characterid=" + characterid + '\'' +
                "quest=" + quest + '\'' +
                "status=" + status + '\'' +
                "time=" + time + '\'' +
                "forfeited=" + forfeited + '\'' +
                "customData=" + customData + '\'' +
                '}';
    }
}
