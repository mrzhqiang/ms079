package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cashshop_limit_sell")
public class DCashShopLimitSell {

    @Id
    @Column(name = "serial", nullable = false)
    private Integer serial;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    public void setSerial(Integer serial) {
        this.serial = serial;
    }

    public Integer getSerial() {
        return serial;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "DCashShopLimitSell{" +
                "serial=" + serial + '\'' +
                "amount=" + amount + '\'' +
                '}';
    }
}
