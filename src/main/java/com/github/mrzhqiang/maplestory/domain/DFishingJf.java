package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fishingjf")
public class DFishingJf extends Model {

    @Id
    Integer id;
    @NotNull
    String accname;
    @NotNull
    Integer fishing;
    @NotNull
    Integer xx;
    @NotNull
    Integer xxx;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccname() {
        return accname;
    }

    public void setAccname(String accname) {
        this.accname = accname;
    }

    public Integer getFishing() {
        return fishing;
    }

    public void setFishing(Integer fishing) {
        this.fishing = fishing;
    }

    public Integer getXx() {
        return xx;
    }

    public void setXx(Integer xx) {
        this.xx = xx;
    }

    public Integer getXxx() {
        return xxx;
    }

    public void setXxx(Integer xxx) {
        this.xxx = xxx;
    }
}
