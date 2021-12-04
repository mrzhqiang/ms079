package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@Entity
@Table(name = "bbs_threads")
public class DBbsThread extends Model {

    @Id
    Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "poster_id")
    DCharacter poster;
    @NotNull
    String name;
    @NotNull
    LocalDateTime timestamp;
    @NotNull
    Integer icon;
    @NotNull
    String startPost;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    DGuild guild;
    @NotNull
    Integer localThreadId;

    @OneToMany(mappedBy = "thread")
    List<DBbsReply> replyList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DCharacter getPoster() {
        return poster;
    }

    public void setPoster(DCharacter poster) {
        this.poster = poster;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getStartPost() {
        return startPost;
    }

    public void setStartPost(String startPost) {
        this.startPost = startPost;
    }

    public DGuild getGuild() {
        return guild;
    }

    public void setGuild(DGuild guild) {
        this.guild = guild;
    }

    public Integer getLocalThreadId() {
        return localThreadId;
    }

    public void setLocalThreadId(Integer localThreadId) {
        this.localThreadId = localThreadId;
    }

    public List<DBbsReply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<DBbsReply> replyList) {
        this.replyList = replyList;
    }
}
