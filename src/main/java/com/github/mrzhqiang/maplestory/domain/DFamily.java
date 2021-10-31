package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "families")
public class DFamily extends Model {

    @Id
    @Column(name = "familyid")
    public Integer id;
    @NotNull
    @OneToOne
    @JoinColumn(name = "leaderid")
    public DCharacter leader;
    @NotNull
    @Column(name = "notice")
    public String notice;
    @OneToMany(mappedBy = "family")
    public List<DCharacter> members;

}
