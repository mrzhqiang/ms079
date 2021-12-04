package com.github.mrzhqiang.maplestory.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.util.Optional;
import java.util.Properties;

/**
 * 资源辅助工具。
 */
public enum Resources {
    ; // no instances

    private static final Logger LOGGER = LoggerFactory.getLogger(Resources.class);

    /**
     * 从外部加载资源。
     * <p>
     * 外部是指程序 jar 包之外的位置。
     *
     * @param file 外部文件地址。
     * @return 包含文件内容的属性。
     */
    public static Properties fromExternal(String file) {
        Preconditions.checkNotNull(file, "file == null");
        Properties properties = new Properties();
        try (Reader reader = new FileReader(file)) {
            properties.load(reader);
        } catch (Exception e) {
            // 我们不希望任何异常打断程序运行，所以必须保持对错误日志的关注
            String message = Strings.lenientFormat("从外部加载 %s 文件出错。", file);
            LOGGER.error(message, e);
        }
        return properties;
    }

    /**
     * 从类路径加载资源。
     * <p>
     * 类路径就是 src/main/resources 下面的文件路径。
     *
     * @param file 类路径文件地址。
     * @return 包含文件内容的属性。
     */
    public static Properties fromClasspath(String file) {
        Preconditions.checkNotNull(file, "file == null");
        ClassLoader classLoader = Optional.ofNullable(ClassLoaders.ofDefault())
                .orElseGet(Resources.class::getClassLoader);
        Properties props = new Properties();
        try (InputStream resourceAsStream = classLoader.getResourceAsStream(file)) {
            props.load(resourceAsStream);
        } catch (Exception e) {
            // 我们不希望任何异常打断程序运行，所以必须保持对错误日志的关注
            String message = Strings.lenientFormat("从类路径加载 %s 文件出错。", file);
            LOGGER.error(message, e);
        }
        return props;
    }
}
