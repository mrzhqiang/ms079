package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "macfilters")
public class DMacFilter extends Model {

    @Id
    @Column(name = "macfilterid")
    public Integer id;
    @NotNull
    @Column(name = "filter")
    public String filter;

}
