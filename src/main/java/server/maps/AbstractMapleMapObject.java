package server.maps;

import com.github.mrzhqiang.maplestory.wz.element.data.Vector;

public abstract class AbstractMapleMapObject implements MapleMapObject {

    private Vector position = Vector.empty();
    private int objectId;

    public abstract MapleMapObjectType getType();

    public Vector getTruePosition() {
        return this.position;
    }

    @Override
    public Vector getPosition() {
        return Vector.of(position);
    }

    @Override
    public void setPosition(Vector position) {
        this.position = Vector.of(position);
    }

    @Override
    public int getObjectId() {
        return objectId;
    }

    @Override
    public void setObjectId(int id) {
        this.objectId = id;
    }
}
