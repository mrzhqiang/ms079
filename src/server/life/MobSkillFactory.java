/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
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
package server.life;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.Pair;

public class MobSkillFactory {

    private static Map<Pair<Integer, Integer>, MobSkill> mobSkills = new HashMap<Pair<Integer, Integer>, MobSkill>();
    private static MapleDataProvider dataSource = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Skill.wz"));
    private static final MapleData skillRoot = dataSource.getData("MobSkill.img");

    public static MobSkill getMobSkill(int skillId, int level) {
        MobSkill ret = mobSkills.get(new Pair<Integer, Integer>(Integer.valueOf(skillId), Integer.valueOf(level)));
        if (ret != null) {
            return ret;
        }
        if (skillRoot == null || skillRoot.getChildren() == null || skillRoot.getChildByPath(String.valueOf(skillId)) == null || skillRoot.getChildByPath(String.valueOf(skillId)).getChildren() == null || skillRoot.getChildByPath(String.valueOf(skillId)).getChildByPath("level") == null) {
            return null;
        }
        final MapleData skillData = skillRoot.getChildByPath(skillId + "/level/" + level);
        if (skillData != null && skillData.getChildren() != null) {
            List<Integer> toSummon = new ArrayList<Integer>();
            for (int i = 0; i > -1; i++) {
                if (skillData.getChildByPath(String.valueOf(i)) == null) {
                    break;
                }
                toSummon.add(Integer.valueOf(MapleDataTool.getInt(skillData.getChildByPath(String.valueOf(i)), 0)));
            }
            final MapleData ltd = skillData.getChildByPath("lt");
            Point lt = null;
            Point rb = null;
            if (ltd != null) {
                lt = (Point) ltd.getData();
                rb = (Point) skillData.getChildByPath("rb").getData();
            }
            ret = new MobSkill(skillId, level);
            ret.addSummons(toSummon);
            ret.setCoolTime(MapleDataTool.getInt("interval", skillData, 0) * 1000);
            ret.setDuration(MapleDataTool.getInt("time", skillData, 1) * 1000);
            ret.setHp(MapleDataTool.getInt("hp", skillData, 100));
            ret.setMpCon(MapleDataTool.getInt(skillData.getChildByPath("mpCon"), 0));
            ret.setSpawnEffect(MapleDataTool.getInt("summonEffect", skillData, 0));
            ret.setX(MapleDataTool.getInt("x", skillData, 1));
            ret.setY(MapleDataTool.getInt("y", skillData, 1));
            ret.setProp(MapleDataTool.getInt("prop", skillData, 100) / 100f);
            ret.setLimit((short) MapleDataTool.getInt("limit", skillData, 0));
            ret.setLtRb(lt, rb);

            mobSkills.put(new Pair<Integer, Integer>(Integer.valueOf(skillId), Integer.valueOf(level)), ret);
        }
        return ret;
    }
}
