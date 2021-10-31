package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "achievements")
public class DAchievement extends Model {

    @EmbeddedId
    public PKAchievement achievement;

    @NotNull
    @OneToOne
    @JoinColumn(name = "accountid")
    public DAccount account;

}
