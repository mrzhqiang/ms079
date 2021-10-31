package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "zaksquads")
public class DZakSquad extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "channel")
    public Integer channel;
    @NotNull
    @Column(name = "leaderid")
    public Integer leaderid;
    @NotNull
    @Column(name = "status")
    public Integer status;
    @NotNull
    @Column(name = "members")
    public Integer members;

}
