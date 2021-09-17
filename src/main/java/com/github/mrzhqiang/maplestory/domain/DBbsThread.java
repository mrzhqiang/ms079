package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "bbs_threads")
public class DBbsThread {

    @Id
    @Column(name = "threadid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer threadid;

    @Column(name = "postercid", nullable = false)
    private Integer postercid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @Column(name = "icon", nullable = false)
    private Integer icon;

    @Column(name = "startpost", nullable = false)
    private String startpost;

    @Column(name = "guildid", nullable = false)
    private Integer guildid;

    @Column(name = "localthreadid", nullable = false)
    private Integer localthreadid;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setStartpost(String startpost) {
        this.startpost = startpost;
    }

    public String getStartpost() {
        return startpost;
    }

    public void setGuildid(Integer guildid) {
        this.guildid = guildid;
    }

    public Integer getGuildid() {
        return guildid;
    }

    public void setLocalthreadid(Integer localthreadid) {
        this.localthreadid = localthreadid;
    }

    public Integer getLocalthreadid() {
        return localthreadid;
    }

    @Override
    public String toString() {
        return "DBbsThread{" +
                "threadid=" + threadid + '\'' +
                "postercid=" + postercid + '\'' +
                "name=" + name + '\'' +
                "timestamp=" + timestamp + '\'' +
                "icon=" + icon + '\'' +
                "startpost=" + startpost + '\'' +
                "guildid=" + guildid + '\'' +
                "localthreadid=" + localthreadid + '\'' +
                '}';
    }
}
