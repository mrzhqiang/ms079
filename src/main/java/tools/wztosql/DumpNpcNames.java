package tools.wztosql;

import com.github.mrzhqiang.maplestory.domain.DWzNPCNameData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzNPCNameData;
import com.github.mrzhqiang.maplestory.util.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Itzik
 */
public class DumpNpcNames {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpNpcNames.class);

    private static final Map<Integer, String> npcNames = new HashMap<>();

    public static void main(String[] args) {
        LOGGER.debug("Dumping npc name data.");
        DumpNpcNames dump = new DumpNpcNames();
        dump.dumpNpcNameData();
        LOGGER.debug("Dump complete.");
    }

    public void dumpNpcNameData() {
        WzData.STRING.directory().findFile("Npc.img");
        new QDWzNPCNameData().delete();
        WzData.STRING.directory().findFile("Npc.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element -> {
                    String n = Strings.padStart(element.name(), 7, '0');
                    try {
                        //only thing we really have to do is check if it exists. if we wanted to, we could get the script as well :3
                        if (WzData.NPC.directory().findFile(n).isPresent()) {
                            String name = Elements.findString(element, "name", "MISSINGNO");
                            if (name.contains("Maple TV") || name.contains("Baby Moon Bunny")) {
                                return;
                            }
                            int nid = Numbers.ofInt(element.name());
                            npcNames.put(nid, name);
                        }
                    } catch (NullPointerException ignored) {
                    } catch (RuntimeException e) { //swallow, don't add if
                    }
                }));
        for (int key : npcNames.keySet()) {
            try {
                DWzNPCNameData nameData = new DWzNPCNameData();
                nameData.setNpc(key);
                nameData.setName(npcNames.get(key));
                nameData.save();
                LOGGER.debug("key: " + key + " name: " + npcNames.get(key));
            } catch (Exception ex) {
                LOGGER.debug("Failed to save key " + key);
            }
        }
    }
}
