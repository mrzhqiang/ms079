package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "hiredmerch")
public class DHiredMerch {

    @Id
    @Column(name = "PackageId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer packageId;

    @Column(name = "characterid")
    private Integer characterid;

    @Column(name = "accountid")
    private Integer accountid;

    @Column(name = "map", nullable = false)
    private Integer map;

    @Column(name = "channel")
    private Integer channel;

    @Column(name = "Mesos")
    private Integer mesos;

    @Column(name = "time")
    private Long time;

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setAccountid(Integer accountid) {
        this.accountid = accountid;
    }

    public Integer getAccountid() {
        return accountid;
    }

    public void setMap(Integer map) {
        this.map = map;
    }

    public Integer getMap() {
        return map;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setMesos(Integer mesos) {
        this.mesos = mesos;
    }

    public Integer getMesos() {
        return mesos;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "DHiredMerch{" +
                "packageId=" + packageId + '\'' +
                "characterid=" + characterid + '\'' +
                "accountid=" + accountid + '\'' +
                "map=" + map + '\'' +
                "channel=" + channel + '\'' +
                "mesos=" + mesos + '\'' +
                "time=" + time + '\'' +
                '}';
    }
}
