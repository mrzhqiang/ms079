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
package handling.world.family;

import client.MapleBuffStat;
import client.MapleCharacter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import server.MapleItemInformationProvider;
import server.MapleStatEffect;
import server.MapleStatEffect.CancelEffectAction;
import server.Timer.BuffTimer;
import tools.MaplePacketCreator;
import tools.Pair;

public class MapleFamilyBuff {

    //todo; read from somewhere
    private static final int event = 2; //numevents
    // 0=tele, 1=summ, 2=drop, 3=exp, 4=both
    // questrecords used for time: 190000 to 190010
    private static final int[] type = {0, 1, 2, 3, 4, 2, 3, 2, 3, 2, 3};
    private static final int[] duration = {0, 0, 15, 15, 30, 15, 15, 30, 30, 30, 30};
    private static final int[] effect = {0, 0, 150, 150, 200, 200, 200, 200, 200, 200, 200};
    private static final int[] rep = {3, 5, 7, 8, 10, 12, 15, 20, 25, 40, 50};//{3, 5, 7, 8, 10, 12, 15, 20, 25, 40, 50/*300,500, 700, 800, 1000, 1200, 1500, 2000, 2500, 4000, 5000*/}; //70% of normal in gms O_O
    private static final String[] name = {
        "直接移动到学院成员身边",
        "直接召唤学院成员",
        "我的爆率 1.5倍(15分钟)",
        "我的经验值 1.5倍(15分钟)",
        "学院成员的团结(30分钟)",
        "我的爆率 2倍(15分钟)",
        "我的经验值 2倍(15分钟)",
        "我的爆率 2倍(30分钟)",
        "我的经验值 2倍(30分钟)",
        "我的组队爆率 2倍(30分钟)",
        "我的组队经验值 2倍(30分钟)"};
    //{"在一起豪嗎", "召喚濕是你?!"/*, "My Drop Rate 1.5x (15min)", "My EXP 1.5x (15min)",
    //"Family Bonding (30min)", "My Drop Rate 2x (15min)", "My EXP 2x (15min)", "My Drop Rate 2x (30min)",
    // "My EXP 2x (30min)", "My Party Drop Rate 2x (30min)", "My Party EXP 2x (30min)"*/
    // };
    private static final String[] desc = {
        "[对象] 我\n[效果] 直接可以移动到指定的学院成员身边。",
        "[对象] 学院成员 1名\n[效果] 直接可以召唤指定的学院成员到现在的地图。",
        "[对象] 我\n[持续效果] 15分钟\n[效果] 打怪爆率增加到 #c1.5倍# \n※ 与爆率活动重叠时失效。",
        "[对象] 我\n[持续效果] 15分钟\n[效果] 打怪经验值增加到 #c1.5倍# \n※ 与经验值活动重叠时失效。",
        "[启动条件] 校谱最低层学院成员6名以上在线时\n[持续效果] 30分钟\n[效果] 爆率和经验值增加到 #c2倍# ※ 与爆率、经验值活动重叠时失效。",
        "[对象] 我\n[持续效果] 15分钟\n[效果] 打怪爆率增加到 #c2倍# \n※ 与爆率活动重叠时失效。",
        "[对象] 我\n[持续效果] 15分钟\n[效果] 打怪经验值增加到 #c2倍# \n※ 与经验值活动重叠时失效。",
        "[对象] 我\n[持续效果] 30分钟\n[效果] 打怪爆率增加到 #c2倍# \n※ 与爆率活动重叠时失效。",
        "[对象] 我\n[持续效果] 30分钟\n[效果] 打怪经验值增加到 #c2倍# \n※ 与经验值活动重叠时失效。",
        "[对象] 我所属组队\n[持续效果] 30分钟\n[效果] 打怪爆率增加到 #c2倍# \n※ 与爆率活动重叠时失效。",
        "[对象] 我所属组队\n[持续效果] 30分钟\n[效果] 打怪经验值增加到 #c2倍# \n※ 与经验值活动重叠时失效。"};
    /*
     * {"[目地] 自己\n[效果] 傳送你家的人到你旁邊.", "[條件] 1 個家人\n[效果] 傳送一位家人到你旁邊.", "[Target]
     * Me\n[Time] 15 min.\n[Effect] Monster drop rate will be increased
     * #c1.5x#.\n* If the Drop Rate event is in progress, this will be
     * nullified.", "[Target] Me\n[Time] 15 min.\n[Effect] EXP earned from
     * hunting will be increased #c1.5x#.\n* If the EXP event is in progress,
     * this will be nullified.", "[Target] At least 6 Family members online that
     * are below me in the Pedigree\n[Time] 30 min.\n[Effect] Monster drop rate
     * and EXP earned will be increased #c2x#. \n* If the EXP event is in
     * progress, this will be nullified.", "[Target] Me\n[Time] 15
     * min.\n[Effect] Monster drop rate will be increased #c2x#.\n* If the Drop
     * Rate event is in progress, this will be nullified.", "[Target] Me\n[Time]
     * 15 min.\n[Effect] EXP earned from hunting will be increased #c2x#.\n* If
     * the EXP event is in progress, this will be nullified.", "[Target]
     * Me\n[Time] 30 min.\n[Effect] Monster drop rate will be increased
     * #c2x#.\n* If the Drop Rate event is in progress, this will be
     * nullified.", "[Target] Me\n[Time] 30 min.\n[Effect] EXP earned from
     * hunting will be increased #c2x#.\n* If the EXP event is in progress, this
     * will be nullified.", "[Target] My party\n[Time] 30 min.\n[Effect] Monster
     * drop rate will be increased #c2x#.\n* If the Drop Rate event is in
     * progress, this will be nullified.", "[Target] My party\n[Time] 30
     * min.\n[Effect] EXP earned from hunting will be increased #c2x#.\n* If the
     * EXP event is in progress, this will be nullified."
     */
    //};
    private final static List<MapleFamilyBuffEntry> buffEntries;

    static {
        buffEntries = new ArrayList<MapleFamilyBuffEntry>();
        for (int i = 0; i < event; i++) { //count = 1, questid = 190000+i
            buffEntries.add(new MapleFamilyBuffEntry(i, name[i], desc[i], 1, rep[i], type[i], 190000 + i, duration[i], effect[i]));
        }
    }

    public static List<MapleFamilyBuffEntry> getBuffEntry() {
        return buffEntries;
    }

    public static MapleFamilyBuffEntry getBuffEntry(int i) {
        return buffEntries.get(i);
    }

    public static class MapleFamilyBuffEntry {

        public String name, desc;
        public int count, rep, type, index, questID, duration, effect;
        public List<Pair<MapleBuffStat, Integer>> effects;

        public MapleFamilyBuffEntry(int index, String name, String desc, int count, int rep, int type, int questID, int duration, int effect) {
            this.name = name;
            this.desc = desc;
            this.count = count;
            this.rep = rep;
            this.type = type;
            this.questID = questID;
            this.index = index;
            this.duration = duration;
            this.effect = effect;
            this.effects = getEffects();
        }

        public int getEffectId() {
            switch (type) {
                case 2: //drop
                    return 2022694;
                case 3: //exp
                    return 2450018;
            }
            return 2022332; //custom
        }

        public final List<Pair<MapleBuffStat, Integer>> getEffects() {
            //custom
            List<Pair<MapleBuffStat, Integer>> ret = new ArrayList<Pair<MapleBuffStat, Integer>>();
            switch (type) {
                case 2: //drop
                    ret.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DROP_RATE, effect));
                    ret.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MESO_RATE, effect));
                    break;
                case 3: //exp
                    ret.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.EXPRATE, effect));
                    break;
                case 4: //both
                    ret.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.EXPRATE, effect));
                    ret.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DROP_RATE, effect));
                    ret.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MESO_RATE, effect));
                    break;
            }
            return ret;
        }

        public void applyTo(MapleCharacter chr) {
            chr.getClient().getSession().write(MaplePacketCreator.giveBuff(-getEffectId(), duration * 60000, effects, null));
            final MapleStatEffect eff = MapleItemInformationProvider.getInstance().getItemEffect(getEffectId());
            chr.cancelEffect(eff, true, -1, effects);
            final long starttime = System.currentTimeMillis();
            final CancelEffectAction cancelAction = new CancelEffectAction(chr, eff, starttime);
            final ScheduledFuture<?> schedule = BuffTimer.getInstance().schedule(cancelAction, ((starttime + (duration * 60000)) - starttime));
            chr.registerEffect(eff, starttime, schedule, effects);
        }
    }
}
