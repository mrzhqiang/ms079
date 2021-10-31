package tools.wztosql;

import client.inventory.MapleInventoryType;
import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.domain.DWzItemAddData;
import com.github.mrzhqiang.maplestory.domain.DWzItemData;
import com.github.mrzhqiang.maplestory.domain.DWzItemEquipData;
import com.github.mrzhqiang.maplestory.domain.DWzItemRewardData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzItemAddData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzItemData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzItemEquipData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzItemRewardData;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzDirectory;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import constants.GameConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class DumpItems {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpItems.class);

    protected final Set<Integer> doneIds = new LinkedHashSet<>();
    protected boolean hadError = false;
    protected boolean update;
    protected int id = 0;

    public DumpItems(boolean update) {
        this.update = update;
    }

    public boolean isHadError() {
        return hadError;
    }

    public void dumpItems(WzDirectory d, boolean charz) {
        d.dirStream().filter(directory -> !directory.name().equalsIgnoreCase("Special")
                        && !directory.name().equalsIgnoreCase("Hair")
                        && !directory.name().equalsIgnoreCase("Face")
                        && !directory.name().equalsIgnoreCase("Afterimage"))
                .forEach(directory -> directory.fileStream().map(WzFile::content)
                        .forEach(iz -> {
                            if (charz || directory.name().equalsIgnoreCase("Pet")) {
                                dumpItem(iz);
                            } else {
                                iz.childrenStream().forEach(this::dumpItem);
                            }
                        }));
    }

    public void dumpItem(WzElement<?> iz) {
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

        if (update && new QDWzItemData().id.eq(id).exists()) {
            return;
        }

        DWzItemData itemData = new DWzItemData();
        itemData.id = id;

        WzElement<?> stringData = getStringData(id);
        if (stringData == null) {
            itemData.name = "";
            itemData.msg = "";
            itemData.desc = "";
        } else {
            itemData.name = Elements.findString(stringData, "name");
            itemData.msg = Elements.findString(stringData, "msg");
            itemData.desc = Elements.findString(stringData, "desc");
        }
        itemData.slotMax = iz.findByName("info/slotMax")
                .map(element1 -> Elements.ofInt(element1, -1))
                .orElseGet(() -> {
                    if (GameConstants.getInventoryType(id) == MapleInventoryType.EQUIP) {
                        return 1;
                    } else {
                        return 100;
                    }
                });

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

        itemData.price = String.valueOf(pEntry);
        itemData.wholePrice = Elements.findInt(iz, "info/price", -1);
        itemData.stateChange = Elements.findInt(iz, "info/stateChangeItem");
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

        itemData.flags = flags;
        itemData.karma = Elements.findInt(iz, "info/tradeAvailable");
        itemData.meso = Elements.findInt(iz, "info/meso");
        itemData.monsterBook = Elements.findInt(iz, "info/mob");
        itemData.itemMakeLevel = Elements.findInt(iz, "info/lv");
        itemData.questId = Elements.findInt(iz, "info/questId");
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
        itemData.scrollReqs = scrollReqs.toString();
        itemData.consumeItem = consumeItem.toString();
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
        itemData.afterImage = iz.findByName("info").map(element1 -> {
            Map<String, Integer> rett = equipStats.get(-1);
            element1.childrenStream().filter(data -> data.name().startsWith("inc"))
                    .forEach(data -> {
                        int gg = Elements.ofInt(data);
                        if (gg != 0) {
                            rett.put(data.name().substring(3), gg);
                        }
                    });
            //save sql, only do the ones that exist
            for (String stat1 : GameConstants.stats) {
                if ("canLevel".equals(stat1)) {
                    if (element1.find("level") != null) {
                        rett.put(stat1, 1);
                    }
                    continue;
                }
                element1.findByName(stat1).ifPresent(d -> {
                    if ("skill".equals(stat1)) {
                        for (int i = 0; i < d.childrenStream().count(); i++) { // List of allowed skillIds
                            rett.put("skillid" + i, Elements.findInt(d, String.valueOf(i)));
                        }
                    } else {
                        int dd = Elements.ofInt(d);
                        if (dd != 0) {
                            rett.put(stat1, dd);
                        }
                    }
                });
            }
            return Elements.findString(element1, "afterImage");
        }).orElse("");

        for (Entry<Integer, Map<String, Integer>> stats : equipStats.entrySet()) {
            for (Entry<String, Integer> stat : stats.getValue().entrySet()) {
                DWzItemEquipData equipData = new DWzItemEquipData();
                equipData.id = id;
                equipData.itemLevel = stats.getKey();
                equipData.key = stat.getKey();
                equipData.value = stat.getValue();
                equipData.save();
            }
        }
        WzElement<?> dat = iz.find("info/addition");
        if (dat != null) {
            dat.childrenStream().forEach(element -> {
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
                                    DWzItemAddData addData = new DWzItemAddData();
                                    addData.id = id;
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
                                            addData.key = element.name().equals("elemBoost") ? "elemboost" : element.name();
                                            addData.subKey = "con:job";
                                            addData.value = sbbb.toString();
                                            addData.save();
                                            break;
                                        case "weekDay":
                                            // 01142367
                                            return;
                                        default:
                                            addData.key = element.name().equals("elemBoost") ? "elemboost" : element.name();
                                            addData.subKey = "con:" + conK.name();
                                            addData.value = conK.value().toString();
                                            addData.save();
                                            break;
                                    }
                                });
                            } else {
                                DWzItemAddData addData = new DWzItemAddData();
                                addData.id = id;
                                addData.key = element.name().equals("elemBoost") ? "elemboost" : element.name();
                                addData.subKey = subKey.name();
                                addData.value = subKey.value().toString();
                                addData.save();
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
            totalprob = dat.childrenStream().map(reward -> {
                DWzItemRewardData rewardData = new DWzItemRewardData();
                rewardData.id = id;
                rewardData.item = Elements.findInt(reward, "item");
                rewardData.prob = Elements.findInt(reward, "prob");
                rewardData.quantity = Elements.findInt(reward, "count");
                rewardData.period = Elements.findInt(reward, "period");
                rewardData.worldMsg = Elements.findString(reward, "worldMsg");
                rewardData.effect = Elements.findString(reward, "Effect");
                rewardData.save();
                return Elements.findInt(reward, "prob");
            }).reduce(0, Integer::sum);
        }
        itemData.totalprob = totalprob;
        itemData.incSkill = incSkill.toString();
        dat = iz.find("replace");
        if (dat != null) {
            itemData.replaceid = Elements.findInt(dat, "itemid");
            itemData.replacemsg = Elements.findString(dat, "msg");
        } else {
            itemData.replaceid = 0;
            itemData.replacemsg = "";
        }
        itemData.create = Elements.findInt(iz, "info/create");
        itemData.save();
    }

    public void dumpItems() {
        if (!update) {
            new QDWzItemData().delete();
            new QDWzItemEquipData().delete();
            new QDWzItemAddData().delete();
            new QDWzItemRewardData().delete();
            LOGGER.debug("已成功删除 wz_itemdata。");
        }

        LOGGER.debug("添加到 wz_itemdata...");
        dumpItems(WzData.ITEM.directory(), false);
        dumpItems(WzData.CHARACTER.directory(), true);
        LOGGER.debug("完成 wz_itemdata...");
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
