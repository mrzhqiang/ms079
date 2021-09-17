package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "storages")
public class DStorage {

    @Id
    @Column(name = "storageid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storageid;

    @Column(name = "accountid", nullable = false)
    private Integer accountid;

    @Column(name = "slots", nullable = false)
    private Integer slots;

    @Column(name = "meso", nullable = false)
    private Integer meso;

    public void setStorageid(Integer storageid) {
        this.storageid = storageid;
    }

    public Integer getStorageid() {
        return storageid;
    }

    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }

    public Integer getAccountid() {
        return accountid;
    }

    public void setSlots(Integer slots) {
        this.slots = slots;
    }

    public Integer getSlots() {
        return slots;
    }

    public void setMeso(Integer meso) {
        this.meso = meso;
    }

    public Integer getMeso() {
        return meso;
    }

    @Override
    public String toString() {
        return "DStorage{" +
                "storageid=" + storageid + '\'' +
                "accountid=" + accountid + '\'' +
                "slots=" + slots + '\'' +
                "meso=" + meso + '\'' +
                '}';
    }
}
