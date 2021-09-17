package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "questactions")
public class DQuestAction {

    @Id
    @Column(name = "questactionid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questactionid;

    @Column(name = "questid", nullable = false)
    private Integer questid;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "data", nullable = false)
    private byte[] data;

    public void setQuestactionid(Integer questactionid) {
        this.questactionid = questactionid;
    }

    public Integer getQuestactionid() {
        return questactionid;
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
        return "DQuestAction{" +
                "questactionid=" + questactionid + '\'' +
                "questid=" + questid + '\'' +
                "status=" + status + '\'' +
                "data=" + data + '\'' +
                '}';
    }
}
