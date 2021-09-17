package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "skills_cooldowns")
public class DSkillCooldown {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "charid", nullable = false)
    private Integer charid;

    @Column(name = "SkillID", nullable = false)
    private Integer skillID;

    @Column(name = "length", nullable = false)
    private Long length;

    @Column(name = "StartTime", nullable = false)
    private Long startTime;

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

    public void setSkillID(Integer skillID) {
        this.skillID = skillID;
    }

    public Integer getSkillID() {
        return skillID;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Long getLength() {
        return length;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "DSkillCooldown{" +
                "id=" + id + '\'' +
                "charid=" + charid + '\'' +
                "skillID=" + skillID + '\'' +
                "length=" + length + '\'' +
                "startTime=" + startTime + '\'' +
                '}';
    }
}
