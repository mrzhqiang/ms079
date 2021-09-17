package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "rings")
public class DRing {

    @Id
    @Column(name = "ringid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ringid;

    @Column(name = "partnerRingId", nullable = false)
    private Integer partnerRingId;

    @Column(name = "partnerChrId", nullable = false)
    private Integer partnerChrId;

    @Column(name = "itemid", nullable = false)
    private Integer itemid;

    @Column(name = "partnername", nullable = false)
    private String partnername;

    public void setRingid(Integer ringid) {
        this.ringid = ringid;
    }

    public Integer getRingid() {
        return ringid;
    }

    public void setPartnerRingId(Integer partnerRingId) {
        this.partnerRingId = partnerRingId;
    }

    public Integer getPartnerRingId() {
        return partnerRingId;
    }

    public void setPartnerChrId(Integer partnerChrId) {
        this.partnerChrId = partnerChrId;
    }

    public Integer getPartnerChrId() {
        return partnerChrId;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setPartnername(String partnername) {
        this.partnername = partnername;
    }

    public String getPartnername() {
        return partnername;
    }

    @Override
    public String toString() {
        return "DRing{" +
                "ringid=" + ringid + '\'' +
                "partnerRingId=" + partnerRingId + '\'' +
                "partnerChrId=" + partnerChrId + '\'' +
                "itemid=" + itemid + '\'' +
                "partnername=" + partnername + '\'' +
                '}';
    }
}
