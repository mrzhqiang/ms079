package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "prizelog")
public class DPrizeLog {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "accid", nullable = false)
    private Integer accid;

    @Column(name = "bossid", nullable = false)
    private String bossid;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setAccid(Integer accid) {
        this.accid = accid;
    }

    public Integer getAccid() {
        return accid;
    }

    public void setBossid(String bossid) {
        this.bossid = bossid;
    }

    public String getBossid() {
        return bossid;
    }

    @Override
    public String toString() {
        return "DPrizeLog{" +
                "id=" + id + '\'' +
                "accid=" + accid + '\'' +
                "bossid=" + bossid + '\'' +
                '}';
    }
}
