package tools.wztosql;

import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.domain.DWzMobSkillData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzMobSkillData;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DumpMobSkills {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpMobSkills.class);

    protected boolean hadError = false;
    protected boolean update;
    protected int id = 0;

    public DumpMobSkills(boolean update) {
        this.update = update;
    }

    public boolean isHadError() {
        return hadError;
    }

    //kinda inefficient
    public void dumpMobSkills() {
        if (!update) {
            new QDWzMobSkillData().delete();
            LOGGER.debug("Deleted wz_mobskilldata successfully.");
        }
        LOGGER.debug("Adding into wz_mobskilldata.....");
        WzData.SKILL.directory().findFile("MobSkill.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element -> {
                    this.id = Numbers.ofInt(element.name());
                    element.findByName("level").map(WzElement::childrenStream)
                            .ifPresent(wzElementStream -> wzElementStream.forEach(lvlz -> {
                                int lvl = Numbers.ofInt(lvlz.name());
                                try {
                                    if (update && new QDWzMobSkillData().skillid.eq(id).level.eq(lvl).exists()) {
                                        return;
                                    }
                                    DWzMobSkillData skillData = new DWzMobSkillData();
                                    skillData.id = id;
                                    skillData.level = lvl;
                                    skillData.hp = Elements.findInt(lvlz, "hp", 100);
                                    skillData.mpcon = Elements.findInt(lvlz, "mpCon", 0);
                                    skillData.x = Elements.findInt(lvlz, "x", 1);
                                    skillData.y = Elements.findInt(lvlz, "y", 1);
                                    skillData.time = Elements.findInt(lvlz, "time", 0);
                                    skillData.prop = Elements.findInt(lvlz, "prop", 100);
                                    skillData.limit = Elements.findInt(lvlz, "limit", 0);
                                    skillData.spawneffect = Elements.findInt(lvlz, "summonEffect", 0);
                                    skillData.interval = Elements.findInt(lvlz, "interval", 0);

                                    StringBuilder summ = new StringBuilder();
                                    List<Integer> toSummon = new ArrayList<>();
                                    for (int i = 0; i < lvlz.childrenStream().count(); i++) {
                                        WzElement<?> wzElement = lvlz.find(String.valueOf(i));
                                        if (wzElement == null) {
                                            break;
                                        }
                                        toSummon.add(Elements.ofInt(wzElement));
                                    }
                                    for (Integer summon : toSummon) {
                                        if (summ.length() > 0) {
                                            summ.append(", ");
                                        }
                                        summ.append(summon);
                                    }
                                    skillData.summons = summ.toString();
                                    if (lvlz.find("lt") != null) {
                                        Vector lt = Elements.findVector(lvlz, "lt");
                                        skillData.ltx = lt.x;
                                        skillData.lty = lt.y;
                                    } else {
                                        skillData.ltx = 0;
                                        skillData.lty = 0;
                                    }
                                    if (lvlz.find("rb") != null) {
                                        Vector rb = Elements.findVector(lvlz, "rb");
                                        skillData.rbx = rb.x;
                                        skillData.rby = rb.y;
                                    } else {
                                        skillData.rbx = 0;
                                        skillData.rby = 0;
                                    }
                                    skillData.once = (Elements.findInt(lvlz, "summonOnce") > 0 ? 1 : 0);
                                    LOGGER.debug("Added skill: " + id + " level " + lvl);
                                    skillData.save();
                                } catch (Exception ignore) {
                                }
                            }));
                }));
        LOGGER.debug("Done wz_mobskilldata...");
    }

    public int currentId() {
        return id;
    }

    public static void main(String[] args) {
        boolean hadError = false;
        boolean update = false;
        long startTime = System.currentTimeMillis();
        for (String file : args) {
            if (file.equalsIgnoreCase("-update")) {
                update = true;
            }
        }
        int currentQuest = 0;
        try {
            final DumpMobSkills dq = new DumpMobSkills(update);
            LOGGER.debug("Dumping mobskills");
            dq.dumpMobSkills();
            hadError |= dq.isHadError();
            currentQuest = dq.currentId();
        } catch (Exception e) {
            hadError = true;
            LOGGER.debug(currentQuest + " skill.", e);
        }
        long endTime = System.currentTimeMillis();
        double elapsedSeconds = (endTime - startTime) / 1000.0;
        int elapsedSecs = (((int) elapsedSeconds) % 60);
        int elapsedMinutes = (int) (elapsedSeconds / 60.0);

        String withErrors = "";
        if (hadError) {
            withErrors = " with errors";
        }
        LOGGER.debug("Finished" + withErrors + " in " + elapsedMinutes + " minutes " + elapsedSecs + " seconds");
    }
}
