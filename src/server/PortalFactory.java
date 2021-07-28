package server;

import java.awt.Point;

import provider.MapleData;
import provider.MapleDataTool;
import server.maps.MapleGenericPortal;
import server.maps.MapleMapPortal;

public class PortalFactory {

    private int nextDoorPortal = 0x80;

    public MaplePortal makePortal(int type, MapleData portal) {
        MapleGenericPortal ret = null;
        if (type == MaplePortal.MAP_PORTAL) {
            ret = new MapleMapPortal();
        } else {
            ret = new MapleGenericPortal(type);
        }
        loadPortal(ret, portal);
        return ret;
    }

    private void loadPortal(MapleGenericPortal myPortal, MapleData portal) {
        myPortal.setName(MapleDataTool.getString(portal.getChildByPath("pn")));
        myPortal.setTarget(MapleDataTool.getString(portal.getChildByPath("tn")));
        myPortal.setTargetMapId(MapleDataTool.getInt(portal.getChildByPath("tm")));
        myPortal.setPosition(new Point(MapleDataTool.getInt(portal.getChildByPath("x")), MapleDataTool.getInt(portal.getChildByPath("y"))));
        String script = MapleDataTool.getString("script", portal, null);
        if (script != null && script.equals("")) {
            script = null;
        }
        myPortal.setScriptName(script);

        if (myPortal.getType() == MaplePortal.DOOR_PORTAL) {
            myPortal.setId(nextDoorPortal);
            nextDoorPortal++;
        } else {
            myPortal.setId(Integer.parseInt(portal.getName()));
        }
        //portalState
        //if (myPortal.getName().equals("join00") && !myPortal.getPortalState()) { //ola ola, ox quiz, maplefitness
        //	if (myPortal.getTargetMapId() == 109030001 || myPortal.getTargetMapId() == 109030101 || myPortal.getTargetMapId() == 109050000 || myPortal.getTargetMapId() == 109040000) { //ola ola, ox quiz
        //		myPortal.setPortalState(false);
        //	}
        //}
    }
}
