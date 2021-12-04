package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.DbForeignKey;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "buddies")
public class DBuddy extends Model {

    @Id
    Integer id;
    @NotNull
    @OneToOne
    @JoinColumn(name = "character_id")
    DCharacter owner;
    @OneToMany
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "buddy_id")
    List<DCharacter> buddies;
    @NotNull
    boolean pending;
    @NotNull
    String groupName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DCharacter getOwner() {
        return owner;
    }

    public void setOwner(DCharacter owner) {
        this.owner = owner;
    }

    public List<DCharacter> getBuddies() {
        return buddies;
    }

    public void setBuddies(List<DCharacter> buddies) {
        this.buddies = buddies;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
