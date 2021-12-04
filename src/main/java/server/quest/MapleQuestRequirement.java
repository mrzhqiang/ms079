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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class MapleQuestRequirement implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;

    private final MapleQuestRequirementType type;
    private final MapleQuest quest;
    public final DWzQuestReqData data;

    private int intStore;
    private String stringStore;
    private List<Pair<Integer, Integer>> dataStore;

    public MapleQuestRequirement(MapleQuest quest, MapleQuestRequirementType type, DWzQuestReqData data) {
        this.type = type;
        this.quest = quest;
        this.data = data;

        switch (type) {
            case PET:
            case MB_CARD:
            case MOB:
            case ITEM:
            case QUEST:
            case SKILL:
            case JOB:
                this.dataStore = new LinkedList<>();
                String[] first = data.getIntStoresFirst().split(", ");
                String[] second = data.getIntStoresSecond().split(", ");
                if ((first.length <= 0) && (data.getIntStoresFirst().length() > 0)) {
                    this.dataStore.add(new Pair<>(Integer.parseInt(data.getIntStoresFirst()), Integer.parseInt(data.getIntStoresSecond())));
                }
                for (int i = 0; i < first.length; i++) {
                    if ((first[i].length() > 0) && (second[i].length() > 0)) {
                        this.dataStore.add(new Pair<>(Integer.parseInt(first[i]), Integer.parseInt(second[i])));
                    }
                }
                break;
            //case partyQuest_S:
            case DAY_BY_DAY:
            case NORMAL_AUTO_START:
            case SUB_JOB_FLAGS:
            case FIELD_ENTER:
            case PET_TAMENESS_MIN:
            case NPC:
            case QUEST_COMPLETE:
            case POP:
            case INTERVAL:
            case MB_MIN:
            case LV_MAX:
            case LV_MIN:
                this.intStore = Integer.parseInt(data.getStringStore());
                break;
            case END:
                this.stringStore = data.getStringStore();
        }
    }

    public boolean check(MapleCharacter c, Integer npcid) {
        switch (type) {
            case JOB:
                for (Pair<Integer, Integer> a : this.dataStore) {
                    if ((a.getRight() == c.getJob()) || (c.isGM())) {
                        return true;
                    }
                }
                return false;
            case SKILL:
                for (Pair<Integer, Integer> a : this.dataStore) {
                    boolean acquire = a.getRight() > 0;
                    int skill = a.getLeft();
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
            case QUEST:
                for (Pair<Integer, Integer> a : this.dataStore) {
                    MapleQuestStatus q = c.getQuest(MapleQuest.getInstance(a.getLeft()));
                    int state = a.getRight();
                    if (state != 0) {
                        if ((q == null) || (q.getStatus() != state)) {
                            return false;
                        }
                    }
                }
                return true;
            case ITEM:
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
            case LV_MIN:
                return c.getLevel() >= this.intStore;
            case LV_MAX:
                return c.getLevel() <= this.intStore;
            case END:
                String timeStr = this.stringStore;
                if ((timeStr == null) || (timeStr.length() <= 0)) {
                    return true;
                }
                LocalDateTime endTime = DateTimeFormatter.ofPattern("yyyyMMddHH").parse(timeStr, LocalDateTime::from);
                // return cal.getTimeInMillis() >= System.currentTimeMillis();
                return LocalDateTime.now().isBefore(endTime);
            case MOB:
                for (Pair<Integer, Integer> a : this.dataStore) {
                    int mobId = a.getLeft();
                    int killReq = a.getRight();
                    if (c.getQuest(this.quest).getMobKills(mobId) < killReq) {
                        return false;
                    }
                }
                return true;
            case NPC:
                return (npcid == null) || (npcid == this.intStore);
            case FIELD_ENTER:
                if (this.intStore > 0) {
                    return this.intStore == c.getMapId();
                }
                return true;
            case MB_MIN:
                return c.getMonsterBook().getSeen() >= this.intStore;
            case MB_CARD:
                for (Pair<Integer, Integer> a : this.dataStore) {
                    int cardId = a.getLeft();
                    int killReq = a.getRight();
                    if (c.getMonsterBook().getLevelByCard(cardId) < killReq) {
                        return false;
                    }
                }
                return true;
            case POP:
                return c.getFame() >= this.intStore;
            case QUEST_COMPLETE:
                return c.getNumQuest() >= this.intStore;
            case INTERVAL:
                return (c.getQuest(this.quest).getStatus() != 2) || (c.getQuest(this.quest).getCompletionTime() <= System.currentTimeMillis() - this.intStore * 60 * 1000L);
            case PET:
                for (Pair<Integer, Integer> a : this.dataStore) {
                    if (c.getPetById(a.getRight()) != -1) {
                        return true;
                    }
                }
                return false;
            case PET_TAMENESS_MIN:
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
            case SUB_JOB_FLAGS:
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
