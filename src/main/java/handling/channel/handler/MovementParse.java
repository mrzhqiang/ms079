package handling.channel.handler;

import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.maps.AnimatedMapleMapObject;
import server.movement.*;
import tools.data.input.SeekableLittleEndianAccessor;

import java.util.ArrayList;
import java.util.List;

public class MovementParse {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovementParse.class);

    //1 = player, 2 = mob, 3 = pet, 4 = summon, 5 = dragon
    public static final List<LifeMovementFragment> parseMovement(final SeekableLittleEndianAccessor lea, int kind) {
        final List<LifeMovementFragment> res = new ArrayList<LifeMovementFragment>();
        final byte numCommands = lea.readByte();//循环次数
        String 类型 = "";
        switch (kind) {
            case 1:
                类型 = "角色移动";
                break;
            case 2:
                类型 = "怪物移动";
                break;
            case 3:
                类型 = "宠物移动";
                break;
            case 4:
                类型 = "召唤兽移动";
                break;
            case 5:
                类型 = "龙移动";
                break;
            case 6:
                类型 = "玩家攻击怪物移动";
                break;
            default:
                break;
        }
        for (byte i = 0; i < numCommands; i++) {
            final byte command = lea.readByte();
            switch (command) {//移动类型
                case -1: {//下跳
                    final short xpos = lea.readShort();
                    final short ypos = lea.readShort();
                    final short unk = lea.readShort();
                    final short fh = lea.readShort();
                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final BounceMovement bm = new BounceMovement(command, Vector.of(xpos, ypos), duration, newstate);
                    bm.setFH(fh);
                    bm.setUnk(unk);
                    res.add(bm);
                    break;
                }
                case 0: // normal move
                case 5:
                case 17: // Float
                {
                    final short xpos = lea.readShort();
                    final short ypos = lea.readShort();
                    final short xwobble = lea.readShort();
                    final short ywobble = lea.readShort();
                    final short unk = lea.readShort();
                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final AbsoluteLifeMovement alm = new AbsoluteLifeMovement(command, Vector.of(xpos, ypos), duration, newstate);
                    alm.setUnk(unk);
                    alm.setPixelsPerSecond(Vector.of(xwobble, ywobble));
                    // log.trace("Move to {},{} command {} wobble {},{} ? {} state {} duration {}", new Object[] { xpos,
                    // xpos, command, xwobble, ywobble, newstate, duration });
                    res.add(alm);
                    break;
                }
                case 1:
                case 2:
                case 6: // fj
                case 12:
                case 13: // Shot-jump-back thing
                case 16: { // Float
                    final short xmod = lea.readShort();
                    final short ymod = lea.readShort();
                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final RelativeLifeMovement rlm = new RelativeLifeMovement(command, Vector.of(xmod, ymod), duration, newstate);
                    res.add(rlm);
                    // log.trace("Relative move {},{} state {}, duration {}", new Object[] { xmod, ymod, newstate,
                    // duration });
                    break;
                }
                case 3:
                case 4: // tele... -.-
                case 7: // assaulter
                case 8: // assassinate
                case 9: // rush
                case 14: {
                    final short xpos = lea.readShort();
                    final short ypos = lea.readShort();
                    final short xwobble = lea.readShort();
                    final short ywobble = lea.readShort();
                    final byte newstate = lea.readByte();
                    final TeleportMovement tm = new TeleportMovement(command, Vector.of(xpos, ypos), newstate);
                    tm.setPixelsPerSecond(Vector.of(xwobble, ywobble));
                    res.add(tm);
                    break;
                }
                case 10: // change equip ???
                    res.add(new ChangeEquipSpecialAwesome(command, lea.readByte()));
                    break;
                case 11: // chair
                {
                    final short xpos = lea.readShort();
                    final short ypos = lea.readShort();
                    final short unk = lea.readShort();
                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final ChairMovement cm = new ChairMovement(command, Vector.of(xpos, ypos), duration, newstate);
                    cm.setUnk(unk);
                    res.add(cm);
                    break;
                }
                /*case 14: {
                    final short xpos = lea.readShort();
                    final short ypos = lea.readShort();
                    final short xwobble = lea.readShort();
                    final short ywobble = lea.readShort();
                    final short unk = lea.readShort();
                    final short fh = lea.readShort();
                    final short xoffset = lea.readShort();
                    final short yoffset = lea.readShort();
                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    JumpDownMovement jdm = new JumpDownMovement(command, new Point(xpos, ypos), duration, newstate);
                    jdm.setUnk(unk);
                    jdm.setPixelsPerSecond(new Point(xwobble, ywobble));
                    jdm.setOffset(new Point(xoffset, yoffset));
                    jdm.setFH(fh);
                    res.add(jdm);
                    break;
                }*/
                case 15: { // Jump Down
                    final short xpos = lea.readShort();
                    final short ypos = lea.readShort();
                    final short xwobble = lea.readShort();
                    final short ywobble = lea.readShort();
                    final short unk = lea.readShort();
                    final short fh = lea.readShort();
                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final JumpDownMovement jdm = new JumpDownMovement(command, Vector.of(xpos, ypos), duration, newstate);
                    jdm.setUnk(unk);
                    jdm.setPixelsPerSecond(Vector.of(xwobble, ywobble));
                    jdm.setFH(fh);

                    res.add(jdm);
                    break;
                }
                case 20:
                case 21:
                case 22: {
                    final short unk = lea.readShort();
                    final byte newstate = lea.readByte();
                    final AranMovement acm = new AranMovement(command, Vector.of(10, 0), newstate, unk);
                    res.add(acm);
                }
                case 18:
                case 19:
                default:
                    LOGGER.debug("移动类型: " + kind + ", 剩下的 : " + (numCommands - res.size()) + " 新的移动类型 ID : " + command + ", 封包 : " + lea.toString(true));
                    return null;
            }
        }
        if (numCommands != res.size()) {
            LOGGER.debug("error in movement");
            return null; // Probably hack
        }
        return res;
    }

    public static final void updatePosition(final List<LifeMovementFragment> movement, final AnimatedMapleMapObject target, final int yoffset) {
        for (final LifeMovementFragment move : movement) {
            if (move instanceof LifeMovement) {
                if (move instanceof AbsoluteLifeMovement) {
                    Vector position = move.getPosition().plusY(yoffset);
                    target.setPosition(position);
                }
                target.setStance(((LifeMovement) move).getNewstate());
            }
        }
    }
}
