package KinMS.db;

import handling.channel.ChannelServer;
import server.maps.MapleMapFactory;


public class AutoCherryMSEventManager
        implements Runnable {

    private static AutoCherryMSEventManager instance = null;
    private ChannelServer cserv;
    private MapleMapFactory mapFactory;

    private AutoCherryMSEventManager() {
    }

    private AutoCherryMSEventManager(ChannelServer cserv, MapleMapFactory mapFactory) {
        this.cserv = cserv;
        this.mapFactory = mapFactory;
    }

    public static AutoCherryMSEventManager newInstance() {
        return new AutoCherryMSEventManager();
    }

    public static AutoCherryMSEventManager newInstance(ChannelServer cserv, MapleMapFactory mapFactory) {
        return AutoCherryMSEventManager.instance = new AutoCherryMSEventManager(cserv, mapFactory);
    }

    public static AutoCherryMSEventManager getInstance() {
        if (instance == null) {
            instance = new AutoCherryMSEventManager();
        }
        return instance;
    }

    public static AutoCherryMSEventManager getInstance(ChannelServer cserv, MapleMapFactory mapFactory) {
        if (instance == null) {
            instance = new AutoCherryMSEventManager(cserv, mapFactory);
        }
        return instance;
    }

    public ChannelServer getChannelServer() {
        return this.cserv;
    }

    public MapleMapFactory getMapleMapFactory() {
        return this.mapFactory;
    }

    public void run() {
        CherryMScustomEventFactory.getInstance().getCherryMSLottery(this.cserv, this.mapFactory).doLottery();
    }
}
