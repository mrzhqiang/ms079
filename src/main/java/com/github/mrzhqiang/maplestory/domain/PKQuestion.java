package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.NotNull;

import javax.persistence.Embeddable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PKQuestion that = (PKQuestion) o;
        return Objects.equals(questionSet, that.questionSet)
                && Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionSet, questionId);
    }
}
