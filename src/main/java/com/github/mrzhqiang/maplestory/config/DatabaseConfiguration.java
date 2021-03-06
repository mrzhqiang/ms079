package com.github.mrzhqiang.maplestory.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;

import java.util.Properties;

/**
 * 数据库配置。
 */
public final class DatabaseConfiguration extends AbstractModule {

    public static final DatabaseConfiguration INSTANCE = new DatabaseConfiguration();

    private DatabaseConfiguration() {
        // inner instance
    }

    @Singleton
    @Provides
    static Database provideEBeanDatabase(DataSourceConfig dataSourceConfig) {
        DatabaseConfig config = new DatabaseConfig();
        config.setDataSourceConfig(dataSourceConfig);
        // Database 默认情况下，自动注册到 DB 单例中，因此可以在全局使用 DB 来执行原生 SQL
        // 通常来说，如果使用 Guice 框架，则最好是将 Database 注入到类中
        // 除非需要在工具类的静态方法中操作数据库，此时可以使用 DB 类
        return DatabaseFactory.create(config);
    }

    @Singleton
    @Provides
    static DataSourceConfig provideDataSourceConfig(@Named("config") Properties properties) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        // 如果 serverName 是 ms079，则表示配置的 Key 值，包含名为 ms079 的中缀
        // 例如：datasource.ms079.username = root
        // 这里我们不设置 serverName
        dataSourceConfig.loadSettings(properties, null);
        return dataSourceConfig;
    }
}
