package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "speedruns")
public class DSpeedRun extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "type")
    public String type;
    @NotNull
    @Column(name = "leader")
    public String leader;
    @NotNull
    @Column(name = "timestring")
    public String timestring;
    @NotNull
    @Column(name = "time")
    public Long time;
    @NotNull
    @Column(name = "members")
    public String members;

}
