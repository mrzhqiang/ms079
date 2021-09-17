package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "accounts_info")
public class DAccountsInfo {

    @Id
    public Integer id;
    @Column(name = "accId", nullable = false)
    public Integer accId;
    @Column(name = "worldId", nullable = false)
    public Integer worldId;
    @Column(name = "cardSlots", nullable = false)
    public Integer cardSlots;
    /**
     * 在线时间点
     */
    @Column(name = "gamePoints", nullable = false)
    public Integer gamePoints;
    /**
     * 时间戳
     */
    @Column(name = "updateTime")
    public Date updateTime;
    @Column(name = "gamePointspd", nullable = false)
    public Integer gamePointspd;
    @Column(name = "gamePointsps", nullable = false)
    public Integer gamePointsps;
    @Column(name = "sjrw", nullable = false)
    public Integer sjrw;
    @Column(name = "sgrw", nullable = false)
    public Integer sgrw;
    @Column(name = "fbrw")
    public Integer fbrw;
    @Column(name = "sbossrw")
    public Integer sbossrw;
    @Column(name = "sgrwa")
    public Integer sgrwa;
    @Column(name = "fbrwa")
    public Integer fbrwa;
    @Column(name = "sbossrwa")
    public Integer sbossrwa;
    @Column(name = "lb")
    public Integer lb;

}
