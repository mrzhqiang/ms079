package server.maps;

import client.MapleClient;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;

public interface MapleMapObject {

    int getObjectId();

    void setObjectId(int id);

    MapleMapObjectType getType();

    Vector getPosition();

    void setPosition(Vector position);

    void sendSpawnData(MapleClient client);

    void sendDestroyData(MapleClient client);
}
