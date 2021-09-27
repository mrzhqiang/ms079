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

    public static void registerClient(final MapleClient c, LoginServer loginServer) {
        if (loginServer.isAdminOnly() && !c.isGm()) {
            c.getSession().write(MaplePacketCreator.serverNotice(1, "服务器正在维护中"));
            c.getSession().write(LoginPacket.getLoginFailed(7));
            return;
        }

        if (System.currentTimeMillis() - lastUpdate > 600000) { // Update once every 10 minutes
            lastUpdate = System.currentTimeMillis();
            final Map<Integer, Integer> load = ChannelServer.getChannelLoad();
            int usersOn = 0;

            if (load == null || load.size() <= 0) { // In an unfortunate event that client logged in before load
                lastUpdate = 0;
                c.getSession().write(LoginPacket.getLoginFailed(7));

                return;
            }
            double loads = load.size();
            double userlimit = loginServer.getUserLimit();
            final double loadFactor = 1200 / ((double) loginServer.getUserLimit() / load.size());
            for (Entry<Integer, Integer> entry : load.entrySet()) {
                usersOn += entry.getValue();
                load.put(entry.getKey(), Math.min(1200, (int) (entry.getValue() * loadFactor)));

            }
            loginServer.setLoad(load, usersOn);
            lastUpdate = System.currentTimeMillis();

        }

        if (c.finishLogin() == 0) {
            if (c.getGender() == 10) {
                c.getSession().write(LoginPacket.getGenderNeeded(c));
            } else {
                c.getSession().write(LoginPacket.getAuthSuccessRequest(c));
                c.getSession().write(LoginPacket.getServerList(0, loginServer.getServerName(), loginServer.getLoad()));
                c.getSession().write(LoginPacket.getEndOfServerList());

            }
            c.setIdleTask(PingTimer.getInstance().schedule(() -> {
//                    c.getSession().close();
            }, 10 * 60 * 10000));
        } else {
            if (c.getGender() == 10) {
                c.getSession().write(LoginPacket.getGenderNeeded(c));

            } else {
                c.getSession().write(LoginPacket.getAuthSuccessRequest(c));
                c.getSession().write(LoginPacket.getServerList(0, loginServer.getServerName(), loginServer.getLoad()));
                c.getSession().write(LoginPacket.getEndOfServerList());

            }
           /* c.getSession().write(LoginPacket.getLoginFailed(7));

            LOGGER.debug("登录Z");
            return;*/
        }
    }
}
