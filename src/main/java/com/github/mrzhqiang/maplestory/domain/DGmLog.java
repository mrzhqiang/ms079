package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "gmlog")
public class DGmLog extends Model {

    @Id
    @Column(name = "gmlogid")
    public Integer id;
    @NotNull
    @Column(name = "cid")
    public Integer cid;
    @NotNull
    @Column(name = "command")
    public String command;
    @NotNull
    @Column(name = "mapid")
    public Integer mapid;
    @NotNull
    @Column(name = "name")
    public String name;
    @NotNull
    @Column(name = "ip")
    public String ip;

}
