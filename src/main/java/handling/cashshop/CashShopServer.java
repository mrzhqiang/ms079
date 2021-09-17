package handling.cashshop;

import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import handling.MapleServerHandler;
import handling.channel.PlayerStorage;
import handling.mina.MapleCodecFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

@Singleton
public class CashShopServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CashShopServer.class);

    private static String IP;

    private final ServerProperties properties;
    private final MapleCodecFactory factory;
    private final MapleServerHandler serverHandler;

    private static PlayerStorage players, playersMTS;
    private static boolean finishedShutdown = false;

    @Inject
    public CashShopServer(ServerProperties properties, MapleCodecFactory factory) {
        this.properties = properties;
        this.factory = factory;
        this.serverHandler = new MapleServerHandler();
        this.serverHandler.setCs(true);
    }

    public void start() {
        String address = properties.getAddress();
        int port = properties.getMallPort();
        IP = address + ":" + port;

        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());
        // todo refactor as netty
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(factory));

        ((SocketSessionConfig) acceptor.getSessionConfig()).setTcpNoDelay(true);
        players = new PlayerStorage(-10);
        playersMTS = new PlayerStorage(-20);

        try {
            acceptor.setHandler(serverHandler);
            acceptor.bind(new InetSocketAddress(port));
            LOGGER.info("商城服务器绑定端口: {}", port);
        } catch (Exception e) {
            LOGGER.error("Binding to port " + port + " failed", e);
            throw new RuntimeException("Binding failed.", e);
        }
    }

    public static String getIP() {
        return IP;
    }

    public static PlayerStorage getPlayerStorage() {
        return players;
    }

    public static PlayerStorage getPlayerStorageMTS() {
        return playersMTS;
    }

    public static void shutdown() {
        if (finishedShutdown) {
            return;
        }
        LOGGER.info("正在断开商城内玩家...");
        players.disconnectAll();
        //playersMTS.disconnectAll();
        // MTSStorage.getInstance().saveBuyNow(true);
        LOGGER.info("正在关闭商城伺服器...");
        //acceptor.unbindAll();
        finishedShutdown = true;
    }

    public static boolean isShutdown() {
        return finishedShutdown;
    }
}
