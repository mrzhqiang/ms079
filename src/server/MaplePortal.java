package server;

import java.awt.Point;

import client.MapleClient;

public interface MaplePortal {

    public static final int MAP_PORTAL = 2;
    public static final int DOOR_PORTAL = 6;

    int getType();

    int getId();

    Point getPosition();

    String getName();

    String getTarget();

    String getScriptName();

    void setScriptName(String newName);

    int getTargetMapId();

    void enterPortal(MapleClient c);

    void setPortalState(boolean state);

    boolean getPortalState();
}
