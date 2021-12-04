package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "shopitems")
public class DShopItem extends Model {

    @Id
    Integer id;
    @NotNull
    Integer shopId;
    @NotNull
    Integer itemId;
    @NotNull
    Integer price;
    @NotNull
    Integer pitch;
    /**
     * sort 是一个任意字段，旨在在修改商店时留出余地。
     * 最小的数字是 104，它为每个项目增加 4，以便为交换插入删除项目留出足够的空间。
     */
    @NotNull
    Integer position;
    Integer reqItem;
    Integer reqItemQ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPitch() {
        return pitch;
    }

    public void setPitch(Integer pitch) {
        this.pitch = pitch;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getReqItem() {
        return reqItem;
    }

    public void setReqItem(Integer reqItem) {
        this.reqItem = reqItem;
    }

    public Integer getReqItemQ() {
        return reqItemQ;
    }

    public void setReqItemQ(Integer reqItemQ) {
        this.reqItemQ = reqItemQ;
    }
}
