package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@Table(name = "uselog")
public class DUseLog extends Model {

    @Column(name = "account")
    public String account;

    @Column(name = "ip")
    public String ip;

    @Column(name = "time")
    public String time;

    @Column(name = "usetype")
    public String usetype;

    @Column(name = "active")
    public String active;

    @Column(name = "newpassword")
    public String newpassword;

    @Column(name = "oldpassword")
    public String oldpassword;

}
