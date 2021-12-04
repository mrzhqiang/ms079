package com.github.mrzhqiang.maplestory.util;

import com.google.common.base.Strings;

public enum Jobs {
    ; // no instances

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
