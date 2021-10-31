package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nxcode")
public class DNxCode extends Model {

    @Id
    @Column(name = "code")
    public String code;
    @NotNull
    @Column(name = "valid")
    public Integer valid;
    @Column(name = "user")
    public String user;
    @NotNull
    @Column(name = "type")
    public Integer type;
    @NotNull
    @Column(name = "item")
    public Integer item;
    @Column(name = "size")
    public Integer size;

}
