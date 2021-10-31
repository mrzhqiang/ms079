package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "queststatusmobs")
public class DQuestStatusMob extends Model {

    @Id
    @Column(name = "queststatusmobid")
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "queststatusid")
    public DQuestStatus questStatus;
    @NotNull
    @Column(name = "mob")
    public Integer mob;
    @NotNull
    @Column(name = "count")
    public Integer count;

}
