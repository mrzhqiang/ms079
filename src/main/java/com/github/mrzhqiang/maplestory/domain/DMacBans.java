package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "macbans")
public class DMacBans extends Model {

    @Id
    @Column(name = "macbanid")
    public Integer id;
    @NotNull
    @Column(name = "mac")
    public String mac;

}
