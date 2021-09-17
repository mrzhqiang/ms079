package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "aclog")
public class DAcLog {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "accid", nullable = false)
    private Integer accid;

    @Column(name = "bossid", nullable = false)
    private String bossid;

    @Column(name = "lastattempt", nullable = false)
    private Date lastattempt;

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

    public void setLastattempt(Date lastattempt) {
        this.lastattempt = lastattempt;
    }

    public Date getLastattempt() {
        return lastattempt;
    }

    @Override
    public String toString() {
        return "DAcLog{" +
                "id=" + id + '\'' +
                "accid=" + accid + '\'' +
                "bossid=" + bossid + '\'' +
                "lastattempt=" + lastattempt + '\'' +
                '}';
    }
}
