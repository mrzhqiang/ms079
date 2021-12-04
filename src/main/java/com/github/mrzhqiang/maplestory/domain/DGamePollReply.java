package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "game_poll_reply")
public class DGamePollReply extends Model {

    @Id
    Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id")
    DAccount account;
    @NotNull
    Integer selectAns;

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

    public Integer getSelectAns() {
        return selectAns;
    }

    public void setSelectAns(Integer selectAns) {
        this.selectAns = selectAns;
    }
}
