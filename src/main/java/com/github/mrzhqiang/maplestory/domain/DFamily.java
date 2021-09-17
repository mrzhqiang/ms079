package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "families")
public class DFamily {

    @Id
    @Column(name = "familyid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer familyid;

    @Column(name = "leaderid", nullable = false)
    private Integer leaderid;

    @Column(name = "notice", nullable = false)
    private String notice;

    public void setFamilyid(Integer familyid) {
        this.familyid = familyid;
    }

    public Integer getFamilyid() {
        return familyid;
    }

    public void setLeaderid(Integer leaderid) {
        this.leaderid = leaderid;
    }

    public Integer getLeaderid() {
        return leaderid;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getNotice() {
        return notice;
    }

    @Override
    public String toString() {
        return "DFamily{" +
                "familyid=" + familyid + '\'' +
                "leaderid=" + leaderid + '\'' +
                "notice=" + notice + '\'' +
                '}';
    }
}
