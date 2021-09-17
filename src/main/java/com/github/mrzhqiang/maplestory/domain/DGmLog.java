package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "gmlog")
public class DGmLog {

    @Id
    @Column(name = "gmlogid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gmlogid;

    @Column(name = "cid", nullable = false)
    private Integer cid;

    @Column(name = "command", nullable = false)
    private String command;

    @Column(name = "mapid", nullable = false)
    private Integer mapid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "ip", nullable = false)
    private String ip;

    public void setGmlogid(Integer gmlogid) {
        this.gmlogid = gmlogid;
    }

    public Integer getGmlogid() {
        return gmlogid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setMapid(Integer mapid) {
        this.mapid = mapid;
    }

    public Integer getMapid() {
        return mapid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    @Override
    public String toString() {
        return "DGmLog{" +
                "gmlogid=" + gmlogid + '\'' +
                "cid=" + cid + '\'' +
                "command=" + command + '\'' +
                "mapid=" + mapid + '\'' +
                "name=" + name + '\'' +
                "ip=" + ip + '\'' +
                '}';
    }
}
