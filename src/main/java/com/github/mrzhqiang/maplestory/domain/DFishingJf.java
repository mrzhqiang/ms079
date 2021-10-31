package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fishingjf")
public class DFishingJf extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "accname")
    public String accname;
    @NotNull
    @Column(name = "fishing")
    public Integer fishing;
    @NotNull
    @Column(name = "XX")
    public Integer XX;
    @NotNull
    @Column(name = "XXX")
    public Integer XXX;

}
