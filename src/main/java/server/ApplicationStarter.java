package server;

import client.MapleCharacter;
import client.SkillFactory;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.github.mrzhqiang.maplestory.domain.query.QDAccount;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.login.LoginInformationProvider;
import handling.login.LoginServer;
import handling.world.World;
import handling.world.family.MapleFamilyBuff;
import io.ebean.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.events.MapleOxQuizFactory;
import server.quest.MapleQuest;
import tools.FileoutputUtil;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Singleton
public final class ApplicationStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStarter.class);

    private static Integer maxOnline = null;

    private final ServerProperties properties;
    private final CashShopServer shopServer;
    private final LoginServer loginServer;
    private final Database database;
    private final Shutdown shutdown;

    @Inject
    public ApplicationStarter(ServerProperties properties,
                              CashShopServer shopServer,
                              LoginServer loginServer,
                              Database database,
                              Shutdown shutdown) {
        this.properties = properties;
        this.shopServer = shopServer;
        this.loginServer = loginServer;
        this.database = database;
        this.shutdown = shutdown;
    }

    public Shutdown getShutdown() {
        return shutdown;
    }

    public void startServer(Injector injector) {
        if (properties.isAdminLogin()) {
            LOGGER.info(">>> 登录模式：只允许管理员登录");
        } else {
            LOGGER.info(">>> 登录模式：允许所有用户登录");
        }
        if (properties.isAutoRegister()) {
            LOGGER.info(">>> 注册模式：自动");
        } else {
            LOGGER.info(">>> 注册模式：手动（目前未实现网页注册、程序注册，需要使用 GM 工具手动创建账号）");
        }

        LOGGER.info(">>> 重置 [账号状态]");
        Stopwatch resetWatch = Stopwatch.createStarted();
        @SuppressWarnings("SqlWithoutWhere")
        int loggedIn = database.sqlUpdate("UPDATE accounts SET loggedin = 0").execute();
        @SuppressWarnings("SqlWithoutWhere")
        int lastGainHM = database.sqlUpdate("UPDATE accounts SET lastGainHM = 0").execute();
        LOGGER.info("<<< [账号状态] 重置完毕，耗时：{}，影响行数：loggedin={} lastGainHM={}", resetWatch.stop(), loggedIn, lastGainHM);

        LOGGER.info(">>> 初始化 [NPC数据]");
        Stopwatch npcWatch = Stopwatch.createStarted();
        WzData.load();
        LOGGER.info("<<< [NPC数据] 初始化完毕，耗时：{}", npcWatch.stop());

        Stopwatch worldWatch = Stopwatch.createStarted();
        LOGGER.info(">>> 初始化 [世界服务器]");
        World.init();
        LOGGER.info("<<< [世界服务器] 初始化完毕，耗时：{}", worldWatch.stop());

        LOGGER.info(">>> 初始化 [时钟线程]");
        Stopwatch timeWatch = Stopwatch.createStarted();
        Timer.WorldTimer.getInstance().start();
        Timer.EtcTimer.getInstance().start();
        Timer.MapTimer.getInstance().start();
        Timer.MobTimer.getInstance().start();
        Timer.CloneTimer.getInstance().start();
        Timer.EventTimer.getInstance().start();
        Timer.BuffTimer.getInstance().start();
        Timer.TimerManager.getInstance().start();//点卷赌博的时钟线程
        LOGGER.info("<<< [时钟线程] 初始化完毕，耗时：{}", timeWatch.stop());

        LOGGER.info(">>> 初始化 [任务数据]");
        Stopwatch questWatch = Stopwatch.createStarted();
        MapleQuest.initQuests();
        LOGGER.info("<<< [任务数据] 初始化完毕，耗时：{}", questWatch.stop());

        LOGGER.info(">>> 初始化 [道具数据]");
        Stopwatch itemWatch = Stopwatch.createStarted();
        MapleItemInformationProvider.getInstance().runEtc();
        MapleItemInformationProvider.getInstance().runItems();
        LOGGER.info("<<< [道具数据] 初始化完毕，耗时：{}", itemWatch.stop());

        LOGGER.info(">>> 初始化 [脏话检测]");
        Stopwatch infoWatch = Stopwatch.createStarted();
        LoginInformationProvider.init();
        LOGGER.info("<<< [脏话检测] 初始化完毕，耗时：{}", infoWatch.stop());

        LOGGER.info(">>> 初始化 [随机奖励]");
        Stopwatch randomWatch = Stopwatch.createStarted();
        RandomRewards.init();
        LOGGER.info("<<< [随机奖励] 初始化完毕，耗时：{}", randomWatch.stop());

        LOGGER.info(">>> 初始化 [OX题目]");
        Stopwatch oxWatch = Stopwatch.createStarted();
        MapleOxQuizFactory.getInstance().initialize();
        LOGGER.info("<<< [OX题目] 初始化完毕，耗时：{}", oxWatch.stop());

        LOGGER.info(">>> 初始化 [技能数据]");
        Stopwatch skillWatch = Stopwatch.createStarted();
        SkillFactory.init();
        MapleFamilyBuff.init();
        MapleCarnivalFactory.init();
        LOGGER.info("<<< [技能数据] 初始化完毕，耗时：{}", skillWatch.stop());

        LOGGER.info(">>> 初始化 [排名系统]");
        Stopwatch rankWatch = Stopwatch.createStarted();
        MapleGuildRanking.getInstance().RankingUpdate();
        LOGGER.info("<<< [排名系统] 初始化完毕，耗时：{}", rankWatch.stop());

        LOGGER.info(">>> 初始化 [商城道具]");
        Stopwatch mallWatch = Stopwatch.createStarted();
        CashItemFactory.getInstance().initialize();
        LOGGER.info("<<< [商城道具] 初始化完毕，耗时：{}", mallWatch.stop());

        LOGGER.info(">>> 初始化 [登录服务器]");
        Stopwatch loginWatch = Stopwatch.createStarted();
        loginServer.run_startup_configurations();
        LOGGER.info("<<< [登录服务器] 初始化完毕，耗时：{}", loginWatch.stop());

        LOGGER.info(">>> 初始化 [频道服务器]");
        Stopwatch channelWatch = Stopwatch.createStarted();
        ChannelServer.startChannel_Main(injector);
        LOGGER.info("<<< [频道服务器] 初始化完毕，耗时：{}", channelWatch.stop());

        LOGGER.info(">>> 初始化 [商城服务器]");
        Stopwatch shopWatch = Stopwatch.createStarted();
        shopServer.start();
        LOGGER.info("<<< [商城服务器] 初始化完毕，耗时：{}", shopWatch.stop());

        Timer.CheatTimer.getInstance().register(AutobanManager.getInstance(), 60000);
        ApplicationStarter.onlineCount(60);
        ApplicationStarter.autoSave(30);
        ApplicationStarter.onlineTime(1);

        if (properties.isRandDrop()) {
            ChannelServer.getInstance(1).getMapFactory().getMap(910000000).spawnRandDrop();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(shutdown));

        try {
            SpeedRunner.getInstance().loadSpeedRuns();
        } catch (SQLException e) {
            LOGGER.error("SpeedRunner 错误", e);
        }
        World.registerRespawn();
        loginServer.setOn();
        LOGGER.info("经验倍率：{}，物品倍率：{}，金币倍率：{}", properties.getExpRate(), properties.getDropRate(), properties.getGoldRate());
        LOGGER.info("当前开放职业：冒险家 = {}, 骑士团 = {}, 战神 = {}", properties.isAdventurer(), properties.isKnights(), properties.isWarGod());
    }

    @SuppressWarnings("SameParameterValue")
    private static void onlineTime(int minutes) {
        LOGGER.debug("开启在线时间记录，{} 分钟自动记录一次", minutes);
        Timer.WorldTimer.getInstance().register(() -> {
            try {
                for (ChannelServer chan : ChannelServer.getAllInstances()) {
                    for (MapleCharacter chr : chan.getPlayerStorage().getAllCharacters()) {
                        if (chr == null) {
                            continue;
                        }
                        chr.gainGamePoints(1);
                        if (chr.getGamePoints() < 5) {
                            chr.resetGamePointsPD();
                        }
                    }
                }
            } catch (Exception ignore) {
            }
        }, TimeUnit.MINUTES.toMillis(minutes));
    }

    @SuppressWarnings("SameParameterValue")
    private static void onlineCount(int minutes) {
        LOGGER.info("开启在线统计，{} 分钟统计一次", minutes);
        Timer.WorldTimer.getInstance().register(() -> {
            Map<Integer, Integer> connected = World.getConnected();
            StringBuilder conStr = new StringBuilder(FileoutputUtil.CurrentReadable_Time() + " 在线人数: ");
            for (int i : connected.keySet()) {
                // fixme ???
                if (i == 0) {
                    int users = connected.get(i);
                    String userFormat = Strings.padStart(String.valueOf(users), 3, ' ');
                    conStr.append(userFormat);
                    if (maxOnline == null || users > maxOnline) {
                        maxOnline = users;
                    }
                    conStr.append(" 最高在线: ");
                    conStr.append(maxOnline);
                    break;
                }
            }
            if (maxOnline != null) {
                LOGGER.info("[在线统计] {}", conStr);
            }
        }, TimeUnit.MINUTES.toMillis(minutes));
    }

    @SuppressWarnings("SameParameterValue")
    private static void autoSave(int minutes) {
        LOGGER.info("开启自动存档，{} 分钟执行一次", minutes);
        Timer.WorldTimer.getInstance().register(() -> {
            int ppl = 0;
            try {
                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                        if (chr == null) {
                            continue;
                        }
                        ppl++;
                        chr.saveToDB(false, false);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("自动存档时，获取玩家人数发生异常。", e);
            }
            LOGGER.info("[自动存档] 完毕，共计保存 " + ppl + " 位玩家数据。");

        }, TimeUnit.MINUTES.toMillis(minutes));
    }

    @Singleton
    public static class Shutdown implements Runnable {

        private final ShutdownServer server;

        @Inject
        public Shutdown(ShutdownServer server) {
            this.server = server;
        }

        @Override
        public void run() {
            server.run();
        }

    }
}
