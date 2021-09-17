package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "speedruns")
public class DSpeedRun {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "leader", nullable = false)
    private String leader;

    @Column(name = "timestring", nullable = false)
    private String timestring;

    @Column(name = "time", nullable = false)
    private Long time;

    @Column(name = "members", nullable = false)
    private String members;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLeader() {
        return leader;
    }

    public void setTimestring(String timestring) {
        this.timestring = timestring;
    }

    public String getTimestring() {
        return timestring;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getTime() {
        return time;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "DSpeedRun{" +
                "id=" + id + '\'' +
                "type=" + type + '\'' +
                "leader=" + leader + '\'' +
                "timestring=" + timestring + '\'' +
                "time=" + time + '\'' +
                "members=" + members + '\'' +
                '}';
    }
}
