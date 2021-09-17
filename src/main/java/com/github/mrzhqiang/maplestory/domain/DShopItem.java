package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "shopitems")
public class DShopItem {

    @Id
    @Column(name = "shopitemid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shopitemid;

    @Column(name = "shopid", nullable = false)
    private Integer shopid;

    @Column(name = "itemid", nullable = false)
    private Integer itemid;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "pitch", nullable = false)
    private Integer pitch;

    /**
     * sort is an arbitrary field designed to give leeway when modifying shops. The lowest number is 104 and it increments by 4 for each item to allow decent space for swapping/inserting/removing items.
     */
    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "reqitem")
    private Integer reqitem;

    @Column(name = "reqitemq")
    private Integer reqitemq;

    public void setShopitemid(Integer shopitemid) {
        this.shopitemid = shopitemid;
    }

    public Integer getShopitemid() {
        return shopitemid;
    }

    public void setShopid(Integer shopid) {
        this.shopid = shopid;
    }

    public Integer getShopid() {
        return shopid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPitch(Integer pitch) {
        this.pitch = pitch;
    }

    public Integer getPitch() {
        return pitch;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPosition() {
        return position;
    }

    public void setReqitem(Integer reqitem) {
        this.reqitem = reqitem;
    }

    public Integer getReqitem() {
        return reqitem;
    }

    public void setReqitemq(Integer reqitemq) {
        this.reqitemq = reqitemq;
    }

    public Integer getReqitemq() {
        return reqitemq;
    }

    @Override
    public String toString() {
        return "DShopItem{" +
                "shopitemid=" + shopitemid + '\'' +
                "shopid=" + shopid + '\'' +
                "itemid=" + itemid + '\'' +
                "price=" + price + '\'' +
                "pitch=" + pitch + '\'' +
                "position=" + position + '\'' +
                "reqitem=" + reqitem + '\'' +
                "reqitemq=" + reqitemq + '\'' +
                '}';
    }
}
