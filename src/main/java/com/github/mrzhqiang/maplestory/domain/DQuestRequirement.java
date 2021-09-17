package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "questrequirements")
public class DQuestRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "questrequirementid", nullable = false)
    private Integer questrequirementid;

    @Column(name = "questid", nullable = false)
    private Integer questid;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "data", nullable = false)
    private byte[] data;

    public void setQuestrequirementid(Integer questrequirementid) {
        this.questrequirementid = questrequirementid;
    }

    public Integer getQuestrequirementid() {
        return questrequirementid;
    }

    public void setQuestid(Integer questid) {
        this.questid = questid;
    }

    public Integer getQuestid() {
        return questid;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "DQuestRequirement{" +
                "questrequirementid=" + questrequirementid + '\'' +
                "questid=" + questid + '\'' +
                "status=" + status + '\'' +
                "data=" + data + '\'' +
                '}';
    }
}
