package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "auth_server_cs")
public class DAuthServerCs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CashShopServerId", nullable = false)
    private Integer cashShopServerId;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "world", nullable = false)
    private Integer world;

    public void setCashShopServerId(Integer cashShopServerId) {
        this.cashShopServerId = cashShopServerId;
    }

    public Integer getCashShopServerId() {
        return cashShopServerId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setWorld(Integer world) {
        this.world = world;
    }

    public Integer getWorld() {
        return world;
    }

    @Override
    public String toString() {
        return "DAuthServerCs{" +
                "cashShopServerId=" + cashShopServerId + '\'' +
                "key=" + key + '\'' +
                "world=" + world + '\'' +
                '}';
    }
}
