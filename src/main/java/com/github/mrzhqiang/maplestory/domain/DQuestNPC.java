package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "questnpc")
public class DQuestNPC {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "npcid")
    private Integer npcid;

    @Column(name = "itemid")
    private Integer itemid;

    @Column(name = "sl")
    private Integer sl;

    @Column(name = "zt")
    private Integer zt;

    @Column(name = "name")
    private String name;

    @Column(name = "item")
    private Integer item;

    @Column(name = "itemsl")
    private Integer itemsl;

    @Column(name = "money")
    private Integer money;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setNpcid(Integer npcid) {
        this.npcid = npcid;
    }

    public Integer getNpcid() {
        return npcid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setSl(Integer sl) {
        this.sl = sl;
    }

    public Integer getSl() {
        return sl;
    }

    public void setZt(Integer zt) {
        this.zt = zt;
    }

    public Integer getZt() {
        return zt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getItem() {
        return item;
    }

    public void setItemsl(Integer itemsl) {
        this.itemsl = itemsl;
    }

    public Integer getItemsl() {
        return itemsl;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getMoney() {
        return money;
    }

    @Override
    public String toString() {
        return "DQuestNPC{" +
                "id=" + id + '\'' +
                "npcid=" + npcid + '\'' +
                "itemid=" + itemid + '\'' +
                "sl=" + sl + '\'' +
                "zt=" + zt + '\'' +
                "name=" + name + '\'' +
                "item=" + item + '\'' +
                "itemsl=" + itemsl + '\'' +
                "money=" + money + '\'' +
                '}';
    }
}
