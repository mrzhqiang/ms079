package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cheatlog")
public class DCheatLog {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "characterid", nullable = false)
    private Integer characterid;

    @Column(name = "offense", nullable = false)
    private String offense;

    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "lastoffensetime", nullable = false)
    private Date lastoffensetime;

    @Column(name = "param", nullable = false)
    private String param;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setOffense(String offense) {
        this.offense = offense;
    }

    public String getOffense() {
        return offense;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setLastoffensetime(Date lastoffensetime) {
        this.lastoffensetime = lastoffensetime;
    }

    public Date getLastoffensetime() {
        return lastoffensetime;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    @Override
    public String toString() {
        return "DCheatLog{" +
                "id=" + id + '\'' +
                "characterid=" + characterid + '\'' +
                "offense=" + offense + '\'' +
                "count=" + count + '\'' +
                "lastoffensetime=" + lastoffensetime + '\'' +
                "param=" + param + '\'' +
                '}';
    }
}
