package client;

import com.github.mrzhqiang.maplestory.skill.Jobs;
import com.github.mrzhqiang.maplestory.skill.Skills;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class SkillFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SkillFactory.class);

    private static final Map<Integer, ISkill> SKILL_CACHE = new HashMap<>();
    private static final Map<Integer, List<Integer>> SKILL_JOB_CACHE = new HashMap<>();
    private static final Map<Integer, SummonSkillEntry> SUMMON_SKILL_INFORMATION = new HashMap<>();

    public static ISkill getSkill(Integer id) {
        if (id == null) {
            return null;
        }

        if (!SKILL_CACHE.isEmpty()) {
            return SKILL_CACHE.get(id);
        }

        Stopwatch started = Stopwatch.createStarted();
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
        LOGGER.info("Skill cache 用时：{}", started.stop());
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

    public static final List<Integer> getSkillsByJob(final int jobId) {
        return SKILL_JOB_CACHE.get(jobId);
    }

    public static final String getSkillName(final int id) {
        ISkill skil = getSkill(id);
        if (skil != null) {
            return skil.getName();
        }
        return null;
    }

    public static final SummonSkillEntry getSummonData(final int skillid) {
        return SUMMON_SKILL_INFORMATION.get(skillid);
    }

    public static final Collection<ISkill> getAllSkills() {
        return SKILL_CACHE.values();
    }
}
