package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wz_questpartydata")
public class DWzQuestPartyData extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "questid")
    public Integer questid;
    @NotNull
    @Column(name = "rank")
    public String rank;
    @NotNull
    @Column(name = "mode")
    public String mode;
    @NotNull
    @Column(name = "property")
    public String property;
    @NotNull
    @Column(name = "value")
    public Integer value;

}
