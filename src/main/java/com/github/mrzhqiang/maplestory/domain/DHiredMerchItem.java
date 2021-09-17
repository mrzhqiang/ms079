package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "hiredmerchitems")
public class DHiredMerchItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventoryitemid", nullable = false)
    private Integer inventoryitemid;

    @Column(name = "characterid")
    private Integer characterid;

    @Column(name = "accountid")
    private Integer accountid;

    @Column(name = "packageid")
    private Integer packageid;

    @Column(name = "itemid", nullable = false)
    private Integer itemid;

    @Column(name = "inventorytype", nullable = false)
    private Integer inventorytype;

    @Column(name = "position", nullable = false)
    private Integer position;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "owner")
    private String owner;

    @Column(name = "GM_Log")
    private String gmLog;

    @Column(name = "uniqueid", nullable = false)
    private Integer uniqueid;

    @Column(name = "flag", nullable = false)
    private Integer flag;

    @Column(name = "expiredate", nullable = false)
    private Long expiredate;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "sender", nullable = false)
    private String sender;

    public void setInventoryitemid(Integer inventoryitemid) {
        this.inventoryitemid = inventoryitemid;
    }

    public Integer getInventoryitemid() {
        return inventoryitemid;
    }

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }

    public Integer getAccountid() {
        return accountid;
    }

    public void setPackageid(Integer packageid) {
        this.packageid = packageid;
    }

    public Integer getPackageid() {
        return packageid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setInventorytype(Integer inventorytype) {
        this.inventorytype = inventorytype;
    }

    public Integer getInventorytype() {
        return inventorytype;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPosition() {
        return position;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void setGmLog(String gmLog) {
        this.gmLog = gmLog;
    }

    public String getGmLog() {
        return gmLog;
    }

    public void setUniqueid(Integer uniqueid) {
        this.uniqueid = uniqueid;
    }

    public Integer getUniqueid() {
        return uniqueid;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setExpiredate(Long expiredate) {
        this.expiredate = expiredate;
    }

    public Long getExpiredate() {
        return expiredate;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "DHiredMerchItem{" +
                "inventoryitemid=" + inventoryitemid + '\'' +
                "characterid=" + characterid + '\'' +
                "accountid=" + accountid + '\'' +
                "packageid=" + packageid + '\'' +
                "itemid=" + itemid + '\'' +
                "inventorytype=" + inventorytype + '\'' +
                "position=" + position + '\'' +
                "quantity=" + quantity + '\'' +
                "owner=" + owner + '\'' +
                "gmLog=" + gmLog + '\'' +
                "uniqueid=" + uniqueid + '\'' +
                "flag=" + flag + '\'' +
                "expiredate=" + expiredate + '\'' +
                "type=" + type + '\'' +
                "sender=" + sender + '\'' +
                '}';
    }
}
