package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wz_itemdata")
public class DWzItemData extends Model {

    @Id
    Integer id;
    String name;
    String msg;
    String desc;
    @NotNull
    Integer slotMax;
    @NotNull
    String price;
    @NotNull
    Integer wholePrice;
    @NotNull
    Integer stateChange;
    @NotNull
    Integer flags;
    @NotNull
    Integer karma;
    @NotNull
    Integer meso;
    @NotNull
    Integer monsterBook;
    @NotNull
    Integer itemMakeLevel;
    @NotNull
    Integer questId;
    String scrollReqs;
    String consumeItem;
    @NotNull
    Integer totalProb;
    @NotNull
    String incSkill;
    @NotNull
    Integer replaceId;
    @NotNull
    String replaceMsg;
    @NotNull
    Integer create;
    @NotNull
    String afterImage;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getSlotMax() {
        return slotMax;
    }

    public void setSlotMax(Integer slotMax) {
        this.slotMax = slotMax;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getWholePrice() {
        return wholePrice;
    }

    public void setWholePrice(Integer wholePrice) {
        this.wholePrice = wholePrice;
    }

    public Integer getStateChange() {
        return stateChange;
    }

    public void setStateChange(Integer stateChange) {
        this.stateChange = stateChange;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    public Integer getKarma() {
        return karma;
    }

    public void setKarma(Integer karma) {
        this.karma = karma;
    }

    public Integer getMeso() {
        return meso;
    }

    public void setMeso(Integer meso) {
        this.meso = meso;
    }

    public Integer getMonsterBook() {
        return monsterBook;
    }

    public void setMonsterBook(Integer monsterBook) {
        this.monsterBook = monsterBook;
    }

    public Integer getItemMakeLevel() {
        return itemMakeLevel;
    }

    public void setItemMakeLevel(Integer itemMakeLevel) {
        this.itemMakeLevel = itemMakeLevel;
    }

    public Integer getQuestId() {
        return questId;
    }

    public void setQuestId(Integer questId) {
        this.questId = questId;
    }

    public String getScrollReqs() {
        return scrollReqs;
    }

    public void setScrollReqs(String scrollReqs) {
        this.scrollReqs = scrollReqs;
    }

    public String getConsumeItem() {
        return consumeItem;
    }

    public void setConsumeItem(String consumeItem) {
        this.consumeItem = consumeItem;
    }

    public Integer getTotalProb() {
        return totalProb;
    }

    public void setTotalProb(Integer totalProb) {
        this.totalProb = totalProb;
    }

    public String getIncSkill() {
        return incSkill;
    }

    public void setIncSkill(String incSkill) {
        this.incSkill = incSkill;
    }

    public Integer getReplaceId() {
        return replaceId;
    }

    public void setReplaceId(Integer replaceId) {
        this.replaceId = replaceId;
    }

    public String getReplaceMsg() {
        return replaceMsg;
    }

    public void setReplaceMsg(String replaceMsg) {
        this.replaceMsg = replaceMsg;
    }

    public Integer getCreate() {
        return create;
    }

    public void setCreate(Integer create) {
        this.create = create;
    }

    public String getAfterImage() {
        return afterImage;
    }

    public void setAfterImage(String afterImage) {
        this.afterImage = afterImage;
    }
}
