package com.github.mrzhqiang.maplestory.wz;

import com.google.common.base.Splitter;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * wz 资源。
 */
public enum WzResource {
    ; // no instances

    public static final String WZ_KEY = "wz.path";
    public static final String DEFAULT_WZ_PATH = "wz";

    /**
     * wz 目录。
     * <p>
     * 默认值是 wz，可通过环境变量进行修改，也可以在启动时，使用 Dwz.path=wz 修改。
     */
    public static final File WZ_DIR = new File(System.getProperty(WZ_KEY, DEFAULT_WZ_PATH));
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

    private static final Logger LOGGER = LoggerFactory.getLogger(WzResource.class);

    /**
     * 作为解析 wz 目录和 xml 文件的例子。
     */
    public static void main(String[] args) {
        File[] stringFiles = STRING_DIR.listFiles((dir, name) -> name.endsWith(".xml"));
        if (stringFiles == null || stringFiles.length == 0) {
            return;
        }

        Path out = Paths.get(System.getProperty("user.dir"), "out");
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
                    .map(it -> Splitter.on('.').split(it).iterator())
                    .filter(Iterator::hasNext)
                    // xxx.img.xml >>> xxx
                    .map(Iterator::next)
                    // resolve 方法：/path/to + args >>> /path/to/args
                    .map(out::resolve)
                    .orElse(out.resolve(filename));
            try {
                // Jsoup 自动用 body 包装 xml 内容，所以这里要拿 body 的子元素
                Elements imgDir = Jsoup.parse(stringFile, StandardCharsets.UTF_8.name()).body().children();
                // select 方法：tag[key=value] 匹配 <tag key="value"></tag>
                Elements strings = imgDir.select("string[name=name]");
                strings.forEach(element -> writeElement(target, element));
            } catch (IOException e) {
                LOGGER.error("无法解析 {} 文件：{}", stringFile, e);
            }
        }
    }

    private static void writeElement(Path target, Element element) {
        Element parent = element.parent();
        String parentName = parent != null ? parent.attr("name") : "unknown";
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
