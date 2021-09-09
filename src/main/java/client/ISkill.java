package client;

import server.MapleStatEffect;
import server.life.Element;

public interface ISkill {

    int getId();

    String getName();

    byte getMaxLevel();

    MapleStatEffect getEffect(int level);

    int getAnimationTime();

    boolean canBeLearnedBy(int job);

    boolean isFourthJob();

    boolean getAction();

    boolean isTimeLimited();

    int getMasterLevel();

    Element getElement();

    boolean isBeginnerSkill();

    boolean hasRequiredSkill();

    boolean isInvisible();

    boolean isChargeSkill();

    int getRequiredSkillLevel();

    int getRequiredSkillId();
}
