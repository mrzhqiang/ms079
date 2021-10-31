package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "aclog")
public class DAcLog extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "accid")
    public Integer accid;
    @NotNull
    @Column(name = "bossid")
    public String bossid;
    @NotNull
    @Column(name = "lastattempt")
    public Date lastattempt;

}
