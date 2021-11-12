package com.github.mrzhqiang.maplestory.wz;

import com.google.common.base.Preconditions;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
    CHARACTER(WzManage.CHARACTER_DIR),
    /**
     * todo 弄清楚含义
     */
    ETC(WzManage.ETC_DIR),
    /**
     * 道具
     */
    ITEM(WzManage.ITEM_DIR),
    /**
     * 地图
     */
    MAP(WzManage.MAP_DIR),
    /**
     * 怪物
     */
    MOB(WzManage.MOB_DIR),
    /**
     * NPC
     */
    NPC(WzManage.NPC_DIR),
    /**
     * 任务
     */
    QUEST(WzManage.QUEST_DIR),
    /**
     * 反应堆？？
     */
    REACTOR(WzManage.REACTOR_DIR),
    /**
     * 技能
     */
    SKILL(WzManage.SKILL_DIR),
    /**
     * 文本
     */
    STRING(WzManage.STRING_DIR),
    /* 以下属于客户端内容，服务器保留这些文件，但不解析 */
//    BASE(WzFiles.BASE_DIR),
//    EFFECT(WzFiles.EFFECT_DIR),
//    MORPH(WzFiles.MORPH_DIR),
//    SOUND(WzFiles.SOUND_DIR),
//    TAMING_MOB(WzFiles.TAMING_MOB_DIR),
//    UI(WzFiles.UI_DIR),
    ;

    /**
     * 加载所有的 wz 文件。
     * <p>
     * 注意：被删除的 wz 文件需要重启才能从内存中清除，之所以不立即清除的原因是，我们需要保证线程安全。
     *
     * @return 基于 RxJava 的流。
     */
    public static Flowable<WzDirectory> load() {
        return Flowable.fromArray(WzData.values())
                .subscribeOn(Schedulers.io())
                .map(WzData::directory)
                .doOnNext(WzDirectory::parse);
    }

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
