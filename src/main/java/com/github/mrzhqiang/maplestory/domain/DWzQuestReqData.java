package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "wz_questreqdata")
public class DWzQuestReqData {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "questid", nullable = false)
    private Integer questid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "stringStore", nullable = false)
    private String stringStore;

    @Column(name = "intStoresFirst", nullable = false)
    private String intStoresFirst;

    @Column(name = "intStoresSecond", nullable = false)
    private String intStoresSecond;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setQuestid(Integer questid) {
        this.questid = questid;
    }

    public Integer getQuestid() {
        return questid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setStringStore(String stringStore) {
        this.stringStore = stringStore;
    }

    public String getStringStore() {
        return stringStore;
    }

    public void setIntStoresFirst(String intStoresFirst) {
        this.intStoresFirst = intStoresFirst;
    }

    public String getIntStoresFirst() {
        return intStoresFirst;
    }

    public void setIntStoresSecond(String intStoresSecond) {
        this.intStoresSecond = intStoresSecond;
    }

    public String getIntStoresSecond() {
        return intStoresSecond;
    }

    @Override
    public String toString() {
        return "DWzQuestReqData{" +
                "id=" + id + '\'' +
                "questid=" + questid + '\'' +
                "name=" + name + '\'' +
                "type=" + type + '\'' +
                "stringStore=" + stringStore + '\'' +
                "intStoresFirst=" + intStoresFirst + '\'' +
                "intStoresSecond=" + intStoresSecond + '\'' +
                '}';
    }
}
