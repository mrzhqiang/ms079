package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "cheatlog")
public class DCheatLog extends Model {

    @Id
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @NotNull
    @Column(name = "offense")
    public String offense;
    @NotNull
    @Column(name = "count")
    public Integer count;
    @NotNull
    @Column(name = "lastoffensetime")
    public LocalDateTime lastoffensetime;
    @NotNull
    @Column(name = "param")
    public String param;

}
