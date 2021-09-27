package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
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
    @OneToOne
    @JoinColumn(name = "characterid", foreignKey = @ForeignKey(NO_CONSTRAINT))
    public DCharacter owner;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "buddyid", foreignKey = @ForeignKey(NO_CONSTRAINT))
    public List<DCharacter> buddies;
    @NotNull
    @Column(name = "pending")
    public Integer pending;
    @NotNull
    @Column(name = "groupname")
    public String groupName;

}
