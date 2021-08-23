package com.github.mrzhqiang.maplestory.wz;

import com.github.mrzhqiang.maplestory.wz.element.ImgDirElement;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public enum WzData {
    CHARACTER(WzFiles.CHARACTER_DIR),
    ETC(WzFiles.ETC_DIR),
    ITEM(WzFiles.ITEM_DIR),
    MAP(WzFiles.MAP_DIR),
    MOB(WzFiles.MOB_DIR),
    NPC(WzFiles.NPC_DIR),
    QUEST(WzFiles.QUEST_DIR),
    REACTOR(WzFiles.REACTOR_DIR),
    SKILL(WzFiles.SKILL_DIR),
    STRING(WzFiles.STRING_DIR),
//    BASE(WzFiles.BASE_DIR),
//    EFFECT(WzFiles.EFFECT_DIR),
//    MORPH(WzFiles.MORPH_DIR),
//    SOUND(WzFiles.SOUND_DIR),
//    TAMING_MOB(WzFiles.TAMING_MOB_DIR),
//    UI(WzFiles.UI_DIR),
    ;
    private static final Logger LOGGER = LoggerFactory.getLogger(WzData.class);

    public static void load() {
        for (WzData value : WzData.values()) {
            value.root.clean();
            value.root.parse();
        }
    }

    private final WzDirectory root;

    /**
     * wz/Character.wz
     * wz/String.wz
     */
    WzData(File wzFile) {
        Preconditions.checkNotNull(wzFile, "wz file == null");
        Preconditions.checkArgument(wzFile.exists(), "wz file %s is not exists", wzFile);
        this.root = new WzDirectory(wzFile.toPath());
    }

    public WzDirectory root() {
        return root;
    }


    public static final class WzDirectory {

        private final Map<Path, WzDirectory> dirs = Maps.newHashMap();
        private final Map<Path, WzFile> files = Maps.newHashMap();

        private final Path path;

        WzDirectory(Path path) {
            Preconditions.checkNotNull(path, "path == null");
            this.path = path;
        }

        @Nullable
        public WzDirectory dir(String name) {
            if (Strings.isNullOrEmpty(name) || dirs.isEmpty()) {
                return null;
            }
            return dirs.get(path.resolve(name));
        }

        @Nullable
        public WzFile file(String name) {
            if (Strings.isNullOrEmpty(name) || files.isEmpty()) {
                return null;
            }
            if (!name.endsWith(".xml")) {
                name = name + ".xml";
            }
            return files.get(path.resolve(name));
        }

        private void parse() {
            if (!Files.isDirectory(path)) {
                return;
            }

            try {
                Files.list(path).forEach(this::attemptParse);
            } catch (IOException e) {
                LOGGER.error("无法列出 {} 目录：{}", path, e);
            }
        }

        private void attemptParse(Path path) {
            try {
                if (Files.isDirectory(path)) {
                    WzDirectory directory = new WzDirectory(path);
                    dirs.put(path, directory);
                } else {
                    Elements imgDir = Jsoup.parse(path.toFile(), "UTF-8").body().children();
                    files.put(path, new WzFile(imgDir));
                }
            } catch (IOException e) {
                LOGGER.error("解析 {} 出现问题：{}", path, e);
            }
        }

        public void clean() {
            dirs.clear();
            files.clear();
        }
    }


    public static class WzFile {

        private final ImgDirElement imgDir;

        WzFile(Elements imgDir) {
            Preconditions.checkNotNull(imgDir, "imgdir == null");
            this.imgDir = ImgDirElement.of(imgDir.first());
        }

        @Nonnull
        public ImgDirElement imgDir() {
            return imgDir;
        }
    }
}
