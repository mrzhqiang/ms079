package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "eventstats")
public class DEventStats extends Model {

    @Id
    @Column(name = "eventstatid")
    public Integer id;
    @NotNull
    @Column(name = "event")
    public String event;
    @NotNull
    @Column(name = "instance")
    public String instance;
    @NotNull
    @Column(name = "characterid")
    public Integer characterid;
    @NotNull
    @Column(name = "channel")
    public Integer channel;
    @NotNull
    @Column(name = "time")
    public Date time;

}
