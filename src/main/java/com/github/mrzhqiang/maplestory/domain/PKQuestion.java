package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class PKQuestion {

    @NotNull
    @Column(name = "questionset")
    public Integer questionset;
    @NotNull
    @Column(name = "questionid")
    public Integer questionid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PKQuestion that = (PKQuestion) o;
        return Objects.equals(questionset, that.questionset)
                && Objects.equals(questionid, that.questionid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionset, questionid);
    }
}
