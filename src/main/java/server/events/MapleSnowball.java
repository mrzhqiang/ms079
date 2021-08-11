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

import client.MapleCharacter;
import client.MapleDisease;
import java.util.concurrent.ScheduledFuture;
import server.Timer.EventTimer;
import server.life.MobSkillFactory;
import server.maps.MapleMap;
import server.maps.SavedLocationType;
import tools.MaplePacketCreator;

public class MapleSnowball extends MapleEvent {

    private MapleSnowballs[] balls = new MapleSnowballs[2];

    public MapleSnowball(final int channel, final int[] mapid) {
        super(channel, mapid);
    }

    @Override
    public void unreset() {
        super.unreset();
        for (int i = 0; i < 2; i++) {
            getSnowBall(i).resetSchedule();
            resetSnowBall(i);
        }
    }

    @Override
    public void reset() {
        super.reset();
        makeSnowBall(0);
        makeSnowBall(1);
    }

    @Override
    public void startEvent() {
        for (int i = 0; i < 2; i++) {
            MapleSnowballs ball = getSnowBall(i);
            ball.broadcast(getMap(0), 0); //gogogo
            ball.setInvis(false);
            ball.broadcast(getMap(0), 5); //idk xd
            getMap(0).broadcastMessage(MaplePacketCreator.enterSnowBall());
        }
    }

    public void resetSnowBall(int teamz) {
        balls[teamz] = null;
    }

    public void makeSnowBall(int teamz) {
        resetSnowBall(teamz);
        balls[teamz] = new MapleSnowballs(teamz);
    }

    public MapleSnowballs getSnowBall(int teamz) {
        return balls[teamz];
    }

    public static class MapleSnowballs {

        private int position = 0;
        private final int team;
        private int startPoint = 0;
        private boolean invis = true;
        private boolean hittable = true;
        private int snowmanhp = 7500;
        private ScheduledFuture<?> snowmanSchedule = null;

        public MapleSnowballs(int team_) {
            this.team = team_;
        }

        public void resetSchedule() {
            if (snowmanSchedule != null) {
                snowmanSchedule.cancel(false);
                snowmanSchedule = null;
            }
        }

        public int getTeam() {
            return team;
        }

        public int getPosition() {
            return position;
        }

        public void setPositionX(int pos) {
            this.position = pos;
        }

        public void setStartPoint(MapleMap map) {
            this.startPoint++;
            broadcast(map, startPoint);
        }

        public boolean isInvis() {
            return invis;
        }

        public void setInvis(boolean i) {
            this.invis = i;
        }

        public boolean isHittable() {
            return hittable && !invis;
        }

        public void setHittable(boolean b) {
            this.hittable = b;
        }

        public int getSnowmanHP() {
            return snowmanhp;
        }

        public void setSnowmanHP(int shp) {
            this.snowmanhp = shp;
        }

        public void broadcast(MapleMap map, int message) {
            for (MapleCharacter chr : map.getCharactersThreadsafe()) {
                if ((team == 0 && chr.getPosition().y > -80) || (team == 1 && chr.getPosition().y <= -80)) {
                chr.getClient().getSession().write(MaplePacketCreator.snowballMessage(team, message));
                }
            }
        }

        //0 ballpos = 329,469
        //900 = 3042,3172
        public int getLeftX() {
            return position * 3 + 175;
        }

        public int getRightX() {
            return getLeftX() + 275; //exact pos where you cant hit it, as it should knockback
        }

        public static void hitSnowball(final MapleCharacter chr) {
            /*                             	TEAM
             0 - bottom snowball
             1 - top snowball
             2 - bottom snowman
             3 - top snowman

             MESSAGE
             0 - start
             1 - past stage 1
             2 - past stage 2
             3 - past stage 3
             4 - unhittable
             5 - rehittable

             ROLL
             0 - start/normal
             1 - roll
             2 - bottom invis
             3 - top invis
             4 - move
             */

            int team = chr.getPosition().y > -80 ? 0 : 1;
            final MapleSnowball sb = ((MapleSnowball) chr.getClient().getChannelServer().getEvent(MapleEventType.雪球赛));
            final MapleSnowballs ball = sb.getSnowBall(team);
            if (ball != null && !ball.isInvis()) {
                boolean snowman = chr.getPosition().x < -360 && chr.getPosition().x > -560;
                if (!snowman) {
                    int damage = (Math.random() < 0.01 || (chr.getPosition().x > ball.getLeftX() && chr.getPosition().x < ball.getRightX())) && ball.isHittable() ? 10 : 0;
                    chr.getMap().broadcastMessage(MaplePacketCreator.hitSnowBall(team, damage, 0, 1));
                    if (damage == 0) {
                        if (Math.random() < 0.2) {
                            chr.getClient().getSession().write(MaplePacketCreator.leftKnockBack());
                            chr.getClient().getSession().write(MaplePacketCreator.enableActions());
                        }
                    } else {
                        ball.setPositionX(ball.getPosition() + 1);
                        //System.out.println("pos: " + chr.getPosition().x + ", ballpos: " + ball.getPosition().x + ", hittable: " + ball.isHittable() + ", startPoints: " + startPoints[0] + "," + startPoints[1] + ", damage: " + damage + ", snowmens: " + snowmens[0] + "," + snowmens[1] + ", extraDistances: " + extraDistances[0] + "," + extraDistances[1] + ", HP: " + ball.getHP());
                        if (ball.getPosition() == 255 || ball.getPosition() == 511 || ball.getPosition() == 767) { // Going to stage
                            ball.setStartPoint(chr.getMap());
                            chr.getMap().broadcastMessage(MaplePacketCreator.rollSnowball(4, sb.getSnowBall(0), sb.getSnowBall(1)));
                        } else if (ball.getPosition() == 899) { // Crossing the finishing line
                            final MapleMap map = chr.getMap();
                            for (int i = 0; i < 2; i++) {
                                sb.getSnowBall(i).setInvis(true);
                                map.broadcastMessage(MaplePacketCreator.rollSnowball(i + 2, sb.getSnowBall(0), sb.getSnowBall(1))); //inviseble
                            }
                            chr.getMap().broadcastMessage(MaplePacketCreator.serverNotice(6, "[恭喜] " + (team == 0 ? "蓝队" : "红队") + " 赢得胜利!"));

                            for (MapleCharacter chrz : chr.getMap().getCharactersThreadsafe()) {
                                if ((team == 0 && chrz.getPosition().y > -80) || (team == 1 && chrz.getPosition().y <= -80)) { //winner
                                    sb.givePrize(chrz);
                                }
                                sb.warpBack(chrz);
                            }
                            sb.unreset();
                        } else if (ball.getPosition() < 899) {
                            chr.getMap().broadcastMessage(MaplePacketCreator.rollSnowball(4, sb.getSnowBall(0), sb.getSnowBall(1)));
                            ball.setInvis(false);
                        }
                    }
                } else if (ball.getPosition() < 899) {
                    int damage = 15;
                    if (Math.random() < 0.3) {
                        damage = 0;
                    }
                    if (Math.random() < 0.05) {
                        damage = 45;
                    }
                    chr.getMap().broadcastMessage(MaplePacketCreator.hitSnowBall(team + 2, damage, 0, 0)); // Hitting the snowman
                    ball.setSnowmanHP(ball.getSnowmanHP() - damage);
                    if (damage > 0) {
                        chr.getMap().broadcastMessage(MaplePacketCreator.rollSnowball(0, sb.getSnowBall(0), sb.getSnowBall(1))); //not sure
                        if (ball.getSnowmanHP() <= 0) {
                            ball.setSnowmanHP(7500);
                            final MapleSnowballs oBall = sb.getSnowBall(team == 0 ? 1 : 0);
                            oBall.setHittable(false);
                            final MapleMap map = chr.getMap();
                            oBall.broadcast(map, 4);
                            oBall.snowmanSchedule = EventTimer.getInstance().schedule(new Runnable() {

                                @Override
                                public void run() {
                                    oBall.setHittable(true);
                                    oBall.broadcast(map, 5);
                                }
                            }, 10000);
                            for (MapleCharacter chrz : chr.getMap().getCharactersThreadsafe()) {
                                if ((ball.getTeam() == 0 && chr.getPosition().y < -80) || (ball.getTeam() == 1 && chr.getPosition().y > -80)) {
                                    chrz.giveDebuff(MapleDisease.SEDUCE, MobSkillFactory.getMobSkill(128, 1)); //go left
                                }
                            }
                        }
                    }
                }
            }
        }
        /*
         if (mapData.getChildByPath("snowBall") != null) {
         <imgdir name="snowBall">
         <imgdir name="0">
         <int name="y" value="155"/>
         <string name="portal" value="st00"/>
         <string name="snowBall" value="Map/Obj/event.img/snowyRock/snowball/0"/>
         <string name="snowMan" value="Map/Obj/event.img/snowyRock/snowball/1"/>
         </imgdir>
         <imgdir name="1">
         <int name="y" value="-84"/>
         <string name="portal" value="st01"/>
         <string name="snowBall" value="Map/Obj/event.img/snowyRock/snowball/0"/>
         <string name="snowMan" value="Map/Obj/event.img/snowyRock/snowball/1"/>
         </imgdir>
         <int name="x" value="-440"/>
         <int name="x0" value="400"/>
         <int name="xMin" value="-60"/>
         <int name="xMax" value="900"/>
         <int name="dx" value="3"/>
         <string name="damageSnowBall" value="10"/>
         <int name="damageSnowMan0" value="15"/>
         <int name="damageSnowMan1" value="45"/>
         <int name="recoveryAmount" value="400"/>
         <int name="snowManHP" value="7500"/>
         <int name="snowManWait" value="10000"/>
         <int name="speed" value="150"/>
         <int name="section1" value="45"/>
         <int name="section2" value="290"/>
         <int name="section3" value="560"/>
         </imgdir>
         }
         */
    }
}
