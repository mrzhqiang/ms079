package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wz_itemdata")
public class DWzItemData extends Model {

    @Id
    @Column(name = "itemid")
    public Integer id;
    @Column(name = "name")
    public String name;
    @Column(name = "msg")
    public String msg;
    @Column(name = "desc")
    public String desc;
    @NotNull
    @Column(name = "slotMax")
    public Integer slotMax;
    @NotNull
    @Column(name = "price")
    public String price;
    @NotNull
    @Column(name = "wholePrice")
    public Integer wholePrice;
    @NotNull
    @Column(name = "stateChange")
    public Integer stateChange;
    @NotNull
    @Column(name = "flags")
    public Integer flags;
    @NotNull
    @Column(name = "karma")
    public Integer karma;
    @NotNull
    @Column(name = "meso")
    public Integer meso;
    @NotNull
    @Column(name = "monsterBook")
    public Integer monsterBook;
    @NotNull
    @Column(name = "itemMakeLevel")
    public Integer itemMakeLevel;
    @NotNull
    @Column(name = "questId")
    public Integer questId;
    @Column(name = "scrollReqs")
    public String scrollReqs;
    @Column(name = "consumeItem")
    public String consumeItem;
    @NotNull
    @Column(name = "totalprob")
    public Integer totalprob;
    @NotNull
    @Column(name = "incSkill")
    public String incSkill;
    @NotNull
    @Column(name = "replaceid")
    public Integer replaceid;
    @NotNull
    @Column(name = "replacemsg")
    public String replacemsg;
    @NotNull
    @Column(name = "create")
    public Integer create;
    @NotNull
    @Column(name = "afterImage")
    public String afterImage;

}
