package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class PKAchievement {

    @Column(name = "achievementid", nullable = false)
    public Integer achievementid;

    @Column(name = "charid", nullable = false)
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
