package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cashshop_modified_items")
public class DCashShopModifiedItem {

    @Id
    public Integer serial;
    @NotNull
    public String name;
    @NotNull
    @Column(name = "discount_price")
    public Integer discountPrice;
    @NotNull
    public Integer mark;
    @NotNull
    @Column(name = "showup")
    public Boolean showup;
    @NotNull
    @Column(name = "itemid")
    public Integer itemid;
    @NotNull
    public Integer priority;
    @NotNull
    @Column(name = "package")
    public Boolean packageField;
    @NotNull
    public Integer period;
    @NotNull
    public Integer gender;
    @NotNull
    public Integer count;
    @NotNull
    public Integer meso;
    @NotNull
    @Column(name = "unk_1")
    public Integer unk1;
    @NotNull
    @Column(name = "unk_2")
    public Integer unk2;
    @NotNull
    @Column(name = "unk_3")
    public Integer unk3;
    @NotNull
    @Column(name = "extra_flags")
    public Integer extraFlags;

}
