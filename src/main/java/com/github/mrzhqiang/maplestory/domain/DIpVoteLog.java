package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "ipvotelog")
public class DIpVoteLog {

    @Id
    @Column(name = "vid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer vid;

    @Column(name = "accid", nullable = false)
    private String accid;

    @Column(name = "ipaddress", nullable = false)
    private String ipaddress;

    @Column(name = "votetime", nullable = false)
    private String votetime;

    @Column(name = "votetype", nullable = false)
    private Integer votetype;

    public void setVid(Integer vid) {
        this.vid = vid;
    }

    public Integer getVid() {
        return vid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getAccid() {
        return accid;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setVotetime(String votetime) {
        this.votetime = votetime;
    }

    public String getVotetime() {
        return votetime;
    }

    public void setVotetype(Integer votetype) {
        this.votetype = votetype;
    }

    public Integer getVotetype() {
        return votetype;
    }

    @Override
    public String toString() {
        return "DIpVoteLog{" +
                "vid=" + vid + '\'' +
                "accid=" + accid + '\'' +
                "ipaddress=" + ipaddress + '\'' +
                "votetime=" + votetime + '\'' +
                "votetype=" + votetype + '\'' +
                '}';
    }
}
