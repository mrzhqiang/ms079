package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "playernpcs")
public class DPlayerNPC {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "hair", nullable = false)
    private Integer hair;

    @Column(name = "face", nullable = false)
    private Integer face;

    @Column(name = "skin", nullable = false)
    private Integer skin;

    @Column(name = "x", nullable = false)
    private Integer x;

    @Column(name = "y", nullable = false)
    private Integer y;

    @Column(name = "map", nullable = false)
    private Integer map;

    @Column(name = "charid", nullable = false)
    private Integer charid;

    @Column(name = "scriptid", nullable = false)
    private Integer scriptid;

    @Column(name = "foothold", nullable = false)
    private Integer foothold;

    @Column(name = "dir", nullable = false)
    private Integer dir;

    @Column(name = "gender", nullable = false)
    private Integer gender;

    @Column(name = "pets")
    private String pets;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setHair(Integer hair) {
        this.hair = hair;
    }

    public Integer getHair() {
        return hair;
    }

    public void setFace(Integer face) {
        this.face = face;
    }

    public Integer getFace() {
        return face;
    }

    public void setSkin(Integer skin) {
        this.skin = skin;
    }

    public Integer getSkin() {
        return skin;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getX() {
        return x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getY() {
        return y;
    }

    public void setMap(Integer map) {
        this.map = map;
    }

    public Integer getMap() {
        return map;
    }

    public void setCharid(Integer charid) {
        this.charid = charid;
    }

    public Integer getCharid() {
        return charid;
    }

    public void setScriptid(Integer scriptid) {
        this.scriptid = scriptid;
    }

    public Integer getScriptid() {
        return scriptid;
    }

    public void setFoothold(Integer foothold) {
        this.foothold = foothold;
    }

    public Integer getFoothold() {
        return foothold;
    }

    public void setDir(Integer dir) {
        this.dir = dir;
    }

    public Integer getDir() {
        return dir;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getGender() {
        return gender;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public String getPets() {
        return pets;
    }

    @Override
    public String toString() {
        return "DPlayerNPC{" +
                "id=" + id + '\'' +
                "name=" + name + '\'' +
                "hair=" + hair + '\'' +
                "face=" + face + '\'' +
                "skin=" + skin + '\'' +
                "x=" + x + '\'' +
                "y=" + y + '\'' +
                "map=" + map + '\'' +
                "charid=" + charid + '\'' +
                "scriptid=" + scriptid + '\'' +
                "foothold=" + foothold + '\'' +
                "dir=" + dir + '\'' +
                "gender=" + gender + '\'' +
                "pets=" + pets + '\'' +
                '}';
    }
}
