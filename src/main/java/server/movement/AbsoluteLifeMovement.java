package server.movement;

import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import tools.data.output.LittleEndianWriter;

public class AbsoluteLifeMovement extends AbstractLifeMovement {

    private Vector pixelsPerSecond, offset;
    private int unk;

    public AbsoluteLifeMovement(int type, Vector position, int duration, int newstate) {
        super(type, position, duration, newstate);
    }

    public Vector getPixelsPerSecond() {
        return pixelsPerSecond;
    }

    public void setPixelsPerSecond(Vector wobble) {
        this.pixelsPerSecond = wobble;
    }

    public Vector getOffset() {
        return offset;
    }

    public void setOffset(Vector wobble) {
        this.offset = wobble;
    }

    public int getUnk() {
        return unk;
    }

    public void setUnk(int unk) {
        this.unk = unk;
    }

    @Override
    public void serialize(LittleEndianWriter lew) {
        lew.write(getType());
        lew.writePos(getPosition());
        lew.writePos(pixelsPerSecond);
        lew.writeShort(unk);
//        lew.writePos(offset);
        lew.write(getNewstate());
        lew.writeShort(getDuration());
    }
}
