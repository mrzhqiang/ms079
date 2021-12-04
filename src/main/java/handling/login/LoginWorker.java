package handling.login;

import client.MapleClient;
import com.github.mrzhqiang.maplestory.domain.Gender;
import com.github.mrzhqiang.maplestory.timer.Timer;
import handling.channel.ChannelServer;
import tools.MaplePacketCreator;
import tools.packet.LoginPacket;

import java.util.Map;
import java.util.Map.Entry;

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
            if (client.getGender() == Gender.UNKNOWN) {
                client.getSession().write(LoginPacket.getGenderNeeded(client));
            } else {
                client.getSession().write(LoginPacket.getAuthSuccessRequest(client));
                client.getSession().write(LoginPacket.getServerList(0, loginServer.getServerName(), loginServer.getLoad()));
                client.getSession().write(LoginPacket.getEndOfServerList());

            }
            client.setIdleTask(Timer.PING.schedule(() -> {
//                    client.getSession().close();
            }, 10 * 60 * 10000));
        } else {
            if (client.getGender() == Gender.UNKNOWN) {
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
