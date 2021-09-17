package server;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.World.Alliance;
import handling.world.World.Broadcast;
import handling.world.World.Family;
import handling.world.World.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.MaplePacketCreator;

import java.util.Set;

@Singleton
public class ShutdownServer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownServer.class);

    public static boolean running = false;
    public int mode = 0;

    private final LoginServer loginServer;

    @Inject
    public ShutdownServer(LoginServer loginServer) {
        this.loginServer = loginServer;
    }

    public static ShutdownServer getInstance() {
        return new ShutdownServer(null);
    }

    /*
     * @Override public void run() { synchronized (this) { if (running) { //Run
     * once! return; } running = true; } World.isShutDown = true; try { for
     * (ChannelServer cs : ChannelServer.getAllInstances()) { cs.setShutdown();
     * } LoginServer.shutdown(); Integer[] chs =
     * ChannelServer.getAllInstance().toArray(new Integer[0]);
     *
     * for (int i : chs) { try { ChannelServer cs =
     * ChannelServer.getInstance(i); synchronized (this) { cs.shutdown(this); //
     * try { // this.wait(); // } catch (InterruptedException ex) { // } } }
     * catch (Exception e) { e.printStackTrace(); } } //
     * CashShopServer.shutdown(); World.Guild.save(); World.Alliance.save();
     * World.Family.save(); DatabaseConnection.closeAll(); } catch (SQLException
     * e) { LOGGER.error("THROW" + e); } WorldTimer.getInstance().stop();
     * MapTimer.getInstance().stop(); MobTimer.getInstance().stop();
     * BuffTimer.getInstance().stop(); CloneTimer.getInstance().stop();
     * EventTimer.getInstance().stop(); EtcTimer.getInstance().stop();
     *
     * try { Thread.sleep(5000); } catch (Exception e) { //shutdown }
     * System.exit(0); //not sure if this is really needed for ChannelServer }
     */
    public void shutdown() {
        run();
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
                LOGGER.debug("频道" + String.valueOf(channel) + " 关闭错误.");
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
            DatabaseConnection.closeAll();
        } catch (Exception ignored) {
        }
        //Timer.PingTimer.getInstance().stop();
        LOGGER.debug("服务端关闭事件 2 已完成.");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            LOGGER.debug("关闭服务端错误 - 2" + e);

        }
        System.exit(0);

    }
}
