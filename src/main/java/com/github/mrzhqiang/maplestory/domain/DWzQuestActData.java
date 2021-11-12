package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "wz_questactdata")
public class DWzQuestActData extends Model {

    @Id
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "questid")
    public DWzQuestData questData;
    @NotNull
    @Column(name = "name")
    public String name;
    @NotNull
    @Column(name = "type")
    public Integer type;
    @NotNull
    @Column(name = "intStore")
    public Integer intStore;
    @NotNull
    @Column(name = "applicableJobs")
    public String applicableJobs;
    @NotNull
    @Column(name = "uniqueid")
    public Integer uniqueid;

}
