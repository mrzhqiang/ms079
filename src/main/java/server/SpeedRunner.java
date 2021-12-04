package server;

import com.github.mrzhqiang.maplestory.domain.query.QDSpeedRun;
import server.maps.SpeedRunType;
import tools.Pair;
import tools.StringUtil;

import java.sql.SQLException;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SpeedRunner {

    private static SpeedRunner instance = new SpeedRunner();
    private final Map<SpeedRunType, Pair<String, Map<Integer, String>>> speedRunData;

    private SpeedRunner() {
        speedRunData = new EnumMap<SpeedRunType, Pair<String, Map<Integer, String>>>(SpeedRunType.class);
    }

    public static final SpeedRunner getInstance() {
        return instance;
    }

    public final Pair<String, Map<Integer, String>> getSpeedRunData(SpeedRunType type) {
        return speedRunData.get(type);
    }

    public final void addSpeedRunData(SpeedRunType type, Pair<StringBuilder, Map<Integer, String>> mib) {
        speedRunData.put(type, new Pair<String, Map<Integer, String>>(mib.getLeft().toString(), mib.getRight()));
    }

    public final void removeSpeedRunData(SpeedRunType type) {
        speedRunData.remove(type);
    }

    public final void loadSpeedRuns() throws SQLException {
        if (speedRunData.size() > 0) {
            return;
        }
        for (SpeedRunType type : SpeedRunType.values()) {
            loadSpeedRunData(type);
        }
    }

    public final void loadSpeedRunData(SpeedRunType type) {
        AtomicInteger rank = new AtomicInteger(1);
        StringBuilder ret = new StringBuilder("#r这是速度运行时间 " + StringUtil.makeEnumHumanReadable(type.name()) + ".#k\r\n\r\n");
        Map<Integer, String> rett = new LinkedHashMap<>();
        new QDSpeedRun().type.eq(type.name()).orderBy().type.asc().setMaxRows(25)
                .findEach(it -> {
                    addSpeedRunData(ret, rett, it.getMembers(), it.getLeader(), rank.get(), it.getTimeString());
                    rank.set(rank.get() + 1);
                });

        speedRunData.put(type, new Pair<>(ret.toString(), rett));
    }

    public Pair<StringBuilder, Map<Integer, String>> addSpeedRunData(StringBuilder ret,
                                                                     Map<Integer, String> rett,
                                                                     String members,
                                                                     String leader,
                                                                     int rank,
                                                                     String timestring) {
        StringBuilder rettt = new StringBuilder();

        String[] membrz = members.split(",");
        rettt.append("#b该远征队 " + leader + "'成功挑战排名为 " + rank + ".#k\r\n\r\n");
        for (int i = 0; i < membrz.length; i++) {
            rettt.append("#r#e");
            rettt.append(i + 1);
            rettt.append(".#n ");
            rettt.append(membrz[i]);
            rettt.append("#k\r\n");
        }
        rett.put(rank, rettt.toString());
        ret.append("#b");
        if (membrz.length > 1) {
            ret.append("#L");
            ret.append(rank);
            ret.append("#");
        }
        ret.append("Rank #e");
        ret.append(rank);
        ret.append("#n#k : ");
        ret.append(leader);
        ret.append(", in ");
        ret.append(timestring);
        if (membrz.length > 1) {
            ret.append("#l");
        }
        ret.append("\r\n");
        return new Pair<>(ret, rett);
    }
}
