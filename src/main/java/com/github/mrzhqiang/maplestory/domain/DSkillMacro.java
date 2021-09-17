package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "skillmacros")
public class DSkillMacro {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "characterid", nullable = false)
    private Integer characterid;

    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "skill1", nullable = false)
    private Integer skill1;

    @Column(name = "skill2", nullable = false)
    private Integer skill2;

    @Column(name = "skill3", nullable = false)
    private Integer skill3;

    @Column(name = "name")
    private String name;

    @Column(name = "shout", nullable = false)
    private Integer shout;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPosition() {
        return position;
    }

    public void setSkill1(Integer skill1) {
        this.skill1 = skill1;
    }

    public Integer getSkill1() {
        return skill1;
    }

    public void setSkill2(Integer skill2) {
        this.skill2 = skill2;
    }

    public Integer getSkill2() {
        return skill2;
    }

    public void setSkill3(Integer skill3) {
        this.skill3 = skill3;
    }

    public Integer getSkill3() {
        return skill3;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setShout(Integer shout) {
        this.shout = shout;
    }

    public Integer getShout() {
        return shout;
    }

    @Override
    public String toString() {
        return "DSkillMacro{" +
                "id=" + id + '\'' +
                "characterid=" + characterid + '\'' +
                "position=" + position + '\'' +
                "skill1=" + skill1 + '\'' +
                "skill2=" + skill2 + '\'' +
                "skill3=" + skill3 + '\'' +
                "name=" + name + '\'' +
                "shout=" + shout + '\'' +
                '}';
    }
}
