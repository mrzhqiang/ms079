package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "gifts")
public class DGift {

    @Id
    @Column(name = "giftid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer giftid;

    @Column(name = "recipient", nullable = false)
    private Integer recipient;

//    @Column(name = "from", nullable = false)
    @Column(name = "from_", nullable = false)
    private String from;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "sn", nullable = false)
    private Integer sn;

    @Column(name = "uniqueid", nullable = false)
    private Integer uniqueid;

    public void setGiftid(Integer giftid) {
        this.giftid = giftid;
    }

    public Integer getGiftid() {
        return giftid;
    }

    public void setRecipient(Integer recipient) {
        this.recipient = recipient;
    }

    public Integer getRecipient() {
        return recipient;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public Integer getSn() {
        return sn;
    }

    public void setUniqueid(Integer uniqueid) {
        this.uniqueid = uniqueid;
    }

    public Integer getUniqueid() {
        return uniqueid;
    }

    @Override
    public String toString() {
        return "DGift{" +
                "giftid=" + giftid + '\'' +
                "recipient=" + recipient + '\'' +
                "from=" + from + '\'' +
                "message=" + message + '\'' +
                "sn=" + sn + '\'' +
                "uniqueid=" + uniqueid + '\'' +
                '}';
    }
}
