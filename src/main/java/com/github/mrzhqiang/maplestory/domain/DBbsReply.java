package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Entity
@Table(name = "bbs_replies")
public class DBbsReply extends Model {

    @Id
    Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "thread_id")
    DBbsThread thread;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "poster_id")
    DCharacter poster;
    @NotNull
    LocalDateTime timestamp;
    @NotNull
    String content;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    DGuild guild;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DBbsThread getThread() {
        return thread;
    }

    public void setThread(DBbsThread thread) {
        this.thread = thread;
    }

    public DCharacter getPoster() {
        return poster;
    }

    public void setPoster(DCharacter poster) {
        this.poster = poster;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public long getTimestampLong() {
        return timestamp.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DGuild getGuild() {
        return guild;
    }

    public void setGuild(DGuild guild) {
        this.guild = guild;
    }
}
