package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "skillmacros")
public class DSkillMacro extends Model {

    @Id
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @NotNull
    @Column(name = "position")
    public Integer position;
    @NotNull
    @Column(name = "skill1")
    public Integer skill1;
    @NotNull
    @Column(name = "skill2")
    public Integer skill2;
    @NotNull
    @Column(name = "skill3")
    public Integer skill3;
    @Column(name = "name")
    public String name;
    @NotNull
    @Column(name = "shout")
    public Integer shout;

}
