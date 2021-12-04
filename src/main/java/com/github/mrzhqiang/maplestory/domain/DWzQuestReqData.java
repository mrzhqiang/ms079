package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wz_questreqdata")
public class DWzQuestReqData extends Model {

    @Id
    Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "quest_id")
    DWzQuestData questData;
    @NotNull
    String name;
    @NotNull
    Integer type;
    @NotNull
    String stringStore;
    @NotNull
    String intStoresFirst;
    @NotNull
    String intStoresSecond;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DWzQuestData getQuestData() {
        return questData;
    }

    public void setQuestData(DWzQuestData questData) {
        this.questData = questData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStringStore() {
        return stringStore;
    }

    public void setStringStore(String stringStore) {
        this.stringStore = stringStore;
    }

    public String getIntStoresFirst() {
        return intStoresFirst;
    }

    public void setIntStoresFirst(String intStoresFirst) {
        this.intStoresFirst = intStoresFirst;
    }

    public String getIntStoresSecond() {
        return intStoresSecond;
    }

    public void setIntStoresSecond(String intStoresSecond) {
        this.intStoresSecond = intStoresSecond;
    }
}
