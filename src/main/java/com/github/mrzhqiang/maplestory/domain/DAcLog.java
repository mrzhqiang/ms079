package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "aclog")
public class DAcLog extends Model {

    @Id
    Integer id;
    @NotNull
    @OneToOne
    @JoinColumn(name = "account_id")
    DAccount account;
    @NotNull
    String bossId;
    @NotNull
    LocalDateTime lastAttempt;

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

    public String getBossId() {
        return bossId;
    }

    public void setBossId(String bossId) {
        this.bossId = bossId;
    }

    public LocalDateTime getLastAttempt() {
        return lastAttempt;
    }

    public void setLastAttempt(LocalDateTime lastAttempt) {
        this.lastAttempt = lastAttempt;
    }
}
