package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class PKMtsItem {

    @Column(name = "id", nullable = false)
    public Integer id;

    @Column(name = "inventoryitemid", nullable = false)
    public Integer inventoryitemid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PKMtsItem pkMtsItem = (PKMtsItem) o;
        return Objects.equals(id, pkMtsItem.id)
                && Objects.equals(inventoryitemid, pkMtsItem.inventoryitemid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, inventoryitemid);
    }
}
