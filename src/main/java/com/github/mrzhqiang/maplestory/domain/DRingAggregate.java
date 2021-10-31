package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.Max;
import io.ebean.annotation.View;

import javax.persistence.*;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@View(name = "rings")
public class DRingAggregate {

    @Max
    public Integer ringid;

    @Max
    public Integer partnerringid;
}
