package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "storages")
public class DStorage extends Model {

    @Id
    Integer id;
    @OneToOne
    @JoinColumn(name = "account_id")
    DAccount account;
    Integer slots;
    Integer meso;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DAccount getAccount() {
        return account;
    }

    public void setAccount(DAccount account) {
        this.account = account;
    }

    public Integer getSlots() {
        return slots;
    }

    public void setSlots(Integer slots) {
        this.slots = slots;
    }

    public Integer getMeso() {
        return meso;
    }

    public void setMeso(Integer meso) {
        this.meso = meso;
    }
}
