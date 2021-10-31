package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mts_cart")
public class DMtsCart extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "characterid")
    public Integer characterid;
    @NotNull
    @Column(name = "itemid")
    public Integer itemid;

}
