package server;

import client.MapleDisease;
import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import server.life.MobSkill;
import server.life.MobSkillFactory;

import java.util.HashMap;
import java.util.Map;

public final class MapleCarnivalFactory {

    private final static MapleCarnivalFactory instance = new MapleCarnivalFactory();
    private final Map<Integer, MCSkill> skills = new HashMap<>();
    private final Map<Integer, MCSkill> guardians = new HashMap<>();

    public MapleCarnivalFactory() {
        //whoosh
        initialize();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void init() {
        getInstance();
    }

    public static MapleCarnivalFactory getInstance() {
        return instance;
    }

    private void initialize() {
        if (!skills.isEmpty()) {
            return;
        }
        WzData.SKILL.directory().findFile("MCSkill.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element -> {
                    int skillId = Numbers.ofInt(element.name());
                    MCSkill skill = MCSkill.of(element);
                    boolean targetBool = Elements.findInt(element, "target", 1) > 1;
                    skill.setTargetsAll(targetBool);
                    skills.put(skillId, skill);
                }));
        WzData.SKILL.directory().findFile("MCGuardian.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element -> {
                    int skillId = Numbers.ofInt(element.name());
                    MCSkill skill = MCSkill.of(element);
                    guardians.put(skillId, skill);
                }));
    }

    public MCSkill getSkill(final int id) {
        return skills.get(id);
    }

    public MCSkill getGuardian(final int id) {
        return guardians.get(id);
    }

    public static final class MCSkill {

        public int cpLoss, skillid, level;
        public boolean targetsAll;

        public static MCSkill of(WzElement<?> element) {
            int cpInt = Elements.findInt(element, "spendCP");
            int mobSkillInt = Elements.findInt(element, "mobSkillID");
            int levelInt = Elements.findInt(element, "level");
            return new MCSkill(cpInt, mobSkillInt, levelInt);
        }

        public MCSkill(int _cpLoss, int _skillid, int _level) {
            cpLoss = _cpLoss;
            skillid = _skillid;
            level = _level;
        }

        public void setTargetsAll(boolean targetsAll) {
            this.targetsAll = targetsAll;
        }

        public MobSkill getSkill() {
            return MobSkillFactory.getMobSkill(skillid, 1); //level?
        }

        public MapleDisease getDisease() {
            if (skillid <= 0) {
                return MapleDisease.getRandom();
            }
            return MapleDisease.getBySkill(skillid);
        }
    }
}
