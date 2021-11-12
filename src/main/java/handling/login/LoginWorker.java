package handling.login;

import java.util.Map;
import java.util.Map.Entry;

import client.MapleClient;
import handling.channel.ChannelServer;
import server.Timer.PingTimer;
import tools.packet.LoginPacket;
import tools.MaplePacketCreator;

/**
 * todo interface
 */
public class LoginWorker {

    private static long lastUpdate = 0;

    public static void registerClient(MapleClient client, LoginServer loginServer) {
        if (loginServer.isAdminOnly() && !client.isGm()) {
            client.getSession().write(MaplePacketCreator.serverNotice(1, "服务器正在维护中"));
            client.getSession().write(LoginPacket.getLoginFailed(7));
            return;
        }

        if (System.currentTimeMillis() - lastUpdate > 600000) { // 每10分钟更新一次
            lastUpdate = System.currentTimeMillis();
            Map<Integer, Integer> load = ChannelServer.getChannelLoad();
            int usersOn = 0;

            if (load == null || load.size() <= 0) { // In an unfortunate event that client logged in before load
                lastUpdate = 0;
                client.getSession().write(LoginPacket.getLoginFailed(7));

                return;
            }
            double loads = load.size();
            double userlimit = LoginServer.getUserLimit();
            double loadFactor = 1200 / ((double) LoginServer.getUserLimit() / load.size());
            for (Entry<Integer, Integer> entry : load.entrySet()) {
                usersOn += entry.getValue();
                load.put(entry.getKey(), Math.min(1200, (int) (entry.getValue() * loadFactor)));

            }
            loginServer.setLoad(load, usersOn);
            lastUpdate = System.currentTimeMillis();

        }

        if (client.finishLogin() == 0) {
            if (client.getGender() == 10) {
                client.getSession().write(LoginPacket.getGenderNeeded(client));
            } else {
                client.getSession().write(LoginPacket.getAuthSuccessRequest(client));
                client.getSession().write(LoginPacket.getServerList(0, loginServer.getServerName(), loginServer.getLoad()));
                client.getSession().write(LoginPacket.getEndOfServerList());

            }
            client.setIdleTask(PingTimer.getInstance().schedule(() -> {
//                    client.getSession().close();
            }, 10 * 60 * 10000));
        } else {
            if (client.getGender() == 10) {
                client.getSession().write(LoginPacket.getGenderNeeded(client));

            } else {
                client.getSession().write(LoginPacket.getAuthSuccessRequest(client));
                client.getSession().write(LoginPacket.getServerList(0, loginServer.getServerName(), loginServer.getLoad()));
                client.getSession().write(LoginPacket.getEndOfServerList());

            }
           /* client.getSession().write(LoginPacket.getLoginFailed(7));

            LOGGER.debug("登录Z");
            return;*/
        }
    }
}
