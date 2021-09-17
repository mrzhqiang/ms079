package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fishing_rewards")
public class DFishingReward {

    @Column(name = "itemid", nullable = false)
    private Integer itemid;

    @Column(name = "chance", nullable = false)
    private Integer chance;

    @Column(name = "expiration")
    private Integer expiration;

    @Column(name = "name")
    private String name;

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setChance(Integer chance) {
        this.chance = chance;
    }

    public Integer getChance() {
        return chance;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }

    public Integer getExpiration() {
        return expiration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DFishingReward{" +
                "itemid=" + itemid + '\'' +
                "chance=" + chance + '\'' +
                "expiration=" + expiration + '\'' +
                "name=" + name + '\'' +
                '}';
    }
}
