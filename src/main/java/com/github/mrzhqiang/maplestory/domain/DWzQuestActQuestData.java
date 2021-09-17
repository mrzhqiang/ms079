package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "wz_questactquestdata")
public class DWzQuestActQuestData {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quest", nullable = false)
    private Integer quest;

    @Column(name = "state", nullable = false)
    private Integer state;

    @Column(name = "uniqueid", nullable = false)
    private Integer uniqueid;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setQuest(Integer quest) {
        this.quest = quest;
    }

    public Integer getQuest() {
        return quest;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return state;
    }

    public void setUniqueid(Integer uniqueid) {
        this.uniqueid = uniqueid;
    }

    public Integer getUniqueid() {
        return uniqueid;
    }

    @Override
    public String toString() {
        return "DWzQuestActQuestData{" +
                "id=" + id + '\'' +
                "quest=" + quest + '\'' +
                "state=" + state + '\'' +
                "uniqueid=" + uniqueid + '\'' +
                '}';
    }
}
