package com.github.mrzhqiang.maplestory.wz;

import com.google.common.base.Preconditions;

import java.io.File;

/**
 * wz 数据。
 * <p>
 * 这是一系列数据的单例实现，包含相关目录和文件。
 * <p>
 * 枚举类是最好的单例。
 */
public enum WzData {
    CHARACTER(WzManage.CHARACTER_DIR),
    ETC(WzManage.ETC_DIR),
    ITEM(WzManage.ITEM_DIR),
    MAP(WzManage.MAP_DIR),
    MOB(WzManage.MOB_DIR),
    NPC(WzManage.NPC_DIR),
    QUEST(WzManage.QUEST_DIR),
    REACTOR(WzManage.REACTOR_DIR),
    SKILL(WzManage.SKILL_DIR),
    STRING(WzManage.STRING_DIR),
//    BASE(WzFiles.BASE_DIR),
//    EFFECT(WzFiles.EFFECT_DIR),
//    MORPH(WzFiles.MORPH_DIR),
//    SOUND(WzFiles.SOUND_DIR),
//    TAMING_MOB(WzFiles.TAMING_MOB_DIR),
//    UI(WzFiles.UI_DIR),
    ;

    /**
     * 将文件内容解析并加载到相应的 ConcurrentHashMap（支持并发操作的内存缓存） 中。
     * <p>
     * 这个方法可以重复调用，以更新修改后的内容。
     * <p>
     * 注意：如果新增或删除 xml 文件以及文件中的内容，此方法将不保证得到预期的效果。
     */
    public static synchronized void load() {
        for (WzData data : WzData.values()) {
            // 只是修改内容，可以重加载。
            // 如果是删除了某些内容，则必须重启服务器，以防止程序异常。
//            data.root.clean();
            data.directory.parse();
        }
    }

    /**
     * 单个 wz 顶级目录。
     */
    private final WzDirectory directory;

    /**
     * @param wzFile 单个 wz 顶级目录。
     */
    WzData(File wzFile) {
        Preconditions.checkNotNull(wzFile, "wz file == null");
        Preconditions.checkArgument(wzFile.exists(), "wz file %s is not exists", wzFile);
        this.directory = new WzDirectory(wzFile.toPath());
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
