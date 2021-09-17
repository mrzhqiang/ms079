package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "wz_itemequipdata")
public class DWzItemEquipData {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "itemid", nullable = false)
    private Integer itemid;

    @Column(name = "itemLevel", nullable = false)
    private Integer itemLevel;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "value", nullable = false)
    private Integer value;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemLevel(Integer itemLevel) {
        this.itemLevel = itemLevel;
    }

    public Integer getItemLevel() {
        return itemLevel;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DWzItemEquipData{" +
                "id=" + id + '\'' +
                "itemid=" + itemid + '\'' +
                "itemLevel=" + itemLevel + '\'' +
                "key=" + key + '\'' +
                "value=" + value + '\'' +
                '}';
    }
}
