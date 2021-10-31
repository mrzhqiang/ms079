package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bosslog")
public class DBossLog extends Model {

    @Id
    @Column(name = "bosslogid")
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @NotNull
    @Column(name = "bossid")
    public String bossid;
    @NotNull
    @Column(name = "lastattempt")
    public LocalDateTime lastattempt;

}
