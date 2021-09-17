package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "wz_customlife")
public class DWzCustomLife {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dataid", nullable = false)
    private Integer dataid;

    @Column(name = "f", nullable = false)
    private Integer f;

    @Column(name = "hide", nullable = false)
    private Integer hide;

    @Column(name = "fh", nullable = false)
    private Integer fh;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "cy", nullable = false)
    private Integer cy;

    @Column(name = "rx0", nullable = false)
    private Integer rx0;

    @Column(name = "rx1", nullable = false)
    private Integer rx1;

    @Column(name = "x", nullable = false)
    private Integer x;

    @Column(name = "y", nullable = false)
    private Integer y;

    @Column(name = "mobtime")
    private Integer mobtime;

    @Column(name = "mid", nullable = false)
    private Integer mid;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setDataid(Integer dataid) {
        this.dataid = dataid;
    }

    public Integer getDataid() {
        return dataid;
    }

    public void setF(Integer f) {
        this.f = f;
    }

    public Integer getF() {
        return f;
    }

    public void setHide(Integer hide) {
        this.hide = hide;
    }

    public Integer getHide() {
        return hide;
    }

    public void setFh(Integer fh) {
        this.fh = fh;
    }

    public Integer getFh() {
        return fh;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setCy(Integer cy) {
        this.cy = cy;
    }

    public Integer getCy() {
        return cy;
    }

    public void setRx0(Integer rx0) {
        this.rx0 = rx0;
    }

    public Integer getRx0() {
        return rx0;
    }

    public void setRx1(Integer rx1) {
        this.rx1 = rx1;
    }

    public Integer getRx1() {
        return rx1;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getX() {
        return x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getY() {
        return y;
    }

    public void setMobtime(Integer mobtime) {
        this.mobtime = mobtime;
    }

    public Integer getMobtime() {
        return mobtime;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getMid() {
        return mid;
    }

    @Override
    public String toString() {
        return "DWzCustomLife{" +
                "id=" + id + '\'' +
                "dataid=" + dataid + '\'' +
                "f=" + f + '\'' +
                "hide=" + hide + '\'' +
                "fh=" + fh + '\'' +
                "type=" + type + '\'' +
                "cy=" + cy + '\'' +
                "rx0=" + rx0 + '\'' +
                "rx1=" + rx1 + '\'' +
                "x=" + x + '\'' +
                "y=" + y + '\'' +
                "mobtime=" + mobtime + '\'' +
                "mid=" + mid + '\'' +
                '}';
    }
}
