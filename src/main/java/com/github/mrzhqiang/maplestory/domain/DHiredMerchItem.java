package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hiredmerchitems")
public class DHiredMerchItem extends Model {

    @Id
    @Column(name = "inventoryitemid")
    public Integer id;
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @ManyToOne
    @JoinColumn(name = "accountid")
    public DAccount account;
    @Column(name = "packageid")
    public Integer packageid;
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

    /**
     * todo 可能是 一对多 关系，需要转为 List
     */
    @OneToOne(mappedBy = "item")
    public DHiredMerchEquipment equipment;
}
