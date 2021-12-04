package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wz_customlife")
public class DWzCustomLife extends Model {

    @Id
    Integer id;
    @NotNull
    Integer dataId;
    @NotNull
    Integer f;
    @NotNull
    Integer hide;
    @NotNull
    Integer fh;
    @NotNull
    String type;
    @NotNull
    Integer cy;
    @NotNull
    Integer rx0;
    @NotNull
    Integer rx1;
    @NotNull
    Integer x;
    @NotNull
    Integer y;
    Integer mobTime;
    @NotNull
    Integer mid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Integer getMobTime() {
        return mobTime;
    }

    public void setMobTime(Integer mobTime) {
        this.mobTime = mobTime;
    }

    public Integer getF() {
        return f;
    }

    public void setF(Integer f) {
        this.f = f;
    }

    public Integer getHide() {
        return hide;
    }

    public void setHide(Integer hide) {
        this.hide = hide;
    }

    public Integer getFh() {
        return fh;
    }

    public void setFh(Integer fh) {
        this.fh = fh;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCy() {
        return cy;
    }

    public void setCy(Integer cy) {
        this.cy = cy;
    }

    public Integer getRx0() {
        return rx0;
    }

    public void setRx0(Integer rx0) {
        this.rx0 = rx0;
    }

    public Integer getRx1() {
        return rx1;
    }

    public void setRx1(Integer rx1) {
        this.rx1 = rx1;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }
}
