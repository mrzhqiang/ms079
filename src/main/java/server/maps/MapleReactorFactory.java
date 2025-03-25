package server.maps;

import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import com.google.common.base.Strings;
import tools.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class MapleReactorFactory {

    private static final Map<Integer, MapleReactorStats> REACTOR_STATS_CACHED = new HashMap<>();

    public static MapleReactorStats getReactor(int rid) {
        MapleReactorStats stats = REACTOR_STATS_CACHED.get(rid);
        if (stats != null) {
            return stats;
        }

        String name = Strings.padStart(String.valueOf(rid), 7, '0');
        int infoId = WzData.REACTOR.directory().findFile(name)
                .map(WzFile::content)
                .map(element -> Elements.findInt(element, "info/link"))
                .filter(e->e != 0)
                .orElse(rid);

        stats = REACTOR_STATS_CACHED.get(infoId);
        if (stats == null) {
            name = Strings.padStart(String.valueOf(infoId), 7, '0');
            stats = WzData.REACTOR.directory().findFile(name)
                    .map(WzFile::content)
                    .map(element -> {
                        MapleReactorStats newStats = new MapleReactorStats();
                        boolean canTouch = Elements.findInt(element, "info/activateByTouch") > 0;
                        boolean areaSet = false;
                        boolean foundState = false;
                        for (byte i = 0; i < element.childrenStream().count(); i++) {
                            Optional<WzElement<?>> optional = element.findByName(String.valueOf(i));
                            if (!optional.isPresent()) {
                                break;
                            }

                            WzElement<?> index = optional.get();
                            Optional<WzElement<?>> eventOptional = index.findByName("event");
                            Optional<WzElement<?>> eventOptional0 = eventOptional.map(it -> it.find("0"));
                            if (eventOptional.isPresent() && eventOptional0.isPresent()) {
                                WzElement<?> event = eventOptional.get();
                                WzElement<?> event0 = eventOptional0.get();
                                int type = Elements.findInt(event0, "type");
                                Pair<Integer, Integer> reactItem = null;
                                if (type == 100) {
                                    int e0 = Elements.findInt(event0, "0");
                                    int e1 = Elements.findInt(event0, "1", 1);
                                    Vector eventLt = Elements.findVector(event0, "lt");
                                    Vector eventRb = Elements.findVector(event0, "rb");
                                    reactItem = new Pair<>(e0, e1);
                                    if (!areaSet) { //only set area of effect for item-triggered reactors once
                                        newStats.setTL(eventLt);
                                        newStats.setBR(eventRb);
                                        areaSet = true;
                                    }
                                }
                                foundState = true;
                                byte state = Elements.findInt(event0, "state").byteValue();
                                byte timeOut = Elements.findInt(event, "timeOut", -1).byteValue();
                                boolean two = Elements.findInt(event0, "2").byteValue() > 0;
                                boolean clickArea = event0.findByName("clickArea").isPresent();
                                byte touch = (byte) (two || clickArea || (type == 9) ? 1 : canTouch ? 2 : 0);
                                newStats.addState(i, type, reactItem, state, timeOut, touch);
                            } else {
                                newStats.addState(i, 999, null, (byte) (foundState ? -1 : (i + 1)), 0, (byte) 0);
                            }
                        }
                        REACTOR_STATS_CACHED.put(infoId, newStats);
                        if (rid != infoId) {
                            REACTOR_STATS_CACHED.put(rid, newStats);
                        }
                        return newStats;
                    }).orElse(new MapleReactorStats());
        } else {
            REACTOR_STATS_CACHED.put(rid, stats);
        }
        return stats;
    }
}
