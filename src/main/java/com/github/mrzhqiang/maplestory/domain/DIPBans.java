package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "ipbans")
public class DIPBans {

    @Id
    @Column(name = "ipbanid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ipbanid;

    @Column(name = "ip", nullable = false)
    private String ip;

    public void setIpbanid(Integer ipbanid) {
        this.ipbanid = ipbanid;
    }

    public Integer getIpbanid() {
        return ipbanid;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "DIPBans{" +
                "ipbanid=" + ipbanid + '\'' +
                "ip=" + ip + '\'' +
                '}';
    }
}
