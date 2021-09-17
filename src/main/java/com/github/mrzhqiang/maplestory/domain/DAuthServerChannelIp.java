package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "auth_server_channel_ip")
public class DAuthServerChannelIp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channelconfigid", nullable = false)
    private Integer channelconfigid;

    @Column(name = "channelid", nullable = false)
    private Integer channelid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "value", nullable = false)
    private String value;

    public void setChannelconfigid(Integer channelconfigid) {
        this.channelconfigid = channelconfigid;
    }

    public Integer getChannelconfigid() {
        return channelconfigid;
    }

    public void setChannelid(Integer channelid) {
        this.channelid = channelid;
    }

    public Integer getChannelid() {
        return channelid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DAuthServerChannelIp{" +
                "channelconfigid=" + channelconfigid + '\'' +
                "channelid=" + channelid + '\'' +
                "name=" + name + '\'' +
                "value=" + value + '\'' +
                '}';
    }
}
