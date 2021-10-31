package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wz_questactquestdata")
public class DWzQuestActQuestData extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "quest")
    public Integer quest;
    @NotNull
    @Column(name = "state")
    public Integer state;
    @NotNull
    @Column(name = "uniqueid")
    public Integer uniqueid;

}
