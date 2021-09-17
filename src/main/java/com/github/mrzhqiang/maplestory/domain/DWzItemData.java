package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wz_itemdata")
public class DWzItemData {

    @Id
    @Column(name = "itemid", nullable = false)
    private Integer itemid;

    @Column(name = "name")
    private String name;

    @Column(name = "msg")
    private String msg;

    @Column(name = "desc")
    private String desc;

    @Column(name = "slotMax", nullable = false)
    private Integer slotMax;

    @Column(name = "price", nullable = false)
    private String price;

    @Column(name = "wholePrice", nullable = false)
    private Integer wholePrice;

    @Column(name = "stateChange", nullable = false)
    private Integer stateChange;

    @Column(name = "flags", nullable = false)
    private Integer flags;

    @Column(name = "karma", nullable = false)
    private Integer karma;

    @Column(name = "meso", nullable = false)
    private Integer meso;

    @Column(name = "monsterBook", nullable = false)
    private Integer monsterBook;

    @Column(name = "itemMakeLevel", nullable = false)
    private Integer itemMakeLevel;

    @Column(name = "questId", nullable = false)
    private Integer questId;

    @Column(name = "scrollReqs")
    private String scrollReqs;

    @Column(name = "consumeItem")
    private String consumeItem;

    @Column(name = "totalprob", nullable = false)
    private Integer totalprob;

    @Column(name = "incSkill", nullable = false)
    private String incSkill;

    @Column(name = "replaceid", nullable = false)
    private Integer replaceid;

    @Column(name = "replacemsg", nullable = false)
    private String replacemsg;

    @Column(name = "create", nullable = false)
    private Integer create;

    @Column(name = "afterImage", nullable = false)
    private String afterImage;

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setSlotMax(Integer slotMax) {
        this.slotMax = slotMax;
    }

    public Integer getSlotMax() {
        return slotMax;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setWholePrice(Integer wholePrice) {
        this.wholePrice = wholePrice;
    }

    public Integer getWholePrice() {
        return wholePrice;
    }

    public void setStateChange(Integer stateChange) {
        this.stateChange = stateChange;
    }

    public Integer getStateChange() {
        return stateChange;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setKarma(Integer karma) {
        this.karma = karma;
    }

    public Integer getKarma() {
        return karma;
    }

    public void setMeso(Integer meso) {
        this.meso = meso;
    }

    public Integer getMeso() {
        return meso;
    }

    public void setMonsterBook(Integer monsterBook) {
        this.monsterBook = monsterBook;
    }

    public Integer getMonsterBook() {
        return monsterBook;
    }

    public void setItemMakeLevel(Integer itemMakeLevel) {
        this.itemMakeLevel = itemMakeLevel;
    }

    public Integer getItemMakeLevel() {
        return itemMakeLevel;
    }

    public void setQuestId(Integer questId) {
        this.questId = questId;
    }

    public Integer getQuestId() {
        return questId;
    }

    public void setScrollReqs(String scrollReqs) {
        this.scrollReqs = scrollReqs;
    }

    public String getScrollReqs() {
        return scrollReqs;
    }

    public void setConsumeItem(String consumeItem) {
        this.consumeItem = consumeItem;
    }

    public String getConsumeItem() {
        return consumeItem;
    }

    public void setTotalprob(Integer totalprob) {
        this.totalprob = totalprob;
    }

    public Integer getTotalprob() {
        return totalprob;
    }

    public void setIncSkill(String incSkill) {
        this.incSkill = incSkill;
    }

    public String getIncSkill() {
        return incSkill;
    }

    public void setReplaceid(Integer replaceid) {
        this.replaceid = replaceid;
    }

    public Integer getReplaceid() {
        return replaceid;
    }

    public void setReplacemsg(String replacemsg) {
        this.replacemsg = replacemsg;
    }

    public String getReplacemsg() {
        return replacemsg;
    }

    public void setCreate(Integer create) {
        this.create = create;
    }

    public Integer getCreate() {
        return create;
    }

    public void setAfterImage(String afterImage) {
        this.afterImage = afterImage;
    }

    public String getAfterImage() {
        return afterImage;
    }

    @Override
    public String toString() {
        return "DWzItemData{" +
                "itemid=" + itemid + '\'' +
                "name=" + name + '\'' +
                "msg=" + msg + '\'' +
                "desc=" + desc + '\'' +
                "slotMax=" + slotMax + '\'' +
                "price=" + price + '\'' +
                "wholePrice=" + wholePrice + '\'' +
                "stateChange=" + stateChange + '\'' +
                "flags=" + flags + '\'' +
                "karma=" + karma + '\'' +
                "meso=" + meso + '\'' +
                "monsterBook=" + monsterBook + '\'' +
                "itemMakeLevel=" + itemMakeLevel + '\'' +
                "questId=" + questId + '\'' +
                "scrollReqs=" + scrollReqs + '\'' +
                "consumeItem=" + consumeItem + '\'' +
                "totalprob=" + totalprob + '\'' +
                "incSkill=" + incSkill + '\'' +
                "replaceid=" + replaceid + '\'' +
                "replacemsg=" + replacemsg + '\'' +
                "create=" + create + '\'' +
                "afterImage=" + afterImage + '\'' +
                '}';
    }
}
