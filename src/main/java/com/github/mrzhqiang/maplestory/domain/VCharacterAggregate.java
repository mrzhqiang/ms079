package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.Max;
import io.ebean.annotation.View;

import javax.persistence.Entity;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@View(name = "characters")
public class VCharacterAggregate {

    @Max
    Integer party;

    public Integer getParty() {
        return party;
    }

    public void setParty(Integer party) {
        this.party = party;
    }
}
