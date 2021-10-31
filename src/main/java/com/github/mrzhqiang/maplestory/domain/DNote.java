package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "notes")
public class DNote extends Model {

    @Id
    public Integer id;
    @NotNull
    @Column(name = "to_")
    public String to;
    @NotNull
    @Column(name = "from_")
    public String from;
    @NotNull
    @Column(name = "message")
    public String message;
    @NotNull
    @Column(name = "timestamp")
    public Long timestamp;
    @NotNull
    @Column(name = "gift")
    public Integer gift;

}
