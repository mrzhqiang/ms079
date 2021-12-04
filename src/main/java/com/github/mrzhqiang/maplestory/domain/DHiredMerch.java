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

@Entity
@Table(name = "hiredmerch")
public class DHiredMerch extends Model {

    @Id
    Integer id;
    @ManyToOne
    @JoinColumn(name = "character_id")
    DCharacter character;
    @ManyToOne
    @JoinColumn(name = "account_id")
    DAccount account;
    @NotNull
    Integer map;
    Integer channel;
    Integer mesos;
    LocalDateTime time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DCharacter getCharacter() {
        return character;
    }

    public void setCharacter(DCharacter character) {
        this.character = character;
    }

    public DAccount getAccount() {
        return account;
    }

    public void setAccount(DAccount account) {
        this.account = account;
    }

    public Integer getMap() {
        return map;
    }

    public void setMap(Integer map) {
        this.map = map;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getMesos() {
        return mesos;
    }

    public void setMesos(Integer mesos) {
        this.mesos = mesos;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
