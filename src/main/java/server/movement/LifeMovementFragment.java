package server.movement;

import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import tools.data.output.LittleEndianWriter;

public interface LifeMovementFragment {

    void serialize(LittleEndianWriter lew);

    Vector getPosition();
}
