package com.github.mrzhqiang.maplestory.di;

import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDGuild;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;

import javax.inject.Singleton;
import java.util.Properties;

/**
 * 数据库模块。
 */
final class DatabaseModule extends AbstractModule {

    static final DatabaseModule INSTANCE = new DatabaseModule();

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

    @Singleton
    @Provides
    static QDAccount qdAccount(Database database) {
        return new QDAccount(database);
    }

    @Singleton
    @Provides
    static QDGuild qdGuild(Database database) {
        return new QDGuild(database);
    }

    @Singleton
    @Provides
    static QDCharacter qdCharacter(Database database) {
        return new QDCharacter(database);
    }
}
