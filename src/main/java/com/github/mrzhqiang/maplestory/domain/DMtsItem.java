package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mtsitems")
public class DMtsItem extends Model {

    @Id
    @Column(name = "inventoryitemid")
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @ManyToOne
    @JoinColumn(name = "accountid")
    public DAccount account;
    @Column(name = "packageId")
    public Integer packageId;
    @NotNull
    @Column(name = "itemid")
    public Integer itemid;
    @NotNull
    @Column(name = "inventorytype")
    public Integer inventorytype;
    @NotNull
    @Column(name = "position")
    public Integer position;
    @NotNull
    @Column(name = "quantity")
    public Integer quantity;
    @Column(name = "owner")
    public String owner;
    @Column(name = "GM_Log")
    public String gmLog;
    @NotNull
    @Column(name = "uniqueid")
    public Integer uniqueid;
    @NotNull
    @Column(name = "flag")
    public Integer flag;
    @NotNull
    @Column(name = "expiredate")
    public LocalDateTime expiredate;
    @NotNull
    @Column(name = "type")
    public Integer type;
    @NotNull
    @Column(name = "sender")
    public String sender;

    @OneToOne(mappedBy = "item")
    public DMtsEquipment equipment;

}
