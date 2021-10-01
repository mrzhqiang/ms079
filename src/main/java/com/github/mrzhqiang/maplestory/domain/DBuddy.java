package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.DbForeignKey;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.ConstraintMode.NO_CONSTRAINT;

@Entity
@Table(name = "buddies")
public class DBuddy extends Model {

    @Id
    public Integer id;
    @NotNull
    @DbForeignKey(noConstraint = true)
    @OneToOne
    @JoinColumn(name = "characterid")
    public DCharacter owner;
    @Column(name = "buddyid")
    public Integer buddies;
    @NotNull
    @Column(name = "pending")
    public Integer pending;
    @NotNull
    @Column(name = "groupname")
    public String groupName;

}
