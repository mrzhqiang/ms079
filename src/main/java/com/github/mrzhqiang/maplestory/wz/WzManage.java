package com.github.mrzhqiang.maplestory.wz;

import com.github.mrzhqiang.helper.Environments;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * wz 文件管理工具。
 */
public final class WzManage {
    private WzManage() {
        // no instance
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(WzManage.class);

    private static final String WZ_KEY = "wz.path";
    private static final String DEFAULT_WZ_PATH = "wz";

    /**
     * wz 目录。
     * <p>
     * 默认值是 wz，可通过环境变量进行修改，也可以在启动时，使用 Dwz.path=wz 修改。
     */
    public static final File WZ_DIR = new File(System.getProperty(WZ_KEY, DEFAULT_WZ_PATH));
    /**
     * 未使用的目录，可能是跟客户端相关，与服务端关系不大，因此找不到任何地方使用。
     */
    @SuppressWarnings("unused")
    public static final File BASE_DIR = new File(WZ_DIR, "Base.wz");
    public static final File CHARACTER_DIR = new File(WZ_DIR, "Character.wz");
    @SuppressWarnings("unused")
    public static final File EFFECT_DIR = new File(WZ_DIR, "Effect.wz");
    public static final File ETC_DIR = new File(WZ_DIR, "Etc.wz");
    public static final File ITEM_DIR = new File(WZ_DIR, "Item.wz");
    public static final File MAP_DIR = new File(WZ_DIR, "Map.wz");
    public static final File MOB_DIR = new File(WZ_DIR, "Mob.wz");
    @SuppressWarnings("unused")
    public static final File MORPH_DIR = new File(WZ_DIR, "Morph.wz");
    public static final File NPC_DIR = new File(WZ_DIR, "Npc.wz");
    public static final File QUEST_DIR = new File(WZ_DIR, "Quest.wz");
    public static final File REACTOR_DIR = new File(WZ_DIR, "Reactor.wz");
    public static final File SKILL_DIR = new File(WZ_DIR, "Skill.wz");
    @SuppressWarnings("unused")
    public static final File SOUND_DIR = new File(WZ_DIR, "Sound.wz");
    public static final File STRING_DIR = new File(WZ_DIR, "String.wz");
    @SuppressWarnings("unused")
    public static final File TAMING_MOB_DIR = new File(WZ_DIR, "TamingMob.wz");
    @SuppressWarnings("unused")
    public static final File UI_DIR = new File(WZ_DIR, "UI.wz");

    /**
     * 作为解析 wz 目录和 xml 文件的例子。
     */
    public static void main(String[] args) {
        File[] stringFiles = STRING_DIR.listFiles((dir, name) -> name.endsWith(".xml"));
        if (stringFiles == null || stringFiles.length == 0) {
            return;
        }

        Path out = Paths.get(Environments.USER_DIR, "out");
        try {
            Files.deleteIfExists(out);
            Files.createDirectories(out);
        } catch (IOException e) {
            LOGGER.error("无法重置 {} 目录：{}", out, e);
        }

        for (File stringFile : stringFiles) {
            if (Files.isDirectory(stringFile.toPath())) {
                continue;
            }

            String filename = stringFile.getName();
            Path target = Optional.of(filename)
                    .map(it -> it.split("\\."))
                    .filter(it -> it.length > 0)
                    // xxx.img.xml >>> xxx
                    .map(it -> it[0])
                    // resolve 方法：/path/to + args >>> /path/to/args
                    .map(out::resolve)
                    .orElse(out.resolve(filename));
            try {
                // Jsoup 自动用 body 包装 xml 内容，所以这里要拿 body 的子元素
                Elements imgDir = Jsoup.parse(stringFile, "UTF-8").body().children();
                // select 方法：tag[key=value] 匹配 <tag key="value"></tag>
                Elements strings = imgDir.select("string[name=name]");
                strings.forEach(element -> writeElement(target, element));
            } catch (IOException e) {
                LOGGER.error("无法解析 {} 文件：{}", stringFile, e);
            }
        }
    }

    private static void writeElement(Path target, Element element) {
        String parentName = element.parent().attr("name");
        String name = element.val();
        // String.wz 中的文件，一般是 name 为第一个元素，如果存在下一个元素，则必定是 desc 描述
        String desc = Optional.ofNullable(element.nextElementSibling()).map(Element::val).orElse("(无描述)");
        String format = String.format("%s - %s - %s%n", parentName, name, desc);
        try {
            Files.write(target, format.getBytes(UTF_8), CREATE, APPEND);
        } catch (IOException e) {
            LOGGER.error("无法写入 {} 文件：{}", target, e);
        }
    }
}
