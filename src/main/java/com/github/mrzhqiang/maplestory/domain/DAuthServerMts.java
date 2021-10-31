package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "auth_server_mts")
public class DAuthServerMts extends Model {

    @Id
    @Column(name = "MTSServerId")
    public Integer id;
    @NotNull
    @Column(name = "key")
    public String key;
    @NotNull
    @Column(name = "world")
    public Integer world;

}
