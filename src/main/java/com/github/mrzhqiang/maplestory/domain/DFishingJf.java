package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "fishingjf")
public class DFishingJf {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "accname", nullable = false)
    private String accname;

    @Column(name = "fishing", nullable = false)
    private Integer fishing;

    @Column(name = "XX", nullable = false)
    private Integer XX;

    @Column(name = "XXX", nullable = false)
    private Integer XXX;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setAccname(String accname) {
        this.accname = accname;
    }

    public String getAccname() {
        return accname;
    }

    public void setFishing(Integer fishing) {
        this.fishing = fishing;
    }

    public Integer getFishing() {
        return fishing;
    }

    public void setXX(Integer XX) {
        this.XX = XX;
    }

    public Integer getXX() {
        return XX;
    }

    public void setXXX(Integer XXX) {
        this.XXX = XXX;
    }

    public Integer getXXX() {
        return XXX;
    }

    @Override
    public String toString() {
        return "DFishingJf{" +
                "id=" + id + '\'' +
                "accname=" + accname + '\'' +
                "fishing=" + fishing + '\'' +
                "XX=" + XX + '\'' +
                "XXX=" + XXX + '\'' +
                '}';
    }
}
