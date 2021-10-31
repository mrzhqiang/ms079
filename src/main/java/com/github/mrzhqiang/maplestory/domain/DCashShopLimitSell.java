package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cashshop_limit_sell")
public class DCashShopLimitSell extends Model {

    @Id
    @Column(name = "serial")
    public Integer id;
    @NotNull
    @Column(name = "amount")
    public Integer amount;

}
