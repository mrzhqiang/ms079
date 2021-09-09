package com.github.mrzhqiang.maplestory.skill;

import com.google.common.base.Strings;

public final class Jobs {
    private Jobs() {
        // no instance
    }

    public static int computeId(int skillId) {
        return skillId / 10000;
    }

    public static String formatId(int jobId) {
        return Strings.padStart(Integer.toString(jobId), 3, '0');
    }

    public static String filename(int skillId) {
        return String.format("%s.img", formatId(computeId(skillId)));
    }
}
