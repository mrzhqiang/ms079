package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "drop_data_global")
public class DDropDataGlobal {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "continent", nullable = false)
    private Integer continent;

    @Column(name = "dropType", nullable = false)
    private Integer dropType;

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

    @Column(name = "comments")
    private String comments;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setContinent(Integer continent) {
        this.continent = continent;
    }

    public Integer getContinent() {
        return continent;
    }

    public void setDropType(Integer dropType) {
        this.dropType = dropType;
    }

    public Integer getDropType() {
        return dropType;
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

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "DDropDataGlobal{" +
                "id=" + id + '\'' +
                "continent=" + continent + '\'' +
                "dropType=" + dropType + '\'' +
                "itemid=" + itemid + '\'' +
                "minimumQuantity=" + minimumQuantity + '\'' +
                "maximumQuantity=" + maximumQuantity + '\'' +
                "questid=" + questid + '\'' +
                "chance=" + chance + '\'' +
                "comments=" + comments + '\'' +
                '}';
    }
}
