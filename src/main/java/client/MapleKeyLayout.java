package client;

import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DKeyMap;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDKeyMap;
import tools.Pair;
import tools.data.output.MaplePacketLittleEndianWriter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MapleKeyLayout implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    private boolean changed = false;
    private final Map<Integer, Pair<Integer, Integer>> keymap;

    public MapleKeyLayout() {
        keymap = new HashMap<>();
    }

    public MapleKeyLayout(Map<Integer, Pair<Integer, Integer>> keys) {
        keymap = keys;
    }

    public final Map<Integer, Pair<Integer, Integer>> Layout() {
        changed = true;
        return keymap;
    }

    public final void writeData(final MaplePacketLittleEndianWriter mplew) {
        Pair<Integer, Integer> binding;
        for (int x = 0; x < 90; x++) {
            binding = keymap.get(x);
            if (binding != null) {
                mplew.write(binding.getLeft());
                mplew.writeInt(binding.getRight());
            } else {
                mplew.write(0);
                mplew.writeInt(0);
            }
        }
    }

    public void saveKeys(int charid) {
        if (!changed || keymap.size() == 0) {
            return;
        }
        new QDKeyMap().character.id.eq(charid).delete();

        DCharacter one = new QDCharacter().id.eq(charid).findOne();
        for (Entry<Integer, Pair<Integer, Integer>> keybinding : keymap.entrySet()) {
            DKeyMap map = new DKeyMap();
            map.character = one;
            map.key = keybinding.getKey();
            map.type = keybinding.getValue().left;
            map.action = keybinding.getValue().right;
            map.save();
        }
    }
}
