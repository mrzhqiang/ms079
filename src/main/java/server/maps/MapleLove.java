package server.maps;

import client.MapleCharacter;
import client.MapleClient;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import handling.MaplePacket;
import tools.MaplePacketCreator;


public class MapleLove extends AbstractMapleMapObject {

    private Vector pos;
    private MapleCharacter owner;
    private String text;
    private int ft;
    private int itemid;

    public MapleLove(MapleCharacter owner, Vector pos, int ft, String text, int itemid) {
        this.owner = owner;
        this.pos = pos;
        this.text = text;
        this.ft = ft;
        this.itemid = itemid;
    }

    public MapleMapObjectType getType() {
        return MapleMapObjectType.LOVE;
    }

    public Vector getPosition() {
        return Vector.of(pos);
    }

    public MapleCharacter getOwner() {
        return this.owner;
    }

    public void setPosition(Vector position) {
        throw new UnsupportedOperationException();
    }

    public void sendDestroyData(MapleClient client) {
        client.getSession().write(makeDestroyData());
    }

    public void sendSpawnData(MapleClient client) {
        client.getSession().write(makeSpawnData());
    }

    public MaplePacket makeSpawnData() {
        return MaplePacketCreator.spawnLove(getObjectId(), this.itemid, this.owner.getName(), this.text, this.pos, this.ft);
    }

    public MaplePacket makeDestroyData() {
        return MaplePacketCreator.removeLove(getObjectId());
    }
}
