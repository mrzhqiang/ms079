package server.movement;

import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import tools.data.output.LittleEndianWriter;

public class AranMovement extends AbstractLifeMovement {

    public AranMovement(int type, Vector position, int duration, int newstate) {
        super(type, position, duration, newstate);
    }

    @Override
    public void serialize(LittleEndianWriter lew) {
        lew.write(getType());
        //lew.writePos(getPosition());
        lew.write(getNewstate());
        lew.writeShort(getDuration());
    }
}
