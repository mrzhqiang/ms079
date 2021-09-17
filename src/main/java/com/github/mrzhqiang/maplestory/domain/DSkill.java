package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "skills")
public class DSkill {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "skillid", nullable = false)
    private Integer skillid;

    @Column(name = "characterid", nullable = false)
    private Integer characterid;

    @Column(name = "skilllevel", nullable = false)
    private Integer skilllevel;

    @Column(name = "masterlevel", nullable = false)
    private Integer masterlevel;

    @Column(name = "expiration", nullable = false)
    private Long expiration;

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

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setSkilllevel(Integer skilllevel) {
        this.skilllevel = skilllevel;
    }

    public Integer getSkilllevel() {
        return skilllevel;
    }

    public void setMasterlevel(Integer masterlevel) {
        this.masterlevel = masterlevel;
    }

    public Integer getMasterlevel() {
        return masterlevel;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public Long getExpiration() {
        return expiration;
    }

    @Override
    public String toString() {
        return "DSkill{" +
                "id=" + id + '\'' +
                "skillid=" + skillid + '\'' +
                "characterid=" + characterid + '\'' +
                "skilllevel=" + skilllevel + '\'' +
                "masterlevel=" + masterlevel + '\'' +
                "expiration=" + expiration + '\'' +
                '}';
    }
}
