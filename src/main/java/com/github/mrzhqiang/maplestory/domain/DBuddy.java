package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.DbForeignKey;
import io.ebean.annotation.NotNull;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "character_id")
    DCharacter owner;
    @ManyToOne(optional = false)
    @DbForeignKey(noConstraint = true)
    @JoinColumn(name = "buddy_id")
    DCharacter buddy;
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

    public DCharacter getBuddies() {
        return buddy;
    }

    public void setBuddies(DCharacter buddy) {
        this.buddy = buddy;
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
