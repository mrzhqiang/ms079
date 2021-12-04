package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "queststatusmobs")
public class DQuestStatusMob extends Model {

    @Id
    Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "quest_status_id")
    DQuestStatus questStatus;
    @NotNull
    Integer mob;
    @NotNull
    Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DQuestStatus getQuestStatus() {
        return questStatus;
    }

    public void setQuestStatus(DQuestStatus questStatus) {
        this.questStatus = questStatus;
    }

    public Integer getMob() {
        return mob;
    }

    public void setMob(Integer mob) {
        this.mob = mob;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
