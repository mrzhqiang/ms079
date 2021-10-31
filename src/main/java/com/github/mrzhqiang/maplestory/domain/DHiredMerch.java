package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "hiredmerch")
public class DHiredMerch extends Model {

    @Id
    @Column(name = "PackageId")
    public Integer id;
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @ManyToOne
    @JoinColumn(name = "accountid")
    public DAccount account;
    @NotNull
    @Column(name = "map")
    public Integer map;
    @Column(name = "channel")
    public Integer channel;
    @Column(name = "Mesos")
    public Integer mesos;
    @Column(name = "time")
    public LocalDateTime time;

}
