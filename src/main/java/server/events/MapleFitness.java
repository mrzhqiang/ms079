/*
 This file is part of the ZeroFusion MapleStory Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 ZeroFusion organized by "RMZero213" <RMZero213@hotmail.com>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package server.events;

import java.util.concurrent.ScheduledFuture;
import client.MapleCharacter;
import server.Timer.EventTimer;
import server.maps.MapleMap;
import server.maps.SavedLocationType;
import tools.MaplePacketCreator;

public class MapleFitness extends MapleEvent {

    private static final long serialVersionUID = 845748950824L;
    private long time = 600000; //change
    private long timeStarted = 0;
    private ScheduledFuture<?> fitnessSchedule, msgSchedule;

    public MapleFitness(final int channel, final int[] mapid) {
        super(channel, mapid);
    }

    @Override
    public void finished(final MapleCharacter chr) {
        givePrize(chr);
        //chr.finishAchievement(20);
    }

    @Override
    public void onMapLoad(MapleCharacter chr) {
        if (isTimerStarted()) {
            chr.getClient().getSession().write(MaplePacketCreator.getClock((int) (getTimeLeft() / 1000)));
        }
    }

    @Override
    public void startEvent() {
        unreset();
        super.reset(); //isRunning = true
        broadcast(MaplePacketCreator.getClock((int) (time / 1000)));
        this.timeStarted = System.currentTimeMillis();
        checkAndMessage();

        fitnessSchedule = EventTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < mapid.length; i++) {
                    for (MapleCharacter chr : getMap(i).getCharactersThreadsafe()) {
                        warpBack(chr);
                    }
                }
                unreset();
            }
        }, this.time);

        broadcast(MaplePacketCreator.serverNotice(0, "门已打开。记得在光柱门↑键进入。"));
    }

    public boolean isTimerStarted() {
        return timeStarted > 0;
    }

    public long getTime() {
        return time;
    }

    public void resetSchedule() {
        this.timeStarted = 0;
        if (fitnessSchedule != null) {
            fitnessSchedule.cancel(false);
        }
        fitnessSchedule = null;
        if (msgSchedule != null) {
            msgSchedule.cancel(false);
        }
        msgSchedule = null;
    }

    @Override
    public void reset() {
        super.reset();
        resetSchedule();
        getMap(0).getPortal("join00").setPortalState(false);
    }

    @Override
    public void unreset() {
        super.unreset();
        resetSchedule();
        getMap(0).getPortal("join00").setPortalState(true);
    }

    public long getTimeLeft() {
        return time - (System.currentTimeMillis() - timeStarted);
    }

   public void checkAndMessage() {
        msgSchedule = EventTimer.getInstance().register(new Runnable() {

            @Override
            public void run() {
                final long timeLeft = getTimeLeft();
                if (timeLeft > 9000 && timeLeft < 11000) {
                    broadcast(MaplePacketCreator.serverNotice(0, "对于你们无法战胜的比赛，我们希望您下一次战胜它！下次再见~"));
                } else if (timeLeft > 11000 && timeLeft < 101000) {
                    broadcast(MaplePacketCreator.serverNotice(0, "好吧，你剩下的时间没有多少了。请抓紧时间!"));
                } else if (timeLeft > 101000 && timeLeft < 241000) {
                    broadcast(MaplePacketCreator.serverNotice(0, "第4阶段，是最后一项 [冒险岛体能测试]. 请不要在最后一刻放弃，拼尽全力. 丰厚的奖励在最高层等着你哦!"));
                } else if (timeLeft > 241000 && timeLeft < 301000) {
                    broadcast(MaplePacketCreator.serverNotice(0, "第三阶段，有很多陷阱，你可能会看到他们，但你不能踩他们.按照你的方式进行下去吧."));
                } else if (timeLeft > 301000 && timeLeft < 361000) {
                    broadcast(MaplePacketCreator.serverNotice(0, "请务必慢慢地移动，小心掉到下面去。"));
                } else if (timeLeft > 361000 && timeLeft < 501000) {
                    broadcast(MaplePacketCreator.serverNotice(0, "请记住，如果你在活动期间死掉了，你会从游戏中淘汰. 如果你想补充HP，使用药水或移动之前先恢复HP。"));
                } else if (timeLeft > 501000 && timeLeft < 601000) {
                    broadcast(MaplePacketCreator.serverNotice(0, "最重要的是你需要知道，不要被猴子扔的香蕉打中，请在规定的时间完成一切！"));
                } else if (timeLeft > 601000 && timeLeft < 661000) {
                    broadcast(MaplePacketCreator.serverNotice(0, "第二阶段障碍是猴子扔香蕉. 请确保沿着正确的路线移动，躲避它们的攻击。"));
                } else if (timeLeft > 661000 && timeLeft < 701000) {
                    broadcast(MaplePacketCreator.serverNotice(0, "请记住，如果你在活动期间死掉了，你会从游戏中淘汰. 如果你想补充HP，使用药水或移动之前先恢复HP。"));
                } else if (timeLeft > 701000 && timeLeft < 781000) {
                    broadcast(MaplePacketCreator.serverNotice(0, "每个人都该参加 [冒险岛体能测试] 参加这个项目，无论完成的顺序，都会获得奖励，所以只是娱乐，慢慢来，清除了4个阶段。"));
                } else if (timeLeft > 781000 && timeLeft < 841000) {
                    broadcast(MaplePacketCreator.serverNotice(0, "不要畏惧苦难，继续下去，享受游戏，重在娱乐嘛."));
                } else if (timeLeft > 841000) {
                    broadcast(MaplePacketCreator.serverNotice(0, "[冒险岛体能测试]有4个阶段，如果你碰巧在游戏过程中死亡，你会从比赛被淘汰，所以请注意这一点。"));
                }
            }
        }, 90000);
    }
}
