package server.movement;

import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import tools.data.output.LittleEndianWriter;

public class TeleportMovement extends AbsoluteLifeMovement {

    public TeleportMovement(int type, Vector position, int duration, int newstate, int newfh) {
        super(type, position, duration, newstate);
    }

    public TeleportMovement(int type, Vector position, int newstate) {
        super(type, position, 0, newstate);
    }

    @Override
    public void serialize(LittleEndianWriter lew) {
        lew.write(getType());
        lew.writeShort(getPosition().x);
        lew.writeShort(getPosition().y);
        lew.writeShort(getPixelsPerSecond().x);
        lew.writeShort(getPixelsPerSecond().y);
        lew.write(getNewstate());
    }
}
