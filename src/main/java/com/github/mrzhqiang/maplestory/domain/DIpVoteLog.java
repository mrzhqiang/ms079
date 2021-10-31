package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ipvotelog")
public class DIpVoteLog extends Model {

    @Id
    @Column(name = "vid")
    public Integer vid;
    @NotNull
    @Column(name = "accid")
    public String accid;
    @NotNull
    @Column(name = "ipaddress")
    public String ipaddress;
    @NotNull
    @Column(name = "votetime")
    public String votetime;
    @NotNull
    @Column(name = "votetype")
    public Integer votetype;

}
