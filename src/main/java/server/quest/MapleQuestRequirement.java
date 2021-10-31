package server.quest;

import client.ISkill;
import client.MapleCharacter;
import client.MapleQuestStatus;
import client.SkillFactory;
import client.inventory.IItem;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import com.github.mrzhqiang.maplestory.domain.DWzQuestReqData;
import constants.GameConstants;
import tools.Pair;

import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class MapleQuestRequirement implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    private MapleQuest quest;
    private MapleQuestRequirementType type;
    private int intStore;
    private String stringStore;
    private List<Pair<Integer, Integer>> dataStore;

    public final DWzQuestReqData data;

    public MapleQuestRequirement(MapleQuest quest, MapleQuestRequirementType type, DWzQuestReqData data) {
        this.type = type;
        this.quest = quest;
        this.data = data;

        switch (type) {
            case pet:
            case mbcard:
            case mob:
            case item:
            case quest:
            case skill:
            case job:
                this.dataStore = new LinkedList<>();
                String[] first = data.intStoresFirst.split(", ");
                String[] second = data.intStoresSecond.split(", ");
                if ((first.length <= 0) && (data.intStoresFirst.length() > 0)) {
                    this.dataStore.add(new Pair<>(Integer.parseInt(data.intStoresFirst), Integer.parseInt(data.intStoresSecond)));
                }
                for (int i = 0; i < first.length; i++) {
                    if ((first[i].length() > 0) && (second[i].length() > 0)) {
                        this.dataStore.add(new Pair<>(Integer.parseInt(first[i]), Integer.parseInt(second[i])));
                    }
                }
                break;
            //case partyQuest_S:
            case dayByDay:
            case normalAutoStart:
            case subJobFlags:
            case fieldEnter:
            case pettamenessmin:
            case npc:
            case questComplete:
            case pop:
            case interval:
            case mbmin:
            case lvmax:
            case lvmin:
                this.intStore = Integer.parseInt(data.stringStore);
                break;
            case end:
                this.stringStore = data.stringStore;
        }
    }

    public boolean check(MapleCharacter c, Integer npcid) {
        switch (type) {
            case job:
                for (Pair a : this.dataStore) {
                    if ((((Integer) a.getRight()).intValue() == c.getJob()) || (c.isGM())) {
                        return true;
                    }
                }
                return false;
            case skill:
                for (Pair<Integer, Integer> a : this.dataStore) {
                    boolean acquire = a.getRight() > 0;
                    int skill = ((Integer) a.getLeft()).intValue();
                    ISkill skil = SkillFactory.getSkill(skill);
                    if (acquire) {
                        if (skil.isFourthJob()) {
                            if (c.getMasterLevel(skil) == 0) {
                                return false;
                            }
                        } else if (c.getSkillLevel(skil) == 0) {
                            return false;
                        }

                    } else if ((c.getSkillLevel(skil) > 0) || (c.getMasterLevel(skil) > 0)) {
                        return false;
                    }
                }

                return true;
            case quest:
                for (Pair<Integer, Integer> a : this.dataStore) {
                    MapleQuestStatus q = c.getQuest(MapleQuest.getInstance(((Integer) a.getLeft()).intValue()));
                    int state = ((Integer) a.getRight()).intValue();
                    if (state != 0) {
                        if ((q == null) && (state == 0)) {
                            continue;
                        }
                        if ((q == null) || (q.getStatus() != state)) {
                            return false;
                        }
                    }
                }
                return true;
            case item:
                for (Pair<Integer, Integer> a : this.dataStore) {
                    int itemId = a.getLeft();
                    short quantity = 0;
                    MapleInventoryType iType = GameConstants.getInventoryType(itemId);
                    for (IItem item : c.getInventory(iType).listById(itemId)) {
                        quantity = (short) (quantity + item.getQuantity());
                    }
                    int count = a.getRight();
                    if ((quantity < count) || ((count <= 0) && (quantity > 0))) {
                        return false;
                    }
                }
                return true;
            case lvmin:
                return c.getLevel() >= this.intStore;
            case lvmax:
                return c.getLevel() <= this.intStore;
            case end:
                String timeStr = this.stringStore;
                if ((timeStr == null) || (timeStr.length() <= 0)) {
                    return true;
                }
                Calendar cal = Calendar.getInstance();
                cal.set(Integer.parseInt(timeStr.substring(0, 4)), Integer.parseInt(timeStr.substring(4, 6)), Integer.parseInt(timeStr.substring(6, 8)), Integer.parseInt(timeStr.substring(8, 10)), 0);
                return cal.getTimeInMillis() >= System.currentTimeMillis();
            case mob:
                for (Pair<Integer, Integer> a : this.dataStore) {
                    int mobId = ((Integer) a.getLeft()).intValue();
                    int killReq = ((Integer) a.getRight()).intValue();
                    if (c.getQuest(this.quest).getMobKills(mobId) < killReq) {
                        return false;
                    }
                }
                return true;
            case npc:
                return (npcid == null) || (npcid.intValue() == this.intStore);
            case fieldEnter:
                if (this.intStore > 0) {
                    return this.intStore == c.getMapId();
                }
                return true;
            case mbmin:
                return c.getMonsterBook().getSeen() >= this.intStore;
            case mbcard:
                for (Pair<Integer, Integer> a : this.dataStore) {
                    int cardId = a.getLeft();
                    int killReq = a.getRight();
                    if (c.getMonsterBook().getLevelByCard(cardId) < killReq) {
                        return false;
                    }
                }
                return true;
            case pop:
                return c.getFame() >= this.intStore;
            case questComplete:
                return c.getNumQuest() >= this.intStore;
            case interval:
                return (c.getQuest(this.quest).getStatus() != 2) || (c.getQuest(this.quest).getCompletionTime() <= System.currentTimeMillis() - this.intStore * 60 * 1000L);
            case pet:
                for (Pair<Integer, Integer> a : this.dataStore) {
                    if (c.getPetById(a.getRight()) != -1) {
                        return true;
                    }
                }
                return false;
            case pettamenessmin:
                for (MaplePet pet : c.getPets()) {
                    if ((pet.getSummoned()) && (pet.getCloseness() >= this.intStore)) {
                        return true;
                    }
                }
                return false;
            /*case partyQuest_S:
                int[] partyQuests = {1200, 1201, 1202, 1203, 1204, 1205, 1206, 1300, 1301, 1302};
                int sRankings = 0;
                for (int i : partyQuests) {
                    String rank = c.getOneInfo(i, "rank");
                    if ((rank != null) && (rank.equals("S"))) {
                        sRankings++;
                    }
                }
                return sRankings >= 5;*/
            case subJobFlags:
                return c.getSubcategory() == (intStore / 2);
            /*case craftMin:
            case willMin:
            case charismaMin:
            case insightMin:
            case charmMin:
            case senseMin:
                return c.getTrait(MapleTrait.MapleTraitType.getByQuestName(this.type.name())).getLevel() >= this.intStore;*/
            default:
                return true;
        }
    }

    public MapleQuestRequirementType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }

    public List<Pair<Integer, Integer>> getDataStore() {
        return this.dataStore;
    }
}
