package client;

import com.github.mrzhqiang.maplestory.util.Jobs;
import com.github.mrzhqiang.maplestory.util.Skills;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;

import java.util.*;

public final class SkillFactory {

    private static final Map<Integer, ISkill> SKILL_CACHE = new HashMap<>();
    private static final Map<Integer, List<Integer>> SKILL_JOB_CACHE = new HashMap<>();
    private static final Map<Integer, SummonSkillEntry> SUMMON_SKILL_INFORMATION = new HashMap<>();

    public static void init() {
        WzData.SKILL.directory().fileStream()
                .filter(it -> it.name().length() <= 12)
                .map(WzFile::content)
                .map(it -> it.findByName("skill").orElse(null))
                .filter(Objects::nonNull)
                .flatMap(WzElement::childrenStream)
                .forEach(element -> Skills.of(element).ifPresent(skill -> {
                    int skillId = skill.getId();
                    SKILL_CACHE.put(skillId, skill);
                    // 技能 id 的后四位为技能编号，除去后四位则是职业编号
                    List<Integer> job = SKILL_JOB_CACHE.computeIfAbsent(skillId / 10000, k -> new ArrayList<>());
                    job.add(skillId);

                    element.findByName("summon/attack1/info")
                            .ifPresent(info -> {
                                SummonSkillEntry sse = new SummonSkillEntry();
                                sse.attackAfter = (short) Elements.findInt(info, "attackAfter", 999999);
                                sse.type = (byte) Elements.findInt(info, "type", 0);
                                sse.mobCount = (byte) Elements.findInt(info, "mobCount", 1);
                                SUMMON_SKILL_INFORMATION.put(skillId, sse);
                            });
                }));
    }

    public static ISkill getSkill(Integer id) {
        if (id == null) {
            return null;
        }

        return SKILL_CACHE.get(id);
    }

    public static ISkill getSkill1(Integer id) {
        ISkill ret = SKILL_CACHE.get(id);
        if (ret != null) {
            return ret;
        }
        synchronized (SKILL_CACHE) {
            ret = SKILL_CACHE.get(id);
            if (ret == null) {
                WzData.SKILL.directory().findFile(Jobs.filename(id))
                        .map(WzFile::content)
                        .flatMap(it -> it.findByName("skill/" + Skills.formatId(id)))
                        .flatMap(Skills::of)
                        .ifPresent(skill -> SKILL_CACHE.put(id, skill));
                ret = SKILL_CACHE.get(id);
            }
            return ret;
        }
    }

    public static List<Integer> getSkillsByJob(Integer jobId) {
        return SKILL_JOB_CACHE.get(jobId);
    }

    public static String getSkillName(Integer id) {
        ISkill skil = getSkill(id);
        if (skil != null) {
            return skil.getName();
        }
        return null;
    }

    public static SummonSkillEntry getSummonData(Integer skillid) {
        return SUMMON_SKILL_INFORMATION.get(skillid);
    }

    public static Collection<ISkill> getAllSkills() {
        return SKILL_CACHE.values();
    }
}
