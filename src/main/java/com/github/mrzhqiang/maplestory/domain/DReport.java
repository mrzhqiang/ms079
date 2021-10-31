package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "reports")
public class DReport extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "reporttime")
    public Date reporttime;
    @NotNull
    @Column(name = "reporterid")
    public Integer reporterid;
    @NotNull
    @Column(name = "victimid")
    public Integer victimid;
    @NotNull
    @Column(name = "reason")
    public Integer reason;
    @NotNull
    @Column(name = "chatlog")
    public String chatlog;
    @NotNull
    @Column(name = "status")
    public String status;

}
