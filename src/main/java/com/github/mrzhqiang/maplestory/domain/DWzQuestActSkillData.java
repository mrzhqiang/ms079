package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "wz_questactskilldata")
public class DWzQuestActSkillData {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "skillid", nullable = false)
    private Integer skillid;

    @Column(name = "skillLevel", nullable = false)
    private Integer skillLevel;

    @Column(name = "masterLevel", nullable = false)
    private Integer masterLevel;

    @Column(name = "uniqueid", nullable = false)
    private Integer uniqueid;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setSkillid(Integer skillid) {
        this.skillid = skillid;
    }

    public Integer getSkillid() {
        return skillid;
    }

    public void setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
    }

    public Integer getSkillLevel() {
        return skillLevel;
    }

    public void setMasterLevel(Integer masterLevel) {
        this.masterLevel = masterLevel;
    }

    public Integer getMasterLevel() {
        return masterLevel;
    }

    public void setUniqueid(Integer uniqueid) {
        this.uniqueid = uniqueid;
    }

    public Integer getUniqueid() {
        return uniqueid;
    }

    @Override
    public String toString() {
        return "DWzQuestActSkillData{" +
                "id=" + id + '\'' +
                "skillid=" + skillid + '\'' +
                "skillLevel=" + skillLevel + '\'' +
                "masterLevel=" + masterLevel + '\'' +
                "uniqueid=" + uniqueid + '\'' +
                '}';
    }
}
