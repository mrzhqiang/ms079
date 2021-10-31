package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@Table(name = "blocklogin")
public class DBlockLogin extends Model {

    @Column(name = "account")
    public String account;
    @Column(name = "blocktime")
    public String blocktime;
    @Column(name = "unblocktime")
    public String unblocktime;
    @Column(name = "ip")
    public String ip;
    @Column(name = "active")
    public String active;

}
