package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@SuppressWarnings("EntityIdMissingInspection")
@Entity
@Table(name = "fishing_rewards")
public class DFishingReward extends Model {

    @NotNull
    @Column(name = "itemid")
    public Integer itemid;
    @NotNull
    @Column(name = "chance")
    public Integer chance;
    @Column(name = "expiration")
    public LocalDateTime expiration;
    @Column(name = "name")
    public String name;

}
