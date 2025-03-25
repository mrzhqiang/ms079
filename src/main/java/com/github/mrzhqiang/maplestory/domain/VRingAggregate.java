package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.Max;
import io.ebean.annotation.View;

import javax.persistence.Column;
import javax.persistence.Entity;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@View(name = "rings")
public class VRingAggregate {

    @Max
    @Column(name = "id")
    Integer ringId;
    @Max
    Integer partnerRingId;

    public Integer getRingId() {
        return ringId;
    }

    public void setRingId(Integer ringId) {
        this.ringId = ringId;
    }

    public Integer getPartnerRingId() {
        return partnerRingId;
    }

    public void setPartnerRingId(Integer partnerRingId) {
        this.partnerRingId = partnerRingId;
    }
}
