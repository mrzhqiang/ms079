package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "wz_questactdata")
public class DWzQuestActData {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "questid", nullable = false)
    private Integer questid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "intStore", nullable = false)
    private Integer intStore;

    @Column(name = "applicableJobs", nullable = false)
    private String applicableJobs;

    @Column(name = "uniqueid", nullable = false)
    private Integer uniqueid;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setQuestid(Integer questid) {
        this.questid = questid;
    }

    public Integer getQuestid() {
        return questid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setIntStore(Integer intStore) {
        this.intStore = intStore;
    }

    public Integer getIntStore() {
        return intStore;
    }

    public void setApplicableJobs(String applicableJobs) {
        this.applicableJobs = applicableJobs;
    }

    public String getApplicableJobs() {
        return applicableJobs;
    }

    public void setUniqueid(Integer uniqueid) {
        this.uniqueid = uniqueid;
    }

    public Integer getUniqueid() {
        return uniqueid;
    }

    @Override
    public String toString() {
        return "DWzQuestActData{" +
                "id=" + id + '\'' +
                "questid=" + questid + '\'' +
                "name=" + name + '\'' +
                "type=" + type + '\'' +
                "intStore=" + intStore + '\'' +
                "applicableJobs=" + applicableJobs + '\'' +
                "uniqueid=" + uniqueid + '\'' +
                '}';
    }
}
