package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.NotNull;

import javax.persistence.Embeddable;

@Embeddable
public class PKQuestion {

    @NotNull
    Integer questionSet;
    @NotNull
    Integer questionId;

    public Integer getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(Integer questionSet) {
        this.questionSet = questionSet;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }
}
