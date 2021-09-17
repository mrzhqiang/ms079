package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wishlist")
public class DWishList {

    @Column(name = "characterid", nullable = false)
    private Integer characterid;

    @Column(name = "sn", nullable = false)
    private Integer sn;

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public Integer getSn() {
        return sn;
    }

    @Override
    public String toString() {
        return "DWishList{" +
                "characterid=" + characterid + '\'' +
                "sn=" + sn + '\'' +
                '}';
    }
}
