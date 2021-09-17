package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "inventorylog")
public class DInventoryLog {

    @Id
    @Column(name = "inventorylogid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventorylogid;

    @Column(name = "inventoryitemid", nullable = false)
    private Integer inventoryitemid;

    @Column(name = "msg", nullable = false)
    private String msg;

    public void setInventorylogid(Integer inventorylogid) {
        this.inventorylogid = inventorylogid;
    }

    public Integer getInventorylogid() {
        return inventorylogid;
    }

    public void setInventoryitemid(Integer inventoryitemid) {
        this.inventoryitemid = inventoryitemid;
    }

    public Integer getInventoryitemid() {
        return inventoryitemid;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "DInventoryLog{" +
                "inventorylogid=" + inventorylogid + '\'' +
                "inventoryitemid=" + inventoryitemid + '\'' +
                "msg=" + msg + '\'' +
                '}';
    }
}
