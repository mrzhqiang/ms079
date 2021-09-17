package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class PKQuestion {

    @Column(name = "questionset", nullable = false)
    public Integer questionset;

    @Column(name = "questionid", nullable = false)
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
