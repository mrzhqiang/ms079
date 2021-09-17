package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "wz_questactitemdata")
public class DWzQuestActItemData {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "itemid", nullable = false)
    private Integer itemid;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "period", nullable = false)
    private Integer period;

    @Column(name = "gender", nullable = false)
    private Integer gender;

    @Column(name = "job", nullable = false)
    private Integer job;

    @Column(name = "jobEx", nullable = false)
    private Integer jobEx;

    @Column(name = "prop", nullable = false)
    private Integer prop;

    @Column(name = "uniqueid", nullable = false)
    private Integer uniqueid;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getGender() {
        return gender;
    }

    public void setJob(Integer job) {
        this.job = job;
    }

    public Integer getJob() {
        return job;
    }

    public void setJobEx(Integer jobEx) {
        this.jobEx = jobEx;
    }

    public Integer getJobEx() {
        return jobEx;
    }

    public void setProp(Integer prop) {
        this.prop = prop;
    }

    public Integer getProp() {
        return prop;
    }

    public void setUniqueid(Integer uniqueid) {
        this.uniqueid = uniqueid;
    }

    public Integer getUniqueid() {
        return uniqueid;
    }

    @Override
    public String toString() {
        return "DWzQuestActItemData{" +
                "id=" + id + '\'' +
                "itemid=" + itemid + '\'' +
                "count=" + count + '\'' +
                "period=" + period + '\'' +
                "gender=" + gender + '\'' +
                "job=" + job + '\'' +
                "jobEx=" + jobEx + '\'' +
                "prop=" + prop + '\'' +
                "uniqueid=" + uniqueid + '\'' +
                '}';
    }
}
