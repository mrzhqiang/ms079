package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@Table(name = "wishlist")
public class DWishList extends Model {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @NotNull
    @Column(name = "sn")
    public Integer sn;

}
