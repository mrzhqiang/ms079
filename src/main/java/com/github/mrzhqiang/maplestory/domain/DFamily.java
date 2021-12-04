package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "families")
public class DFamily extends Model {

    @Id
    Integer id;
    @NotNull
    @OneToOne
    @JoinColumn(name = "leader_id")
    DCharacter leader;
    @NotNull
    String notice;

    @OneToMany(mappedBy = "family")
    List<DCharacter> members;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DCharacter getLeader() {
        return leader;
    }

    public void setLeader(DCharacter leader) {
        this.leader = leader;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<DCharacter> getMembers() {
        return members;
    }

    public void setMembers(List<DCharacter> members) {
        this.members = members;
    }
}
