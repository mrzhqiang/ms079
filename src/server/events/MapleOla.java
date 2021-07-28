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
import server.Randomizer;
import server.Timer.EventTimer;
import tools.MaplePacketCreator;
import server.maps.MapleMap;
import server.maps.SavedLocationType;

public class MapleOla extends MapleEvent {

    private static final long serialVersionUID = 845748150824L;
    private long time = 600000; //reduce for less time
    private long timeStarted = 0;
    private transient ScheduledFuture<?> olaSchedule;
    private int[] stages = new int[3];
    //stg1 = ch00-ch04 = 5 ports
    //stg2 = ch00-ch07 = 8 ports
    //stg3 = ch00-ch15 = 16 ports

    public MapleOla(final int channel, final int[] mapid) {
        super(channel, mapid);
    }

    @Override
    public void finished(final MapleCharacter chr) {
        givePrize(chr);
        //chr.finishAchievement(21);
    }

    @Override
    public void onMapLoad(MapleCharacter chr) {
        if (isTimerStarted()) {
            chr.getClient().getSession().write(MaplePacketCreator.getClock((int) (getTimeLeft() / 1000)));
        }
    }

    @Override
    public void startEvent() { // TODO: Messages
        unreset();
        super.reset(); //isRunning = true
        broadcast(MaplePacketCreator.getClock((int) (time / 1000)));
        this.timeStarted = System.currentTimeMillis();

        olaSchedule = EventTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < mapid.length; i++) {
                    for (MapleCharacter chr : getMap(i).getCharactersThreadsafe()) {
                        warpBack(chr);
                    }
                    unreset();
                }
            }
        }, this.time);

        broadcast(MaplePacketCreator.serverNotice(0, "门已打开。按箭头↑键进入入口."));
    }

    public boolean isTimerStarted() {
        return timeStarted > 0;
    }

    public long getTime() {
        return time;
    }

    public void resetSchedule() {
        this.timeStarted = 0;
        if (olaSchedule != null) {
            olaSchedule.cancel(false);
        }
        olaSchedule = null;
    }

    @Override
    public void reset() {
        super.reset();
        resetSchedule();
        getMap(0).getPortal("join00").setPortalState(false);
        stages = new int[]{0, 0, 0};
    }

    @Override
    public void unreset() {
        super.unreset();
        resetSchedule();
        getMap(0).getPortal("join00").setPortalState(true);
        stages = new int[]{Randomizer.nextInt(5), Randomizer.nextInt(8), Randomizer.nextInt(15)};
    }

    public long getTimeLeft() {
        return time - (System.currentTimeMillis() - timeStarted);
    }

    public boolean isCharCorrect(String portalName, int mapid) {
        final int st = stages[(mapid % 10) - 1];
        return portalName.equals("ch" + (st < 10 ? "0" : "") + st);
    }
}
