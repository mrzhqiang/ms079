package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.View;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@View(name = "readable_cheatlog")
public class ReadableCheatLog {

    @Column(name = "accountname")
    public String accountName;
    @Column(name = "accountid")
    public Integer accountId;
    @Column(name = "name")
    public String name;
    @Column(name = "characterid")
    public Integer characterId;
    @Column(name = "offense")
    public String offense;
    @Column(name = "count")
    public Integer count;
    @Column(name = "lastoffensetime")
    public LocalDateTime lastOffenseTime;
    @Column(name = "param")
    public String param;
}
