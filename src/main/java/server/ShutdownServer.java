package server;

import javax.inject.Inject;
import javax.inject.Singleton;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.World.Alliance;
import handling.world.World.Broadcast;
import handling.world.World.Family;
import handling.world.World.Guild;
import io.ebean.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.MaplePacketCreator;

import java.util.Set;

@Singleton
public final class ShutdownServer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownServer.class);

    private final LoginServer loginServer;

    @Inject
    public ShutdownServer(LoginServer loginServer) {
        this.loginServer = loginServer;
    }

    @Override
    public void run() {
        //Timer
        Timer.WorldTimer.getInstance().stop();
        Timer.MapTimer.getInstance().stop();
        Timer.BuffTimer.getInstance().stop();
        Timer.CloneTimer.getInstance().stop();
        Timer.EventTimer.getInstance().stop();
        Timer.EtcTimer.getInstance().stop();

        //Merchant
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            cs.closeAllMerchant();
        }

        try {
            //Guild
            Guild.save();
            //Alliance
            Alliance.save();
            //Family
            Family.save();
        } catch (Exception ignored) {
        }

        Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(0, " 游戏服务器将关闭维护，请玩家安全下线..."));
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            try {
                cs.setServerMessage("游戏服务器将关闭维护，请玩家安全下线...");
            } catch (Exception ignored) {
            }
        }
        Set<Integer> channels = ChannelServer.getAllInstance();

        for (Integer channel : channels) {
            try {
                ChannelServer cs = ChannelServer.getInstance(channel);
                cs.saveAll();
                cs.setFinishShutdown();
                cs.shutdown();
            } catch (Exception e) {
                LOGGER.error("频道" + channel + " 关闭错误.", e);
            }
        }

        LOGGER.debug("服务端关闭事件 1 已完成.");
        LOGGER.debug("服务端关闭事件 2 开始...");

        try {
            if (loginServer != null) {
                loginServer.shutdown();
            }
            LOGGER.debug("登录伺服器关闭完成...");
        } catch (Exception ignored) {
        }
        try {
            CashShopServer.shutdown();
            LOGGER.debug("商城伺服器关闭完成...");
        } catch (Exception ignored) {
        }
        try {
            DB.getDefault().shutdown();
        } catch (Exception ignored) {
        }
        //Timer.PingTimer.getInstance().stop();
        LOGGER.debug("服务端关闭事件 2 已完成.");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            LOGGER.error("关闭服务端错误 - 2", e);

        }
        System.exit(0);
    }
}
