package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.NotNull;

import javax.persistence.Embeddable;

@Embeddable
public class PKAchievement {

    @NotNull
    Integer achievementId;
    @NotNull
    Integer charId;

    public Integer getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(Integer achievementId) {
        this.achievementId = achievementId;
    }

    public Integer getCharId() {
        return charId;
    }

    public void setCharId(Integer charId) {
        this.charId = charId;
    }
}
