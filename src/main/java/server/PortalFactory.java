package server;

import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import com.google.common.base.Strings;
import server.maps.MapleGenericPortal;
import server.maps.MapleMapPortal;

public class PortalFactory {

    private int nextDoorPortal = 0x80;

    public MaplePortal makePortal(int type, WzElement<?> portal) {
        MapleGenericPortal ret;
        if (type == MaplePortal.MAP_PORTAL) {
            ret = new MapleMapPortal();
        } else {
            ret = new MapleGenericPortal(type);
        }
        loadPortal(ret, portal);
        return ret;
    }

    private void loadPortal(MapleGenericPortal myPortal, WzElement<?> portal) {
        String name = Elements.findString(portal, "pn");
        myPortal.setName(name);
        String target = Elements.findString(portal, "tn");
        myPortal.setTarget(target);
        int targetMapId = Elements.findInt(portal, "tm");
        myPortal.setTargetMapId(targetMapId);
        int x = Elements.findInt(portal, "x");
        int y = Elements.findInt(portal, "y");
        myPortal.setPosition(Vector.of(x, y));
        String script = Strings.emptyToNull(Elements.findString(portal, "script"));
        myPortal.setScriptName(script);
        if (myPortal.getType() == MaplePortal.DOOR_PORTAL) {
            myPortal.setId(nextDoorPortal);
            nextDoorPortal++;
        } else {
            myPortal.setId(Numbers.ofInt(portal.name()));
        }
    }
}
