package com.github.mrzhqiang.maplestory.wz;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public enum WzData {
    BASE(WzFiles.BASE_DIR),
    CHARACTER(WzFiles.CHARACTER_DIR),
    EFFECT(WzFiles.EFFECT_DIR),
    ETC(WzFiles.ETC_DIR),
    ITEM(WzFiles.ITEM_DIR),
    MAP(WzFiles.MAP_DIR),
    MOB(WzFiles.MOB_DIR),
    MORPH(WzFiles.MORPH_DIR),
    NPC(WzFiles.NPC_DIR),
    QUEST(WzFiles.QUEST_DIR),
    REACTOR(WzFiles.REACTOR_DIR),
    SKILL(WzFiles.SKILL_DIR),
    SOUND(WzFiles.SOUND_DIR),
    STRING(WzFiles.STRING_DIR),
    TAMING_MOB(WzFiles.TAMING_MOB_DIR),
    UI(WzFiles.UI_DIR),
    ;

    private static final Logger LOGGER = LoggerFactory.getLogger(WzData.class);

    private final WzDirectory root;

    WzData(File file) {
        Preconditions.checkNotNull(file, "file == null");
        this.root = new WzDirectory();
        this.root.parse(file.toPath());
    }

    public static class WzDirectory {
        private final Map<Path, WzDirectory> dirs = Maps.newHashMap();
        private final Map<Path, WzFile> files = Maps.newHashMap();

        public void parse(Path path) {
            if (!Files.isDirectory(path) || Files.notExists(path)) {
                return;
            }

            try {
                Files.list(path).forEach(this::attemptParse);
            } catch (IOException e) {
                LOGGER.error("列出 {} 目录时，出现错误：{}", path, e);
            }
        }

        private void attemptParse(Path path) {
            try {
                if (Files.isDirectory(path)) {
                    parseDirectory(path);
                } else {
                    parseFile(path);
                }
            } catch (IOException e) {
                LOGGER.error("解析 {} 出现问题：{}", path, e);
            }
        }

        private void parseDirectory(Path path) throws IOException {
            WzDirectory directory = new WzDirectory();
            directory.parse(path);
            dirs.put(path, directory);
        }

        private void parseFile(Path path) throws IOException {
            Elements imgDir = Jsoup.parse(path.toFile(), "UTF-8").body().children();
            files.put(path, new WzFile(imgDir));
        }
    }

    public static class WzFile {

        private final Elements imgDir;

        public WzFile(Elements imgDir) {
            Preconditions.checkNotNull(imgDir, "imgdir == null");
            this.imgDir = imgDir;
        }

        public String name() {
            return imgDir.attr("name");
        }

        public Elements content() {
            return imgDir;
        }
    }

}
