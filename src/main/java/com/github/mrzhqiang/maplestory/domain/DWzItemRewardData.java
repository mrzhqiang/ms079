package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wz_itemrewarddata")
public class DWzItemRewardData extends Model {

    @Id
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "itemid")
    public DWzItemData itemData;
    @NotNull
    @Column(name = "item")
    public Integer item;
    @NotNull
    @Column(name = "prob")
    public Integer prob;
    @NotNull
    @Column(name = "quantity")
    public Integer quantity;
    @NotNull
    @Column(name = "period")
    public Integer period;
    @NotNull
    @Column(name = "worldMsg")
    public String worldMsg;
    @NotNull
    @Column(name = "effect")
    public String effect;

}
