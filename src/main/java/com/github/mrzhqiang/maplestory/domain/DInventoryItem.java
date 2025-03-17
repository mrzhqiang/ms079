package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inventoryitems")
public class DInventoryItem extends Model {

    @Id
    Integer id;
    @ManyToOne
    @JoinColumn(name = "character_id")
    DCharacter character;
    @ManyToOne
    @JoinColumn(name = "account_id")
    DAccount account;
    Integer packageId;
    @NotNull
    Integer itemId;
    @NotNull
    Integer inventoryType;
    @NotNull
    Integer position;
    @NotNull
    Integer quantity;
    String owner = "";
    String gmLog;
    @NotNull
    Integer uniqueId = -1;
    @NotNull
    Integer flag = 0;
    @NotNull
    Long expireDate = -1L;
    @NotNull
    Integer type = 0;
    @NotNull
    String sender = "";

    @OneToOne(mappedBy = "item")
//    @JoinColumn(name = "item_id")
    DInventoryEquipment equipment;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DCharacter getCharacter() {
        return character;
    }

    public void setCharacter(DCharacter character) {
        this.character = character;
    }

    public DAccount getAccount() {
        return account;
    }

    public void setAccount(DAccount account) {
        this.account = account;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(Integer inventoryType) {
        this.inventoryType = inventoryType;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGmLog() {
        return gmLog;
    }

    public void setGmLog(String gmLog) {
        this.gmLog = gmLog;
    }

    public Integer getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public DInventoryEquipment getEquipment() {
        return equipment;
    }

    public void setEquipment(DInventoryEquipment equipment) {
        this.equipment = equipment;
    }
}
