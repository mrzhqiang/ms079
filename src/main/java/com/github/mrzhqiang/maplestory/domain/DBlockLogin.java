package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "blocklogin")
public class DBlockLogin {

    @Column(name = "account")
    private String account;

    @Column(name = "blocktime")
    private String blocktime;

    @Column(name = "unblocktime")
    private String unblocktime;

    @Column(name = "ip")
    private String ip;

    @Column(name = "active")
    private String active;

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setBlocktime(String blocktime) {
        this.blocktime = blocktime;
    }

    public String getBlocktime() {
        return blocktime;
    }

    public void setUnblocktime(String unblocktime) {
        this.unblocktime = unblocktime;
    }

    public String getUnblocktime() {
        return unblocktime;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive() {
        return active;
    }

    @Override
    public String toString() {
        return "DBlockLogin{" +
                "account=" + account + '\'' +
                "blocktime=" + blocktime + '\'' +
                "unblocktime=" + unblocktime + '\'' +
                "ip=" + ip + '\'' +
                "active=" + active + '\'' +
                '}';
    }
}
