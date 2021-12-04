package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

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
    Integer id;
    @NotNull
    Integer tab;
    @NotNull
    Integer price;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "character_id")
    DCharacter character;
    @NotNull
    String seller;
    @NotNull
    LocalDateTime expiration;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTab() {
        return tab;
    }

    public void setTab(Integer tab) {
        this.tab = tab;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public DCharacter getCharacter() {
        return character;
    }

    public void setCharacter(DCharacter character) {
        this.character = character;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
}
