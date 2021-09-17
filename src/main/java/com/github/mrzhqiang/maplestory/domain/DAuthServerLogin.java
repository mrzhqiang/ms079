package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "auth_server_login")
public class DAuthServerLogin {

    @Id
    @Column(name = "loginserverid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loginserverid;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "world", nullable = false)
    private Integer world;

    public void setLoginserverid(Integer loginserverid) {
        this.loginserverid = loginserverid;
    }

    public Integer getLoginserverid() {
        return loginserverid;
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
        return "DAuthServerLogin{" +
                "loginserverid=" + loginserverid + '\'' +
                "key=" + key + '\'' +
                "world=" + world + '\'' +
                '}';
    }
}
