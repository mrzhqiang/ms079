package com.github.mrzhqiang.maplestory.util;

import com.google.common.base.Strings;

/**
 * 职业辅助工具。
 */
public final class Jobs {
    private Jobs() {
        // no instances
    }

    /**
     * 计算职业编号。
     *
     * @param skillId 技能编号。
     * @return 职业编号。
     */
    public static int computeId(int skillId) {
        return skillId / 10000;
    }

    /**
     * 格式化职业编号。
     *
     * @param jobId 职业编号。
     * @return 技能编号。
     */
    public static String formatId(int jobId) {
        return Strings.padStart(Integer.toString(jobId), 3, '0');
    }

    /**
     * 技能编号对应的 wz 定义文件名称。
     *
     * @param skillId 技能编号。
     * @return wz 定义文件名称。
     */
    public static String filename(int skillId) {
        return String.format("%s.img", formatId(computeId(skillId)));
    }
}
