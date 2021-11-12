package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "bbs_replies")
public class DBbsReply extends Model {

    @Id
    @Column(name = "replyid")
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "threadid")
    public DBbsThread thread;
    @NotNull
    @Column(name = "postercid")
    public Integer postercid;
    @NotNull
    @Column(name = "timestamp")
    public Long timestamp;
    @NotNull
    @Column(name = "content")
    public String content;

    @ManyToOne
    @JoinColumn(name = "guildid")
    public DGuild guild;

}
