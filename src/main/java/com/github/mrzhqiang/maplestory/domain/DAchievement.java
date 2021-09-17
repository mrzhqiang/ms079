package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "achievements")
public class DAchievement {

    @EmbeddedId
    public PKAchievement achievement;

    @OneToOne
    @Column(name = "accountid", nullable = false)
    public Integer accountid;

}
