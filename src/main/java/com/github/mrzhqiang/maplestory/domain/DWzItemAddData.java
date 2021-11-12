package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "wz_itemadddata")
public class DWzItemAddData extends Model {

    @Id
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "itemid")
    public DWzItemData itemData;
    @NotNull
    @Column(name = "key")
    public String key;
    @NotNull
    @Column(name = "subKey")
    public String subKey;
    @NotNull
    @Column(name = "value")
    public String value;

}
