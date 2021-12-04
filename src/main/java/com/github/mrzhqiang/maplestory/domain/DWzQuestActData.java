package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wz_questactdata")
public class DWzQuestActData extends Model {

    @Id
    Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "quest_id")
    DWzQuestData questData;
    @NotNull
    String name;
    @NotNull
    Integer type;
    @NotNull
    Integer intStore;
    @NotNull
    String applicableJobs;
    @NotNull
    Integer uniqueId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DWzQuestData getQuestData() {
        return questData;
    }

    public void setQuestData(DWzQuestData questData) {
        this.questData = questData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIntStore() {
        return intStore;
    }

    public void setIntStore(Integer intStore) {
        this.intStore = intStore;
    }

    public String getApplicableJobs() {
        return applicableJobs;
    }

    public void setApplicableJobs(String applicableJobs) {
        this.applicableJobs = applicableJobs;
    }

    public Integer getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }
}
