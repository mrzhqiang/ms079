package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class PKAchievement {
    @NotNull
    @Column(name = "achievementid")
    public Integer achievementid;

    @NotNull
    @Column(name = "charid")
    public Integer charid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PKAchievement that = (PKAchievement) o;
        return Objects.equals(achievementid, that.achievementid)
                && Objects.equals(charid, that.charid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(achievementid, charid);
    }
}
