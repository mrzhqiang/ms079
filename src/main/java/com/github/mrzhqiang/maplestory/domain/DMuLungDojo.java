package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mulungdojo")
public class DMuLungDojo extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "charid")
    public Integer charid;
    @NotNull
    @Column(name = "stage")
    public Integer stage;

}
