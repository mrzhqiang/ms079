package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hypay")
public class DHyPay extends Model {

    @Id
    Integer id;
    String accName;
    @NotNull
    Integer payUsed;
    @NotNull
    Integer pay;
    @NotNull
    Integer payReward;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public Integer getPayUsed() {
        return payUsed;
    }

    public void setPayUsed(Integer payUsed) {
        this.payUsed = payUsed;
    }

    public Integer getPay() {
        return pay;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }

    public Integer getPayReward() {
        return payReward;
    }

    public void setPayReward(Integer payReward) {
        this.payReward = payReward;
    }
}
