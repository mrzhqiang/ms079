package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "skills_cooldowns")
public class DSkillCooldown extends Model {

    @Id
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "charid")
    public DCharacter character;
    @NotNull
    @Column(name = "SkillID")
    public Integer skillID;
    @NotNull
    @Column(name = "length")
    public Long length;
    @NotNull
    @Column(name = "StartTime")
    public Long startTime;

}
