/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

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
package server;

import java.util.ArrayList;

import java.util.List;

import java.util.Map;

import java.util.Map.Entry;

import tools.ArrayMap;

import tools.Pair;

public class MapleAchievements {

    /*private Map<Integer, MapleAchievement> achievements = new ArrayMap<Integer, MapleAchievement>();
     private static MapleAchievements instance = new MapleAchievements();

     protected MapleAchievements() {
     achievements.put(1, new MapleAchievement("got their first point", 1000, false));
     achievements.put(2, new MapleAchievement("reaching level 30", 3000, false));
     achievements.put(3, new MapleAchievement("reaching Level 70", 7000, false));
     achievements.put(4, new MapleAchievement("reaching Level 120", 12000, false));
     achievements.put(5, new MapleAchievement("reaching level 200", 20000, false));
     achievements.put(7, new MapleAchievement("reached 50 fame", 5000, false));
     achievements.put(8, new MapleAchievement("equipped a dragon item", 8000, false));
     achievements.put(9, new MapleAchievement("equipped a reverse item", 9000, false));
     achievements.put(10, new MapleAchievement("equipped a timeless item", 10000, false));
     achievements.put(11, new MapleAchievement("said our server rocks", 1000, false));
     achievements.put(12, new MapleAchievement("killed Anego", 3500, false));
     achievements.put(13, new MapleAchievement("killed Papulatus", 2500, false));
     achievements.put(14, new MapleAchievement("killed a Pianus", 2500, false));
     achievements.put(15, new MapleAchievement("killed the almighty Zakum", 5000, false));
     achievements.put(16, new MapleAchievement("defeated Horntail", 30000, false));
     achievements.put(17, new MapleAchievement("defeated Pink Been", 50000, false));
     achievements.put(18, new MapleAchievement("killed a boss", 1000, false));
     achievements.put(19, new MapleAchievement("won the event 'OX Quiz'", 10000, false));
     achievements.put(20, new MapleAchievement("won the event 'MapleFitness'", 10000, false));
     achievements.put(21, new MapleAchievement("won the event 'Ola Ola'", 10000, false));
     achievements.put(22, new MapleAchievement("defeating BossQuest HELL mode", 100000));
     achievements.put(23, new MapleAchievement("killed the almighty Chaos Zakum", 15000, false));
     achievements.put(24, new MapleAchievement("defeated Chaos Horntail", 40000, false));
     }

     public static MapleAchievements getInstance() {
     return instance;
     }

     public MapleAchievement getById(int id) {
     return achievements.get(id);
     }

     public Integer getByMapleAchievement(MapleAchievement ma) {
     for (Entry<Integer, MapleAchievement> achievement : this.achievements.entrySet()) {
     if (achievement.getValue() == ma) {
     return achievement.getKey();
     }
     }
     return null;
     }*/
}
