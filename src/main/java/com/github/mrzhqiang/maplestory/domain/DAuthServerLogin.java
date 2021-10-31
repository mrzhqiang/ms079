package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "auth_server_login")
public class DAuthServerLogin extends Model {

    @Id
    @Column(name = "loginserverid")
    public Integer id;
    @NotNull
    @Column(name = "key")
    public String key;
    @NotNull
    @Column(name = "world")
    public Integer world;

}
