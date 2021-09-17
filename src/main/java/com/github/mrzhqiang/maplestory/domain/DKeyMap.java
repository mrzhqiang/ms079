package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "keymap")
public class DKeyMap {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "characterid", nullable = false)
    private Integer characterid;

    @Column(name = "key", nullable = false)
    private Integer key;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "action", nullable = false)
    private Integer action;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getKey() {
        return key;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "DKeyMap{" +
                "id=" + id + '\'' +
                "characterid=" + characterid + '\'' +
                "key=" + key + '\'' +
                "type=" + type + '\'' +
                "action=" + action + '\'' +
                '}';
    }
}
