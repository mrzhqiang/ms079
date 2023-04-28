package server.life;

import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.google.common.base.Strings;
import tools.Pair;

import java.util.HashMap;
import java.util.Map;

public class MobAttackInfoFactory {

    private static final MobAttackInfoFactory instance = new MobAttackInfoFactory();
    private static final Map<Pair<Integer, Integer>, MobAttackInfo> mobAttacks = new HashMap<>();

    public static MobAttackInfoFactory getInstance() {
        return instance;
    }

    public MobAttackInfo getMobAttackInfo(MapleMonster mob, int attack) {
        MobAttackInfo ret = mobAttacks.get(new Pair<>(mob.getId(), attack));
        if (ret != null) {
            return ret;
        }

        String mobXml = Strings.padStart(String.valueOf(mob.getId()), 7, '0');
        return WzData.MOB.directory().findFile(mobXml)
                .map(WzFile::content)
                .map(element -> element.findByName("info/link")
                        .map(Elements::ofString)
                        .map(name -> Strings.padStart(name, 7, '0'))
                        .flatMap(name -> WzData.MOB.directory().findFile(name))
                        .map(WzFile::content)
                        .orElse(element))
                .flatMap(element -> element.findByName("attack" + (attack + 1) + "/info"))
                .map(element -> {
                    MobAttackInfo info = new MobAttackInfo();
                    WzElement<?> wzElement = (WzElement<?>) element;
                    info.setDeadlyAttack(wzElement.findByName("deadlyAttack").isPresent());
                    info.setMpBurn(Elements.findInt(wzElement, "mpBurn"));
                    info.setDiseaseSkill(Elements.findInt(wzElement, "disease"));
                    info.setDiseaseLevel(Elements.findInt(wzElement, "level"));
                    info.setMpCon(Elements.findInt(wzElement, "conMP"));
                    mobAttacks.put(new Pair<>(mob.getId(), attack), info);
                    return info;
                }).orElse(null);
    }
}
