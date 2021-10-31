package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "auth_server_channel")
public class DAuthServerChannel extends Model {

    @Id
    @Column(name = "channelid")
    public Integer id;
    @NotNull
    @Column(name = "world")
    public Integer world;
    @Column(name = "number")
    public Integer number;
    @NotNull
    @Column(name = "key")
    public String key;

}
