package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@Table(name = "wishlist")
public class DWishList extends Model {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "character_id")
    DCharacter character;
    @NotNull
    Integer sn;

    public DCharacter getCharacter() {
        return character;
    }

    public void setCharacter(DCharacter character) {
        this.character = character;
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }
}
