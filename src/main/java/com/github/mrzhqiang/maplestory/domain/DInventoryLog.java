package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "inventorylog")
public class DInventoryLog extends Model {

    @Id
    @Column(name = "inventorylogid")
    public Integer id;
    @NotNull
    @Column(name = "inventoryitemid")
    public Integer inventoryitemid;
    @NotNull
    @Column(name = "msg")
    public String msg;

}
