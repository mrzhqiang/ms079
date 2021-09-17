package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "wz_itemrewarddata")
public class DWzItemRewardData {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "itemid", nullable = false)
    private Integer itemid;

    @Column(name = "item", nullable = false)
    private Integer item;

    @Column(name = "prob", nullable = false)
    private Integer prob;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "period", nullable = false)
    private Integer period;

    @Column(name = "worldMsg", nullable = false)
    private String worldMsg;

    @Column(name = "effect", nullable = false)
    private String effect;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getItem() {
        return item;
    }

    public void setProb(Integer prob) {
        this.prob = prob;
    }

    public Integer getProb() {
        return prob;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setWorldMsg(String worldMsg) {
        this.worldMsg = worldMsg;
    }

    public String getWorldMsg() {
        return worldMsg;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getEffect() {
        return effect;
    }

    @Override
    public String toString() {
        return "DWzItemRewardData{" +
                "id=" + id + '\'' +
                "itemid=" + itemid + '\'' +
                "item=" + item + '\'' +
                "prob=" + prob + '\'' +
                "quantity=" + quantity + '\'' +
                "period=" + period + '\'' +
                "worldMsg=" + worldMsg + '\'' +
                "effect=" + effect + '\'' +
                '}';
    }
}
