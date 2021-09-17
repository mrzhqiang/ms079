package com.github.mrzhqiang.maplestory.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

/**
 * 服务端配置。
 */
public final class ServerConfiguration extends AbstractModule {

    public static final ServerConfiguration INSTANCE = new ServerConfiguration();

    private ServerConfiguration() {
        // inner instance
    }

    private static final String CONFIG_FILE = "服务端配置.ini";

    /**
     * 主要是提供给数据库配置使用，其他地方暂时用不到。
     */
    @Named("config")
    @Singleton
    @Provides
    static Properties provideProperties() {
        Properties properties = new Properties();
        try (Reader reader = new FileReader(CONFIG_FILE)) {
            properties.load(reader);
        } catch (Exception e) {
            throw new RuntimeException(String.format("加载 %s 文件出现问题。", CONFIG_FILE), e);
        }
        return properties;
    }

}
