package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "achievements")
public class DAchievement extends Model {

    @EmbeddedId
    PKAchievement achievement;

    @NotNull
    @OneToOne
    @JoinColumn(name = "account_id")
    DAccount account;

    public PKAchievement getAchievement() {
        return achievement;
    }

    public void setAchievement(PKAchievement achievement) {
        this.achievement = achievement;
    }

    public DAccount getAccount() {
        return account;
    }

    public void setAccount(DAccount account) {
        this.account = account;
    }
}
