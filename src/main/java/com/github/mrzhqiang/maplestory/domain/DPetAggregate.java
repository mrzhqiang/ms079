package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.Max;
import io.ebean.annotation.View;

import javax.persistence.*;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@View(name = "pets")
public class DPetAggregate {

    @Max
    public Integer petid;
}
