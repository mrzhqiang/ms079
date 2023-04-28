package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.NotNull;

import javax.persistence.Embeddable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PKAchievement that = (PKAchievement) o;
        return Objects.equals(achievementId, that.achievementId)
                && Objects.equals(charId, that.charId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(achievementId, charId);
    }
}
