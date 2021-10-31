package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "invitecodedata")
public class DInviteCodeData extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "code")
    public String code;
    @Column(name = "user")
    public String user;
    @Column(name = "time")
    public String time;
    @Column(name = "ip")
    public String ip;
    @NotNull
    @Column(name = "active")
    public Integer active;

}
