package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "wz_npcnamedata")
public class DWzNPCNameData {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "npc", nullable = false)
    private Integer npc;

    @Column(name = "name", nullable = false)
    private String name;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setNpc(Integer npc) {
        this.npc = npc;
    }

    public Integer getNpc() {
        return npc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "DWzNPCNameData{" +
                "id=" + id + '\'' +
                "npc=" + npc + '\'' +
                "name=" + name + '\'' +
                '}';
    }
}
