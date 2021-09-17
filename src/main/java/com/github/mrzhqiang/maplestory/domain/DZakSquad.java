package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "zaksquads")
public class DZakSquad {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "channel", nullable = false)
    private Integer channel;

    @Column(name = "leaderid", nullable = false)
    private Integer leaderid;

    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "members", nullable = false)
    private Integer members;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setLeaderid(Integer leaderid) {
        this.leaderid = leaderid;
    }

    public Integer getLeaderid() {
        return leaderid;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setMembers(Integer members) {
        this.members = members;
    }

    public Integer getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "DZakSquad{" +
                "id=" + id + '\'' +
                "channel=" + channel + '\'' +
                "leaderid=" + leaderid + '\'' +
                "status=" + status + '\'' +
                "members=" + members + '\'' +
                '}';
    }
}
