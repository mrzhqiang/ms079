package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "skills_cooldowns")
public class DSkillCooldown extends Model {

    @Id
    Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "char_id")
    DCharacter character;
    @NotNull
    Integer skillId;
    @NotNull
    Long length;
    @NotNull
    Long startTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DCharacter getCharacter() {
        return character;
    }

    public void setCharacter(DCharacter character) {
        this.character = character;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
}
