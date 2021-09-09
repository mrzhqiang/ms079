package com.github.mrzhqiang.maplestory.skill;

import client.Skill;
import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.life.Element;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 技能工具类。
 */
public final class Skills {
    private Skills() {
        // no instance
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Skills.class);

    public static String formatId(int id) {
        return Strings.padStart(Integer.toString(id), 7, '0');
    }

    public static Optional<String> nameById(int id) {
        return WzData.STRING.directory().findFile("Skill.img")
                .map(WzFile::content)
                .map(element -> Elements.findString(element, formatId(id) + "/name"));
    }

    public static Optional<Skill> of(WzElement<?> element) {
        return Optional.ofNullable(element)
                .map(WzElement::name)
                .map(Numbers::ofInt)
                .filter(integer -> integer > 0)
                .map(id -> createSkill(element, id));
    }

    private static Skill createSkill(WzElement<?> element, Integer id) {
        Skill skill = new Skill(id);
        skill.setName(Skills.nameById(id).orElse(""));
        skill.setElement(Element.NEUTRAL);
        String elemAttr = Elements.findString(element, "elemAttr");
        if (!Strings.isNullOrEmpty(elemAttr)) {
            skill.setElement(Element.getFromChar(elemAttr.charAt(0)));
        }
        skill.setInvisible(Elements.findInt(element, "invisible") > 0);
        skill.setTimeLimited(Elements.findInt(element, "timeLimited") > 0);
        skill.setMasterLevel(Elements.findInt(element, "masterLevel"));
        int typeValue = Elements.findInt(element, "skillType");
        boolean isBuff = typeValue == 2;
        if (typeValue == 0) {
            isBuff = checkBuffByEffectAndHitAndBall(element);
            isBuff = checkBuffByAction(isBuff, handleAction(element, skill));
            isBuff = checkBuffById(isBuff, id);
        }
        handleKeydown(element, skill);
        handleLevel(element, skill, isBuff);
        return skill;
    }

    private static void handleLevel(WzElement<?> element, Skill skill, boolean isBuff) {
        element.findByName("level")
                .map(WzElement::childrenStream)
                .map(stream -> stream
                        .map(it -> Effects.ofSkill(skill.getId(), isBuff, it))
                        .collect(Collectors.toList()))
                .ifPresent(skill::setEffects);
    }

    private static boolean checkBuffByEffectAndHitAndBall(WzElement<?> element) {
        return element.findByName("effect").isPresent()
                && Objects.isNull(element.find("hit"))
                && Objects.isNull(element.find("ball"));
    }

    private static boolean checkBuffByAction(boolean isBuff, WzElement<?> action) {
        isBuff |= Optional.ofNullable(action)
                .flatMap(it -> it.findByName("0"))
                .map(Elements::ofString)
                .filter("alert2"::equals)
                .isPresent();
        return isBuff;
    }

    private static void handleKeydown(WzElement<?> element, Skill skill) {
        skill.setChargeskill(element.findByName("keydown").isPresent());
    }

    private static boolean checkBuffById(boolean isBuff, Integer id) {
        switch (id) {
            case 2301002: // heal is alert2 but not overtime...
            case 2111003: // poison mist
            case 12111005: // Flame Gear
            case 2111002: // explosion
            case 4211001: // chakra
            case 2121001: // Big bang
            case 2221001: // Big bang
            case 2321001: // Big bang
                isBuff = false;
                break;
            case 1004: // monster riding
            case 10001004:
            case 20001004:
            case 20011004:
            case 30001004:
            case 1026: //Soaring
            case 10001026:
            case 20001026:
            case 20011026:
            case 30001026:
            case 9101004: // hide is a buff -.- atleast for us o.o"
            case 1111002: // combo
            case 4211003: // pickpocket
            case 4111001: // mesoup
            case 15111002: // Super Transformation
            case 5111005: // Transformation
            case 5121003: // Super Transformation
            case 13111005: // Alabtross
            case 21000000: // Aran Combo
            case 21101003: // Body Pressure
            case 21110000:
            case 5211001: // Pirate octopus summon
            case 5211002:
            case 5220002: // wrath of the octopi
            case 5001005: //dash
            case 15001003:
            case 5211006: //homing beacon
            case 5220011: //bullseye
            case 5110001: //energy charge
            case 15100004:
            case 5121009: //speed infusion
            case 15111005:
            case 22121001: //element reset
            case 22131001: //magic shield
            case 22141002: //magic booster
            case 22151002: //killer wing
            case 22151003: //magic resist
            case 22171000: //maple warrior
            case 22171004: //hero will
            case 22181000: //onyx blessing
            case 22181003: //soul stone
                //case 22121000:
                //case 22141003:
                //case 22151001:
                //case 22161002:
            case 4331003: //owl spirit
            case 15101006: //spark
            case 15111006: //spark
            case 4321000: //tornado spin
            case 1320009: //beholder's buff.. passive
            case 35120000:
            case 35001002: //TEMP. mech
            case 9001004: // hide
            case 4341002:
            case 32001003: //dark aura
            case 32120000:
            case 32101002: //blue aura
            case 32110000:
            case 32101003: //yellow aura
            case 32120001:
            case 35101007: //perfect armor
            case 35121006: //satellite safety
            case 35001001: //flame
            case 35101009:
            case 35111007: //TEMP
            case 35121005: //missile
            case 35121013:
                //case 12101005://自然力重置，怎么加了没用啊
            case 14100005://夜行者 驱逐
                //case 35111004: //siege
            case 35101002: //TEMP
            case 33111003: //puppet ?
            case 1211009:
            case 1111007:
            case 5221006:
            case 1311007: //magic,armor,atk crash
                isBuff = true;
                break;
        }
        return isBuff;
    }

    private static WzElement<?> handleAction(WzElement<?> element, Skill skill) {
        Optional<WzElement<?>> action = element.findByName("action");
        boolean isAction = action.isPresent();
        if (!isAction) {
            isAction = element.findByName("prepare/action").isPresent();
            if (!isAction) {
                switch (skill.getId()) {
                    case 5201001:
                    case 5221009:
                    case 4221001:
                    case 4321001: // 在 Skill.wz 目录下找不到
                    case 4321000: // not found
                    case 4331001: // not found
                    case 3101005: // or is this really hack
                        LOGGER.error("触发硬编码的 action 检测");
                        isAction = true;
                        break;
                }
            }
        }
        skill.setAction(isAction);
        return action.orElse(null);
    }

}
