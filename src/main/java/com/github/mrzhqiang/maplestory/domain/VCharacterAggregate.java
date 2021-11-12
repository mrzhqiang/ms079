package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.Max;
import io.ebean.annotation.View;

import javax.persistence.Entity;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@View(name = "characters")
public class VCharacterAggregate {

    @Max
    public Integer party;
}
