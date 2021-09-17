package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "eventstats")
public class DEventStats {

    @Id
    @Column(name = "eventstatid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventstatid;

    @Column(name = "event", nullable = false)
    private String event;

    @Column(name = "instance", nullable = false)
    private String instance;

    @Column(name = "characterid", nullable = false)
    private Integer characterid;

    @Column(name = "channel", nullable = false)
    private Integer channel;

    @Column(name = "time", nullable = false)
    private Date time;

    public void setEventstatid(Integer eventstatid) {
        this.eventstatid = eventstatid;
    }

    public Integer getEventstatid() {
        return eventstatid;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getInstance() {
        return instance;
    }

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "DEventStats{" +
                "eventstatid=" + eventstatid + '\'' +
                "event=" + event + '\'' +
                "instance=" + instance + '\'' +
                "characterid=" + characterid + '\'' +
                "channel=" + channel + '\'' +
                "time=" + time + '\'' +
                '}';
    }
}
