package com.github.mrzhqiang.maplestory.auth;

import com.github.mrzhqiang.maplestory.api.RunnableServer;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import handling.MapleServerHandler;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.Triple;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;

@Singleton
public final class AuthenticationServer implements RunnableServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServer.class);

    private final Set<String> loginIpAuth = Sets.newConcurrentHashSet();
    private final Map<Integer, Triple<String, String, Integer>> loginAuth = Maps.newConcurrentMap();

    private Map<Integer, Integer> load = Maps.newConcurrentMap();
    private int usersOn = 0;
    private int userLimit;

    private final NioSocketAcceptor acceptor;
    private final MapleServerHandler serverHandler;
    private final ProtocolCodecFilter codecFilter;
    private final ServerProperties properties;

    private boolean finishedShutdown = true;

    @Inject
    public AuthenticationServer(NioSocketAcceptor acceptor, MapleServerHandler serverHandler,
                                ProtocolCodecFilter codecFilter, ServerProperties properties) {
        this.acceptor = acceptor;
        this.serverHandler = serverHandler;
        this.codecFilter = codecFilter;
        this.properties = properties;
        this.userLimit = properties.getOnlineLimit();
    }

    public void put(int characterId, String ip, String tempIp, int channel) {
        loginAuth.put(characterId, Triple.of(ip, tempIp, channel));
        loginIpAuth.add(ip);
    }

    public boolean containsIpAuth(String ip) {
        return loginIpAuth.contains(ip);
    }

    public void removeIpAuth(String ip) {
        loginIpAuth.remove(ip);
    }

    public void addIpAuth(String ip) {
        loginIpAuth.add(ip);
    }

    public void addChannel(int channel) {
        load.put(channel, 0);
    }

    public void removeChannel(int channel) {
        load.remove(channel);
    }

    public Map<Integer, Integer> getLoad() {
        return load;
    }

    public void setLoad(Map<Integer, Integer> load, int usersOn) {
        this.load = load;
        this.usersOn = usersOn;
    }

    public int getUsersOn() {
        return usersOn;
    }

    public int getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(int userLimit) {
        this.userLimit = userLimit;
    }

    public boolean isShutdown() {
        return finishedShutdown;
    }

    public void setOn() {
        finishedShutdown = false;
    }

    @Override
    public void init() {
        // 注释掉的代码，已经是默认值，不需要初始化
//        IoBuffer.setUseDirectBuffer(false);
//        IoBuffer.setAllocator(new SimpleBufferAllocator());
        acceptor.getFilterChain().addLast("codec", codecFilter);
        acceptor.setHandler(serverHandler);
        //acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
        acceptor.getSessionConfig().setTcpNoDelay(true);
    }

    @Override
    public void run() {
        int port = properties.getLoginPort();
        try {
            acceptor.bind(new InetSocketAddress(port));
            LOGGER.info("登录器服务器绑定端口：" + port);
        } catch (IOException e) {
            LOGGER.error("绑定到端口 " + port + " 失败！", e);
        }
    }

    @Override
    public void close() throws IOException {
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
}
