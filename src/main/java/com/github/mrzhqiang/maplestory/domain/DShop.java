package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "shops")
public class DShop {

    @Id
    @Column(name = "shopid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shopid;

    @Column(name = "npcid", nullable = false)
    private Integer npcid;

    public void setShopid(Integer shopid) {
        this.shopid = shopid;
    }

    public Integer getShopid() {
        return shopid;
    }

    public void setNpcid(Integer npcid) {
        this.npcid = npcid;
    }

    public Integer getNpcid() {
        return npcid;
    }

    @Override
    public String toString() {
        return "DShop{" +
                "shopid=" + shopid + '\'' +
                "npcid=" + npcid + '\'' +
                '}';
    }
}
