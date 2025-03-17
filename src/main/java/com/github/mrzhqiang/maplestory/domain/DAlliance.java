package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "alliances")
public class DAlliance extends Model {

    @Id
    Integer id;
    @NotNull
    String name;
    @NotNull
    @OneToOne
    @JoinColumn(name = "leader_id")
    DCharacter leader;
    @NotNull
    @OneToOne
    @JoinColumn(name = "guild1")
    DGuild guild1;
    @NotNull
    @OneToOne
    @JoinColumn(name = "guild2")
    DGuild guild2;
    @OneToOne
    @JoinColumn(name = "guild3")
    DGuild guild3;
    @OneToOne
    @JoinColumn(name = "guild4")
    DGuild guild4;
    @OneToOne
    @JoinColumn(name = "guild5")
    DGuild guild5;
    @NotNull
    String rank1;
    @NotNull
    String rank2;
    @NotNull
    String rank3;
    @NotNull
    String rank4;
    @NotNull
    String rank5;
    @NotNull
    Integer capacity = 2;
    @NotNull
    String notice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DCharacter getLeader() {
        return leader;
    }

    public void setLeader(DCharacter leader) {
        this.leader = leader;
    }

    public DGuild getGuild1() {
        return guild1;
    }

    public void setGuild1(DGuild guild1) {
        this.guild1 = guild1;
    }

    public DGuild getGuild2() {
        return guild2;
    }

    public void setGuild2(DGuild guild2) {
        this.guild2 = guild2;
    }

    public DGuild getGuild3() {
        return guild3;
    }

    public void setGuild3(DGuild guild3) {
        this.guild3 = guild3;
    }

    public DGuild getGuild4() {
        return guild4;
    }

    public void setGuild4(DGuild guild4) {
        this.guild4 = guild4;
    }

    public DGuild getGuild5() {
        return guild5;
    }

    public void setGuild5(DGuild guild5) {
        this.guild5 = guild5;
    }

    public String getRank1() {
        return rank1;
    }

    public void setRank1(String rank1) {
        this.rank1 = rank1;
    }

    public String getRank2() {
        return rank2;
    }

    public void setRank2(String rank2) {
        this.rank2 = rank2;
    }

    public String getRank3() {
        return rank3;
    }

    public void setRank3(String rank3) {
        this.rank3 = rank3;
    }

    public String getRank4() {
        return rank4;
    }

    public void setRank4(String rank4) {
        this.rank4 = rank4;
    }

    public String getRank5() {
        return rank5;
    }

    public void setRank5(String rank5) {
        this.rank5 = rank5;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
