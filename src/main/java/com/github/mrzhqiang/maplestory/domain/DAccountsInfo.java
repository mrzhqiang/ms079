package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "accounts_info")
public class DAccountsInfo extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "accId")
    public Integer accId;
    @NotNull
    @Column(name = "worldId")
    public Integer worldId;
    @NotNull
    @Column(name = "cardSlots")
    public Integer cardSlots;
    /**
     * 在线时间点
     */
    @NotNull
    @Column(name = "gamePoints")
    public Integer gamePoints;
    /**
     * 时间戳
     */
    @Column(name = "updateTime")
    public LocalDate updateTime;
    @NotNull
    @Column(name = "gamePointspd")
    public Integer gamePointspd;
    @NotNull
    @Column(name = "gamePointsps")
    public Integer gamePointsps;
    @NotNull
    @Column(name = "sjrw")
    public Integer sjrw;
    @NotNull
    @Column(name = "sgrw")
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
