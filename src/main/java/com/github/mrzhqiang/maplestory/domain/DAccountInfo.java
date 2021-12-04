package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "accounts_info",
        indexes = {@Index(name = "accid", columnList = "account_id"),
                @Index(name = "worldid", columnList = "world_id")})
public class DAccountInfo extends Model {

    @Id
    Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "account_id")
    DAccount account;

    @NotNull
    Integer worldId = 0;
    @NotNull
    Integer cardSlots = 3;
    /**
     * 在线时间点
     */
    @NotNull
    Integer gamePoints = 0;
    LocalDateTime updated;
    @NotNull
    Integer gamePointsPd = 0;
    @NotNull
    Integer gamePointsPs = 0;
    @NotNull
    Integer sjrw = 0;
    @NotNull
    Integer sgrw = 0;
    Integer fbrw = 0;
    Integer sbossrw = 0;
    Integer sgrwa = 0;
    Integer fbrwa = 0;
    Integer sbossrwa = 0;
    Integer lb = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DAccount getAccount() {
        return account;
    }

    public void setAccount(DAccount account) {
        this.account = account;
    }

    public Integer getWorldId() {
        return worldId;
    }

    public void setWorldId(Integer worldId) {
        this.worldId = worldId;
    }

    public Integer getCardSlots() {
        return cardSlots;
    }

    public void setCardSlots(Integer cardSlots) {
        this.cardSlots = cardSlots;
    }

    public Integer getGamePoints() {
        return gamePoints;
    }

    public void setGamePoints(Integer gamePoints) {
        this.gamePoints = gamePoints;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public Integer getGamePointsPd() {
        return gamePointsPd;
    }

    public void setGamePointsPd(Integer gamePointsPd) {
        this.gamePointsPd = gamePointsPd;
    }

    public Integer getGamePointsPs() {
        return gamePointsPs;
    }

    public void setGamePointsPs(Integer gamePointsPs) {
        this.gamePointsPs = gamePointsPs;
    }

    public Integer getSjrw() {
        return sjrw;
    }

    public void setSjrw(Integer sjrw) {
        this.sjrw = sjrw;
    }

    public Integer getSgrw() {
        return sgrw;
    }

    public void setSgrw(Integer sgrw) {
        this.sgrw = sgrw;
    }

    public Integer getFbrw() {
        return fbrw;
    }

    public void setFbrw(Integer fbrw) {
        this.fbrw = fbrw;
    }

    public Integer getSbossrw() {
        return sbossrw;
    }

    public void setSbossrw(Integer sbossrw) {
        this.sbossrw = sbossrw;
    }

    public Integer getSgrwa() {
        return sgrwa;
    }

    public void setSgrwa(Integer sgrwa) {
        this.sgrwa = sgrwa;
    }

    public Integer getFbrwa() {
        return fbrwa;
    }

    public void setFbrwa(Integer fbrwa) {
        this.fbrwa = fbrwa;
    }

    public Integer getSbossrwa() {
        return sbossrwa;
    }

    public void setSbossrwa(Integer sbossrwa) {
        this.sbossrwa = sbossrwa;
    }

    public Integer getLb() {
        return lb;
    }

    public void setLb(Integer lb) {
        this.lb = lb;
    }
}
