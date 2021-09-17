package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "buddies")
public class DBuddy {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "characterid", nullable = false)
    private Integer characterid;

    @Column(name = "buddyid", nullable = false)
    private Integer buddyid;

    @Column(name = "pending", nullable = false)
    private Integer pending;

    @Column(name = "groupname", nullable = false)
    private String groupname;

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

    public void setBuddyid(Integer buddyid) {
        this.buddyid = buddyid;
    }

    public Integer getBuddyid() {
        return buddyid;
    }

    public void setPending(Integer pending) {
        this.pending = pending;
    }

    public Integer getPending() {
        return pending;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getGroupname() {
        return groupname;
    }

    @Override
    public String toString() {
        return "DBuddy{" +
                "id=" + id + '\'' +
                "characterid=" + characterid + '\'' +
                "buddyid=" + buddyid + '\'' +
                "pending=" + pending + '\'' +
                "groupname=" + groupname + '\'' +
                '}';
    }
}
