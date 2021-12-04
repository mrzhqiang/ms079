package com.github.mrzhqiang.maplestory.wz;

import com.google.common.base.Preconditions;

import java.io.File;

/**
 * wz 数据工具。
 * <p>
 * 包含指定 wz 目录下的子目录及文件。
 */
public enum WzData {

    /**
     * 玩家相关
     */
    CHARACTER(WzResource.CHARACTER_DIR),
    /**
     * todo 弄清楚含义
     */
    ETC(WzResource.ETC_DIR),
    /**
     * 道具
     */
    ITEM(WzResource.ITEM_DIR),
    /**
     * 地图
     */
    MAP(WzResource.MAP_DIR),
    /**
     * 怪物
     */
    MOB(WzResource.MOB_DIR),
    /**
     * NPC
     */
    NPC(WzResource.NPC_DIR),
    /**
     * 任务
     */
    QUEST(WzResource.QUEST_DIR),
    /**
     * 反应堆？？
     */
    REACTOR(WzResource.REACTOR_DIR),
    /**
     * 技能
     */
    SKILL(WzResource.SKILL_DIR),
    /**
     * 文本
     */
    STRING(WzResource.STRING_DIR),
    /* 以下属于客户端内容，服务器保留这些文件，但不解析 */
//    BASE(WzFiles.BASE_DIR),
//    EFFECT(WzFiles.EFFECT_DIR),
//    MORPH(WzFiles.MORPH_DIR),
//    SOUND(WzFiles.SOUND_DIR),
//    TAMING_MOB(WzFiles.TAMING_MOB_DIR),
//    UI(WzFiles.UI_DIR),
    ;

    /**
     * 单个 wz 顶级目录。
     */
    private final WzDirectory directory;

    /**
     * @param directory wz 目录。
     */
    WzData(File directory) {
        Preconditions.checkNotNull(directory, "wz directory == null");
        Preconditions.checkArgument(directory.exists(),
                "wz directory %s is not exists", directory);
        this.directory = new WzDirectory(directory.toPath());
    }

    /**
     * wz 文件夹下的顶级目录，比如：wz/Character.wz
     *
     * @return wz 顶级目录。永不返回 Null 值。
     */
    public WzDirectory directory() {
        return directory;
    }
}
