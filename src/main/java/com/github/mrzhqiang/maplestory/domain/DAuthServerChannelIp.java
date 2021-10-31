package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "auth_server_channel_ip")
public class DAuthServerChannelIp extends Model {

    @Id
    @Column(name = "channelconfigid")
    public Integer id;
    @NotNull
    @Column(name = "channelid")
    public Integer channelid;
    @NotNull
    @Column(name = "name")
    public String name;
    @NotNull
    @Column(name = "value")
    public String value;

}
