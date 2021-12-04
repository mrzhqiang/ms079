package server;

import client.MapleClient;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;

public interface MaplePortal {

    int MAP_PORTAL = 2;
    int DOOR_PORTAL = 6;

    int getType();

    int getId();

    Vector getPosition();

    String getName();

    String getTarget();

    String getScriptName();

    void setScriptName(String newName);

    int getTargetMapId();

    void enterPortal(MapleClient c);

    void setPortalState(boolean state);

    boolean getPortalState();
}
