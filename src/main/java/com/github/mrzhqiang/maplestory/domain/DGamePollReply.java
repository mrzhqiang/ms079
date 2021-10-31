package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "game_poll_reply")
public class DGamePollReply extends Model {

    @Id
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "AccountId")
    public DAccount account;
    @NotNull
    @Column(name = "SelectAns")
    public Integer selectAns;

}
