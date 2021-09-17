package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "game_poll_reply")
public class DGamePollReply {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "AccountId", nullable = false)
    private Integer accountId;

    @Column(name = "SelectAns", nullable = false)
    private Integer selectAns;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setSelectAns(Integer selectAns) {
        this.selectAns = selectAns;
    }

    public Integer getSelectAns() {
        return selectAns;
    }

    @Override
    public String toString() {
        return "DGamePollReply{" +
                "id=" + id + '\'' +
                "accountId=" + accountId + '\'' +
                "selectAns=" + selectAns + '\'' +
                '}';
    }
}
