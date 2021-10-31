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
@Table(name = "bbs_threads")
public class DBbsThread extends Model {

    @Id
    @Column(name = "threadid")
    public Integer id;
    @NotNull
    @Column(name = "postercid")
    public Integer postercid;
    @NotNull
    @Column(name = "name")
    public String name;
    @NotNull
    @Column(name = "timestamp")
    public Long timestamp;
    @NotNull
    @Column(name = "icon")
    public Integer icon;
    @NotNull
    @Column(name = "startpost")
    public String startpost;

    @ManyToOne
    @JoinColumn(name = "guildid")
    public DGuild guild;
    @NotNull
    @Column(name = "localthreadid")
    public Integer localthreadid;

}
