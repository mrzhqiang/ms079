package server;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.Equip;
import client.inventory.IItem;
import client.inventory.ItemFlag;
import client.inventory.MapleInventoryType;
import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.util.Effects;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.*;
import com.google.common.collect.Maps;
import constants.GameConstants;
import database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.Pair;
import tools.Triple;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MapleItemInformationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleItemInformationProvider.class);

    private final static MapleItemInformationProvider instance = new MapleItemInformationProvider();
    protected Map<Integer, Boolean> onEquipUntradableCache = new HashMap<>();
    protected final Map<Integer, List<Integer>> scrollReqCache = new HashMap<>();
    protected final Map<Integer, Short> slotMaxCache = new HashMap<>();
    protected final Map<Integer, Integer> getExpCache = new HashMap<>();
    protected final Map<Integer, List<StructPotentialItem>> potentialCache = new HashMap<>();
    protected final Map<Integer, MapleStatEffect> itemEffects = new HashMap<>();
    protected final Map<Integer, Map<String, Integer>> equipStatsCache = new HashMap<>();
    protected final Map<Integer, Map<String, Byte>> itemMakeStatsCache = new HashMap<>();
    protected final Map<Integer, Short> itemMakeLevel = new HashMap<>();
    protected final Map<Integer, Equip> equipCache = new HashMap<>();
    protected final Map<Integer, Double> priceCache = new HashMap<>();
    protected final Map<Integer, Integer> wholePriceCache = new HashMap<>();
    protected final Map<Integer, Integer> projectileWatkCache = new HashMap<>();
    protected final Map<Integer, Integer> monsterBookID = new HashMap<>();
    protected final Map<Integer, String> nameCache = new HashMap<>();
    protected final Map<Integer, String> descCache = new HashMap<>();
    protected final Map<Integer, String> msgCache = new HashMap<>();
    protected final Map<Integer, Map<String, Integer>> SkillStatsCache = new HashMap<>();
    protected final Map<Integer, Byte> consumeOnPickupCache = new HashMap<>();
    protected final Map<Integer, Boolean> dropRestrictionCache = new HashMap<>();
    protected final Map<Integer, Boolean> accCache = new HashMap<>();
    protected final Map<Integer, Boolean> pickupRestrictionCache = new HashMap<>();
    protected final Map<Integer, Integer> stateChangeCache = new HashMap<>();
    protected final Map<Integer, Integer> mesoCache = new HashMap<>();
    protected final Map<Integer, Boolean> notSaleCache = new HashMap<>();
    protected final Map<Integer, Integer> karmaEnabledCache = new HashMap<>();
    protected Map<Integer, Boolean> karmaCache = new HashMap<>();
    protected final Map<Integer, Boolean> isQuestItemCache = new HashMap<>();
    protected final Map<Integer, Boolean> blockPickupCache = new HashMap<>();
    protected final Map<Integer, List<Integer>> petsCanConsumeCache = new HashMap<>();
    protected final Map<Integer, Boolean> logoutExpireCache = new HashMap<>();
    protected final Map<Integer, List<Pair<Integer, Integer>>> summonMobCache = new HashMap<>();
    protected final Map<Integer, Map<Integer, Map<String, Integer>>> equipIncsCache = new HashMap<>();
    protected final Map<Integer, Map<Integer, List<Integer>>> equipSkillsCache = new HashMap<>();
    protected final Map<Integer, Pair<Integer, List<StructRewardItem>>> RewardItem = new HashMap<>();
    protected final Map<Byte, StructSetItem> setItems = new HashMap<>();
    protected final Map<Integer, Pair<Integer, List<Integer>>> questItems = new HashMap<>();
    protected Map<Integer, MapleInventoryType> inventoryTypeCache = new HashMap<>();
    protected final Map<Integer, Map<Integer, StructItemOption>> socketCache = new HashMap<>(); // Grade, (id, data)
    protected final Map<String, List<Triple<String, Vector, Vector>>> afterImage = new HashMap<>();
    protected final Map<Integer, Integer> mobIds = new HashMap<>();
    protected final Map<Integer, ItemInformation> dataCache = new HashMap<>();
    protected final Map<Integer, Triple<Integer, List<Integer>, List<Integer>>> monsterBookSets = new HashMap<>();

    protected MapleItemInformationProvider() {
        // LOGGER.debug("加载 物品信息 :::");
    }

    /* public final void load() {
        if (setItems.size() != 0 || potentialCache.size() != 0) {
            return;
        }
        getAllItems();
    }*/
    public void runEtc() {
        if (!setItems.isEmpty() || !potentialCache.isEmpty() || !socketCache.isEmpty()) {
            return;
        }
        /* List<Triple<String, Point, Point>> thePointK = new ArrayList<>();
        List<Triple<String, Point, Point>> thePointA = new ArrayList<>();
        
        final MapleDataDirectoryEntry a = (MapleDataDirectoryEntry) chrData.getRoot().getEntry("Afterimage");
        for (MapleDataEntry b : a.getFiles()) {
            final MapleData iz = chrData.getData("Afterimage/" + b.getName());
            List<Triple<String, Point, Point>> thePoint = new ArrayList<>();
            Map<String, Pair<Point, Point>> dummy = new HashMap<>();
            for (MapleData i : iz) {
                for (MapleData xD : i) {
                    if (xD.getName().contains("prone") || xD.getName().contains("double") || xD.getName().contains("triple")) {
                        continue;
                    }
                    if ((b.getName().contains("bow") || b.getName().contains("Bow")) && !xD.getName().contains("shoot")) {
                        continue;
                    }
                    if ((b.getName().contains("gun") || b.getName().contains("cannon")) && !xD.getName().contains("shot")) {
                        continue;
                    }
                    if (dummy.containsKey(xD.getName())) {
                        if (xD.getChildByPath("lt") != null) {
                            Point lt = (Point) xD.getChildByPath("lt").getData();
                            Point ourLt = dummy.get(xD.getName()).left;
                            if (lt.x < ourLt.x) {
                                ourLt.x = lt.x;
                            }
                            if (lt.y < ourLt.y) {
                                ourLt.y = lt.y;
                            }
                        }
                        if (xD.getChildByPath("rb") != null) {
                            Point rb = (Point) xD.getChildByPath("rb").getData();
                            Point ourRb = dummy.get(xD.getName()).right;
                            if (rb.x > ourRb.x) {
                                ourRb.x = rb.x;
                            }
                            if (rb.y > ourRb.y) {
                                ourRb.y = rb.y;
                            }
                        }
                    } else {
                        Point lt = null, rb = null;
                        if (xD.getChildByPath("lt") != null) {
                            lt = (Point) xD.getChildByPath("lt").getData();
                        }
                        if (xD.getChildByPath("rb") != null) {
                            rb = (Point) xD.getChildByPath("rb").getData();
                        }
                        dummy.put(xD.getName(), new Pair<>(lt, rb));
                    }
                }
            }
            for (Entry<String, Pair<Point, Point>> ez : dummy.entrySet()) {
                if (ez.getKey().length() > 2 && ez.getKey().substring(ez.getKey().length() - 2, ez.getKey().length() - 1).equals("D")) { //D = double weapon
                    thePointK.add(new Triple<>(ez.getKey(), ez.getValue().left, ez.getValue().right));
                } else if (ez.getKey().contains("PoleArm")) { //D = double weapon
                    thePointA.add(new Triple<>(ez.getKey(), ez.getValue().left, ez.getValue().right));
                } else {
                    thePoint.add(new Triple<>(ez.getKey(), ez.getValue().left, ez.getValue().right));
                }
            }
            afterImage.put(b.getName().substring(0, b.getName().length() - 4), thePoint);
        }
        afterImage.put("katara", thePointK); //hackish
        afterImage.put("aran", thePointA); //hackish*/
    }

    public void runItems() {
        try {
            Connection con = DatabaseConnection.getConnection();

            // Load Item Data
            PreparedStatement ps = con.prepareStatement("SELECT * FROM wz_itemdata");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                initItemInformation(rs);
            }
            rs.close();
            ps.close();

            // Load Item Equipment Data
            ps = con.prepareStatement("SELECT * FROM wz_itemequipdata ORDER BY itemid");
            rs = ps.executeQuery();
            while (rs.next()) {
                initItemEquipData(rs);
            }
            rs.close();
            ps.close();

            // Load Item Addition Data
            ps = con.prepareStatement("SELECT * FROM wz_itemadddata ORDER BY itemid");
            rs = ps.executeQuery();
            while (rs.next()) {
                initItemAddData(rs);
            }
            rs.close();
            ps.close();

            // Load Item Reward Data
            ps = con.prepareStatement("SELECT * FROM wz_itemrewarddata ORDER BY itemid");
            rs = ps.executeQuery();
            while (rs.next()) {
                initItemRewardData(rs);
            }
            rs.close();
            ps.close();

            // Finalize all Equipments
            for (Entry<Integer, ItemInformation> entry : dataCache.entrySet()) {
                if (GameConstants.getInventoryType(entry.getKey()) == MapleInventoryType.EQUIP) {
                    finalizeEquipData(entry.getValue());
                }
            }
        } catch (SQLException ex) {
            LOGGER.error("加载道具数据出错" + ex);
        }
        LOGGER.debug("共加载" + dataCache.size() + " 个道具信息");
    }

    public void finalizeEquipData(ItemInformation item) {
        int itemId = item.itemId;

        // Some equips do not have equip data. So we initialize it anyway if not initialized
        // already
        // Credits: Jay :)
        if (item.equipStats == null) {
            item.equipStats = new HashMap<>();
        }

        item.eq = new Equip(itemId, (byte) 0, -1, (byte) 0);
        short stats = GameConstants.getStat(itemId, 0);
        if (stats > 0) {
            item.eq.setStr(stats);
            item.eq.setDex(stats);
            item.eq.setInt(stats);
            item.eq.setLuk(stats);
        }
        stats = GameConstants.getATK(itemId, 0);
        if (stats > 0) {
            item.eq.setWatk(stats);
            item.eq.setMatk(stats);
        }
        stats = GameConstants.getHpMp(itemId, 0);
        if (stats > 0) {
            item.eq.setHp(stats);
            item.eq.setMp(stats);
        }
        stats = GameConstants.getDEF(itemId, 0);
        if (stats > 0) {
            item.eq.setWdef(stats);
            item.eq.setMdef(stats);
        }
        if (item.equipStats.size() > 0) {
            for (Entry<String, Integer> stat : item.equipStats.entrySet()) {
                final String key = stat.getKey();
                switch (key) {
                    case "STR":
                        item.eq.setStr(GameConstants.getStat(itemId, stat.getValue()));
                        break;
                    case "DEX":
                        item.eq.setDex(GameConstants.getStat(itemId, stat.getValue()));
                        break;
                    case "INT":
                        item.eq.setInt(GameConstants.getStat(itemId, stat.getValue()));
                        break;
                    case "LUK":
                        item.eq.setLuk(GameConstants.getStat(itemId, stat.getValue()));
                        break;
                    case "PAD":
                        item.eq.setWatk(GameConstants.getATK(itemId, stat.getValue()));
                        break;
                    case "PDD":
                        item.eq.setWdef(GameConstants.getDEF(itemId, stat.getValue()));
                        break;
                    case "MAD":
                        item.eq.setMatk(GameConstants.getATK(itemId, stat.getValue()));
                        break;
                    case "MDD":
                        item.eq.setMdef(GameConstants.getDEF(itemId, stat.getValue()));
                        break;
                    case "ACC":
                        item.eq.setAcc((short) stat.getValue().intValue());
                        break;
                    case "EVA":
                        item.eq.setAvoid((short) stat.getValue().intValue());
                        break;
                    case "Speed":
                        item.eq.setSpeed((short) stat.getValue().intValue());
                        break;
                    case "Jump":
                        item.eq.setJump((short) stat.getValue().intValue());
                        break;
                    case "MHP":
                        item.eq.setHp(GameConstants.getHpMp(itemId, stat.getValue()));
                        break;
                    case "MMP":
                        item.eq.setMp(GameConstants.getHpMp(itemId, stat.getValue()));
                        break;
                    case "tuc":
                        item.eq.setUpgradeSlots(stat.getValue().byteValue());
                        break;
                    case "Craft":
                        item.eq.setHands(stat.getValue().shortValue());
                        break;
                    case "durability":
                        item.eq.setDurability(stat.getValue());
                        break;
                    case "charmEXP":
                        item.eq.setCharmEXP(stat.getValue().shortValue());
                        break;
                    case "PVPDamage":
                        item.eq.setPVPDamage(stat.getValue().shortValue());
                        break;
                }
            }
            if (item.equipStats.get("cash") != null && item.eq.getCharmEXP() <= 0) { //set the exp
                short exp = 0;
                int identifier = itemId / 10000;
                if (GameConstants.isWeapon(itemId) || identifier == 106) { //weapon overall
                    exp = 60;
                } else if (identifier == 100) { //hats
                    exp = 50;
                } else if (GameConstants.isAccessory(itemId) || identifier == 102 || identifier == 108 || identifier == 107) { //gloves shoes accessory
                    exp = 40;
                } else if (identifier == 104 || identifier == 105 || identifier == 110) { //top bottom cape
                    exp = 30;
                }
                item.eq.setCharmEXP(exp);
            }
        }
    }

    public void initItemRewardData(ResultSet sqlRewardData) throws SQLException {
        final int itemID = sqlRewardData.getInt("itemid");
        if (tmpInfo == null || tmpInfo.itemId != itemID) {
            if (!dataCache.containsKey(itemID)) {
                LOGGER.debug("[initItemRewardData] Tried to load an item while this is not in the cache: " + itemID);
                return;
            }
            tmpInfo = dataCache.get(itemID);
        }

        if (tmpInfo.rewardItems == null) {
            tmpInfo.rewardItems = new ArrayList<>();
        }

        StructRewardItem add = new StructRewardItem();
        add.itemid = sqlRewardData.getInt("item");
        add.period = (add.itemid == 1122017 ? Math.max(sqlRewardData.getInt("period"), 7200) : sqlRewardData.getInt("period"));
        add.prob = (short) sqlRewardData.getInt("prob");
        add.quantity = sqlRewardData.getShort("quantity");
        add.worldmsg = sqlRewardData.getString("worldMsg").length() <= 0 ? null : sqlRewardData.getString("worldMsg");
        add.effect = sqlRewardData.getString("effect");

        tmpInfo.rewardItems.add(add);
    }

    public void initItemAddData(ResultSet sqlAddData) throws SQLException {
        final int itemID = sqlAddData.getInt("itemid");
        if (tmpInfo == null || tmpInfo.itemId != itemID) {
            if (!dataCache.containsKey(itemID)) {
                LOGGER.debug("[initItemAddData] Tried to load an item while this is not in the cache: " + itemID);
                return;
            }
            tmpInfo = dataCache.get(itemID);
        }

        if (tmpInfo.equipAdditions == null) {
            tmpInfo.equipAdditions = new LinkedList<>();
        }

        while (sqlAddData.next()) {
            tmpInfo.equipAdditions.add(new Triple<>(sqlAddData.getString("key"), sqlAddData.getString("subKey"), sqlAddData.getString("value")));
        }
    }

    private ItemInformation tmpInfo = null;

    public void initItemEquipData(ResultSet sqlEquipData) throws SQLException {
        final int itemID = sqlEquipData.getInt("itemid");
        if (tmpInfo == null || tmpInfo.itemId != itemID) {
            if (!dataCache.containsKey(itemID)) {
                LOGGER.debug("[initItemEquipData] Tried to load an item while this is not in the cache: " + itemID);
                return;
            }
            tmpInfo = dataCache.get(itemID);
        }

        if (tmpInfo.equipStats == null) {
            tmpInfo.equipStats = new HashMap<>();
        }

        final int itemLevel = sqlEquipData.getInt("itemLevel");
        if (itemLevel == -1) {
            tmpInfo.equipStats.put(sqlEquipData.getString("key"), sqlEquipData.getInt("value"));
        } else {
            if (tmpInfo.equipIncs == null) {
                tmpInfo.equipIncs = new HashMap<>();
            }

            Map<String, Integer> toAdd = tmpInfo.equipIncs.computeIfAbsent(itemLevel, k -> new HashMap<>());
            toAdd.put(sqlEquipData.getString("key"), sqlEquipData.getInt("value"));
        }
    }

    public void initItemInformation(ResultSet sqlItemData) throws SQLException {
        final ItemInformation ret = new ItemInformation();
        final int itemId = sqlItemData.getInt("itemid");
        ret.itemId = itemId;
        ret.slotMax = GameConstants.getSlotMax(itemId) > 0 ? GameConstants.getSlotMax(itemId) : sqlItemData.getShort("slotMax");
        ret.price = Double.parseDouble(sqlItemData.getString("price"));
        ret.wholePrice = sqlItemData.getInt("wholePrice");
        ret.stateChange = sqlItemData.getInt("stateChange");
        ret.name = sqlItemData.getString("name");
        ret.desc = sqlItemData.getString("desc");
        ret.msg = sqlItemData.getString("msg");

        ret.flag = sqlItemData.getInt("flags");

        ret.karmaEnabled = sqlItemData.getByte("karma");
        ret.meso = sqlItemData.getInt("meso");
        ret.monsterBook = sqlItemData.getInt("monsterBook");
        ret.itemMakeLevel = sqlItemData.getShort("itemMakeLevel");
        ret.questId = sqlItemData.getInt("questId");
        ret.create = sqlItemData.getInt("create");
        ret.replaceItem = sqlItemData.getInt("replaceId");
        ret.replaceMsg = sqlItemData.getString("replaceMsg");
        ret.afterImage = sqlItemData.getString("afterImage");
        ret.cardSet = 0;
        if (ret.monsterBook > 0 && itemId / 10000 == 238) {
            mobIds.put(ret.monsterBook, itemId);
            for (Entry<Integer, Triple<Integer, List<Integer>, List<Integer>>> set : monsterBookSets.entrySet()) {
                if (set.getValue().mid.contains(itemId)) {
                    ret.cardSet = set.getKey();
                    break;
                }
            }
        }

        final String scrollRq = sqlItemData.getString("scrollReqs");
        if (scrollRq.length() > 0) {
            ret.scrollReqs = new ArrayList<>();
            final String[] scroll = scrollRq.split(",");
            for (String s : scroll) {
                if (s.length() > 1) {
                    ret.scrollReqs.add(Integer.parseInt(s));
                }
            }
        }
        final String consumeItem = sqlItemData.getString("consumeItem");
        if (consumeItem.length() > 0) {
            ret.questItems = new ArrayList<>();
            final String[] scroll = scrollRq.split(",");
            for (String s : scroll) {
                if (s.length() > 1) {
                    ret.questItems.add(Integer.parseInt(s));
                }
            }
        }

        ret.totalprob = sqlItemData.getInt("totalprob");
        final String incRq = sqlItemData.getString("incSkill");
        if (incRq.length() > 0) {
            ret.incSkill = new ArrayList<>();
            final String[] scroll = incRq.split(",");
            for (String s : scroll) {
                if (s.length() > 1) {
                    ret.incSkill.add(Integer.parseInt(s));
                }
            }
        }
        dataCache.put(itemId, ret);
    }

    public final List<StructPotentialItem> getPotentialInfo(final int potId) {
        return potentialCache.get(potId);
    }

    public final Map<Integer, List<StructPotentialItem>> getAllPotentialInfo() {
        return potentialCache;
    }

    public static MapleItemInformationProvider getInstance() {
        return instance;
    }

    /*public final List<Pair<Integer, String>> getAllItems() {
        if (itemNameCache.size() != 0) {
            return itemNameCache;
        }
        final List<Pair<Integer, String>> itemPairs = new ArrayList<Pair<Integer, String>>();
        MapleData itemsData;

        itemsData = stringData.getData("Cash.img");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }

        itemsData = stringData.getData("Consume.img");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }

        itemsData = stringData.getData("Eqp.img").getChildByPath("Eqp");
        for (final MapleData eqpType : itemsData.getChildren()) {
            for (final MapleData itemFolder : eqpType.getChildren()) {
                itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
            }
        }

        itemsData = stringData.getData("Etc.img").getChildByPath("Etc");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }

        itemsData = stringData.getData("Ins.img");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }

        itemsData = stringData.getData("Pet.img");
        for (final MapleData itemFolder : itemsData.getChildren()) {
            itemPairs.add(new Pair<Integer, String>(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME")));
        }
        return itemPairs;
    }*/
    protected final WzElement<?> getStringData(final int itemId) {
        String cat = null;
        Optional<WzFile> data;

        if (itemId >= 5010000) {
            data = WzData.STRING.directory().findFile("Cash.img");
        } else if (itemId >= 2000000 && itemId < 3000000) {
            data = WzData.STRING.directory().findFile("Consume.img");
        } else if ((itemId >= 1142000 && itemId < 1143000) || (itemId >= 1010000 && itemId < 1040000) || (itemId >= 1122000 && itemId < 1123000)) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Accessory";
        } else if (itemId >= 1000000 && itemId < 1010000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Cap";
        } else if (itemId >= 1102000 && itemId < 1103000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Cape";
        } else if (itemId >= 1040000 && itemId < 1050000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Coat";
        } else if (itemId >= 20000 && itemId < 25000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Face";
        } else if (itemId >= 1080000 && itemId < 1090000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Glove";
        } else if (itemId >= 30000 && itemId < 40000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Hair";
        } else if (itemId >= 1050000 && itemId < 1060000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Longcoat";
        } else if (itemId >= 1060000 && itemId < 1070000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Pants";
        } else if (itemId >= 1610000 && itemId < 1660000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Mechanic";
        } else if (itemId >= 1802000 && itemId < 1810000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "PetEquip";
        } else if (itemId >= 1920000 && itemId < 2000000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Dragon";
        } else if (itemId >= 1112000 && itemId < 1120000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Ring";
        } else if (itemId >= 1092000 && itemId < 1100000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Shield";
        } else if (itemId >= 1070000 && itemId < 1080000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Shoes";
        } else if (itemId >= 1900000 && itemId < 1920000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Taming";
        } else if (itemId >= 1300000 && itemId < 1800000) {
            data = WzData.STRING.directory().findFile("Eqp.img");
            cat = "Weapon";
        } else if (itemId >= 4000000 && itemId < 5000000) {
            data = WzData.STRING.directory().findFile("Etc.img");
        } else if (itemId >= 3000000 && itemId < 4000000) {
            data = WzData.STRING.directory().findFile("Ins.img");
            ;
        } else if (itemId >= 5000000/* && itemId < 5010000*/) {
            data = WzData.STRING.directory().findFile("Pet.img");
        } else {
            return null;
        }
        if (cat == null) {
            return data.map(WzFile::content)
                    .map(it -> it.find(String.valueOf(itemId)))
                    .orElse(null);
        } else {
            String finalCat = cat;
            return data.map(WzFile::content)
                    .flatMap(it -> it.findByName("Eqp/" + finalCat + "/" + itemId))
                    .orElse(null);
        }
    }

    protected final WzElement<?> getItemData(final int itemId) {
        String idStr = "0" + itemId;
        Optional<ImgdirElement> first = WzData.ITEM.directory().dirStream()
                .map(dir -> dir.findFile(idStr.substring(0, 4))
                        .map(file -> file.content().find(idStr))
                        .map(element -> ((ImgdirElement) element))
                        .orElseGet(() -> dir.findFile(idStr.substring(1))
                                .map(WzFile::content)
                                .orElse(null)))
                .findFirst();
        return first.orElseGet(() -> WzData.CHARACTER.directory().dirStream()
                .map(dir -> dir.findFile(idStr)
                        .map(WzFile::content)
                        .orElse(null))
                .findFirst()
                .orElse(null));
    }

    /**
     * returns the maximum of items in one slot
     */
    public final short getSlotMax(final MapleClient c, final int itemId) {
        if (slotMaxCache.containsKey(itemId)) {
            return slotMaxCache.get(itemId);
        }
        short ret = 0;
        WzElement<?> item = getItemData(itemId);
        if (item != null) {
            ret = item.findByName("info/slotMax")
                    .map(Elements::ofInt)
                    .map(Integer::shortValue)
                    .orElseGet(() -> {
                        if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                            return (short) 1;
                        } else {
                            return (short) 100;
                        }
                    });
        }
        slotMaxCache.put(itemId, ret);
        return ret;
    }

    public final int getWholePrice(final int itemId) {
        if (wholePriceCache.containsKey(itemId)) {
            return wholePriceCache.get(itemId);
        }
        WzElement<?> item = getItemData(itemId);
        if (item == null) {
            return -1;
        }
        int pEntry = Elements.findInt(item, "info/price", -1);
        wholePriceCache.put(itemId, pEntry);
        return pEntry;
    }

    public final double getPrice(final int itemId) {

        if (priceCache.containsKey(itemId)) {
            return priceCache.get(itemId);
        }
        final WzElement<?> item = getItemData(itemId);
        if (item == null) {
            return -1;
        }
        double pEntry = item.findByName("info")
                .map(element -> Elements.findDouble(element, "unitPrice"))
                .orElseGet(() -> item.findByName("info")
                        .map(element -> Elements.findDouble(element, "price"))
                        .orElse((double) -1));
        if (itemId == 2070019 || itemId == 2330007) {
            pEntry = 1.0;
        }
        priceCache.put(itemId, pEntry);
        return pEntry;
    }

    public final Map<String, Byte> getItemMakeStats(final int itemId) {
        if (itemMakeStatsCache.containsKey(itemId)) {
            return itemMakeStatsCache.get(itemId);
        }
        if (itemId / 10000 != 425) {
            return null;
        }
        final Map<String, Byte> ret = new LinkedHashMap<String, Byte>();
        final WzElement<?> item = getItemData(itemId);
        if (item == null) {
            return null;
        }
        WzElement<?> info = item.find("info");
        if (info == null) {
            return null;
        }
        ret.put("incPAD", (byte) Elements.findInt(info, "incPAD"));
        ret.put("incMAD", (byte) Elements.findInt(info, "incMAD"));
        ret.put("incACC", (byte) Elements.findInt(info, "incACC"));
        ret.put("incEVA", (byte) Elements.findInt(info, "incEVA"));
        ret.put("incSpeed", (byte) Elements.findInt(info, "incSpeed"));
        ret.put("incJump", (byte) Elements.findInt(info, "incJump"));
        ret.put("incMaxHP", (byte) Elements.findInt(info, "incMaxHP"));
        ret.put("incMaxMP", (byte) Elements.findInt(info, "incMaxMP"));
        ret.put("incSTR", (byte) Elements.findInt(info, "incSTR"));
        ret.put("incINT", (byte) Elements.findInt(info, "incINT"));
        ret.put("incLUK", (byte) Elements.findInt(info, "incLUK"));
        ret.put("incDEX", (byte) Elements.findInt(info, "incDEX"));
        ret.put("randOption", (byte) Elements.findInt(info, "randOption"));
        ret.put("randStat", (byte) Elements.findInt(info, "randStat"));
        itemMakeStatsCache.put(itemId, ret);
        return ret;
    }

    private int rand(int min, int max) {
        return Math.abs(Randomizer.rand(min, max));
    }

    public Equip levelUpEquip(Equip equip, Map<String, Integer> sta) {
        Equip nEquip = (Equip) equip.copy();
        //is this all the stats?
        try {
            for (Entry<String, Integer> stat : sta.entrySet()) {
                switch (stat.getKey()) {
                    case "STRMin":
                        nEquip.setStr((short) (nEquip.getStr() + rand(stat.getValue(), sta.get("STRMax"))));
                        break;
                    case "DEXMin":
                        nEquip.setDex((short) (nEquip.getDex() + rand(stat.getValue(), sta.get("DEXMax"))));
                        break;
                    case "INTMin":
                        nEquip.setInt((short) (nEquip.getInt() + rand(stat.getValue(), sta.get("INTMax"))));
                        break;
                    case "LUKMin":
                        nEquip.setLuk((short) (nEquip.getLuk() + rand(stat.getValue(), sta.get("LUKMax"))));
                        break;
                    case "PADMin":
                        nEquip.setWatk((short) (nEquip.getWatk() + rand(stat.getValue(), sta.get("PADMax"))));
                        break;
                    case "PDDMin":
                        nEquip.setWdef((short) (nEquip.getWdef() + rand(stat.getValue(), sta.get("PDDMax"))));
                        break;
                    case "MADMin":
                        nEquip.setMatk((short) (nEquip.getMatk() + rand(stat.getValue(), sta.get("MADMax"))));
                        break;
                    case "MDDMin":
                        nEquip.setMdef((short) (nEquip.getMdef() + rand(stat.getValue(), sta.get("MDDMax"))));
                        break;
                    case "ACCMin":
                        nEquip.setAcc((short) (nEquip.getAcc() + rand(stat.getValue(), sta.get("ACCMax"))));
                        break;
                    case "EVAMin":
                        nEquip.setAvoid((short) (nEquip.getAvoid() + rand(stat.getValue(), sta.get("EVAMax"))));
                        break;
                    case "SpeedMin":
                        nEquip.setSpeed((short) (nEquip.getSpeed() + rand(stat.getValue(), sta.get("SpeedMax"))));
                        break;
                    case "JumpMin":
                        nEquip.setJump((short) (nEquip.getJump() + rand(stat.getValue(), sta.get("JumpMax"))));
                        break;
                    case "MHPMin":
                        nEquip.setHp((short) (nEquip.getHp() + rand(stat.getValue(), sta.get("MHPMax"))));
                        break;
                    case "MMPMin":
                        nEquip.setMp((short) (nEquip.getMp() + rand(stat.getValue(), sta.get("MMPMax"))));
                        break;
                    case "MaxHPMin":
                        nEquip.setHp((short) (nEquip.getHp() + rand(stat.getValue(), sta.get("MaxHPMax"))));
                        break;
                    case "MaxMPMin":
                        nEquip.setMp((short) (nEquip.getMp() + rand(stat.getValue(), sta.get("MaxMPMax"))));
                        break;
                }
            }
        } catch (NullPointerException e) {
            //catch npe because obviously the wz have some error XD
            e.printStackTrace();
        }
        return nEquip;
    }

    public final Map<Integer, Map<String, Integer>> getEquipIncrements(final int itemId) {
        Map<Integer, Map<String, Integer>> dataMap = equipIncsCache.get(itemId);
        if (dataMap != null) {
            return dataMap;
        }

        WzElement<?> item = getItemData(itemId);
        if (item == null) {
            return Collections.emptyMap();
        }

        Map<Integer, Map<String, Integer>> ret = new LinkedHashMap<>();
        item.findByName("info/level/info")
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(dat -> {
                    Map<String, Integer> incs = dat.childrenStream()
                            .filter(data -> data.name().length() > 3)
                            .collect(Collectors.toMap(data -> data.name().substring(3), Elements::ofInt));
                    ret.put(Numbers.ofInt(dat.name()), incs);
                }));
        equipIncsCache.put(itemId, ret);
        return ret;
    }

    public final Map<Integer, List<Integer>> getEquipSkills(final int itemId) {
        Map<Integer, List<Integer>> listMap = equipSkillsCache.get(itemId);
        if (listMap != null) {
            return listMap;
        }

        WzElement<?> item = getItemData(itemId);
        if (item == null) {
            return Collections.emptyMap();
        }

        Map<Integer, List<Integer>> ret = new LinkedHashMap<>();
        item.findByName("info/level/case")
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.flatMap(WzElement::childrenStream)
                        .filter(data -> data.name().length() == 1)
                        .forEach(data -> {
                            List<Integer> adds = data.findByName("Skill").map(it -> it.childrenStream()
                                            .map(element -> Elements.findInt(element, "id"))
                                            .collect(Collectors.toList()))
                                    .orElse(Collections.emptyList());
                            ret.put(Numbers.ofInt(data.name()), adds);
                        }));
        equipSkillsCache.put(itemId, ret);
        return ret;
    }

    public final Map<String, Integer> getEquipStats(final int itemId) {
        Map<String, Integer> map = equipStatsCache.get(itemId);
        if (map != null) {
            return map;
        }

        WzElement<?> item = getItemData(itemId);
        if (item == null) {
            return Collections.emptyMap();
        }

        Map<String, Integer> ret = new LinkedHashMap<>();
        item.findByName("info")
                .map(info -> {
                    peekInfo(ret, info);
                    if (GameConstants.isMagicWeapon(itemId)) {
                        ret.put("elemDefault", Elements.findInt(info, "elemDefault", 100));
                        ret.put("incRMAS", Elements.findInt(info, "incRMAS", 100));
                        ret.put("incRMAF", Elements.findInt(info, "incRMAF", 100));
                        ret.put("incRMAI", Elements.findInt(info, "incRMAI", 100));
                    }
                    return info;
                })
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.filter(element -> element.name().startsWith("inc"))
                        .forEach(element -> ret.put(element.name().substring(3), Elements.ofInt(element))));
        equipStatsCache.put(itemId, ret);
        return ret;
    }

    private void peekInfo(Map<String, Integer> ret, WzElement<?> info) {
        ret.put("tuc", Elements.findInt(info, "tuc"));
        ret.put("reqLevel", Elements.findInt(info, "reqLevel"));
        ret.put("reqJob", Elements.findInt(info, "reqJob"));
        ret.put("reqSTR", Elements.findInt(info, "reqSTR"));
        ret.put("reqDEX", Elements.findInt(info, "reqDEX"));
        ret.put("reqINT", Elements.findInt(info, "reqINT"));
        ret.put("reqLUK", Elements.findInt(info, "reqLUK"));
        ret.put("reqPOP", Elements.findInt(info, "reqPOP"));
        ret.put("cash", Elements.findInt(info, "cash"));
        ret.put("canLevel", info.findByName("level").map(element -> 1).orElse(0));
        ret.put("cursed", Elements.findInt(info, "cursed"));
        ret.put("success", Elements.findInt(info, "success"));
        ret.put("setItemID", Elements.findInt(info, "setItemID"));
        ret.put("equipTradeBlock", Elements.findInt(info, "equipTradeBlock"));
        ret.put("durability", Elements.findInt(info, "durability", -1));
    }

    public final boolean canEquip(Map<String, Integer> stats, int itemid, int level,
                                  int job, int fame, int str, int dex, int luk, int int_, int supremacy) {
        if ((level + supremacy) >= stats.get("reqLevel")
                && str >= stats.get("reqSTR")
                && dex >= stats.get("reqDEX")
                && luk >= stats.get("reqLUK")
                && int_ >= stats.get("reqINT")) {
            int fameReq = stats.get("reqPOP");
            return fameReq == 0 || fame >= fameReq;
        }
        return false;
    }

    public final int getReqLevel(final int itemId) {//判断装备等级
        if (getEquipStats(itemId).isEmpty()) {
            return 0;
        }
        return getEquipStats(itemId).get("reqLevel");
    }

    public final boolean isCashItem(final int itemId) {
        if (getEquipStats(itemId).isEmpty()) {
            return false;
        }
        return getEquipStats(itemId).get("cash") == 1;
    }

    public final int getSlots(final int itemId) {
        if (getEquipStats(itemId).isEmpty()) {
            return 0;
        }
        return getEquipStats(itemId).get("tuc");
    }

    public final int getSetItemID(final int itemId) {
        if (getEquipStats(itemId).isEmpty()) {
            return 0;
        }
        return getEquipStats(itemId).get("setItemID");
    }

    public final StructSetItem getSetItem(final int setItemId) {
        return setItems.get((byte) setItemId);
    }

    public final List<Integer> getScrollReqs(final int itemId) {
        List<Integer> list = scrollReqCache.get(itemId);
        if (list != null) {
            return list;
        }

        WzElement<?> itemData = getItemData(itemId);
        List<Integer> ret = Optional.ofNullable(itemData)
                .map(element -> element.find("req"))
                .map(WzElement::childrenStream)
                .map(stream -> stream.map(Elements::ofInt).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        scrollReqCache.put(itemId, ret);
        return ret;
    }

    public final IItem scrollEquipWithId(final IItem equip, final IItem scrollId, final boolean ws, final MapleCharacter chr, final int vegas, boolean checkIfGM) {
        if (equip.getType() == 1) { // See IItem.java
            final Equip nEquip = (Equip) equip;
            final Map<String, Integer> stats = getEquipStats(scrollId.getItemId());
            final Map<String, Integer> eqstats = getEquipStats(equip.getItemId());
            final int succ = (GameConstants.isTablet(scrollId.getItemId())
                    ? GameConstants.getSuccessTablet(scrollId.getItemId(), nEquip.getLevel())
                    : ((GameConstants.isEquipScroll(scrollId.getItemId())
                    || GameConstants.isPotentialScroll(scrollId.getItemId()) ? 0 : stats.get("success"))));
            final int curse = (GameConstants.isTablet(scrollId.getItemId())
                    ? GameConstants.getCurseTablet(scrollId.getItemId(), nEquip.getLevel())
                    : ((GameConstants.isEquipScroll(scrollId.getItemId())
                    || GameConstants.isPotentialScroll(scrollId.getItemId()) ? 0 : stats.get("cursed"))));
            int success = succ + (vegas == 5610000 && succ == 10 ? 20 : (vegas == 5610001 && succ == 60 ? 30 : 0));
            if (GameConstants.isPotentialScroll(scrollId.getItemId())
                    || GameConstants.isEquipScroll(scrollId.getItemId())
                    || Randomizer.nextInt(100) <= success
                    || checkIfGM) {
                switch (scrollId.getItemId()) {
                    case 2049000:
                    case 2049001:
                    case 2049002:
                    case 2049003:
                    case 2049004:
                    case 2049005: {
                        if (nEquip.getLevel() + nEquip.getUpgradeSlots() < eqstats.get("tuc")) {
                            nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 1));
                        }
                        break;
                    }
                    case 2049006:
                    case 2049007:
                    case 2049008: {
                        if (nEquip.getLevel() + nEquip.getUpgradeSlots() < eqstats.get("tuc")) {
                            nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() + 2));
                        }
                        break;
                    }
                    case 2040727: // Spikes on shoe, prevents slip
                    {
                        byte flag = nEquip.getFlag();
                        flag |= ItemFlag.SPIKES.getValue();
                        nEquip.setFlag(flag);
                        break;
                    }
                    case 2041058: // Cape for Cold protection
                    {
                        byte flag = nEquip.getFlag();
                        flag |= ItemFlag.COLD.getValue();
                        nEquip.setFlag(flag);
                        break;
                    }
                    default: {
                        if (GameConstants.isChaosScroll(scrollId.getItemId())) {
                            final int z = GameConstants.getChaosNumber(scrollId.getItemId());
                            if (nEquip.getStr() > 0) {
                                nEquip.setStr((short) (nEquip.getStr() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getDex() > 0) {
                                nEquip.setDex((short) (nEquip.getDex() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getInt() > 0) {
                                nEquip.setInt((short) (nEquip.getInt() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getLuk() > 0) {
                                nEquip.setLuk((short) (nEquip.getLuk() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getWatk() > 0) {
                                nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getWdef() > 0) {
                                nEquip.setWdef((short) (nEquip.getWdef() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getMatk() > 0) {
                                nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getMdef() > 0) {
                                nEquip.setMdef((short) (nEquip.getMdef() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getAcc() > 0) {
                                nEquip.setAcc((short) (nEquip.getAcc() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getAvoid() > 0) {
                                nEquip.setAvoid((short) (nEquip.getAvoid() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getSpeed() > 0) {
                                nEquip.setSpeed((short) (nEquip.getSpeed() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getJump() > 0) {
                                nEquip.setJump((short) (nEquip.getJump() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getHp() > 0) {
                                nEquip.setHp((short) (nEquip.getHp() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            if (nEquip.getMp() > 0) {
                                nEquip.setMp((short) (nEquip.getMp() + Randomizer.nextInt(z) * (Randomizer.nextBoolean() ? 1 : -1)));
                            }
                            break;
                        } else if (GameConstants.isEquipScroll(scrollId.getItemId())) {
                            final int chanc = Math.max((scrollId.getItemId() == 2049300 ? 100 : 80) - (nEquip.getEnhance() * 10), 10);
                            if (Randomizer.nextInt(100) > chanc) {
                                return null; //destroyed, nib
                            }
                            if (nEquip.getStr() > 0 || Randomizer.nextInt(50) == 1) { //1/50
                                nEquip.setStr((short) (nEquip.getStr() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getDex() > 0 || Randomizer.nextInt(50) == 1) { //1/50
                                nEquip.setDex((short) (nEquip.getDex() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getInt() > 0 || Randomizer.nextInt(50) == 1) { //1/50
                                nEquip.setInt((short) (nEquip.getInt() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getLuk() > 0 || Randomizer.nextInt(50) == 1) { //1/50
                                nEquip.setLuk((short) (nEquip.getLuk() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getWatk() > 0 && GameConstants.isWeapon(nEquip.getItemId())) {
                                nEquip.setWatk((short) (nEquip.getWatk() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getWdef() > 0 || Randomizer.nextInt(40) == 1) { //1/40
                                nEquip.setWdef((short) (nEquip.getWdef() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getMatk() > 0 && GameConstants.isWeapon(nEquip.getItemId())) {
                                nEquip.setMatk((short) (nEquip.getMatk() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getMdef() > 0 || Randomizer.nextInt(40) == 1) { //1/40
                                nEquip.setMdef((short) (nEquip.getMdef() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getAcc() > 0 || Randomizer.nextInt(20) == 1) { //1/20
                                nEquip.setAcc((short) (nEquip.getAcc() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getAvoid() > 0 || Randomizer.nextInt(20) == 1) { //1/20
                                nEquip.setAvoid((short) (nEquip.getAvoid() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getSpeed() > 0 || Randomizer.nextInt(10) == 1) { //1/10
                                nEquip.setSpeed((short) (nEquip.getSpeed() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getJump() > 0 || Randomizer.nextInt(10) == 1) { //1/10
                                nEquip.setJump((short) (nEquip.getJump() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getHp() > 0 || Randomizer.nextInt(5) == 1) { //1/5
                                nEquip.setHp((short) (nEquip.getHp() + Randomizer.nextInt(5)));
                            }
                            if (nEquip.getMp() > 0 || Randomizer.nextInt(5) == 1) { //1/5
                                nEquip.setMp((short) (nEquip.getMp() + Randomizer.nextInt(5)));
                            }
                            nEquip.setEnhance((byte) (nEquip.getEnhance() + 1));
                            break;
                        } else if (GameConstants.isPotentialScroll(scrollId.getItemId())) {
                            if (nEquip.getState() == 0) {
                                final int chanc = scrollId.getItemId() == 2049400 ? 90 : 70;
                                if (Randomizer.nextInt(100) > chanc) {
                                    return null; //destroyed, nib
                                }
                                nEquip.resetPotential();
                            }
                            break;
                        } else {
                            for (Entry<String, Integer> stat : stats.entrySet()) {
                                final String key = stat.getKey();

                                switch (key) {
                                    case "STR":
                                        nEquip.setStr((short) (nEquip.getStr() + stat.getValue()));
                                        break;
                                    case "DEX":
                                        nEquip.setDex((short) (nEquip.getDex() + stat.getValue()));
                                        break;
                                    case "INT":
                                        nEquip.setInt((short) (nEquip.getInt() + stat.getValue()));
                                        break;
                                    case "LUK":
                                        nEquip.setLuk((short) (nEquip.getLuk() + stat.getValue()));
                                        break;
                                    case "PAD":
                                        nEquip.setWatk((short) (nEquip.getWatk() + stat.getValue()));
                                        break;
                                    case "PDD":
                                        nEquip.setWdef((short) (nEquip.getWdef() + stat.getValue()));
                                        break;
                                    case "MAD":
                                        nEquip.setMatk((short) (nEquip.getMatk() + stat.getValue()));
                                        break;
                                    case "MDD":
                                        nEquip.setMdef((short) (nEquip.getMdef() + stat.getValue()));
                                        break;
                                    case "ACC":
                                        nEquip.setAcc((short) (nEquip.getAcc() + stat.getValue()));
                                        break;
                                    case "EVA":
                                        nEquip.setAvoid((short) (nEquip.getAvoid() + stat.getValue()));
                                        break;
                                    case "Speed":
                                        nEquip.setSpeed((short) (nEquip.getSpeed() + stat.getValue()));
                                        break;
                                    case "Jump":
                                        nEquip.setJump((short) (nEquip.getJump() + stat.getValue()));
                                        break;
                                    case "MHP":
                                        nEquip.setHp((short) (nEquip.getHp() + stat.getValue()));
                                        break;
                                    case "MMP":
                                        nEquip.setMp((short) (nEquip.getMp() + stat.getValue()));
                                        break;
                                    case "MHPr":
                                        nEquip.setHpR((short) (nEquip.getHpR() + stat.getValue()));
                                        break;
                                    case "MMPr":
                                        nEquip.setMpR((short) (nEquip.getMpR() + stat.getValue()));
                                        break;
                                }
                            }
                            break;
                        }
                    }
                }
                if (!GameConstants.isCleanSlate(scrollId.getItemId()) && !GameConstants.isSpecialScroll(scrollId.getItemId()) && !GameConstants.isEquipScroll(scrollId.getItemId()) && !GameConstants.isPotentialScroll(scrollId.getItemId())) {
                    nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - 1));
                    nEquip.setLevel((byte) (nEquip.getLevel() + 1));
                }
            } else {
                if (!ws && !GameConstants.isCleanSlate(scrollId.getItemId()) && !GameConstants.isSpecialScroll(scrollId.getItemId()) && !GameConstants.isEquipScroll(scrollId.getItemId()) && !GameConstants.isPotentialScroll(scrollId.getItemId())) {
                    nEquip.setUpgradeSlots((byte) (nEquip.getUpgradeSlots() - 1));
                }
                if (Randomizer.nextInt(99) < curse) {
                    return null;
                }
            }
        }
        return equip;
    }

    public final IItem getEquipById(final int equipId) {
        return getEquipById(equipId, -1);
    }

    public final IItem getEquipById(final int equipId, final int ringId) {
        final Equip nEquip = new Equip(equipId, (byte) 0, ringId, (byte) 0);
        nEquip.setQuantity((short) 1);
        final Map<String, Integer> stats = getEquipStats(equipId);
        if (!stats.isEmpty()) {
            for (Entry<String, Integer> stat : stats.entrySet()) {
                final String key = stat.getKey();
                switch (key) {
                    case "STR":
                        nEquip.setStr((short) stat.getValue().intValue());
                        break;
                    case "DEX":
                        nEquip.setDex((short) stat.getValue().intValue());
                        break;
                    case "INT":
                        nEquip.setInt((short) stat.getValue().intValue());
                        break;
                    case "LUK":
                        nEquip.setLuk((short) stat.getValue().intValue());
                        break;
                    case "PAD":
                        nEquip.setWatk((short) stat.getValue().intValue());
                        break;
                    case "PDD":
                        nEquip.setWdef((short) stat.getValue().intValue());
                        break;
                    case "MAD":
                        nEquip.setMatk((short) stat.getValue().intValue());
                        break;
                    case "MDD":
                        nEquip.setMdef((short) stat.getValue().intValue());
                        break;
                    case "ACC":
                        nEquip.setAcc((short) stat.getValue().intValue());
                        break;
                    case "EVA":
                        nEquip.setAvoid((short) stat.getValue().intValue());
                        break;
                    case "Speed":
                        nEquip.setSpeed((short) stat.getValue().intValue());
                        break;
                    case "Jump":
                        nEquip.setJump((short) stat.getValue().intValue());
                        break;
                    case "MHP":
                        nEquip.setHp((short) stat.getValue().intValue());
                        break;
                    case "MMP":
                        nEquip.setMp((short) stat.getValue().intValue());
                        break;
                    case "MHPr":
                        nEquip.setHpR((short) stat.getValue().intValue());
                        break;
                    case "MMPr":
                        nEquip.setMpR((short) stat.getValue().intValue());
                        break;
                    case "tuc":
                        nEquip.setUpgradeSlots(stat.getValue().byteValue());
                        break;
                    case "Craft":
                        nEquip.setHands(stat.getValue().shortValue());
                        break;
                    case "durability":
                        nEquip.setDurability(stat.getValue());
//                } else if (key.equals("afterImage")) {
                        break;
                }
            }
        }
        equipCache.put(equipId, nEquip);
        return nEquip.copy();
    }

    private short getRandStat(final short defaultValue, final int maxRange) {
        if (defaultValue == 0) {
            return 0;
        }
        // vary no more than ceil of 10% of stat
        final int lMaxRange = (int) Math.min(Math.ceil(defaultValue * 0.1), maxRange);

        return (short) ((defaultValue - lMaxRange) + Math.floor(Math.random() * (lMaxRange * 2 + 1)));
    }

    public final Equip randomizeStats(final Equip equip) {
        equip.setStr(getRandStat(equip.getStr(), 5));
        equip.setDex(getRandStat(equip.getDex(), 5));
        equip.setInt(getRandStat(equip.getInt(), 5));
        equip.setLuk(getRandStat(equip.getLuk(), 5));
        equip.setMatk(getRandStat(equip.getMatk(), 5));
        equip.setWatk(getRandStat(equip.getWatk(), 5));
        equip.setAcc(getRandStat(equip.getAcc(), 5));
        equip.setAvoid(getRandStat(equip.getAvoid(), 5));
        equip.setJump(getRandStat(equip.getJump(), 5));
        equip.setHands(getRandStat(equip.getHands(), 5));
        equip.setSpeed(getRandStat(equip.getSpeed(), 5));
        equip.setWdef(getRandStat(equip.getWdef(), 10));
        equip.setMdef(getRandStat(equip.getMdef(), 10));
        equip.setHp(getRandStat(equip.getHp(), 10));
        equip.setMp(getRandStat(equip.getMp(), 10));
        return equip;
    }

    public final MapleStatEffect getItemEffect(int itemId) {
        MapleStatEffect ret = itemEffects.get(itemId);
        if (ret == null) {
            WzElement<?> item = getItemData(itemId);
            if (item == null) {
                return null;
            }

            WzElement<?> spec = item.find("spec");
            if (spec == null) {
                return null;
            }

            ret = Effects.ofItem(itemId, spec);
            itemEffects.put(itemId, ret);
        }
        return ret;
    }

    public final List<Pair<Integer, Integer>> getSummonMobs(final int itemId) {
        List<Pair<Integer, Integer>> list = summonMobCache.get(itemId);
        if (list != null) {
            return list;
        }
        if (!GameConstants.isSummonSack(itemId)) {
            return Collections.emptyList();
        }

        WzElement<?> itemData = getItemData(itemId);
        if (itemData == null) {
            return Collections.emptyList();
        }

        List<Pair<Integer, Integer>> mobPairs = itemData.findByName("mob")
                .map(WzElement::childrenStream)
                .map(stream -> stream.map(element -> {
                    Integer idInt = Elements.findInt(element, "id");
                    Integer probInt = Elements.findInt(element, "prob");
                    return new Pair<>(idInt, probInt);
                }).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        summonMobCache.put(itemId, mobPairs);
        return mobPairs;
    }

    public final int getCardMobId(final int id) {
        if (id == 0) {
            return 0;
        }
        Integer integer = monsterBookID.get(id);
        if (integer != null) {
            return integer;
        }

        WzElement<?> data = getItemData(id);
        int monsterid = Optional.ofNullable(data)
                .map(element -> Elements.findInt(element, "info/mob"))
                .orElse(0);
        if (monsterid == 0) { // Hack.
            return 0;
        }
        monsterBookID.put(id, monsterid);
        return monsterid;
    }

    public final int getWatkForProjectile(final int itemId) {
        Integer atk = projectileWatkCache.get(itemId);
        if (atk != null) {
            return atk;
        }

        WzElement<?> data = getItemData(itemId);
        atk = Optional.ofNullable(data)
                .map(element -> Elements.findInt(element, "info/incPAD"))
                .orElse(0);
        projectileWatkCache.put(itemId, atk);
        return atk;
    }

    public final boolean canScroll(final int scrollid, final int itemid) {
        return (scrollid / 100) % 100 == (itemid / 10000) % 100;
    }

    public final String getName(final int itemId) {
        String name = nameCache.get(itemId);
        if (name != null) {
            return name;
        }
        WzElement<?> element = getStringData(itemId);
        if (element == null) {
            return null;
        }

        String ret = Elements.findString(element, "name", "(null)");
        nameCache.put(itemId, ret);
        return ret;
    }

    public final String getDesc(final int itemId) {
        String desc = descCache.get(itemId);
        if (desc != null) {
            return desc;
        }
        WzElement<?> element = getStringData(itemId);
        if (element == null) {
            return null;
        }
        String ret = Elements.findString(element, "desc", null);
        descCache.put(itemId, ret);
        return ret;
    }

    public final String getMsg(final int itemId) {
        String msg = msgCache.get(itemId);
        if (msg != null) {
            return msg;
        }
        WzElement<?> element = getStringData(itemId);
        if (element == null) {
            return null;
        }
        String ret = Elements.findString(element, "msg", null);
        msgCache.put(itemId, ret);
        return ret;
    }

    public final short getItemMakeLevel(final int itemId) {
        Short lvl = itemMakeLevel.get(itemId);
        if (lvl != null) {
            return lvl;
        }

        if (itemId / 10000 != 400) {
            return 0;
        }

        WzElement<?> itemData = getItemData(itemId);
        lvl = Optional.ofNullable(itemData)
                .map(element -> Elements.findInt(element, "info/lv"))
                .map(Integer::shortValue)
                .orElse((short) 0);
        itemMakeLevel.put(itemId, lvl);
        return lvl;
    }

    public final byte isConsumeOnPickup(final int itemId) {
        // 0 = not, 1 = consume on pickup, 2 = consume + party
        Byte consume = consumeOnPickupCache.get(itemId);
        if (consume != null) {
            return consume;
        }

        WzElement<?> data = getItemData(itemId);
        consume = Optional.ofNullable(data)
                .map(element -> Elements.findInt(element, "spec/consumeOnPickup"))
                .map(Integer::byteValue)
                .orElse((byte) 0);
        if (consume == 0) {
            consume = Optional.ofNullable(data)
                    .map(element -> Elements.findInt(element, "specEx/consumeOnPickup"))
                    .map(Integer::byteValue)
                    .orElse((byte) 0);
        }
        if (consume == 1) {
            boolean present = Elements.findInt(data, "spec/party") > 0;
            if (present) {
                consume = 2;
            }
        }
        consumeOnPickupCache.put(itemId, consume);
        return consume;
    }

    public final boolean isDropRestricted(final int itemId) {
        Boolean trade = dropRestrictionCache.get(itemId);
        if (trade != null) {
            return trade;
        }

        WzElement<?> data = getItemData(itemId);
        trade = Optional.ofNullable(data)
                .map(element -> element.findByName("info/tradeBlock")
                        .map(Elements::ofInt)
                        .orElseGet(() -> element.findByName("info/quest")
                                .map(Elements::ofInt)
                                .orElse(0)))
                .filter(integer -> integer == 1)
                .isPresent();
        dropRestrictionCache.put(itemId, trade);
        return trade;
    }

    public final boolean isPickupRestricted(final int itemId) {
        Boolean aBoolean = pickupRestrictionCache.get(itemId);
        if (aBoolean != null) {
            return aBoolean;
        }

        WzElement<?> itemData = getItemData(itemId);
        boolean bRestricted = Optional.ofNullable(itemData)
                .map(element -> Elements.findInt(element, "info/only"))
                .filter(integer -> integer == 1)
                .isPresent();
        pickupRestrictionCache.put(itemId, bRestricted);
        return bRestricted;
    }

    public final boolean isAccountShared(final int itemId) {
        Boolean aBoolean = accCache.get(itemId);
        if (aBoolean != null) {
            return aBoolean;
        }

        WzElement<?> itemData = getItemData(itemId);
        boolean bRestricted = Optional.ofNullable(itemData)
                .map(element -> Elements.findInt(element, "info/accountSharable"))
                .filter(integer -> integer == 1)
                .isPresent();
        accCache.put(itemId, bRestricted);
        return bRestricted;
    }

    public final int getStateChangeItem(final int itemId) {
        Integer integer = stateChangeCache.get(itemId);
        if (integer != null) {
            return integer;
        }

        WzElement<?> itemData = getItemData(itemId);
        int triggerItem = Optional.ofNullable(itemData)
                .map(element -> Elements.findInt(element, "info/stateChangeItem"))
                .orElse(0);
        stateChangeCache.put(itemId, triggerItem);
        return triggerItem;
    }

    public final int getMeso(final int itemId) {
        Integer integer = mesoCache.get(itemId);
        if (integer != null) {
            return integer;
        }

        WzElement<?> itemData = getItemData(itemId);
        int triggerItem = Optional.ofNullable(itemData)
                .map(element -> Elements.findInt(element, "info/meso"))
                .orElse(0);
        mesoCache.put(itemId, triggerItem);
        return triggerItem;
    }

    public final boolean isKarmaEnabled(final int itemId) {
        Integer integer = karmaEnabledCache.get(itemId);
        if (integer != null) {
            return integer == 1;
        }

        WzElement<?> itemData = getItemData(itemId);
        int iRestricted = Optional.ofNullable(itemData)
                .map(element -> Elements.findInt(element, "info/tradeAvailable"))
                .orElse(0);
        karmaEnabledCache.put(itemId, iRestricted);
        return iRestricted == 1;
    }

    public final boolean isPKarmaEnabled(final int itemId) {
        Integer integer = karmaEnabledCache.get(itemId);
        if (integer != null) {
            return integer == 2;
        }

        WzElement<?> itemData = getItemData(itemId);
        int iRestricted = Optional.ofNullable(itemData)
                .map(element -> Elements.findInt(element, "info/tradeAvailable"))
                .orElse(0);
        karmaEnabledCache.put(itemId, iRestricted);
        return iRestricted == 2;
    }

    public final boolean isPickupBlocked(final int itemId) {
        Boolean aBoolean = blockPickupCache.get(itemId);
        if (aBoolean != null) {
            return aBoolean;
        }

        WzElement<?> itemData = getItemData(itemId);
        boolean iRestricted = Optional.ofNullable(itemData)
                .map(element -> Elements.findInt(element, "info/pickUpBlock"))
                .filter(integer -> integer == 1)
                .isPresent();
        blockPickupCache.put(itemId, iRestricted);
        return iRestricted;
    }

    public final boolean isLogoutExpire(final int itemId) {
        Boolean aBoolean = logoutExpireCache.get(itemId);
        if (aBoolean != null) {
            return aBoolean;
        }

        WzElement<?> itemData = getItemData(itemId);
        boolean iRestricted = Optional.ofNullable(itemData)
                .map(element -> Elements.findInt(element, "info/expireOnLogout"))
                .filter(integer -> integer == 1)
                .isPresent();
        logoutExpireCache.put(itemId, iRestricted);
        return iRestricted;
    }

    public final boolean cantSell(final int itemId) { //true = cant sell, false = can sell
        Boolean aBoolean = notSaleCache.get(itemId);
        if (aBoolean != null) {
            return aBoolean;
        }

        WzElement<?> itemData = getItemData(itemId);
        boolean bRestricted = Optional.ofNullable(itemData)
                .map(element -> Elements.findInt(element, "info/notSale"))
                .filter(integer -> integer == 1)
                .isPresent();
        notSaleCache.put(itemId, bRestricted);
        return bRestricted;
    }

    public final Pair<Integer, List<StructRewardItem>> getRewardItem(final int itemid) {
        Pair<Integer, List<StructRewardItem>> pair = RewardItem.get(itemid);
        if (pair != null) {
            return pair;
        }

        WzElement<?> data = getItemData(itemid);
        if (data == null) {
            return null;
        }

        AtomicInteger totalprob = new AtomicInteger(); // As there are some rewards with prob above 2000, we can't assume it's always 100
        List<StructRewardItem> all = data.findByName("reward").map(WzElement::childrenStream)
                .map(stream -> stream.map(element -> {
                    StructRewardItem struct = new StructRewardItem();
                    struct.itemid = Elements.findInt(element, "item");
                    struct.prob = (short) Elements.findInt(element, "prob");
                    struct.quantity = (short) Elements.findInt(element, "count");
                    struct.effect = Elements.findString(element, "Effect");
                    struct.worldmsg = Elements.findString(element, "worldMsg", null);
                    struct.period = Elements.findInt(element, "period", -1);
                    totalprob.addAndGet(struct.prob);
                    return struct;
                }).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        Pair<Integer, List<StructRewardItem>> toreturn = new Pair<>(totalprob.get(), all);
        RewardItem.put(itemid, toreturn);
        return toreturn;
    }

    public final Map<String, Integer> getSkillStats(final int itemId) {
        Map<String, Integer> map = SkillStatsCache.get(itemId);
        if (map != null) {
            return map;
        }
        if (!(itemId / 10000 == 228 || itemId / 10000 == 229 || itemId / 10000 == 562)) { // Skillbook and mastery book
            return null;
        }
        WzElement<?> item = getItemData(itemId);
        if (item == null) {
            return null;
        }

        Optional<WzElement<?>> info = item.findByName("info");
        Map<String, Integer> ret = info.map(WzElement::childrenStream)
                .map(elements -> elements.filter(data -> data.name().startsWith("inc"))
                        .collect(Collectors.toMap(element -> element.name().substring(3), Elements::ofInt)))
                .orElseGet(Maps::newHashMap);

        Integer masterLevel = info.map(element -> Elements.findInt(element, "masterLevel")).orElse(0);
        ret.put("masterLevel", masterLevel);
        Integer reqSkillLevel = info.map(element -> Elements.findInt(element, "reqSkillLevel")).orElse(0);
        ret.put("reqSkillLevel", reqSkillLevel);
        Integer success = info.map(element -> Elements.findInt(element, "success")).orElse(0);
        ret.put("success", success);

        Map<String, Integer> skill = info.map(element -> element.find("skill"))
                .map(WzElement::childrenStream)
                .map(stream -> stream.collect(Collectors
                        .toMap(element -> String.format("skillid%s", element.name()), Elements::ofInt)))
                .orElse(Collections.emptyMap());
        ret.putAll(skill);

        SkillStatsCache.put(itemId, ret);
        return ret;
    }

    public final List<Integer> petsCanConsume(final int itemId) {
        List<Integer> integers = petsCanConsumeCache.get(itemId);
        if (integers != null) {
            return integers;
        }

        WzElement<?> data = getItemData(itemId);
        WzElement<?> spec;
        if (data == null || (spec = data.find("spec")) == null) {
            return Collections.emptyList();
        }

        List<Integer> ret = spec.childrenStream()
                .filter(this::checkNumber)
                .map(Elements::ofInt)
                .filter(integer -> integer != 0)
                .collect(Collectors.toList());
        petsCanConsumeCache.put(itemId, ret);
        return ret;
    }

    private boolean checkNumber(WzElement<?> element) {
        try {
            Integer.parseInt(element.name());
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    public final boolean isQuestItem(final int itemId) {
        Boolean aBoolean = isQuestItemCache.get(itemId);
        if (aBoolean != null) {
            return aBoolean;
        }

        WzElement<?> itemData = getItemData(itemId);

        Boolean questItem = Optional.ofNullable(itemData)
                .flatMap(element -> element.findByName("info/quest"))
                .map(Elements::ofInt)
                .map(integer -> integer == 1)
                .orElse(false);
        isQuestItemCache.put(itemId, questItem);
        return questItem;
    }

    public final Pair<Integer, List<Integer>> questItemInfo(final int itemId) {
        Pair<Integer, List<Integer>> listPair = questItems.get(itemId);
        if (listPair != null) {
            return listPair;
        }

        WzElement<?> item;
        if (itemId / 10000 != 422 || (item = getItemData(itemId)) == null) {
            return null;
        }

        List<Integer> consumeItems = item.findByName("info/consumeItem")
                .map(WzElement::childrenStream)
                .map(stream -> stream.map(Elements::ofInt).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        Integer questId = item.findByName("info/questId").map(Elements::ofInt).orElse(0);
        Pair<Integer, List<Integer>> questItem = new Pair<>(questId, consumeItems);
        questItems.put(itemId, questItem);
        return questItem;
    }

    public final boolean itemExists(final int itemId) {
        if (GameConstants.getInventoryType(itemId) == MapleInventoryType.UNDEFINED) {
            return false;
        }
        return getItemData(itemId) != null;
    }

    public final boolean isCash(final int itemId) {
        Map<String, Integer> equipStats = getEquipStats(itemId);
        if (equipStats.isEmpty()) {
            return GameConstants.getInventoryType(itemId) == MapleInventoryType.CASH;
        }
        return GameConstants.getInventoryType(itemId) == MapleInventoryType.CASH || equipStats.get("cash") > 0;
    }

    public MapleInventoryType getInventoryType(int itemId) {
        MapleInventoryType type = inventoryTypeCache.get(itemId);
        if (type != null) {
            return type;
        }

        String idStr = "0" + itemId;
        MapleInventoryType ret = WzData.ITEM.directory().dirStream()
                .map(directory -> {
                    if (directory.findFile(idStr.substring(0, 4)).isPresent()
                            || directory.findFile(idStr.substring(1)).isPresent()) {
                        return MapleInventoryType.getByWZName(directory.name());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> WzData.CHARACTER.directory().dirStream()
                        .map(wzDirectory -> wzDirectory.findFile(idStr)
                                .map(wzFile -> MapleInventoryType.EQUIP)
                                .orElse(MapleInventoryType.UNDEFINED))
                        .findFirst()
                        .orElse(MapleInventoryType.UNDEFINED));
        inventoryTypeCache.put(itemId, ret);
        return ret;
    }

    public short getPetFlagInfo(int itemId) {
        short flag = 0;
        if (itemId / 10000 != 500) {
            return flag;
        }

        WzElement<?> item = getItemData(itemId);
        if (item == null) {
            return flag;
        }

        boolean present = checkPetFlagInfo(item, "pickupItem");
        if (present) {
            flag = (short) (flag | 0x1);
        }
        present = checkPetFlagInfo(item, "longRange");
        if (present) {
            flag = (short) (flag | 0x2);
        }
        present = checkPetFlagInfo(item, "pickupAll");
        if (present) {
            flag = (short) (flag | 0x4);
        }
        present = checkPetFlagInfo(item, "sweepForDrop");
        if (present) {
            flag = (short) (flag | 0x10);
        }
        present = checkPetFlagInfo(item, "consumeHP");
        if (present) {
            flag = (short) (flag | 0x20);
        }
        present = checkPetFlagInfo(item, "consumeMP");
        if (present) {
            flag = (short) (flag | 0x40);
        }
        //this.petFlagInfo.put(Integer.valueOf(itemId), Short.valueOf(flag));
        return flag;
    }

    private boolean checkPetFlagInfo(WzElement<?> data, String pickupItem) {
        return data.findByName("info/" + pickupItem)
                .map(Elements::ofInt)
                .filter(integer -> integer > 0)
                .isPresent();
    }

    public boolean isKarmaAble(int itemId) {
        Boolean aBoolean = this.karmaCache.get(itemId);
        if (aBoolean != null) {
            return aBoolean;
        }

        WzElement<?> itemData = getItemData(itemId);
        if (itemData == null) {
            return false;
        }

        Boolean bRestricted = itemData.findByName("info/tradeAvailable")
                .map(Elements::ofInt)
                .map(integer -> integer > 0) //可以交易
                .orElse(false);
        this.karmaCache.put(itemId, bRestricted);
        return bRestricted;
    }

    public List<Pair<String, Integer>> getItemLevelupStats(int itemId, int level, boolean timeless) {
        //timeless 永恒
        List<Pair<String, Integer>> list = new LinkedList<>();
        WzElement<?> data = getItemData(itemId); //获得该物品所有节点
        Optional.ofNullable(data)
                .flatMap(element -> element.findByName("info/level/info/" + level))//获取与装备的[道具等级]相应的节点
                .ifPresent(element -> {
                    handleItemLevelupStats(element, "incDEX").ifPresent(list::add);
                    handleItemLevelupStats(element, "incSTR").ifPresent(list::add);
                    handleItemLevelupStats(element, "incINT").ifPresent(list::add);
                    handleItemLevelupStats(element, "incLUK").ifPresent(list::add);
                    handleItemLevelupStats(element, "incMHP").ifPresent(list::add);
                    handleItemLevelupStats(element, "incMMP").ifPresent(list::add);
                    handleItemLevelupStats(element, "incPAD").ifPresent(list::add);
                    handleItemLevelupStats(element, "incMAD").ifPresent(list::add);
                    handleItemLevelupStats(element, "incPDD").ifPresent(list::add);
                    handleItemLevelupStats(element, "incMDD").ifPresent(list::add);
                    handleItemLevelupStats(element, "incACC").ifPresent(list::add);
                    handleItemLevelupStats(element, "incEVA").ifPresent(list::add);
                    handleItemLevelupStats(element, "incSpeed").ifPresent(list::add);
                    handleItemLevelupStats(element, "incJump").ifPresent(list::add);
                });
        return list;
    }

    private Optional<Pair<String, Integer>> handleItemLevelupStats(WzElement<?> element, String name) {
        return element.findByName(name + "Min")
                .filter(it -> Math.random() < 0.9)
                .map(Elements::ofInt)
                .map(min -> new Pair<>(name, rand(min, element.findByName(name + "Max")
                        .map(Elements::ofInt)
                        .orElse(min))));
    }

    public boolean isUntradeableOnEquip(int itemId) {
        Boolean aBoolean = onEquipUntradableCache.get(itemId);
        if (aBoolean != null) {
            return aBoolean;
        }

        WzElement<?> itemData = getItemData(itemId);
        if (itemData == null) {
            return false;
        }

        boolean untradableOnEquip = Elements.findInt(itemData, "info/equipTradeBlock") > 0;
        onEquipUntradableCache.put(itemId, untradableOnEquip);
        return untradableOnEquip;
    }

    public int getExpCache(int itemId) {
        Integer integer = getExpCache.get(itemId);
        if (integer != null) {
            return integer;
        }

        WzElement<?> item = getItemData(itemId);
        if (item == null) {
            return 0;
        }

        int pEntry = Elements.findInt(item, "spec/exp");
        getExpCache.put(itemId, pEntry);
        return pEntry;
    }

    public int getLimitBreak(int itemId) {
        Map<String, Integer> equipStats = getEquipStats(itemId);
        if (equipStats.isEmpty() || (!equipStats.containsKey("limitBreak"))) {
            return 999999;
        }
        return equipStats.get("limitBreak");
    }
}
