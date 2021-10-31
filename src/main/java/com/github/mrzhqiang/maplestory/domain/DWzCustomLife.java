package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "wz_customlife")
public class DWzCustomLife extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "dataid")
    public Integer dataid;
    @NotNull
    @Column(name = "f")
    public Integer f;
    @NotNull
    @Column(name = "hide")
    public Integer hide;
    @NotNull
    @Column(name = "fh")
    public Integer fh;
    @NotNull
    @Column(name = "type")
    public String type;
    @NotNull
    @Column(name = "cy")
    public Integer cy;
    @NotNull
    @Column(name = "rx0")
    public Integer rx0;
    @NotNull
    @Column(name = "rx1")
    public Integer rx1;
    @NotNull
    @Column(name = "x")
    public Integer x;
    @NotNull
    @Column(name = "y")
    public Integer y;

    @Column(name = "mobtime")
    public Integer mobtime;
    @NotNull
    @Column(name = "mid")
    public Integer mid;

}
