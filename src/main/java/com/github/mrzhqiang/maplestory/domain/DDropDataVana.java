package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "drop_data_vana")
public class DDropDataVana {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dropperid", nullable = false)
    private Integer dropperid;

    @Column(name = "flags", nullable = false)
    private String flags;

    @Column(name = "itemid", nullable = false)
    private Integer itemid;

    @Column(name = "minimum_quantity", nullable = false)
    private Integer minimumQuantity;

    @Column(name = "maximum_quantity", nullable = false)
    private Integer maximumQuantity;

    @Column(name = "questid", nullable = false)
    private Integer questid;

    @Column(name = "chance", nullable = false)
    private Integer chance;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setDropperid(Integer dropperid) {
        this.dropperid = dropperid;
    }

    public Integer getDropperid() {
        return dropperid;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getFlags() {
        return flags;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setMinimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }

    public Integer getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMaximumQuantity(Integer maximumQuantity) {
        this.maximumQuantity = maximumQuantity;
    }

    public Integer getMaximumQuantity() {
        return maximumQuantity;
    }

    public void setQuestid(Integer questid) {
        this.questid = questid;
    }

    public Integer getQuestid() {
        return questid;
    }

    public void setChance(Integer chance) {
        this.chance = chance;
    }

    public Integer getChance() {
        return chance;
    }

    @Override
    public String toString() {
        return "DDropDataVana{" +
                "id=" + id + '\'' +
                "dropperid=" + dropperid + '\'' +
                "flags=" + flags + '\'' +
                "itemid=" + itemid + '\'' +
                "minimumQuantity=" + minimumQuantity + '\'' +
                "maximumQuantity=" + maximumQuantity + '\'' +
                "questid=" + questid + '\'' +
                "chance=" + chance + '\'' +
                '}';
    }
}
