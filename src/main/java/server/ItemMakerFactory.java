package server;

import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.Pair;

import java.util.*;

public class ItemMakerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemMakerFactory.class);

    private final static ItemMakerFactory instance = new ItemMakerFactory();
    protected Map<Integer, ItemMakerCreateEntry> createCache = new HashMap<>();
    protected Map<Integer, GemCreateEntry> gemCache = new HashMap<>();

    public static ItemMakerFactory getInstance() {
        // DO ItemMakerFactory.getInstance() on ChannelServer startup.
        return instance;
    }

    protected ItemMakerFactory() {
        LOGGER.info("Loading ItemMakerFactory :::");
        // 0 = Item upgrade crystals
        // 1 / 2/ 4/ 8 = Item creation

        byte totalupgrades, reqMakerLevel;
        int reqLevel, cost, quantity, stimulator;
        GemCreateEntry ret;
        ItemMakerCreateEntry imt;

        WzData.ETC.directory().findFile("ItemMake.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(this::handleItemMake));
    }

    private void handleItemMake(WzElement<?> element) {
        int type = Numbers.ofInt(element.name());
        switch (type) {
            case 0: { // Caching of gem
                element.childrenStream().forEach(it -> {
                    GemCreateEntry entry = getGemCreateEntry(it);
                    gemCache.put(Numbers.ofInt(it.name()), entry);
                });
                break;
            }
            case 1: // Warrior
            case 2: // Magician
            case 4: // Bowman
            case 8: // Thief
            case 16: { // Pirate
                element.childrenStream().forEach(it -> {
                    ItemMakerCreateEntry entry = getItemMakerCreateEntry(it);
                    createCache.put(Numbers.ofInt(it.name()), entry);
                });
                break;
            }
        }
    }

    private ItemMakerCreateEntry getItemMakerCreateEntry(WzElement<?> element) {
        int reqLevelInt = Elements.findInt(element, "reqLevel");
        byte reqMakerLevel = (byte) Elements.findInt(element, "reqSkillLevel");
        int cost = Elements.findInt(element, "meso");
        int quantity = Elements.findInt(element, "itemNum");
        byte totalupgrades = (byte) Elements.findInt(element, "tuc");
        int stimulator = Elements.findInt(element, "catalyst");
        ItemMakerCreateEntry entry = new ItemMakerCreateEntry(
                cost, reqLevelInt, reqMakerLevel, quantity, totalupgrades, stimulator);

        element.findByName("recipe")
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(recipe -> {
                    int itemId = Elements.findInt(recipe, "item");
                    int countInt = Elements.findInt(recipe, "count");
                    entry.addReqItem(itemId, countInt);
                }));
        return entry;
    }

    private GemCreateEntry getGemCreateEntry(WzElement<?> element) {
        int reqLevel = Elements.findInt(element, "reqLevel");
        byte reqSkillLevel = (byte) Elements.findInt(element, "reqSkillLevel");
        int cost = Elements.findInt(element, "meso");
        int quantity = Elements.findInt(element, "itemNum");
        GemCreateEntry entry = new GemCreateEntry(cost, reqLevel, reqSkillLevel, quantity);

        element.findByName("randomReward")
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(reward -> {
                    int itemId = Elements.findInt(reward, "item");
                    int probInt = Elements.findInt(reward, "prob");
                    entry.addRandomReward(itemId, probInt);
                }));
        element.findByName("recipe")
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(recipe -> {
                    int itemId = Elements.findInt(recipe, "item");
                    int countInt = Elements.findInt(recipe, "count");
                    entry.addReqRecipe(itemId, countInt);
                }));
        return entry;
    }

    public GemCreateEntry getGemInfo(int itemid) {
        return gemCache.get(itemid);
    }

    public ItemMakerCreateEntry getCreateInfo(int itemid) {
        return createCache.get(itemid);
    }

    public static class GemCreateEntry {

        private int reqLevel, reqMakerLevel;
        private int cost, quantity;
        private List<Pair<Integer, Integer>> randomReward = new ArrayList<Pair<Integer, Integer>>();
        private List<Pair<Integer, Integer>> reqRecipe = new ArrayList<Pair<Integer, Integer>>();

        public GemCreateEntry(int cost, int reqLevel, int reqMakerLevel, int quantity) {
            this.cost = cost;
            this.reqLevel = reqLevel;
            this.reqMakerLevel = reqMakerLevel;
            this.quantity = quantity;
        }

        public int getRewardAmount() {
            return quantity;
        }

        public List<Pair<Integer, Integer>> getRandomReward() {
            return randomReward;
        }

        public List<Pair<Integer, Integer>> getReqRecipes() {
            return reqRecipe;
        }

        public int getReqLevel() {
            return reqLevel;
        }

        public int getReqSkillLevel() {
            return reqMakerLevel;
        }

        public int getCost() {
            return cost;
        }

        protected void addRandomReward(int itemId, int prob) {
            randomReward.add(new Pair<Integer, Integer>(itemId, prob));
        }

        protected void addReqRecipe(int itemId, int count) {
            reqRecipe.add(new Pair<Integer, Integer>(itemId, count));
        }
    }

    public static class ItemMakerCreateEntry {

        private int reqLevel;
        private int cost, quantity, stimulator;
        private byte tuc, reqMakerLevel;
        private List<Pair<Integer, Integer>> reqItems = new ArrayList<Pair<Integer, Integer>>(); // itemId / amount
        private List<Integer> reqEquips = new ArrayList<Integer>();

        public ItemMakerCreateEntry(int cost, int reqLevel, byte reqMakerLevel, int quantity, byte tuc, int stimulator) {
            this.cost = cost;
            this.tuc = tuc;
            this.reqLevel = reqLevel;
            this.reqMakerLevel = reqMakerLevel;
            this.quantity = quantity;
            this.stimulator = stimulator;
        }

        public byte getTUC() {
            return tuc;
        }

        public int getRewardAmount() {
            return quantity;
        }

        public List<Pair<Integer, Integer>> getReqItems() {
            return reqItems;
        }

        public List<Integer> getReqEquips() {
            return reqEquips;
        }

        public int getReqLevel() {
            return reqLevel;
        }

        public byte getReqSkillLevel() {
            return reqMakerLevel;
        }

        public int getCost() {
            return cost;
        }

        public int getStimulator() {
            return stimulator;
        }

        protected void addReqItem(int itemId, int amount) {
            reqItems.add(new Pair<Integer, Integer>(itemId, amount));
        }
    }
}
