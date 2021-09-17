package com.github.mrzhqiang.maplestory.wz;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * wz 目录。
 * <p>
 * 可以用来查找目录下的子目录和子文件，同时提供目录流和文件流操作。
 * <p>
 * 支持重载时的并发查询，使用 ConcurrentHashMap 保证线程安全。
 */
public final class WzDirectory {

    private static final Logger LOGGER = LoggerFactory.getLogger(WzDirectory.class);

    // key 字符串为目录名称，带 .wz 后缀。不会导致冲突，因为重名目录属于子目录中的 dirs
    private final Map<String, WzDirectory> dirs = Maps.newConcurrentMap();
    // key 字符串为文件名称，不带 .xml 后缀。不会导致冲突，因为重名文件属于子目录中的 files
    private final Map<String, WzFile> files = Maps.newConcurrentMap();
    // 当前目录的路径
    private final Path path;

    /**
     * @param path wz 文件路径。不允许为 Null
     */
    WzDirectory(Path path) {
        Preconditions.checkNotNull(path, "path == null");
        this.path = path;
    }

    /**
     * 通过指定名字，找到当前目录下可能存在的 wz 目录。
     *
     * @param name 指定名字，比如：Skill.wz
     * @return 可选的 wz 目录。
     */
    public Optional<WzDirectory> findDir(String name) {
        return Optional.ofNullable(name)
                .filter(it -> !Strings.isNullOrEmpty(it))
                .filter(it -> !dirs.isEmpty())
                .map(dirs::get);
    }

    /**
     * 通过指定名字，找到当前目录下可能存在的 wz 文件。
     * <p>
     * 名字如果不包含 .img 中缀，则自动补充。
     * <p>
     * 名字如果不是 .xml 结尾，则自动补充。
     *
     * @param name 指定名字，比如：000.img.xml 或 000.img 或 000，这三个名字返回相同的内容
     * @return 可选的 wz 文件。
     */
    public Optional<WzFile> findFile(String name) {
        return Optional.ofNullable(name)
                .filter(it -> !Strings.isNullOrEmpty(it))
                .filter(it -> !files.isEmpty())
                .map(WzFile::formatName)
                .map(files::get);
    }

    /**
     * @return 子目录流
     */
    public Stream<WzDirectory> dirStream() {
        return dirs.values().stream();
    }

    /**
     * @return 子文件流
     */
    public Stream<WzFile> fileStream() {
        return files.values().stream();
    }

    /**
     * @return 目录名字。
     */
    public String name() {
        return path.getFileName().toString();
    }

    /**
     * 解析当前目录文件下的所有内容，包括子目录和子文件。
     */
    void parse() {
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
            String fileName = path.getFileName().toString();
            if (Files.isDirectory(path)) {
                WzDirectory directory = new WzDirectory(path);
                dirs.put(fileName, directory);
            } else {
                Elements data = Jsoup.parse(path.toFile(), "UTF-8").body().children();
                files.put(fileName, new WzFile(fileName, data));
            }
        } catch (IOException e) {
            LOGGER.error("解析 {} 出现问题：{}", path, e);
        }
    }
}