package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "queststatusmobs")
public class DQuestStatusMob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queststatusmobid", nullable = false)
    private Integer queststatusmobid;

    @Column(name = "queststatusid", nullable = false)
    private Integer queststatusid;

    @Column(name = "mob", nullable = false)
    private Integer mob;

    @Column(name = "count", nullable = false)
    private Integer count;

    public void setQueststatusmobid(Integer queststatusmobid) {
        this.queststatusmobid = queststatusmobid;
    }

    public Integer getQueststatusmobid() {
        return queststatusmobid;
    }

    public void setQueststatusid(Integer queststatusid) {
        this.queststatusid = queststatusid;
    }

    public Integer getQueststatusid() {
        return queststatusid;
    }

    public void setMob(Integer mob) {
        this.mob = mob;
    }

    public Integer getMob() {
        return mob;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "DQuestStatusMob{" +
                "queststatusmobid=" + queststatusmobid + '\'' +
                "queststatusid=" + queststatusid + '\'' +
                "mob=" + mob + '\'' +
                "count=" + count + '\'' +
                '}';
    }
}
