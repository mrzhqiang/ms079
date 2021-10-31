package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@Table(name = "loginlog")
public class DLoginLog extends Model {

    @Column(name = "account")
    public String account;

    @Column(name = "password")
    public String password;

    @Column(name = "logintype")
    public String logintype;

    @Column(name = "ip")
    public String ip;

    @Column(name = "time")
    public String time;

    @Column(name = "active")
    public String active;

}
