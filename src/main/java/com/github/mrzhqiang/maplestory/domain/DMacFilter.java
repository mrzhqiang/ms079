package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "macfilters")
public class DMacFilter {

    @Id
    @Column(name = "macfilterid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer macfilterid;

    @Column(name = "filter", nullable = false)
    private String filter;

    public void setMacfilterid(Integer macfilterid) {
        this.macfilterid = macfilterid;
    }

    public Integer getMacfilterid() {
        return macfilterid;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }

    @Override
    public String toString() {
        return "DMacFilter{" +
                "macfilterid=" + macfilterid + '\'' +
                "filter=" + filter + '\'' +
                '}';
    }
}
