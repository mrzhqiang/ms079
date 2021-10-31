package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "gifts")
public class DGift extends Model {

    @Id
    @Column(name = "giftid")
    public Integer id;
    @NotNull
    @Column(name = "recipient")
    public Integer recipient;
    @NotNull
    @Column(name = "from_")
    public String from;
    @NotNull
    @Column(name = "message")
    public String message;
    @NotNull
    @Column(name = "sn")
    public Integer sn;
    @NotNull
    @Column(name = "uniqueid")
    public Integer uniqueid;

}
