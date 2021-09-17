package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "bbs_replies")
public class DBbsReply {

    @Id
    @Column(name = "replyid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer replyid;

    @Column(name = "threadid", nullable = false)
    private Integer threadid;

    @Column(name = "postercid", nullable = false)
    private Integer postercid;

    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "guildid", nullable = false)
    private Integer guildid;

    public void setReplyid(Integer replyid) {
        this.replyid = replyid;
    }

    public Integer getReplyid() {
        return replyid;
    }

    public void setThreadid(Integer threadid) {
        this.threadid = threadid;
    }

    public Integer getThreadid() {
        return threadid;
    }

    public void setPostercid(Integer postercid) {
        this.postercid = postercid;
    }

    public Integer getPostercid() {
        return postercid;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setGuildid(Integer guildid) {
        this.guildid = guildid;
    }

    public Integer getGuildid() {
        return guildid;
    }

    @Override
    public String toString() {
        return "DBbsReply{" +
                "replyid=" + replyid + '\'' +
                "threadid=" + threadid + '\'' +
                "postercid=" + postercid + '\'' +
                "timestamp=" + timestamp + '\'' +
                "content=" + content + '\'' +
                "guildid=" + guildid + '\'' +
                '}';
    }
}
