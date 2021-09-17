package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "macbans")
public class DMacBans {

    @Id
    @Column(name = "macbanid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer macbanid;

    @Column(name = "mac", nullable = false)
    private String mac;

    public void setMacbanid(Integer macbanid) {
        this.macbanid = macbanid;
    }

    public Integer getMacbanid() {
        return macbanid;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMac() {
        return mac;
    }

    @Override
    public String toString() {
        return "DMacBans{" +
                "macbanid=" + macbanid + '\'' +
                "mac=" + mac + '\'' +
                '}';
    }
}
