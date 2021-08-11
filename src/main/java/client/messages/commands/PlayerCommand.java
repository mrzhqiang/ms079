package client.messages.commands;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import client.inventory.PetDataFactory;
import constants.ServerConstants.PlayerGMRank;
import handling.world.World;
import java.util.Arrays;
import scripting.NPCScriptManager;
import server.life.MapleMonster;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.StringUtil;
import tools.packet.PetPacket;

/**
 *
 * @author Emilyx3
 */
public class PlayerCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.NORMAL;
    }

    public static class 存档 extends save {
    }

    public static class 帮助 extends help {
    }

    public static class 领取点券 extends gainPoint {
    }

    public static class 爆率 extends Mobdrop {
    }

    public static class ea extends 查看 {
    }

    public static class 解卡 extends 查看 {
    }

    public static class 查看 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            //   PredictCardFactory.getInstance().initialize();
            NPCScriptManager.getInstance().dispose(c);
            c.getSession().write(MaplePacketCreator.enableActions());
            c.getPlayer().dropMessage(1, "假死已处理完毕.");
            c.getPlayer().dropMessage(6, "当前时间是" + FileoutputUtil.CurrentReadable_Time() + " GMT+8 | 经验值倍率 " + (Math.round(c.getPlayer().getEXPMod()) * 100) * Math.round(c.getPlayer().getStat().expBuff / 100.0) + "%, 怪物倍率 " + (Math.round(c.getPlayer().getDropMod()) * 100) * Math.round(c.getPlayer().getStat().dropBuff / 100.0) + "%, 金币倍率 " + Math.round(c.getPlayer().getStat().mesoBuff / 100.0) * 100 + "%");
            c.getPlayer().dropMessage(6, "当前延迟 " + c.getPlayer().getClient().getLatency() + " 毫秒");
            //  NPCScriptManager.getInstance().start(c, 9102001);
            if (c.getPlayer().isAdmin()) {
                c.sendPacket(MaplePacketCreator.sendPyramidEnergy("massacre_hit", String.valueOf(50)));

//                  c.sendPacket(MaplePacketCreator.sendPyramidResult((byte) 1, 23));
//                MapleCharacter chr = c.getPlayer();
//                for (MaplePet pet : chr.getPets()) {
//                    if (pet.getSummoned()) {
//                        int newFullness = pet.getFullness() - PetDataFactory.getHunger(pet.getPetItemId());
//                        newFullness = 100;
//                        if (newFullness <= 5) {
//                            pet.setFullness(15);
//                            chr.unequipPet(pet, true);
//                        } else {
//                            pet.setFullness(newFullness);
//                            chr.getClient().getSession().write(PetPacket.updatePet(pet, chr.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), true));
//                        }
//                    }
//                }
//                c.getPlayer().getStat().setDex((short) 4);
//                c.getPlayer().updateSingleStat(MapleStat.DEX, 4);
                //   System.out.println(new Date(System.currentTimeMillis() + (long) (3 * 60 * 60 * 1000)));
                //     c.getPlayer().getGuild().gainGP(50);  
                //     c.getPlayer().saveToDB(false, false);
            }//      
            return 1;
        }
    }

    public static class save extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().saveToDB(false, false);
            c.getPlayer().dropMessage("存档成功");
            return 1;
        }
    }

    public static class gainPoint extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.getSession().write(MaplePacketCreator.enableActions());
            NPCScriptManager npc = NPCScriptManager.getInstance();
            npc.start(c, 9270034);
            return 1;
        }
    }

    public static class Mobdrop extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            NPCScriptManager.getInstance().dispose(c);
            c.getSession().write(MaplePacketCreator.enableActions());
            NPCScriptManager npc = NPCScriptManager.getInstance();
            npc.start(c, 2000);
            return 1;
        }
    }

    public static class Mob extends CommandExecute {

        public int execute(MapleClient c, String[] splitted) {
            MapleMonster mob = null;
            for (MapleMapObject monstermo : c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), 100000.0D, Arrays.asList(new MapleMapObjectType[]{MapleMapObjectType.MONSTER}))) {
                mob = (MapleMonster) monstermo;
                if (mob.isAlive()) {
                    c.getPlayer().dropMessage(6, "怪物: " + mob.toString());
                    break;
                }
            }
            if (mob == null) {
                c.getPlayer().dropMessage(6, "查看失败: 1.没有找到需要查看的怪物信息. 2.你周围没有怪物出现. 3.有些怪物禁止查看.");
            }
            return 1;
        }
    }

    public static class CGM extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted[1] == null) {
                c.getPlayer().dropMessage(6, "请打字谢谢.");
                return 1;
            }
            if (c.getPlayer().isGM()) {
                c.getPlayer().dropMessage(6, "因为你自己是GM无法使用此命令,可以尝试!cngm <讯息> 來建立GM聊天頻道~");
                return 1;
            }
            if (!c.getPlayer().getCheatTracker().GMSpam(100000, 1)) { // 5 minutes.
                World.Broadcast.broadcastGMMessage(MaplePacketCreator.serverNotice(6, "頻道 " + c.getPlayer().getClient().getChannel() + " 玩家 [" + c.getPlayer().getName() + "] : " + StringUtil.joinStringFrom(splitted, 1)).getBytes());
                c.getPlayer().dropMessage(6, "讯息已经发给GM了!");
            } else {
                c.getPlayer().dropMessage(6, "为了防止对GM刷屏所以每1分鐘只能发一次.");
            }
            return 1;
        }
    }

    public static class help extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "指令列表 :");
            c.getPlayer().dropMessage(5, "@解卡/@查看/@ea  <解除异常+查看当前状态>");
            c.getPlayer().dropMessage(5, "@CGM 讯息        <传送讯息給GM>");
            c.getPlayer().dropMessage(5, "@爆率 爆率       <查询当前地图怪物爆率>");
            c.getPlayer().dropMessage(5, "@领取点券        < 充值领取点券 >");
            c.getPlayer().dropMessage(5, "@存档            < 储存当前人物信息 >");
            return 1;
        }
    }
}
