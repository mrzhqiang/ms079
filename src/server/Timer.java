package server;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import tools.FileoutputUtil;

public abstract class Timer {

    public static class WorldTimer extends Timer {

        private static WorldTimer instance = new WorldTimer();

        private WorldTimer() {
            name = "Worldtimer";
        }

        public static WorldTimer getInstance() {
            return instance;
        }
    }

    public static class MapTimer extends Timer {

        private static MapTimer instance = new MapTimer();

        private MapTimer() {
            name = "Maptimer";
        }

        public static MapTimer getInstance() {
            return instance;
        }
    }

    public static class TimerManager extends Timer {

        private static TimerManager instance = new TimerManager();

        private TimerManager() {
            name = "TimerManager";
        }

        public static TimerManager getInstance() {
            return instance;
        }
    }

    public static class BuffTimer extends Timer {

        private static BuffTimer instance = new BuffTimer();

        private BuffTimer() {
            name = "Bufftimer";
        }

        public static BuffTimer getInstance() {
            return instance;
        }
    }

    public static class EventTimer extends Timer {

        private static EventTimer instance = new EventTimer();

        private EventTimer() {
            name = "Eventtimer";
        }

        public static EventTimer getInstance() {
            return instance;
        }
    }

    public static class CloneTimer extends Timer {

        private static CloneTimer instance = new CloneTimer();

        private CloneTimer() {
            name = "Clonetimer";
        }

        public static CloneTimer getInstance() {
            return instance;
        }
    }

    public static class EtcTimer extends Timer {

        private static EtcTimer instance = new EtcTimer();

        private EtcTimer() {
            name = "Etctimer";
        }

        public static EtcTimer getInstance() {
            return instance;
        }
    }

    public static class MobTimer extends Timer {

        private static MobTimer instance = new MobTimer();

        private MobTimer() {
            name = "Mobtimer";
        }

        public static MobTimer getInstance() {
            return instance;
        }
    }

    public static class CheatTimer extends Timer {

        private static CheatTimer instance = new CheatTimer();

        private CheatTimer() {
            name = "Cheattimer";
        }

        public static CheatTimer getInstance() {
            return instance;
        }
    }

    public static class PingTimer extends Timer {

        private static PingTimer instance = new PingTimer();

        private PingTimer() {
            name = "Pingtimer";
        }

        public static PingTimer getInstance() {
            return instance;
        }
    }

    private ScheduledThreadPoolExecutor ses;
    protected String file, name;

    public void start() {
        if (ses != null && !ses.isShutdown() && !ses.isTerminated()) {
            return;
        }
        file = "日志/logs/Log_" + name + "_Except.rtf";
        final String tname = name + Randomizer.nextInt(); //just to randomize it. nothing too big
        final ThreadFactory thread = new ThreadFactory() {

            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                final Thread t = new Thread(r);
                t.setName(tname + "-Worker-" + threadNumber.getAndIncrement());
                return t;
            }
        };

        final ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(3, thread);
        stpe.setKeepAliveTime(10, TimeUnit.MINUTES);
        stpe.allowCoreThreadTimeOut(true);
        stpe.setCorePoolSize(4);
        stpe.setMaximumPoolSize(8);
        stpe.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        ses = stpe;
    }

    public void stop() {
        ses.shutdown();
    }

    public ScheduledFuture<?> register(Runnable r, long repeatTime, long delay) {
        if (ses == null) {
            return null;
        }
        return ses.scheduleAtFixedRate(new LoggingSaveRunnable(r, file), delay, repeatTime, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> register(Runnable r, long repeatTime) {
        if (ses == null) {
            return null;
        }
        return ses.scheduleAtFixedRate(new LoggingSaveRunnable(r, file), 0, repeatTime, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> schedule(Runnable r, long delay) {
        if (ses == null) {
            return null;
        }
        return ses.schedule(new LoggingSaveRunnable(r, file), delay, TimeUnit.MILLISECONDS);
    }

    public ScheduledFuture<?> scheduleAtTimestamp(Runnable r, long timestamp) {
        return schedule(r, timestamp - System.currentTimeMillis());
    }

    private static class LoggingSaveRunnable implements Runnable {

        Runnable r;
        String file;

        public LoggingSaveRunnable(final Runnable r, final String file) {
            this.r = r;
            this.file = file;
        }

        @Override
        public void run() {
            try {
                r.run();
            } catch (Throwable t) {
                FileoutputUtil.outputFileError(file, t);
                //t.printStackTrace(); //mostly this gives un-needed errors... that take up a lot of space
            }
        }
    }
}
