package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "drop_data_global")
public class DDropDataGlobal extends Model {

    @Id
    public Long id;
    @NotNull
    @Column(name = "continent")
    public Integer continent;
    @NotNull
    @Column(name = "dropType")
    public Integer dropType;
    @NotNull
    @Column(name = "itemid")
    public Integer itemid;
    @NotNull
    @Column(name = "minimum_quantity")
    public Integer minQuantity;
    @NotNull
    @Column(name = "maximum_quantity")
    public Integer maxQuantity;
    @NotNull
    @Column(name = "questid")
    public Integer questid;
    @NotNull
    @Column(name = "chance")
    public Integer chance;
    @Column(name = "comments")
    public String comments;

}
