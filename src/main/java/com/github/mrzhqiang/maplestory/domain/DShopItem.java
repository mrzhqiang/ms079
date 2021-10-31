package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "shopitems")
public class DShopItem extends Model {

    @Id
    @Column(name = "shopitemid")
    public Integer id;
    @NotNull
    @Column(name = "shopid")
    public Integer shopid;
    @NotNull
    @Column(name = "itemid")
    public Integer itemid;
    @NotNull
    @Column(name = "price")
    public Integer price;
    @NotNull
    @Column(name = "pitch")
    public Integer pitch;
    /**
     * sort 是一个任意字段，旨在在修改商店时留出余地。
     * 最小的数字是 104，它为每个项目增加 4，以便为交换插入删除项目留出足够的空间。
     */
    @NotNull
    @Column(name = "position")
    public Integer position;

    @Column(name = "reqitem")
    public Integer reqitem;

    @Column(name = "reqitemq")
    public Integer reqitemq;

}
