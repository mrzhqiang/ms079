package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wz_oxdata")
public class DWzOxData {

    @EmbeddedId
    public PKQuestion pkQuestion;

    @Column(name = "question", nullable = false)
    public String question;

    @Column(name = "display", nullable = false)
    public String display;

    @Column(name = "answer", nullable = false)
    public String answer;

}
