package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "pets")
public class DPet {

    @Id
    @Column(name = "petid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer petid;

    @Column(name = "name")
    private String name;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "closeness", nullable = false)
    private Integer closeness;

    @Column(name = "fullness", nullable = false)
    private Integer fullness;

    @Column(name = "seconds", nullable = false)
    private Integer seconds;

    @Column(name = "flags", nullable = false)
    private Integer flags;

    public void setPetid(Integer petid) {
        this.petid = petid;
    }

    public Integer getPetid() {
        return petid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public void setCloseness(Integer closeness) {
        this.closeness = closeness;
    }

    public Integer getCloseness() {
        return closeness;
    }

    public void setFullness(Integer fullness) {
        this.fullness = fullness;
    }

    public Integer getFullness() {
        return fullness;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    public Integer getFlags() {
        return flags;
    }

    @Override
    public String toString() {
        return "DPet{" +
                "petid=" + petid + '\'' +
                "name=" + name + '\'' +
                "level=" + level + '\'' +
                "closeness=" + closeness + '\'' +
                "fullness=" + fullness + '\'' +
                "seconds=" + seconds + '\'' +
                "flags=" + flags + '\'' +
                '}';
    }
}
