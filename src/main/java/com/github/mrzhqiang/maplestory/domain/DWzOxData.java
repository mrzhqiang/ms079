package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "wz_oxdata")
public class DWzOxData extends Model {

    @EmbeddedId
    PKQuestion pkQuestion;
    @NotNull
    String question;
    @NotNull
    String display;
    @NotNull
    String answer;

    public PKQuestion getPkQuestion() {
        return pkQuestion;
    }

    public void setPkQuestion(PKQuestion pkQuestion) {
        this.pkQuestion = pkQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
