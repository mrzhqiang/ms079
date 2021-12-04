package com.github.mrzhqiang.maplestory;

import com.github.mrzhqiang.maplestory.di.Injectors;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.mrzhqiang.maplestory.starter.ApplicationStarter;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * 小枫叶应用程序。
 * <p>
 * 这里是命令行启动入口，要通过 GUI 界面启动，请使用 gui.GUIApplication 类。
 */
@Singleton
public final class MapleStoryApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleStoryApplication.class);

    public static void main(String[] args) {
        Injectors.get(MapleStoryApplication.class).start();
    }

    private final ApplicationStarter starter;

    @Inject
    public MapleStoryApplication(ApplicationStarter starter) {
        this.starter = starter;
    }

    public void start() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        starter.startServer();
        LOGGER.info("服务端启动完毕！总计耗时：{}，现在可以进入游戏了...", stopwatch.stop());
    }
}
