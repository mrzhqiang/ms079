package server.quest;

import constants.GameConstants;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import client.MapleCharacter;
import client.MapleQuestStatus;
import database.DatabaseConnection;
import database.DatabaseConnectionWZ;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import scripting.NPCScriptManager;
import provider.MapleData;
import tools.MaplePacketCreator;
import tools.Pair;

public class MapleQuest implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    private static Map<Integer, MapleQuest> quests = new LinkedHashMap();
    protected int id;
    protected List<MapleQuestRequirement> startReqs = new LinkedList();
    protected List<MapleQuestRequirement> completeReqs = new LinkedList();
    protected List<MapleQuestAction> startActs = new LinkedList();
    protected List<MapleQuestAction> completeActs = new LinkedList();
    protected Map<String, List<Pair<String, Pair<String, Integer>>>> partyQuestInfo = new LinkedHashMap();
    protected Map<Integer, Integer> relevantMobs = new LinkedHashMap();
    private boolean autoStart = false;
    private boolean autoPreComplete = false;
    private boolean repeatable = false;
    private boolean customend = false;
    private boolean blocked = false;
    private boolean autoAccept = false;
    private boolean autoComplete = false;
    private boolean scriptedStart = false;
    private int viewMedalItem = 0;
    private int selectedSkillID = 0;
    protected String name = "";

    protected MapleQuest(final int id) {
        this.id = id;
    }

    private static MapleQuest loadQuest(ResultSet rs, PreparedStatement psr, PreparedStatement psa, PreparedStatement pss,
                                        PreparedStatement psq, PreparedStatement psi, PreparedStatement psp) throws SQLException {
        MapleQuest ret = new MapleQuest(rs.getInt("questid"));
        ret.name = rs.getString("name");
        ret.autoStart = (rs.getInt("autoStart") > 0);
        ret.autoPreComplete = (rs.getInt("autoPreComplete") > 0);
        ret.autoAccept = (rs.getInt("autoAccept") > 0);
        ret.autoComplete = (rs.getInt("autoComplete") > 0);
        ret.viewMedalItem = rs.getInt("viewMedalItem");
        ret.selectedSkillID = rs.getInt("selectedSkillID");
        ret.blocked = (rs.getInt("blocked") > 0);

        psr.setInt(1, ret.id);
        ResultSet rse = psr.executeQuery();
        while (rse.next()) {
            MapleQuestRequirementType type = MapleQuestRequirementType.getByWZName(rse.getString("name"));
            MapleQuestRequirement req = new MapleQuestRequirement(ret, type, rse);
            if (type.equals(MapleQuestRequirementType.interval)) {
                ret.repeatable = true;
            } else if (type.equals(MapleQuestRequirementType.normalAutoStart)) {
                ret.repeatable = true;
                ret.autoStart = true;
            } else if (type.equals(MapleQuestRequirementType.startscript)) {
                ret.scriptedStart = true;
            } else if (type.equals(MapleQuestRequirementType.endscript)) {
                ret.customend = true;
            } else if (type.equals(MapleQuestRequirementType.mob)) {
                for (Pair<Integer, Integer> mob : req.getDataStore()) {
                    ret.relevantMobs.put(mob.left, mob.right);
                }
            }
            if (rse.getInt("type") == 0) {
                ret.startReqs.add(req);
            } else {
                ret.completeReqs.add(req);
            }
        }
        rse.close();

        psa.setInt(1, ret.id);
        rse = psa.executeQuery();
        while (rse.next()) {
            MapleQuestActionType ty = MapleQuestActionType.getByWZName(rse.getString("name"));
            if (rse.getInt("type") == 0) {
                if ((ty == MapleQuestActionType.item) && (ret.id == 7103)) {
                    continue;
                }
                ret.startActs.add(new MapleQuestAction(ty, rse, ret, pss, psq, psi));
            } else {
                if ((ty == MapleQuestActionType.item) && (ret.id == 7102)) {
                    continue;
                }
                ret.completeActs.add(new MapleQuestAction(ty, rse, ret, pss, psq, psi));
            }
        }
        rse.close();

        psp.setInt(1, ret.id);
        rse = psp.executeQuery();
        while (rse.next()) {
            if (!ret.partyQuestInfo.containsKey(rse.getString("rank"))) {
                ret.partyQuestInfo.put(rse.getString("rank"), new ArrayList());
            }
            ((List) ret.partyQuestInfo.get(rse.getString("rank"))).add(new Pair(rse.getString("mode"), new Pair(rse.getString("property"), Integer.valueOf(rse.getInt("value")))));
        }
        rse.close();
        return ret;
    }

    /**
     * Creates a new instance of MapleQuest
     */
    /*private static boolean loadQuest(MapleQuest ret, int id) throws NullPointerException {
        // read reqs
        final MapleData basedata1 = requirements.getChildByPath(String.valueOf(id));
        final MapleData basedata2 = actions.getChildByPath(String.valueOf(id));

        if (basedata1 == null || basedata2 == null) {
            return false;
        }
        //-------------------------------------------------
        final MapleData startReqData = basedata1.getChildByPath("0");
        if (startReqData != null) {
            final List<MapleData> startC = startReqData.getChildren();
            if (startC != null && startC.size() > 0) {
                for (MapleData startReq : startC) {
                    final MapleQuestRequirementType type = MapleQuestRequirementType.getByWZName(startReq.getName());
                    if (type.equals(MapleQuestRequirementType.interval)) {
                        ret.repeatable = true;
                    }
                    final MapleQuestRequirement req = new MapleQuestRequirement(ret, type, startReq);
                    if (req.getType().equals(MapleQuestRequirementType.mob)) {
                        for (MapleData mob : startReq.getChildren()) {
                            ret.relevantMobs.put(
                                    MapleDataTool.getInt(mob.getChildByPath("id")),
                                    MapleDataTool.getInt(mob.getChildByPath("count"), 0));
                        }
                    }
                    ret.startReqs.add(req);
                }
            }
        }
        //-------------------------------------------------
        final MapleData completeReqData = basedata1.getChildByPath("1");
        if (completeReqData != null) {
            final List<MapleData> completeC = completeReqData.getChildren();
            if (completeC != null && completeC.size() > 0) {
                for (MapleData completeReq : completeC) {
                    MapleQuestRequirement req = new MapleQuestRequirement(ret, MapleQuestRequirementType.getByWZName(completeReq.getName()), completeReq);
                    if (req.getType().equals(MapleQuestRequirementType.mob)) {
                        for (MapleData mob : completeReq.getChildren()) {
                            ret.relevantMobs.put(
                                    MapleDataTool.getInt(mob.getChildByPath("id")),
                                    MapleDataTool.getInt(mob.getChildByPath("count"), 0));
                        }
                    } else if (req.getType().equals(MapleQuestRequirementType.endscript)) {
                        ret.customend = true;
                    }
                    ret.completeReqs.add(req);
                }
            }
        }
        // read acts
        final MapleData startActData = basedata2.getChildByPath("0");
        if (startActData != null) {
            final List<MapleData> startC = startActData.getChildren();
            for (MapleData startAct : startC) {
                ret.startActs.add(new MapleQuestAction(MapleQuestActionType.getByWZName(startAct.getName()), startAct, ret));
            }
        }
        final MapleData completeActData = basedata2.getChildByPath("1");

        if (completeActData != null) {
            final List<MapleData> completeC = completeActData.getChildren();
            for (MapleData completeAct : completeC) {
                ret.completeActs.add(new MapleQuestAction(MapleQuestActionType.getByWZName(completeAct.getName()), completeAct, ret));
            }
        }
        final MapleData questInfo = info.getChildByPath(String.valueOf(id));
        if (questInfo != null) {
            ret.name = MapleDataTool.getString("name", questInfo, "");
            ret.autoStart = MapleDataTool.getInt("autoStart", questInfo, 0) == 1;
            ret.autoPreComplete = MapleDataTool.getInt("autoPreComplete", questInfo, 0) == 1;
            ret.viewMedalItem = MapleDataTool.getInt("viewMedalItem", questInfo, 0);
            ret.selectedSkillID = MapleDataTool.getInt("selectedSkillID", questInfo, 0);
        }

        final MapleData pquestInfo = pinfo.getChildByPath(String.valueOf(id));
        if (pquestInfo != null) {
            for (MapleData d : pquestInfo.getChildByPath("rank")) {
                List<Pair<String, Pair<String, Integer>>> pInfo = new ArrayList<Pair<String, Pair<String, Integer>>>();
                //LinkedHashMap<String, List<Pair<String, Pair<String, Integer>>>>
                for (MapleData c : d) {
                    for (MapleData b : c) {
                        pInfo.add(new Pair<String, Pair<String, Integer>>(c.getName(), new Pair<String, Integer>(b.getName(), MapleDataTool.getInt(b, 0))));
                    }
                }
                ret.partyQuestInfo.put(d.getName(), pInfo);
            }
        }

        return true;
    }
     */
    public List<Pair<String, Pair<String, Integer>>> getInfoByRank(final String rank) {
        return partyQuestInfo.get(rank);
    }

    public final int getSkillID() {
        return selectedSkillID;
    }

    public final String getName() {
        return name;
    }

    public static void initQuests() {
        try {
            Connection con = DatabaseConnectionWZ.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM wz_questdata");
            PreparedStatement psr = con.prepareStatement("SELECT * FROM wz_questreqdata WHERE questid = ?");
            PreparedStatement psa = con.prepareStatement("SELECT * FROM wz_questactdata WHERE questid = ?");
            PreparedStatement pss = con.prepareStatement("SELECT * FROM wz_questactskilldata WHERE uniqueid = ?");
            PreparedStatement psq = con.prepareStatement("SELECT * FROM wz_questactquestdata WHERE uniqueid = ?");
            PreparedStatement psi = con.prepareStatement("SELECT * FROM wz_questactitemdata WHERE uniqueid = ?");
            PreparedStatement psp = con.prepareStatement("SELECT * FROM wz_questpartydata WHERE questid = ?");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                quests.put(rs.getInt("questid"), loadQuest(rs, psr, psa, pss, psq, psi, psp));
            }
            rs.close();
            ps.close();
            psr.close();
            psa.close();
            pss.close();
            psq.close();
            psi.close();
            psp.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("共加载 " + quests.size() + " 个任务信息.");
    }

    /* public static void initQuests() {
        questData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Quest.wz"));
        actions = questData.getData("Act.img");
        requirements = questData.getData("Check.img");
        info = questData.getData("QuestInfo.img");
        pinfo = questData.getData("PQuest.img");
    }*/
    public static void clearQuests() {
        quests.clear();
        initQuests(); //test
    }

    public static MapleQuest getInstance(int id) {
        MapleQuest ret = (MapleQuest) quests.get(Integer.valueOf(id));
        if (ret == null) {
            ret = new MapleQuest(id);
            quests.put(Integer.valueOf(id), ret);
        }
        return ret;
    }

    /*public static MapleQuest getInstance(int id) {
        MapleQuest ret = quests.get(id);
        if (ret == null) {
            ret = new MapleQuest(id);
            try {
                if (GameConstants.isCustomQuest(id) || !loadQuest(ret, id)) {
                    ret = new MapleCustomQuest(id);
                }
                quests.put(id, ret);
            } catch (Exception ex) {
                ex.printStackTrace();
                FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
                FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Caused by questID " + id);
                System.out.println("Caused by questID " + id);
                return new MapleCustomQuest(id);
            }
        }
        return ret;
    }*/
    public boolean canStart(MapleCharacter c, Integer npcid) {
        if (c.getQuest(this).getStatus() != 0 && !(c.getQuest(this).getStatus() == 2 && repeatable)) {
            return false;
        }
        for (MapleQuestRequirement r : startReqs) {
            if (!r.check(c, npcid)) {
                return false;
            }
        }
        return true;
    }

    public boolean canComplete(MapleCharacter c, Integer npcid) {
        if (c.getQuest(this).getStatus() != 1) {
            return false;
        }
        for (MapleQuestRequirement r : completeReqs) {
            if (!r.check(c, npcid)) {
                return false;
            }
        }
        return true;
    }

    public final void RestoreLostItem(final MapleCharacter c, final int itemid) {
        for (final MapleQuestAction a : startActs) {
            if (a.RestoreLostItem(c, itemid)) {
                break;
            }
        }
    }

    public void start(MapleCharacter c, int npc) {
        if ((autoStart || checkNPCOnMap(c, npc)) && canStart(c, npc)) {
            for (MapleQuestAction a : startActs) {
                if (!a.checkEnd(c, null)) { //just in case
                    return;
                }
            }
            for (MapleQuestAction a : startActs) {
                a.runStart(c, null);
            }
            if (!customend) {
                forceStart(c, npc, null);
            } else {
                NPCScriptManager.getInstance().endQuest(c.getClient(), npc, getId(), true);
            }
        }
    }

    public void complete(MapleCharacter c, int npc) {
        complete(c, npc, null);
    }

    public void complete(MapleCharacter c, int npc, Integer selection) {
        if ((autoPreComplete || checkNPCOnMap(c, npc)) && canComplete(c, npc)) {
            if (npc != 9010000) {
                for (MapleQuestAction a : completeActs) {
                    if (!a.checkEnd(c, selection)) {
                        return;
                    }
                }
                forceComplete(c, npc);
                for (MapleQuestAction a : completeActs) {
                    a.runEnd(c, selection);
                }
            }
            // we save forfeits only for logging purposes, they shouldn't matter anymore
            // completion time is set by the constructor

            c.getClient().getSession().write(MaplePacketCreator.showSpecialEffect(10)); // Quest completion
            c.getMap().broadcastMessage(c, MaplePacketCreator.showSpecialEffect(c.getId(), 10), false);
        } else {
            if (npc != 9010000) {
                for (MapleQuestAction a : completeActs) {
                    if (!a.checkEnd(c, selection)) {
                        return;
                    }
                }
                forceComplete(c, npc);
                for (MapleQuestAction a : completeActs) {
                    a.runEnd(c, selection);
                }
            }
            // we save forfeits only for logging purposes, they shouldn't matter anymore
            // completion time is set by the constructor

            c.getClient().getSession().write(MaplePacketCreator.showSpecialEffect(10)); // Quest completion
            c.getMap().broadcastMessage(c, MaplePacketCreator.showSpecialEffect(c.getId(), 10), false);
        }
    }

    public void forfeit(MapleCharacter c) {
        if (c.getQuest(this).getStatus() != (byte) 1) {
            return;
        }
        final MapleQuestStatus oldStatus = c.getQuest(this);
        final MapleQuestStatus newStatus = new MapleQuestStatus(this, (byte) 0);
        newStatus.setForfeited(oldStatus.getForfeited() + 1);
        newStatus.setCompletionTime(oldStatus.getCompletionTime());
        c.updateQuest(newStatus);
    }

    public void forceStart(MapleCharacter c, int npc, String customData) {
        final MapleQuestStatus newStatus = new MapleQuestStatus(this, (byte) 1, npc);
        newStatus.setForfeited(c.getQuest(this).getForfeited());
        newStatus.setCompletionTime(c.getQuest(this).getCompletionTime());
        newStatus.setCustomData(customData);
        c.updateQuest(newStatus);
    }

    public void forceComplete(MapleCharacter c, int npc) {
        final MapleQuestStatus newStatus = new MapleQuestStatus(this, (byte) 2, npc);
        newStatus.setForfeited(c.getQuest(this).getForfeited());
        c.updateQuest(newStatus);
        c.getClient().getSession().write(MaplePacketCreator.showSpecialEffect(10)); // Quest completion
        c.getMap().broadcastMessage(c, MaplePacketCreator.showSpecialEffect(c.getId(), 10), false);
    }

    public int getId() {
        return id;
    }

    public Map<Integer, Integer> getRelevantMobs() {
        return relevantMobs;
    }

    private boolean checkNPCOnMap(MapleCharacter player, int npcid) {
        //mir = 1013000
        return (GameConstants.isEvan(player.getJob()) && npcid == 1013000) || (player.getMap() != null && player.getMap().containsNPC(npcid));
    }

    public int getMedalItem() {
        return viewMedalItem;
    }

    public static enum MedalQuest {

        新手冒险家(29005, 29015, 15, new int[]{104000000, 104010001, 100000006, 104020000, 100000000, 100010000, 100040000, 100040100, 101010103, 101020000, 101000000, 102000000, 101030104, 101030406, 102020300, 103000000, 102050000, 103010001, 103030200, 110000000}),
        ElNath(29006, 29012, 50, new int[]{200000000, 200010100, 200010300, 200080000, 200080100, 211000000, 211030000, 211040300, 211040400, 211040401}),
        LudusLake(29007, 29012, 40, new int[]{222000000, 222010400, 222020000, 220000000, 220020300, 220040200, 221020701, 221000000, 221030600, 221040400}),
        Underwater(29008, 29012, 40, new int[]{230000000, 230010400, 230010200, 230010201, 230020000, 230020201, 230030100, 230040000, 230040200, 230040400}),
        MuLung(29009, 29012, 50, new int[]{251000000, 251010200, 251010402, 251010500, 250010500, 250010504, 250000000, 250010300, 250010304, 250020300}),
        NihalDesert(29010, 29012, 70, new int[]{261030000, 261020401, 261020000, 261010100, 261000000, 260020700, 260020300, 260000000, 260010600, 260010300}),
        MinarForest(29011, 29012, 70, new int[]{240000000, 240010200, 240010800, 240020401, 240020101, 240030000, 240040400, 240040511, 240040521, 240050000}),
        Sleepywood(29014, 29015, 50, new int[]{105040300, 105070001, 105040305, 105090200, 105090300, 105090301, 105090312, 105090500, 105090900, 105080000});
        public int questid, level, lquestid;
        public int[] maps;

        private MedalQuest(int questid, int lquestid, int level, int[] maps) {
            this.questid = questid; //infoquest = questid -2005, customdata = questid -1995
            this.level = level;
            this.lquestid = lquestid;
            this.maps = maps; //note # of maps
        }
    }
}
