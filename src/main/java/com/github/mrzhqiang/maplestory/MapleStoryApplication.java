package com.github.mrzhqiang.maplestory;

import com.github.mrzhqiang.maplestory.config.DatabaseConfiguration;
import com.github.mrzhqiang.maplestory.config.ServerConfiguration;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.google.common.base.Stopwatch;
import com.google.inject.Guice;
import com.google.inject.Injector;
import constants.ServerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.ApplicationStarter;

public final class MapleStoryApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleStoryApplication.class);

    public static void main(String[] args) {
        Stopwatch allWatch = Stopwatch.createStarted();
        Injector injector = Guice.createInjector(
                ServerConfiguration.INSTANCE,
                DatabaseConfiguration.INSTANCE
        );
        ApplicationStarter starter = injector.getInstance(ApplicationStarter.class);
        ServerConstants.setProperties(injector.getInstance(ServerProperties.class));
        starter.startServer(injector);
        LOGGER.info("服务端启动完毕！总计耗时：{}，现在可以进入游戏了...", allWatch.stop());
    }

}
