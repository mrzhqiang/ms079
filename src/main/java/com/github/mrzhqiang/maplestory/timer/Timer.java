package com.github.mrzhqiang.maplestory.timer;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Randomizer;

import javax.annotation.Nonnull;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public enum Timer {
    WORLD("Worldtimer"),
    MAP("Maptimer"),
    //点卷赌博的时钟线程
    MANAGER("TimerManager"),
    BUFF("Bufftimer"),
    EVENT("Eventtimer"),
    CLONE("Clonetimer"),
    ETC("Etctimer"),
    MOB("Mobtimer"),
    CHEAT("Cheattimer"),
    PING("Pingtimer"),
    ;

    private static final Logger LOGGER = LoggerFactory.getLogger(Timer.class);

    private final String name;

    private ScheduledThreadPoolExecutor poolExecutor;

    Timer(String name) {
        this.name = name;
    }

    public static void init() {
        LOGGER.info(">>> 初始化 [时钟线程]");
        Stopwatch timeWatch = Stopwatch.createStarted();
        for (Timer timer : Timer.values()) {
            timer.start();
        }
        LOGGER.info("<<< [时钟线程] 初始化完毕，耗时：{}", timeWatch.stop());
    }

    public void start() {
        if (poolExecutor != null && !poolExecutor.isShutdown() && !poolExecutor.isTerminated()) {
            return;
        }
        String tname = name + Randomizer.nextInt(); //just to randomize it. nothing too big
        ThreadFactory threadFactory = new ThreadFactory() {

            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(@Nonnull Runnable runnable) {
                Thread t = new Thread(runnable);
                t.setName(tname + "-Worker-" + threadNumber.getAndIncrement());
                return t;
            }
        };

        ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(3, threadFactory);
        stpe.setKeepAliveTime(10, TimeUnit.MINUTES);
        stpe.allowCoreThreadTimeOut(true);
        stpe.setCorePoolSize(4);
        stpe.setMaximumPoolSize(8);
        stpe.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        poolExecutor = stpe;
    }

    public void stop() {
        poolExecutor.shutdown();
    }

    public ScheduledFuture<?> register(Runnable runnable, long repeatTime, long delay) {
        if (poolExecutor == null) {
            return null;
        }
        return poolExecutor.scheduleAtFixedRate(new LoggingSaveRunnable(runnable), delay, repeatTime, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> register(Runnable runnable, long repeatTime) {
        if (poolExecutor == null) {
            return null;
        }
        return poolExecutor.scheduleAtFixedRate(new LoggingSaveRunnable(runnable), 0, repeatTime, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> schedule(Runnable runnable, long delay) {
        if (poolExecutor == null) {
            return null;
        }
        return poolExecutor.schedule(new LoggingSaveRunnable(runnable), delay, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> scheduleAtTimestamp(Runnable runnable, long timestamp) {
        return schedule(runnable, timestamp - System.currentTimeMillis());
    }

    private static class LoggingSaveRunnable implements Runnable {

        Runnable runnable;

        LoggingSaveRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            try {
                runnable.run();
            } catch (Throwable t) {
                LOGGER.error("运行定时任务时出错", t);
            }
        }
    }
}
