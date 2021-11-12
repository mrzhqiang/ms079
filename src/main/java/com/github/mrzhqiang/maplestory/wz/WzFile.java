package com.github.mrzhqiang.maplestory.wz;

import com.github.mrzhqiang.maplestory.wz.element.ImgdirElement;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import org.jsoup.select.Elements;

import java.util.Collection;

/**
 * wz 文件。
 * <p>
 * 类似 String.wz/Skill.img.xml 这样的文件，包括文件内容数据。
 */
public final class WzFile {

    /**
     * 文件中缀常量。
     */
    private static final String FILE_INFIX = ".img";
    /**
     * 文件后缀常量。
     */
    private static final String FILE_SUFFIX = ".xml";

    /**
     * 格式化文件名。
     *
     * @param filename 文件名，可以是全名，也可以是不带后缀以及中缀的名字
     * @return 文件全名，包含中缀以及后缀
     * @throws NullPointerException     文件名参数不能为 Null
     * @throws IllegalArgumentException 文件名参数不能是空字符串
     */
    public static String formatName(String filename) {
        Preconditions.checkNotNull(filename, "filename == null");
        Preconditions.checkArgument(!filename.isEmpty(), "filename is empty!");

        if (filename.endsWith(FILE_SUFFIX)) {
            return filename;
        }
        if (filename.endsWith(FILE_INFIX)) {
            return String.format("%s%s", filename, FILE_SUFFIX);
        }
        return String.format("%s%s%s", filename, FILE_INFIX, FILE_SUFFIX);
    }

    /**
     * 隐藏文件格式。
     *
     * @param filename 文件名。可以是全名，也可以是已经隐藏中缀以及后缀的名字
     * @return 隐藏格式的文件名。
     */
    public static String hideFormat(String filename) {
        Preconditions.checkNotNull(filename, "filename == null");
        if (filename.endsWith(FILE_SUFFIX) || filename.endsWith(FILE_INFIX)) {
            return Splitter.on(".").split(filename).iterator().next();
        }
        return filename;
    }

    /* Skill.img.xml */
    private final String name;
    /*
     * <imgdir name="000.img">
     * ...
     * </imgdir>
     * */
    private final ImgdirElement content;

    /**
     * @param filename 当前文件名的完整名字。
     * @param source   文件数据源。
     */
    WzFile(String filename, Elements source) {
        Preconditions.checkNotNull(filename, "filename == null");
        Preconditions.checkNotNull(source, "source == null");
        this.name = filename;
        this.content = ImgdirElement.of(source.first());
    }

    /**
     * @return 文件名称。此名称没有去除后缀，比如：Skill.img.xml
     */
    public String name() {
        return name;
    }

    /**
     * @return 文件小名。此名称去除了中缀以及后缀
     */
    public String smallName() {
        return hideFormat(name);
    }

    /**
     * @return xml 文件内容
     */
    public ImgdirElement content() {
        return content;
    }

    /**
     * @return 文件内容的子元素列表。
     */
    public Collection<WzElement<?>> contentChildren() {
        return content.children();
    }
}