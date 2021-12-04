package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@Table(name = "loginlog")
public class DLoginLog extends Model {

    String account;
    String password;
    String loginType;
    String ip;
    String time;
    String active;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
