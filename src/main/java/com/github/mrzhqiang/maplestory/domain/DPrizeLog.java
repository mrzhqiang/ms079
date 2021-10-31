package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "prizelog")
public class DPrizeLog extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "accid")
    public Integer accid;
    @NotNull
    @Column(name = "bossid")
    public String bossid;

}
