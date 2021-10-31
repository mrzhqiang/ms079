package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "pets")
public class DPet extends Model {

    @Id
    @Column(name = "petid")
    public Integer id;
    @Column(name = "name")
    public String name;
    @NotNull
    @Column(name = "level")
    public Integer level;
    @NotNull
    @Column(name = "closeness")
    public Integer closeness;
    @NotNull
    @Column(name = "fullness")
    public Integer fullness;
    @NotNull
    @Column(name = "seconds")
    public Integer seconds;
    @NotNull
    @Column(name = "flags")
    public Integer flags;

}
