package handling.channel;

import com.github.mrzhqiang.maplestory.domain.DGuild;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDGuild;
import io.ebean.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.mrzhqiang.maplestory.timer.Timer;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MapleGuildRanking {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleGuildRanking.class);

    private static final MapleGuildRanking instance = new MapleGuildRanking();
    private final List<GuildRankingInfo> ranks = new LinkedList<>();
    private final List<levelRankingInfo> ranks1 = new LinkedList<>();
    private final List<mesoRankingInfo> ranks2 = new LinkedList<>();

    public void RankingUpdate() {
        Timer.WORLD.register(() -> {
            try {
                reload();
                showLevelRank();
                showMesoRank();
            } catch (Exception ex) {
                LOGGER.error("Could not update rankings", ex);
            }
        }, 60 * 60 * 1000, 60 * 60 * 1000);
    }

    public static MapleGuildRanking getInstance() {
        return instance;
    }

    public List<GuildRankingInfo> getGuildRank() {
        if (ranks.isEmpty()) {
            reload();
        }
        return ranks;
    }

    public List<levelRankingInfo> getLevelRank() {
        if (ranks1.isEmpty()) {
            showLevelRank();
        }
        return ranks1;
    }

    public List<mesoRankingInfo> getMesoRank() {
        if (ranks2.isEmpty()) {
            showMesoRank();
        }
        return ranks2;
    }

    private void reload() {
        ranks.clear();

        new QDGuild().orderBy().gp.desc()
                .setMaxRows(50)
                .findEach(data -> ranks.add(new GuildRankingInfo(data)));
    }

    private void showLevelRank() {
        ranks1.clear();
        List<levelRankingInfo> infoList = new QDCharacter().gm.lt(1)
                .orderBy().level.desc()
                .setMaxRows(100)
                .findStream()
                .map(it -> new levelRankingInfo(it.getName(), it.getLevel(), it.getStr(), it.getDex(), it.getIntelligence(), it.getLuk()))
                .collect(Collectors.toList());
        ranks1.addAll(infoList);
    }

    private void showMesoRank() {
        ranks2.clear();

        List<mesoRankingInfo> infos = DB.findNative(mesoRankingInfo.class,
                        "SELECT chr.name,chr.str,chr.dex,chr,int_ as intelligence,chr.luk, ( chr.meso + s.meso ) as money " +
                                "FROM `characters` as chr , `storages` as s " +
                                "WHERE chr.gm < 1 AND s.account_id = chr.account_id " +
                                "ORDER BY money DESC LIMIT 20")
                .findList();
        ranks2.addAll(infos);
    }

    public static class mesoRankingInfo {

        private final String name;
        private final long money;
        private final int str, dex, intelligence, luk;

        public mesoRankingInfo(String name, long money, int str, int dex, int intelligence, int luk) {
            this.name = name;
            this.money = money;
            this.str = str;
            this.dex = dex;
            this.intelligence = intelligence;
            this.luk = luk;
        }

        public String getName() {
            return name;
        }

        public long getMeso() {
            return money;
        }

        public int getStr() {
            return str;
        }

        public int getDex() {
            return dex;
        }

        public int getInt() {
            return intelligence;
        }

        public int getLuk() {
            return luk;
        }
    }

    public static class levelRankingInfo {

        private final String name;
        private final int level, str, dex, _int, luk;

        public levelRankingInfo(String name, int level, int str, int dex, int intt, int luk) {
            this.name = name;
            this.level = level;
            this.str = str;
            this.dex = dex;
            this._int = intt;
            this.luk = luk;
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }

        public int getStr() {
            return str;
        }

        public int getDex() {
            return dex;
        }

        public int getInt() {
            return _int;
        }

        public int getLuk() {
            return luk;
        }
    }

    public static class GuildRankingInfo {

        public final DGuild guild;

        public GuildRankingInfo(DGuild dGuild) {
            this.guild = dGuild;
        }

        public String getName() {
            return guild.getName();
        }

        public int getGP() {
            return guild.getGp();
        }

        public int getLogo() {
            return guild.getLogo();
        }

        public int getLogoColor() {
            return guild.getLogoColor();
        }

        public int getLogoBg() {
            return guild.getLogoBg();
        }

        public int getLogoBgColor() {
            return guild.getLogoBgColor();
        }
    }
}
