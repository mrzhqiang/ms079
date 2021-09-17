package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "uselog")
public class DUseLog {

    @Column(name = "account")
    private String account;

    @Column(name = "ip")
    private String ip;

    @Column(name = "time")
    private String time;

    @Column(name = "usetype")
    private String usetype;

    @Column(name = "active")
    private String active;

    @Column(name = "newpassword")
    private String newpassword;

    @Column(name = "oldpassword")
    private String oldpassword;

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setUsetype(String usetype) {
        this.usetype = usetype;
    }

    public String getUsetype() {
        return usetype;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive() {
        return active;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    @Override
    public String toString() {
        return "DUseLog{" +
                "account=" + account + '\'' +
                "ip=" + ip + '\'' +
                "time=" + time + '\'' +
                "usetype=" + usetype + '\'' +
                "active=" + active + '\'' +
                "newpassword=" + newpassword + '\'' +
                "oldpassword=" + oldpassword + '\'' +
                '}';
    }
}
