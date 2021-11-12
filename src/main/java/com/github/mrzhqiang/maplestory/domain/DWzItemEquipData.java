package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "wz_itemequipdata")
public class DWzItemEquipData extends Model {

    @Id
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "itemid")
    public DWzItemData itemData;
    @NotNull
    @Column(name = "itemLevel")
    public Integer itemLevel;
    @NotNull
    @Column(name = "key")
    public String key;
    @NotNull
    @Column(name = "value")
    public Integer value;

}
