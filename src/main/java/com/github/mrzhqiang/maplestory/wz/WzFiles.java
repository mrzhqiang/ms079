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

public enum WzFiles {
    ;
    private static final Logger LOGGER = LoggerFactory.getLogger(WzFiles.class);

    public static final String WZ_KEY = "wz.path";
    public static final String DEFAULT_WZ_PATH = "/wz";
    public static final File WZ_DIR = new File(System.getProperty(WZ_KEY, Environments.USER_DIR + DEFAULT_WZ_PATH));

    public static final File BASE_DIR = new File(WZ_DIR, "Base.wz");
    public static final File CHARACTER_DIR = new File(WZ_DIR, "Character.wz");
    public static final File EFFECT_DIR = new File(WZ_DIR, "Effect.wz");
    public static final File ETC_DIR = new File(WZ_DIR, "Etc.wz");
    public static final File ITEM_DIR = new File(WZ_DIR, "Item.wz");
    public static final File MAP_DIR = new File(WZ_DIR, "Map.wz");
    public static final File MOB_DIR = new File(WZ_DIR, "Mob.wz");
    public static final File MORPH_DIR = new File(WZ_DIR, "Morph.wz");
    public static final File NPC_DIR = new File(WZ_DIR, "Npc.wz");
    public static final File QUEST_DIR = new File(WZ_DIR, "Quest.wz");
    public static final File REACTOR_DIR = new File(WZ_DIR, "Reactor.wz");
    public static final File SKILL_DIR = new File(WZ_DIR, "Skill.wz");
    public static final File SOUND_DIR = new File(WZ_DIR, "Sound.wz");
    public static final File STRING_DIR = new File(WZ_DIR, "String.wz");
    public static final File TAMING_MOB_DIR = new File(WZ_DIR, "TamingMob.wz");
    public static final File UI_DIR = new File(WZ_DIR, "UI.wz");

    public static void main(String[] args) {
        // 必须是 debug 模式下才可以输出到 out 目录
        if (!Environments.debug()) {
            return;
        }

        File[] foundFiles = new File(WzFiles.WZ_DIR, "String.wz")
                .listFiles((dir, name) -> name.endsWith(".xml"));
        if (foundFiles == null) {
            return;
        }

        Path out = Paths.get(Environments.USER_DIR, "out");
        try {
            Files.deleteIfExists(out);
            Files.createDirectories(out);
        } catch (IOException e) {
            LOGGER.error("无法操作目录：" + out, e);
        }

        for (File foundFile : foundFiles) {
            try {
                // Jsoup 在解析时自动用 html + body 包装 xml 内容，所以这里要拿到 body 的子元素
                Elements imgDir = Jsoup.parse(foundFile, "UTF-8").body().children();
                // select 方法执行 css 选择器语法
                // 比如 tag[key=value] 是匹配 <tag key="value"></tag> 元素
                Elements strings = imgDir.select("string[name=name]");
                String filename = foundFile.getName();
                Path target = Optional.of(filename)
                        .map(it -> it.split("\\."))
                        .filter(it -> it.length > 0)
                        .map(it -> it[0])
                        // resolve 方法将指定参数拼接到路径之后，比如 /path/to + args =>>> /path/to/args
                        // 这里的作用是按照 String.wz 目录结构输出
                        .map(out::resolve)
                        .orElse(out.resolve(filename));
                strings.forEach(element -> writeElement(target, element));
            } catch (IOException e) {
                LOGGER.error("解析 [" + foundFile + "] 文件出现错误！", e);
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
            // 以 UTF-8 字节流写入内容到指定路径，CREATE 参数是文件不存在自动创建，APPEND 参数是每次追加到末尾
            Files.write(target, format.getBytes(UTF_8), CREATE, APPEND);
        } catch (IOException e) {
            LOGGER.error("写入到 [" + target + "] 文件出现错误！", e);
        }
    }
}
