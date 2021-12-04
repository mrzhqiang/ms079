package com.github.mrzhqiang.maplestory.di;

import com.github.mrzhqiang.maplestory.util.Resources;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;

import javax.inject.Singleton;
import java.util.Properties;

/**
 * 配置模块。
 */
final class ConfigurationModule extends AbstractModule {

    static final ConfigurationModule INSTANCE = new ConfigurationModule();

    private static final String CONFIG_FILENAME = "服务端配置.ini";

    /**
     * 主要是提供给数据库配置使用，其他地方暂时用不到。
     */
    @Named("config")
    @Singleton
    @Provides
    static Properties provideProperties() {
        return Resources.fromExternal(CONFIG_FILENAME);
    }
}
