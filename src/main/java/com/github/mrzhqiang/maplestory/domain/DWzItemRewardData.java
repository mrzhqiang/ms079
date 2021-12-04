package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wz_itemrewarddata")
public class DWzItemRewardData extends Model {

    @Id
    Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    DWzItemData itemData;
    @NotNull
    Integer item;
    @NotNull
    Integer prob;
    @NotNull
    Integer quantity;
    @NotNull
    Integer period;
    @NotNull
    String worldMsg;
    @NotNull
    String effect;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DWzItemData getItemData() {
        return itemData;
    }

    public void setItemData(DWzItemData itemData) {
        this.itemData = itemData;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getProb() {
        return prob;
    }

    public void setProb(Integer prob) {
        this.prob = prob;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getWorldMsg() {
        return worldMsg;
    }

    public void setWorldMsg(String worldMsg) {
        this.worldMsg = worldMsg;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }
}
