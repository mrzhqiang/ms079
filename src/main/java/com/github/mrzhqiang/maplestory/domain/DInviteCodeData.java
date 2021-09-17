package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "invitecodedata")
public class DInviteCodeData {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "user")
    private String user;

    @Column(name = "time")
    private String time;

    @Column(name = "ip")
    private String ip;

    @Column(name = "active", nullable = false)
    private Integer active;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getActive() {
        return active;
    }

    @Override
    public String toString() {
        return "DInviteCodeData{" +
                "id=" + id + '\'' +
                "code=" + code + '\'' +
                "user=" + user + '\'' +
                "time=" + time + '\'' +
                "ip=" + ip + '\'' +
                "active=" + active + '\'' +
                '}';
    }
}
