package com.github.mrzhqiang.maplestory.domain;

import com.google.common.base.MoreObjects;
import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ipbans")
public class DIPBans extends Model {

    @Id
    @Column(name = "ipbanid")
    public Integer id;
    @NotNull
    @Column(name = "ip")
    public String ip;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("ip", ip)
                .toString();
    }
}
