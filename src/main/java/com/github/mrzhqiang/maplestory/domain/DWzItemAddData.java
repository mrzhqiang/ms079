package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "wz_itemadddata")
public class DWzItemAddData {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "itemid", nullable = false)
    private Integer itemid;

    @Column(name = "key", nullable = false)
    private String key;

    @Column(name = "subKey", nullable = false)
    private String subKey;

    @Column(name = "value", nullable = false)
    private String value;

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

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setSubKey(String subKey) {
        this.subKey = subKey;
    }

    public String getSubKey() {
        return subKey;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DWzItemAddData{" +
                "id=" + id + '\'' +
                "itemid=" + itemid + '\'' +
                "key=" + key + '\'' +
                "subKey=" + subKey + '\'' +
                "value=" + value + '\'' +
                '}';
    }
}
