package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cashshop_modified_items")
public class DCashShopModifiedItem extends Model {

    @Id
    @Column(name = "serial")
    public Integer id;
    @NotNull
    @Column(name = "name")
    public String name;
    @NotNull
    @Column(name = "discount_price")
    public Integer discountPrice;
    @NotNull
    @Column(name = "mark")
    public Integer mark;
    @NotNull
    @Column(name = "showup")
    public Boolean showup;
    @NotNull
    @Column(name = "itemid")
    public Integer itemid;
    @NotNull
    @Column(name = "priority")
    public Integer priority;
    @NotNull
    @Column(name = "package")
    public Boolean packageField;
    @NotNull
    @Column(name = "period")
    public Integer period;
    @NotNull
    @Column(name = "gender")
    public Integer gender;
    @NotNull
    @Column(name = "count")
    public Integer count;
    @NotNull
    @Column(name = "meso")
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
