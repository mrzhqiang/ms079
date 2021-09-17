package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "hypay")
public class DHyPay {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "accname")
    private String accname;

    @Column(name = "payUsed", nullable = false)
    private Integer payUsed;

    @Column(name = "pay", nullable = false)
    private Integer pay;

    @Column(name = "payReward", nullable = false)
    private Integer payReward;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setAccname(String accname) {
        this.accname = accname;
    }

    public String getAccname() {
        return accname;
    }

    public void setPayUsed(Integer payUsed) {
        this.payUsed = payUsed;
    }

    public Integer getPayUsed() {
        return payUsed;
    }

    public void setPay(Integer pay) {
        this.pay = pay;
    }

    public Integer getPay() {
        return pay;
    }

    public void setPayReward(Integer payReward) {
        this.payReward = payReward;
    }

    public Integer getPayReward() {
        return payReward;
    }

    @Override
    public String toString() {
        return "DHyPay{" +
                "id=" + id + '\'' +
                "accname=" + accname + '\'' +
                "payUsed=" + payUsed + '\'' +
                "pay=" + pay + '\'' +
                "payReward=" + payReward + '\'' +
                '}';
    }
}
