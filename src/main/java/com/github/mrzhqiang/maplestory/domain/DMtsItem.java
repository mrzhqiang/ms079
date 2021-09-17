package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "mts_items")
public class DMtsItem {

    @EmbeddedId
    public PKMtsItem itemId;

    @Column(name = "tab", nullable = false)
    public Integer tab;

    @Column(name = "price", nullable = false)
    public Integer price;

    @Column(name = "characterid", nullable = false)
    public Integer characterid;
    @Column(name = "accountid")
    public Integer accountid;
    @Column(name = "packageId")
    public Integer packageId;
    @Column(name = "itemid", nullable = false)
    public Integer itemid;
    @Column(name = "inventorytype", nullable = false)
    public Integer inventorytype;
    @Column(name = "position", nullable = false)
    public Integer position;
    @Column(name = "quantity", nullable = false)
    public Integer quantity;
    @Column(name = "owner")
    public String owner;
    @Column(name = "GM_Log")
    public String gmLog;
    @Column(name = "uniqueid", nullable = false)
    public Integer uniqueid;
    @Column(name = "flag", nullable = false)
    public Integer flag;
    @Column(name = "expiredate", nullable = false)
    public Long expiredate;
    @Column(name = "type", nullable = false)
    public Integer type;
    @Column(name = "sender", nullable = false)
    public String sender;

    @Column(name = "seller", nullable = false)
    public String seller;

    @Column(name = "expiration", nullable = false)
    public Long expiration;

}
