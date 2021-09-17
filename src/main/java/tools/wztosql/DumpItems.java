package tools.wztosql;

import client.inventory.MapleInventoryType;
import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzDirectory;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import constants.GameConstants;
import database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

public class DumpItems {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpItems.class);

    protected final Set<Integer> doneIds = new LinkedHashSet<>();
    protected boolean hadError = false;
    protected boolean update;
    protected int id = 0;
    private final Connection con = DatabaseConnection.getConnection();

    public DumpItems(boolean update) {
        this.update = update;
    }

    public boolean isHadError() {
        return hadError;
    }

    public void dumpItems() throws Exception {
        if (!hadError) {
            PreparedStatement psa = con.prepareStatement("INSERT INTO wz_itemadddata(itemid, `key`, `subKey`, `value`) VALUES (?, ?, ?, ?)");
            PreparedStatement psr = con.prepareStatement("INSERT INTO wz_itemrewarddata(itemid, item, prob, quantity, period, worldMsg, effect) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement ps = con.prepareStatement("INSERT INTO wz_itemdata(itemid, name, msg, `desc`, slotMax, price, wholePrice, stateChange, flags, karma, meso, monsterBook, itemMakeLevel, questId, scrollReqs, consumeItem, totalprob, incSkill, replaceId, replaceMsg, `create`, afterImage) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pse = con.prepareStatement("INSERT INTO wz_itemequipdata(itemid, itemLevel, `key`, `value`) VALUES (?, ?, ?, ?)");
            try {
                dumpItems(psa, psr, ps, pse);
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.debug(id + " quest.");
                hadError = true;
            } finally {
                psa.executeBatch();
                psa.close();
                psr.executeBatch();
                psr.close();
                pse.executeBatch();
                pse.close();
                ps.executeBatch();
                ps.close();
            }
        }
    }

    public void delete(String sql) throws Exception {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    public boolean doesExist(String sql) throws Exception {
        boolean ret;
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            ret = rs.next();
        }
        return ret;
    }

    public void dumpItems(WzDirectory d, PreparedStatement psa, PreparedStatement psr,
                          PreparedStatement ps, PreparedStatement pse, boolean charz) {
        d.dirStream().filter(directory -> !directory.name().equalsIgnoreCase("Special")
                        && !directory.name().equalsIgnoreCase("Hair")
                        && !directory.name().equalsIgnoreCase("Face")
                        && !directory.name().equalsIgnoreCase("Afterimage"))
                .forEach(directory -> directory.fileStream().map(WzFile::content)
                        .forEach(iz -> {
                            if (charz || directory.name().equalsIgnoreCase("Pet")) {
                                try {
                                    dumpItem(psa, psr, ps, pse, iz);
                                } catch (Exception e) {
                                    LOGGER.error("导出数据出错", e);
                                }
                            } else {
                                iz.childrenStream().forEach(itemData -> {
                                    try {
                                        dumpItem(psa, psr, ps, pse, itemData);
                                    } catch (Exception e) {
                                        LOGGER.error("导出数据出错", e);
                                    }
                                });
                            }
                        }));
    }

    public void dumpItem(PreparedStatement psa, PreparedStatement psr, PreparedStatement ps,
                         PreparedStatement pse, WzElement<?> iz) throws Exception {
        try {
            String name = iz.name();
            if (name.endsWith(".img")) {
                name = name.replaceAll("\\.img", "");
            }
            this.id = Numbers.ofInt(name);
        } catch (NumberFormatException nfe) { //not we need
            return;
        }
        if (doneIds.contains(id) || GameConstants.getInventoryType(id) == MapleInventoryType.UNDEFINED) {
            return;
        }
        doneIds.add(id);
        if (update && doesExist("SELECT * FROM wz_itemdata WHERE itemid = " + id)) {
            return;
        }
        ps.setInt(1, id);
        WzElement<?> stringData = getStringData(id);
        if (stringData == null) {
            ps.setString(2, "");
            ps.setString(3, "");
            ps.setString(4, "");
        } else {
            ps.setString(2, Elements.findString(stringData, "name"));
            ps.setString(3, Elements.findString(stringData, "msg"));
            ps.setString(4, Elements.findString(stringData, "desc"));
        }
        short ret = iz.findByName("info/slotMax")
                .map(element -> Elements.ofInt(element, -1))
                .map(Integer::shortValue)
                .orElseGet(() -> {
                    if (GameConstants.getInventoryType(id) == MapleInventoryType.EQUIP) {
                        return (short) 1;
                    } else {
                        return (short) 100;
                    }
                });
        ps.setInt(5, ret);

        double pEntry;
        if (iz.find("info/unitPrice") != null) {
            try {
                pEntry = Elements.findDouble(iz, "info/unitPrice");
            } catch (Exception ignore) {
                pEntry = Elements.findInt(iz, "info/unitPrice", -1);
            }
        } else {
            if (iz.find("info/price") == null) {
                pEntry = -1.0;
            } else {
                pEntry = Elements.findInt(iz, "info/price", -1);
            }
        }
        if (id == 2070019 || id == 2330007) {
            pEntry = 1.0;
        }
        ps.setString(6, String.valueOf(pEntry));
        ps.setInt(7, Elements.findInt(iz, "info/price", -1));
        ps.setInt(8, Elements.findInt(iz, "info/stateChangeItem"));
        int flags = Elements.findInt(iz, "info/bagType");
        if (Elements.findInt(iz, "info/notSale") > 0) {
            flags |= 0x10;
        }
        if (Elements.findInt(iz, "info/expireOnLogout") > 0) {
            flags |= 0x20;
        }
        if (Elements.findInt(iz, "info/pickUpBlock") > 0) {
            flags |= 0x40;
        }
        if (Elements.findInt(iz, "info/only") > 0) {
            flags |= 0x80;
        }
        if (Elements.findInt(iz, "info/accountSharable") > 0) {
            flags |= 0x100;
        }
        if (Elements.findInt(iz, "info/quest") > 0) {
            flags |= 0x200;
        }
        if (id != 4310008 && Elements.findInt(iz, "info/tradeBlock") > 0) {
            flags |= 0x400;
        }
        if (Elements.findInt(iz, "info/accountShareTag") > 0) {
            flags |= 0x800;
        }
        if (Elements.findInt(iz, "info/mobHP") > 0 && Elements.findInt(iz, "info/mobHP") < 100) {
            flags |= 0x1000;
        }
        ps.setInt(9, flags);
        ps.setInt(10, Elements.findInt(iz, "info/tradeAvailable"));
        ps.setInt(11, Elements.findInt(iz, "info/meso"));
        ps.setInt(12, Elements.findInt(iz, "info/mob"));
        ps.setInt(13, Elements.findInt(iz, "info/lv"));
        ps.setInt(14, Elements.findInt(iz, "info/questId"));
        StringBuilder scrollReqs = new StringBuilder(), consumeItem = new StringBuilder(), incSkill = new StringBuilder();
        iz.findByName("req").map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element -> {
                    if (scrollReqs.length() > 0) {
                        scrollReqs.append(",");
                    }
                    scrollReqs.append(Elements.ofInt(element));
                }));
        iz.findByName("consumeItem").map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element -> {
                    if (consumeItem.length() > 0) {
                        consumeItem.append(",");
                    }
                    consumeItem.append(Elements.ofInt(element));
                }));
        ps.setString(15, scrollReqs.toString());
        ps.setString(16, consumeItem.toString());
        Map<Integer, Map<String, Integer>> equipStats = new HashMap<>();
        equipStats.put(-1, new HashMap<>());
        iz.findByName("mob").map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element -> {
                    int id = Elements.findInt(element, "id");
                    int prob = Elements.findInt(element, "prob");
                    equipStats.get(-1).put("mob" + id, prob);
                }));
        iz.findByName("info/level/case").map(WzElement::childrenStream)
                .map(stream -> stream.flatMap(WzElement::childrenStream))
                .ifPresent(stream -> stream
                        .filter(data -> data.name().length() == 1)
                        .map(element -> element.find("Skill"))
                        .filter(Objects::nonNull)
                        .flatMap(WzElement::childrenStream)
                        .forEach(skil -> {
                            int id = Elements.findInt(skil, "id");
                            if (id != 0) {
                                if (incSkill.length() > 0) {
                                    incSkill.append(",");
                                }
                                incSkill.append(id);
                            }
                        }));
        iz.findByName("info/level/info").map(WzElement::childrenStream)
                .ifPresent(stream -> stream.filter(element -> Elements.findInt(element, "exp") != 0)
                        .forEach(element -> {
                            int lv = Numbers.ofInt(element.name());
                            equipStats.computeIfAbsent(lv, k -> new HashMap<>());
                            element.childrenStream().forEach(data -> {
                                if (data.name().length() > 3) {
                                    equipStats.get(lv).put(data.name().substring(3), Elements.ofInt(data));
                                }
                            });
                        }));
        String s22 = iz.findByName("info").map(element -> {
            Map<String, Integer> rett = equipStats.get(-1);
            element.childrenStream().filter(data -> data.name().startsWith("inc"))
                    .forEach(data -> {
                        int gg = Elements.ofInt(data);
                        if (gg != 0) {
                            rett.put(data.name().substring(3), gg);
                        }
                    });
            //save sql, only do the ones that exist
            for (String stat : GameConstants.stats) {
                if ("canLevel".equals(stat)) {
                    if (element.find("level") != null) {
                        rett.put(stat, 1);
                    }
                    continue;
                }
                element.findByName(stat).ifPresent(d -> {
                    if ("skill".equals(stat)) {
                        for (int i = 0; i < d.childrenStream().count(); i++) { // List of allowed skillIds
                            rett.put("skillid" + i, Elements.findInt(d, String.valueOf(i)));
                        }
                    } else {
                        int dd = Elements.ofInt(d);
                        if (dd != 0) {
                            rett.put(stat, dd);
                        }
                    }
                });
            }
            return Elements.findString(element, "afterImage");
        }).orElse("");
        ps.setString(22, s22);

        pse.setInt(1, id);
        for (Entry<Integer, Map<String, Integer>> stats : equipStats.entrySet()) {
            pse.setInt(2, stats.getKey());
            for (Entry<String, Integer> stat : stats.getValue().entrySet()) {
                pse.setString(3, stat.getKey());
                pse.setInt(4, stat.getValue());
                pse.addBatch();
            }
        }
        WzElement<?> dat = iz.find("info/addition");
        if (dat != null) {
            psa.setInt(1, id);
            dat.childrenStream().forEach(element -> {
                Pair<String, Integer> incs = null;
                switch (element.name()) {
                    case "statinc":
                    case "critical":
                    case "skill":
                    case "mobdie":
                    case "hpmpchange":
                    case "elemboost":
                    case "elemBoost":
                    case "mobcategory":
                    case "boss":
                        element.childrenStream().forEach(subKey -> {
                            if (subKey.name().equals("con")) {
                                subKey.childrenStream().forEach(conK -> {
                                    try {
                                        switch (conK.name()) {
                                            case "job":
                                                StringBuilder sbbb = new StringBuilder();
                                                if (conK.value() == null) { // a loop
                                                    conK.childrenStream().forEach(ids -> {
                                                        sbbb.append(ids.value().toString());
                                                        sbbb.append(",");
                                                    });
                                                    sbbb.deleteCharAt(sbbb.length() - 1);
                                                } else {
                                                    sbbb.append(conK.value().toString());
                                                }
                                                psa.setString(2, element.name().equals("elemBoost") ? "elemboost" : element.name());
                                                psa.setString(3, "con:job");
                                                psa.setString(4, sbbb.toString());
                                                psa.addBatch();
                                                break;
                                            case "weekDay":
                                                // 01142367
                                                return;
                                            default:
                                                psa.setString(2, element.name().equals("elemBoost") ? "elemboost" : element.name());
                                                psa.setString(3, "con:" + conK.name());
                                                psa.setString(4, conK.value().toString());
                                                psa.addBatch();
                                                break;
                                        }
                                    } catch (Exception e) {
                                        LOGGER.error("处理 SQL 出错", e);
                                    }
                                });
                            } else {
                                try {
                                    psa.setString(2, element.name().equals("elemBoost") ? "elemboost" : element.name());
                                    psa.setString(3, subKey.name());
                                    psa.setString(4, subKey.value().toString());
                                    psa.addBatch();
                                } catch (SQLException e) {
                                    LOGGER.error("处理 SQL 出错", e);
                                }
                            }
                        });
                        break;
                    default:
                        LOGGER.error("UNKNOWN EQ ADDITION : " + element.name() + " from " + id);
                        break;
                }
            });
        }
        int totalprob = 0;
        dat = iz.find("reward");
        if (dat != null) {
            psr.setInt(1, id);
            totalprob = dat.childrenStream().map(reward -> {
                try {
                    psr.setInt(2, Elements.findInt(reward, "item"));
                    psr.setInt(3, Elements.findInt(reward, "prob"));
                    psr.setInt(4, Elements.findInt(reward, "count"));
                    psr.setInt(5, Elements.findInt(reward, "period"));
                    psr.setString(6, Elements.findString(reward, "worldMsg"));
                    psr.setString(7, Elements.findString(reward, "Effect"));
                    psr.addBatch();
                } catch (SQLException e) {
                    LOGGER.error("处理 SQL 出错", e);
                }
                return Elements.findInt(reward, "prob");
            }).reduce(0, Integer::sum);
        }
        ps.setInt(17, totalprob);
        ps.setString(18, incSkill.toString());
        dat = iz.find("replace");
        if (dat != null) {
            ps.setInt(19, Elements.findInt(dat, "itemid"));
            ps.setString(20, Elements.findString(dat, "msg"));
        } else {
            ps.setInt(19, 0);
            ps.setString(20, "");
        }
        ps.setInt(21, Elements.findInt(iz, "info/create"));
        ps.addBatch();
    }
    //kinda inefficient

    public void dumpItems(PreparedStatement psa, PreparedStatement psr, PreparedStatement ps, PreparedStatement pse) throws Exception {
        if (!update) {
            delete("DELETE FROM wz_itemdata");
            delete("DELETE FROM wz_itemequipdata");
            delete("DELETE FROM wz_itemadddata");
            delete("DELETE FROM wz_itemrewarddata");
            LOGGER.debug("Deleted wz_itemdata successfully.");
        }
        LOGGER.debug("Adding into wz_itemdata.....");
        dumpItems(WzData.ITEM.directory(), psa, psr, ps, pse, false);
        dumpItems(WzData.CHARACTER.directory(), psa, psr, ps, pse, true);
        LOGGER.debug("Done wz_itemdata...");
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
            final DumpItems dq = new DumpItems(update);
            LOGGER.debug("Dumping Items");
            dq.dumpItems();
            hadError |= dq.isHadError();
            currentQuest = dq.currentId();
        } catch (Exception e) {
            hadError = true;
            e.printStackTrace();
            LOGGER.debug(currentQuest + " quest.");
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

    //    private final MapleDataProvider string = MapleDataProviderFactory.getDataProvider(WzManage.STRING_DIR);
//    protected final MapleData cashStringData = string.getData("Cash.img");
//    protected final MapleData consumeStringData = string.getData("Consume.img");
//    protected final MapleData eqpStringData = string.getData("Eqp.img");
//    protected final MapleData etcStringData = string.getData("Etc.img");
//    protected final MapleData insStringData = string.getData("Ins.img");
//    protected final MapleData petStringData = string.getData("Pet.img");
    protected final WzElement<?> getStringData(final int itemId) {
        String cat = null;
        Optional<WzElement<?>> data;

        if (itemId >= 5010000) {
            data = WzData.STRING.directory().findFile("Cash.img").map(WzFile::content);
        } else if (itemId >= 2000000 && itemId < 3000000) {
            data = WzData.STRING.directory().findFile("Consume.img").map(WzFile::content);
        } else if ((itemId >= 1132000 && itemId < 1183000) || (itemId >= 1010000 && itemId < 1040000) || (itemId >= 1122000 && itemId < 1123000)) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Accessory";
        } else if (itemId >= 1172000 && itemId < 1180000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/MonsterBook";
        } else if (itemId >= 1662000 && itemId < 1680000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Android";
        } else if (itemId >= 1000000 && itemId < 1010000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Cap";
        } else if (itemId >= 1102000 && itemId < 1103000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Cape";
        } else if (itemId >= 1040000 && itemId < 1050000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Coat";
        } else if (itemId >= 20000 && itemId < 22000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Face";
        } else if (itemId >= 1080000 && itemId < 1090000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Glove";
        } else if (itemId >= 30000 && itemId < 35000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Hair";
        } else if (itemId >= 1050000 && itemId < 1060000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Longcoat";
        } else if (itemId >= 1060000 && itemId < 1070000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Pants";
        } else if (itemId >= 1610000 && itemId < 1660000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Mechanic";
        } else if (itemId >= 1802000 && itemId < 1820000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/PetEquip";
        } else if (itemId >= 1920000 && itemId < 2000000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Dragon";
        } else if (itemId >= 1112000 && itemId < 1120000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Ring";
        } else if (itemId >= 1092000 && itemId < 1100000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Shield";
        } else if (itemId >= 1070000 && itemId < 1080000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Shoes";
        } else if (itemId >= 1900000 && itemId < 1920000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Taming";
        } else if (itemId >= 1200000 && itemId < 1210000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Totem";
        } else if (itemId >= 1210000 && itemId < 1800000) {
            data = WzData.STRING.directory().findFile("Eqp.img").map(WzFile::content);
            cat = "Eqp/Weapon";
        } else if (itemId >= 4000000 && itemId < 5000000) {
            data = WzData.STRING.directory().findFile("Etc.img").map(WzFile::content);
            cat = "Etc";
        } else if (itemId >= 3000000 && itemId < 4000000) {
            data = WzData.STRING.directory().findFile("Ins.img").map(WzFile::content);
        } else if (itemId >= 5000000 && itemId < 5010000) {
            data = WzData.STRING.directory().findFile("Pet.img").map(WzFile::content);
        } else {
            return null;
        }
        if (cat == null) {
            return data.map(element -> element.find(String.valueOf(itemId))).orElse(null);
        } else {
            String name = cat + "/" + itemId;
            return data.flatMap(element -> element.findByName(name)).orElse(null);
        }
    }
}
