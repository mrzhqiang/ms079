package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.View;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@View(name = "readable_last_hour_cheatlog")
public class ReadableLastHourCheatLog {

    @Column(name = "accountname")
    public String accountName;
    @Column(name = "accountid")
    public Integer accountId;
    @Column(name = "name")
    public String name;
    @Column(name = "characterid")
    public Integer characterId;
    @Column(name = "numrepos")
    public Long numRepos;
}
