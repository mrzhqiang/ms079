package com.github.mrzhqiang.maplestory.starter;

import client.MapleCharacter;
import client.SkillFactory;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.github.mrzhqiang.maplestory.auth.AuthenticationServer;
import com.github.mrzhqiang.maplestory.service.AccountService;
import com.github.mrzhqiang.maplestory.service.ServerInfoService;
import com.github.mrzhqiang.maplestory.service.WorldService;
import com.github.mrzhqiang.maplestory.wz.WzLoadSubscriber;
import com.github.mrzhqiang.maplestory.wz.WzResource;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.login.LoginInformationProvider;
import handling.world.World;
import handling.world.family.MapleFamilyBuff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.AutobanManager;
import server.CashItemFactory;
import server.MapleCarnivalFactory;
import server.MapleItemInformationProvider;
import server.RandomRewards;
import server.ShutdownServer;
import server.SpeedRunner;
import com.github.mrzhqiang.maplestory.timer.Timer;
import server.events.MapleOxQuizFactory;
import server.quest.MapleQuest;
import tools.FileoutputUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 应用启动器。
 * <p>
 * 公共的启动类，命令行启动方式和 GUI 启动方式的通用逻辑。
 */
@Singleton
public final class ApplicationStarter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStarter.class);

    private static Integer maxOnline = null;

    private final ServerProperties properties;
    private final AccountService accountService;
    private final WorldService worldService;
    private final ServerInfoService serverInfoService;

    private final CashShopServer shopServer;
    private final AuthenticationServer authenticationServer;
    private final ShutdownServer shutdown;

    @Inject
    public ApplicationStarter(ServerProperties properties, AccountService accountService,
                              WorldService worldService, ServerInfoService serverInfoService,
                              CashShopServer shopServer, AuthenticationServer authenticationServer,
                              ShutdownServer shutdown) {
        this.properties = properties;
        this.accountService = accountService;
        this.worldService = worldService;
        this.serverInfoService = serverInfoService;
        this.shopServer = shopServer;
        this.authenticationServer = authenticationServer;
        this.shutdown = shutdown;
    }

    public void startServer() {
        WzResource.load().subscribe(new WzLoadSubscriber() {
            @Override
            public void onError(Throwable t) {
                LOGGER.error(">>> 错误",t);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                accountService.resetAccount();
                worldService.init();
                Timer.init();
                serverInfoService.display();

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
                authenticationServer.init();
                authenticationServer.run();
                LOGGER.info("<<< [登录服务器] 初始化完毕，耗时：{}", loginWatch.stop());

                LOGGER.info(">>> 初始化 [频道服务器]");
                Stopwatch channelWatch = Stopwatch.createStarted();
                ChannelServer.startChannel_Main();
                LOGGER.info("<<< [频道服务器] 初始化完毕，耗时：{}", channelWatch.stop());

                LOGGER.info(">>> 初始化 [商城服务器]");
                Stopwatch shopWatch = Stopwatch.createStarted();
                shopServer.start();
                LOGGER.info("<<< [商城服务器] 初始化完毕，耗时：{}", shopWatch.stop());

                Timer.CHEAT.register(AutobanManager.getInstance(), 60000);
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
                authenticationServer.setOn();
            }
        });
    }

    @SuppressWarnings("SameParameterValue")
    private static void onlineTime(int minutes) {
        LOGGER.debug("开启在线时间记录，{} 分钟自动记录一次", minutes);
        Timer.WORLD.register(() -> {
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
        Timer.WORLD.register(() -> {
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
        Timer.WORLD.register(() -> {
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

}
