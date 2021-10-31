package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rings")
public class DRing extends Model {

    @Id
    @Column(name = "ringid")
    public Integer id;
    @NotNull
    @Column(name = "partnerRingId")
    public Integer partnerRingId;
    @NotNull
    @Column(name = "partnerChrId")
    public Integer partnerChrId;
    @NotNull
    @Column(name = "itemid")
    public Integer itemid;
    @NotNull
    @Column(name = "partnername")
    public String partnername;

}
