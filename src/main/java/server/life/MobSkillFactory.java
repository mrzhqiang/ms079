package server.life;

import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.google.common.collect.Lists;
import tools.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobSkillFactory {

    private static final Map<Pair<Integer, Integer>, MobSkill> mobSkills = new HashMap<>();

    public static MobSkill getMobSkill(int skillId, int level) {
        MobSkill ret = mobSkills.get(new Pair<>(skillId, level));
        if (ret != null) {
            return ret;
        }

        MobSkill skill = new MobSkill(skillId, level);
        WzData.SKILL.directory().findFile("MobSkill.img")
                .map(WzFile::content)
                .flatMap(element -> element.findByName(skillId + "/level/" + level))
                .ifPresent(element -> {
                    WzElement<?> wzElement = (WzElement<?>) element;
                    int count = (int) wzElement.childrenStream().count();
                    List<Integer> toSummon = Lists.newArrayListWithCapacity(count);
                    for (int i = 0; i < count; i++) {
                        toSummon.add(Elements.findInt(wzElement, String.valueOf(i)));
                    }
                    skill.addSummons(toSummon);
                    int interval = Elements.findInt(wzElement, "interval") * 1000;
                    skill.setCoolTime(interval);
                    int time = Elements.findInt(wzElement, "time", 1) * 1000;
                    skill.setDuration(time);
                    int hp = Elements.findInt(wzElement, "hp", 100);
                    skill.setHp(hp);
                    int mpCon = Elements.findInt(wzElement, "mpCon");
                    skill.setMpCon(mpCon);
                    int summonEffect = Elements.findInt(wzElement, "summonEffect");
                    skill.setSpawnEffect(summonEffect);
                    int x = Elements.findInt(wzElement, "x", 1);
                    skill.setX(x);
                    int y = Elements.findInt(wzElement, "y", 1);
                    skill.setY(y);
                    float prop = Elements.findInt(wzElement, "prop", 100) / 100f;
                    skill.setProp(prop);
                    short limit = Elements.findInt(wzElement, "limit").shortValue();
                    skill.setLimit(limit);
                    skill.setLt(Elements.findVector(wzElement, "lt"));
                    skill.setRb(Elements.findVector(wzElement, "rb"));
                    mobSkills.put(new Pair<>(skillId, level), skill);
                });
        return skill;
    }
}
