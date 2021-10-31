package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wz_oxdata")
public class DWzOxData extends Model {

    @EmbeddedId
    public PKQuestion pkQuestion;

    @NotNull
    @Column(name = "question")
    public String question;

    @NotNull
    @Column(name = "display")
    public String display;

    @NotNull
    @Column(name = "answer")
    public String answer;

}
