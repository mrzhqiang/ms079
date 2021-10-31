package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wz_questreqdata")
public class DWzQuestReqData extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "questid")
    public Integer questid;
    @NotNull
    @Column(name = "name")
    public String name;
    @NotNull
    @Column(name = "type")
    public Integer type;
    @NotNull
    @Column(name = "stringStore")
    public String stringStore;
    @NotNull
    @Column(name = "intStoresFirst")
    public String intStoresFirst;
    @NotNull
    @Column(name = "intStoresSecond")
    public String intStoresSecond;

}
