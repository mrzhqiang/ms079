package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "loginlog")
public class DLoginLog {

    @Column(name = "account")
    private String account;

    @Column(name = "password")
    private String password;

    @Column(name = "logintype")
    private String logintype;

    @Column(name = "ip")
    private String ip;

    @Column(name = "time")
    private String time;

    @Column(name = "active")
    private String active;

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setLogintype(String logintype) {
        this.logintype = logintype;
    }

    public String getLogintype() {
        return logintype;
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

    public void setActive(String active) {
        this.active = active;
    }

    public String getActive() {
        return active;
    }

    @Override
    public String toString() {
        return "DLoginLog{" +
                "account=" + account + '\'' +
                "password=" + password + '\'' +
                "logintype=" + logintype + '\'' +
                "ip=" + ip + '\'' +
                "time=" + time + '\'' +
                "active=" + active + '\'' +
                '}';
    }
}
