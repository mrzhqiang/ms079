package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.Max;
import io.ebean.annotation.View;

import javax.persistence.Entity;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@View(name = "pets")
public class VPetAggregate {

    @Max
    Integer petId;

    public Integer getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }
}
