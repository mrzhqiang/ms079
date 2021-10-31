package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "auth_server_cs")
public class DAuthServerCs extends Model {

    @Id
    @Column(name = "CashShopServerId")
    public Integer id;
    @NotNull
    @Column(name = "key")
    public String key;
    @NotNull
    @Column(name = "world")
    public Integer world;

}
