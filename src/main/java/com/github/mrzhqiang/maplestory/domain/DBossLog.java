package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bosslog")
public class DBossLog {

    @Id
    @Column(name = "bosslogid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bosslogid;

    @Column(name = "characterid", nullable = false)
    private Integer characterid;

    @Column(name = "bossid", nullable = false)
    private String bossid;

    @Column(name = "lastattempt", nullable = false)
    private Date lastattempt;

    public void setBosslogid(Integer bosslogid) {
        this.bosslogid = bosslogid;
    }

    public Integer getBosslogid() {
        return bosslogid;
    }

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
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
        return "DBossLog{" +
                "bosslogid=" + bosslogid + '\'' +
                "characterid=" + characterid + '\'' +
                "bossid=" + bossid + '\'' +
                "lastattempt=" + lastattempt + '\'' +
                '}';
    }
}
