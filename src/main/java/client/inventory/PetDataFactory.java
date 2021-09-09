package client.inventory;

import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import tools.Pair;

import java.util.HashMap;
import java.util.Map;

public class PetDataFactory {

    private static final Map<Pair<Integer, Integer>, PetCommand> PET_COMMAND_CACHED = new HashMap<>();
    private static final Map<Integer, Integer> PET_HUNGER = new HashMap<>();

    public static PetCommand getPetCommand(int petId, int skillId) {
        Pair<Integer, Integer> key = new Pair<>(petId, skillId);
        PetCommand ret = PET_COMMAND_CACHED.get(key);
        if (ret != null) {
            return ret;
        }

        WzData.ITEM.directory().findDir("Pet")
                .flatMap(it -> it.findFile(String.valueOf(petId)))
                .map(WzFile::content)
                .map(element -> {
                    String id = String.valueOf(skillId);
                    int prob = Elements.findInt(element, "interact/" + id + "/prob", 0);
                    int inc = Elements.findInt(element, "interact/" + id + "/inc", 0);
                    return new PetCommand(petId, skillId, prob, inc);
                })
                .ifPresent(petCommand -> PET_COMMAND_CACHED.put(key, petCommand));
        return PET_COMMAND_CACHED.get(key);
    }

    public static int getHunger(int petId) {
        Integer ret = PET_HUNGER.get(petId);
        if (ret != null) {
            return ret;
        }

        WzData.ITEM.directory().findDir("Pet")
                .flatMap(it -> it.findFile(String.valueOf(petId)))
                .map(WzFile::content)
                .ifPresent(element -> PET_HUNGER.put(petId, Elements.findInt(element, "info/hungry", 1)));
        return PET_HUNGER.get(petId);
    }
}
