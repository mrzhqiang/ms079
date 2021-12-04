package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rings")
public class DRing extends Model {

    @Id
    Integer id;
    @NotNull
    Integer partnerRingId;
    @NotNull
    Integer partnerChrId;
    @NotNull
    Integer itemId;
    @NotNull
    String partnerName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPartnerRingId() {
        return partnerRingId;
    }

    public void setPartnerRingId(Integer partnerRingId) {
        this.partnerRingId = partnerRingId;
    }

    public Integer getPartnerChrId() {
        return partnerChrId;
    }

    public void setPartnerChrId(Integer partnerChrId) {
        this.partnerChrId = partnerChrId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }
}
