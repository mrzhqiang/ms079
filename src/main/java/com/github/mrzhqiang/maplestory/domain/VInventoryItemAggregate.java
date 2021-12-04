package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.Max;
import io.ebean.annotation.View;

import javax.persistence.Entity;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@View(name = "inventoryitems")
public class VInventoryItemAggregate {

    @Max
    Integer uniqueId;

    public Integer getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }
}
