package server.movement;

import com.github.mrzhqiang.maplestory.wz.element.data.Vector;

public interface LifeMovement extends LifeMovementFragment {

    @Override
    Vector getPosition();

    int getNewstate();

    int getDuration();

    int getType();
}
