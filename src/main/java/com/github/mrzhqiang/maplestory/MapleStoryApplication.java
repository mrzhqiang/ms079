package com.github.mrzhqiang.maplestory;

import com.github.mrzhqiang.maplestory.di.Injectors;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.ApplicationStarter;


public final class MapleStoryApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleStoryApplication.class);

    public static void main(String[] args) {
        Stopwatch allWatch = Stopwatch.createStarted();
        // 初始化注入器并获得应用启动器
        ApplicationStarter starter = Injectors.get(ApplicationStarter.class);
        starter.startServer();
        LOGGER.info("服务端启动完毕！总计耗时：{}，现在可以进入游戏了...", allWatch.stop());
    }

}
