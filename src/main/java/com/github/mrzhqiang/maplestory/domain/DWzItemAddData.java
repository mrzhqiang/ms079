package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wz_itemadddata")
public class DWzItemAddData extends Model {

    @Id
    Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    DWzItemData itemData;
    @NotNull
    String key;
    @NotNull
    String subKey;
    @NotNull
    String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DWzItemData getItemData() {
        return itemData;
    }

    public void setItemData(DWzItemData itemData) {
        this.itemData = itemData;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSubKey() {
        return subKey;
    }

    public void setSubKey(String subKey) {
        this.subKey = subKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
