package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hypay")
public class DHyPay extends Model {

    @Id
    public Integer id;
    @Column(name = "accname")
    public String accname;
    @NotNull
    @Column(name = "payUsed")
    public Integer payUsed;
    @NotNull
    @Column(name = "pay")
    public Integer pay;
    @NotNull
    @Column(name = "payReward")
    public Integer payReward;

}
