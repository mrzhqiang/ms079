package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "auth_server_mts")
public class DAuthServerMts {

    @Id
    @Column(name = "MTSServerId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer MTSServerId;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "world", nullable = false)
    private Integer world;

    public void setMTSServerId(Integer MTSServerId) {
        this.MTSServerId = MTSServerId;
    }

    public Integer getMTSServerId() {
        return MTSServerId;
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
        return "DAuthServerMts{" +
                "MTSServerId=" + MTSServerId + '\'' +
                "key=" + key + '\'' +
                "world=" + world + '\'' +
                '}';
    }
}
