package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shops")
public class DShop extends Model {

    @Id
    @Column(name = "shopid")
    public Integer id;
    @NotNull
    @Column(name = "npcid")
    public Integer npcid;

}
