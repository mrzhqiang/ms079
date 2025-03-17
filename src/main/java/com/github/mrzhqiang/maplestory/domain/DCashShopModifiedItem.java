package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cashshop_modified_items")
public class DCashShopModifiedItem extends Model {

    @Id
    Integer id;
    @NotNull
    String name;
    @NotNull
    @Column(name = "discount_price")
    Integer discountPrice;
    @NotNull
    Integer mark;
    @NotNull
    boolean showUp;
    @NotNull
    Integer itemId;
    @NotNull
    Integer priority;
    @NotNull
    @Column(name = "package_field")
    boolean packageField;
    @NotNull
    Integer period;
    @NotNull
    Integer gender;
    @NotNull
    Integer count;
    @NotNull
    Integer meso;
    @NotNull
    @Column(name = "unk_1")
    Integer unk1;
    @NotNull
    @Column(name = "unk_2")
    Integer unk2;
    @NotNull
    @Column(name = "unk_3")
    Integer unk3;
    @NotNull
    @Column(name = "extra_flags")
    Integer extraFlags;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    public boolean isShowUp() {
        return showUp;
    }

    public void setShowUp(boolean showUp) {
        this.showUp = showUp;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public boolean isPackageField() {
        return packageField;
    }

    public void setPackageField(boolean packageField) {
        this.packageField = packageField;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getMeso() {
        return meso;
    }

    public void setMeso(Integer meso) {
        this.meso = meso;
    }

    public Integer getUnk1() {
        return unk1;
    }

    public void setUnk1(Integer unk1) {
        this.unk1 = unk1;
    }

    public Integer getUnk2() {
        return unk2;
    }

    public void setUnk2(Integer unk2) {
        this.unk2 = unk2;
    }

    public Integer getUnk3() {
        return unk3;
    }

    public void setUnk3(Integer unk3) {
        this.unk3 = unk3;
    }

    public Integer getExtraFlags() {
        return extraFlags;
    }

    public void setExtraFlags(Integer extraFlags) {
        this.extraFlags = extraFlags;
    }
}
