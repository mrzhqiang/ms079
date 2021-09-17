package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "auth_server_channel")
public class DAuthServerChannel {

    @Id
    @Column(name = "channelid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer channelid;

    @Column(name = "world", nullable = false)
    private Integer world;

    @Column(name = "number")
    private Integer number;

    @Column(name = "key", nullable = false)
    private String key;

    public void setChannelid(Integer channelid) {
        this.channelid = channelid;
    }

    public Integer getChannelid() {
        return channelid;
    }

    public void setWorld(Integer world) {
        this.world = world;
    }

    public Integer getWorld() {
        return world;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "DAuthServerChannel{" +
                "channelid=" + channelid + '\'' +
                "world=" + world + '\'' +
                "number=" + number + '\'' +
                "key=" + key + '\'' +
                '}';
    }
}
