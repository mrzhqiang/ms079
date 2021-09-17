package handling.login;

import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import handling.MapleServerHandler;
import handling.mina.MapleCodecFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.Triple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class LoginServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServer.class);

    private static final Set<String> LOGIN_IP_AUTH = new ConcurrentHashSet<>();
    private static final Map<Integer, Triple<String, String, Integer>> LOGIN_AUTH = new ConcurrentHashMap<>();

    private Map<Integer, Integer> load = new HashMap<>();

    private final int port;
    private final int maxCharacters;
    private final String serverName;
    private final boolean adminOnly;

    private final MapleCodecFactory factory;
    private final MapleServerHandler serverHandler;

    private static int flag;
    private static int userLimit;
    private static String eventMessage;
    private boolean finishedShutdown = true;
    private int usersOn = 0;
    private IoAcceptor acceptor;

    @Inject
    public LoginServer(ServerProperties properties, MapleCodecFactory factory) {
        LoginServer.flag = properties.getFlag();
        this.port = properties.getLoginPort();
        LoginServer.userLimit = properties.getOnlineLimit();
        this.maxCharacters = properties.getCharactersLimit();
        this.serverName = properties.getName();
        LoginServer.eventMessage = properties.getLoginEventMessage();
        this.adminOnly = properties.isAdminLogin();
        this.factory = factory;
        this.serverHandler = new MapleServerHandler();
        // fixme 循环依赖
        this.serverHandler.setLoginServer(this);
    }

    public static void putLoginAuth(int chrid, String ip, String tempIp, int channel) {
        LOGIN_AUTH.put(chrid, new Triple<>(ip, tempIp, channel));
        LOGIN_IP_AUTH.add(ip);
    }

    public static Triple<String, String, Integer> getLoginAuth(int chrid) {
        return LOGIN_AUTH.remove(chrid);
    }

    public static boolean containsIPAuth(String ip) {
        return LOGIN_IP_AUTH.contains(ip);
    }

    public static void removeIPAuth(String ip) {
        LOGIN_IP_AUTH.remove(ip);
    }

    public static void addIPAuth(String ip) {
        LOGIN_IP_AUTH.add(ip);
    }

    public void addChannel(final int channel) {
        load.put(channel, 0);
    }

    public void removeChannel(final int channel) {
        load.remove(channel);
    }

    public void run_startup_configurations() {
        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(factory));
        acceptor.setHandler(serverHandler);
        //acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
        ((SocketSessionConfig) acceptor.getSessionConfig()).setTcpNoDelay(true);

        try {
            acceptor.bind(new InetSocketAddress(port));
            LOGGER.info("登录器服务器绑定端口：" + port);
        } catch (IOException e) {
            LOGGER.error("绑定到端口 " + port + " 失败！", e);
        }
    }

    public void shutdown() {
        if (finishedShutdown) {
            return;
        }
        LOGGER.info("正在关闭登录服务器...");
        //       acceptor.setCloseOnDeactivation(true);
//        for (IoSession ss : acceptor.getManagedSessions().values()) {
//            ss.close(true);
//        }
        //acceptor.unbind();
        finishedShutdown = true; //nothing. lol
    }

    public String getServerName() {
        return serverName;
    }

    public static String getEventMessage() {
        return eventMessage;
    }

    public static int getFlag() {
        return flag;
    }

    public int getMaxCharacters() {
        return maxCharacters;
    }

    public Map<Integer, Integer> getLoad() {
        return load;
    }

    public void setLoad(final Map<Integer, Integer> load_, final int usersOn_) {
        load = load_;
        usersOn = usersOn_;
    }

    public void setEventMessage(final String newMessage) {
        eventMessage = newMessage;
    }

    public void setFlag(int newflag) {
        flag = newflag;
    }

    public static int getUserLimit() {
        return userLimit;
        //  return Integer.parseInt(ServerProperties.getProperty("randall.UserLimit"));
    }

    public int getUsersOn() {
        return usersOn;
    }

    public static void setUserLimit(final int newLimit) {
        userLimit = newLimit;
    }

    public int getNumberOfSessions() {
        return acceptor.getManagedSessions().size();
    }

    public boolean isAdminOnly() {
        return adminOnly;
    }

    public boolean isShutdown() {
        return finishedShutdown;
    }

    public void setOn() {
        finishedShutdown = false;
    }
}
