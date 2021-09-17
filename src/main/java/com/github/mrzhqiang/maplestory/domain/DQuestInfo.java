package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "questinfo")
public class DQuestInfo {

    @Id
    @Column(name = "questinfoid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questinfoid;

    @Column(name = "characterid", nullable = false)
    private Integer characterid;

    @Column(name = "quest", nullable = false)
    private Integer quest;

    @Column(name = "customData")
    private String customData;

    public void setQuestinfoid(Integer questinfoid) {
        this.questinfoid = questinfoid;
    }

    public Integer getQuestinfoid() {
        return questinfoid;
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

    public void setCustomData(String customData) {
        this.customData = customData;
    }

    public String getCustomData() {
        return customData;
    }

    @Override
    public String toString() {
        return "DQuestInfo{" +
                "questinfoid=" + questinfoid + '\'' +
                "characterid=" + characterid + '\'' +
                "quest=" + quest + '\'' +
                "customData=" + customData + '\'' +
                '}';
    }
}
