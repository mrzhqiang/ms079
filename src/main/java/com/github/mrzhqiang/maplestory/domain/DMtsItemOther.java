package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "mts_items")
public class DMtsItemOther extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "tab")
    public Integer tab;
    @NotNull
    @Column(name = "price")
    public Integer price;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @NotNull
    @Column(name = "seller")
    public String seller;
    @NotNull
    @Column(name = "expiration")
    public LocalDateTime expiration;

}
