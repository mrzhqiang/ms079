package server.quest;

import client.ISkill;
import client.MapleCharacter;
import client.MapleQuestStatus;
import client.MapleStat;
import client.Skill;
import client.SkillFactory;
import client.inventory.InventoryException;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import provider.MapleDataTool;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.RandomRewards;
import server.Randomizer;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.Triple;

public class MapleQuestAction
        implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    private MapleQuestActionType type;
    private MapleQuest quest;
    private int intStore = 0;
    private List<Integer> applicableJobs = new ArrayList();
    private List<QuestItem> items = null;
    private List<Triple<Integer, Integer, Integer>> skill = null;
    private List<Pair<Integer, Integer>> state = null;

    public MapleQuestAction(MapleQuestActionType type, ResultSet rse, MapleQuest quest, PreparedStatement pss, PreparedStatement psq, PreparedStatement psi)
            throws SQLException {
        this.type = type;
        this.quest = quest;

        this.intStore = rse.getInt("intStore");
        String[] jobs = rse.getString("applicableJobs").split(", ");
        if ((jobs.length <= 0) && (rse.getString("applicableJobs").length() > 0)) {
            this.applicableJobs.add(Integer.valueOf(Integer.parseInt(rse.getString("applicableJobs"))));
        }
        for (String j : jobs) {
            if (j.length() > 0) {
                this.applicableJobs.add(Integer.valueOf(Integer.parseInt(j)));
            }
        }
        ResultSet rs;
        switch (type) {
            case item:
                this.items = new ArrayList<QuestItem>();
                psi.setInt(1, rse.getInt("uniqueid"));
                rs = psi.executeQuery();
                while (rs.next()) {
                    this.items.add(new QuestItem(rs.getInt("itemid"), rs.getInt("count"), rs.getInt("period"), rs.getInt("gender"), rs.getInt("job"), rs.getInt("jobEx"), rs.getInt("prop")));//, rs.getInt("jobEx")
                }
                rs.close();
                break;
            case quest:
                this.state = new ArrayList<Pair<Integer, Integer>>();
                psq.setInt(1, rse.getInt("uniqueid"));
                rs = psq.executeQuery();
                while (rs.next()) {
                    this.state.add(new Pair(Integer.valueOf(rs.getInt("quest")), Integer.valueOf(rs.getInt("state"))));
                }
                rs.close();
                break;
            case skill:
                this.skill = new ArrayList<Triple<Integer, Integer, Integer>>();
                pss.setInt(1, rse.getInt("uniqueid"));
                rs = pss.executeQuery();
                while (rs.next()) {
                    this.skill.add(new Triple(Integer.valueOf(rs.getInt("skillid")), Integer.valueOf(rs.getInt("skillLevel")), Integer.valueOf(rs.getInt("masterLevel"))));
                }
                rs.close();
        }
    }

    private static boolean canGetItem(QuestItem item, MapleCharacter chr) {
        if (item.gender != 2 && item.gender >= 0 && item.gender != chr.getGender()) {
            return false;
        }
        if (item.job > 0) {
            List<Integer> code = getJobBy5ByteEncoding(item.job);
            boolean jobFound = false;
            for (int codec : code) {
                if (codec / 100 == chr.getJob() / 100) {
                    jobFound = true;
                    break;
                }
            }
            if (!jobFound && item.jobEx > 0) {
                List<Integer> codeEx = getJobBySimpleEncoding(item.jobEx);
                for (int codec : codeEx) {
                    if ((codec / 100 % 10) == (chr.getJob() / 100 % 10)) {
                        jobFound = true;
                        break;
                    }
                }
            }
            return jobFound;
        }
        return true;
    }

    /*
    private static boolean canGetItem(QuestItem item, MapleCharacter c) {
        if ((item.gender != 2) && (item.gender >= 0) && (item.gender != c.getGender())) {
            return false;
        }
        if (item.job > 0) {
            List code = getJobBy5ByteEncoding(item.job);
            boolean jobFound = false;
            for (Iterator i$ = code.iterator(); i$.hasNext();) {
                int codec = ((Integer) i$.next()).intValue();
                if (codec / 100 == c.getJob() / 100) {
                    jobFound = true;
                    break;
                }
            }
            Iterator i$;
            if ((!jobFound) && (item.jobEx > 0)) {
                List codeEx = getJobBySimpleEncoding(item.jobEx);
                for (i$ = codeEx.iterator(); i$.hasNext();) {
                    int codec = ((Integer) i$.next()).intValue();
                    if (codec / 100 % 10 == c.getJob() / 100 % 10) {
                        jobFound = true;
                        break;
                    }
                }
            }
            return jobFound;
        }
        return true;
    }*/
    public boolean RestoreLostItem(MapleCharacter c, int itemid) {
        if (this.type == MapleQuestActionType.item) {
            for (QuestItem item : this.items) {
                if (item.itemid == itemid) {
                    if (!c.haveItem(item.itemid, item.count, true, false)) {
                        MapleInventoryManipulator.addById(c.getClient(), item.itemid, (short) item.count, "从新获得任务道具 " + this.quest.getId() + " on " + FileoutputUtil.CurrentReadable_Date());
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void runStart(MapleCharacter c, Integer extSelection) {
        MapleQuestStatus status;
        int selection;
        int extNum;
        switch (type) {
            case exp:
                status = c.getQuest(this.quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                c.gainExp(this.intStore, true, true, true);//完成任务经验
                //c.gainExp(this.intStore * GameConstants.getExpRate_Quest(c.getLevel()) * c.getStat().questBonus * (c.getTrait(MapleTrait.MapleTraitType.sense).getLevel() * 3 / 10 + 100) / 100, true, true, true);
                break;
            case item:
                Map props = new HashMap();
                for (QuestItem item : this.items) {
                    if ((item.prop > 0) && (canGetItem(item, c))) {
                        for (int i = 0; i < item.prop; i++) {
                            props.put(Integer.valueOf(props.size()), Integer.valueOf(item.itemid));
                        }
                    }
                }
                selection = 0;
                extNum = 0;
                if (props.size() > 0) {
                    selection = ((Integer) props.get(Integer.valueOf(Randomizer.nextInt(props.size())))).intValue();
                }
                for (QuestItem item : this.items) {
                    if (!canGetItem(item, c)) {
                        continue;
                    }
                    int id = item.itemid;
                    if ((item.prop != -2)
                            && (item.prop == -1
                                    ? (extSelection != null) || (extSelection.intValue() != extNum++)
                                    : id != selection)) {
                        continue;
                    }
                    short count = (short) item.count;
                    if (count < 0) {
                        try {
                            MapleInventoryManipulator.removeById(c.getClient(), GameConstants.getInventoryType(id), id, count * -1, true, false);
                        } catch (InventoryException ie) {
                            System.err.println("[h4x] Completing a quest without meeting the requirements" + ie);
                        }
                        c.getClient().getSession().write(MaplePacketCreator.getShowItemGain(id, count, true));
                    } else {
                        int period = item.period / 1440;
                        String name = MapleItemInformationProvider.getInstance().getName(id);
                        if ((id / 10000 == 114) && (name != null) && (name.length() > 0)) {
                            String msg = "恭喜您获得勋章 <" + name + ">";
                            c.dropMessage(-1, msg);
                            c.dropMessage(5, msg);
                        }
                        MapleInventoryManipulator.addById(c.getClient(), id, count, "任务获得 " + this.quest.getId() + " 时间: " + FileoutputUtil.CurrentReadable_Date());
                        //MapleInventoryManipulator.addById(c.getClient(), id, count, "", null, period, "任务获得 " + this.quest.getId() + " 时间: " + FileoutputUtil.CurrentReadable_Date());
                        c.getClient().getSession().write(MaplePacketCreator.getShowItemGain(id, count, true));
                    }
                }
                break;
            case nextQuest:
                status = c.getQuest(this.quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                c.getClient().getSession().write(MaplePacketCreator.updateQuestFinish(this.quest.getId(), status.getNpc(), this.intStore));
                break;
            case money:
                status = c.getQuest(this.quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                c.gainMeso(this.intStore, true, true);
                break;
            case quest:
                for (Pair<Integer, Integer> q : this.state) {
                    c.updateQuest(new MapleQuestStatus(MapleQuest.getInstance(q.left), q.right));
                }
                break;
            case skill:
                for (Triple skills : this.skill) {
                    int skillid = ((Integer) skills.left).intValue();
                    int skillLevel = ((Integer) skills.mid).intValue();
                    int masterLevel = ((Integer) skills.right).intValue();
                    ISkill skillObject = SkillFactory.getSkill(skillid);
                    boolean found = false;
                    for (Iterator i$ = this.applicableJobs.iterator(); i$.hasNext();) {
                        int applicableJob = ((Integer) i$.next()).intValue();
                        if (c.getJob() == applicableJob) {
                            found = true;
                            break;
                        }
                    }
                    if ((skillObject.isBeginnerSkill()) || (found)) {
                        c.changeSkillLevel(skillObject, (byte) Math.max(skillLevel, c.getSkillLevel(skillObject)), (byte) Math.max(masterLevel, c.getMasterLevel(skillObject)));
                    }
                }
                break;
            case pop:
                status = c.getQuest(this.quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                int fameGain = this.intStore;
                c.addFame(fameGain);
                c.updateSingleStat(MapleStat.FAME, c.getFame());
                c.getClient().getSession().write(MaplePacketCreator.getShowFameGain(fameGain));
                break;
            case buffItemID:
                status = c.getQuest(this.quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                int tobuff = this.intStore;
                if (tobuff <= 0) {
                    break;
                }
                MapleItemInformationProvider.getInstance().getItemEffect(tobuff).applyTo(c);
                break;
            case infoNumber:
                break;
            case sp:
                status = c.getQuest(this.quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                int sp_val = this.intStore;
                if (this.applicableJobs.size() > 0) {
                    int finalJob = 0;
                    for (Iterator i$ = this.applicableJobs.iterator(); i$.hasNext();) {
                        int job_val = ((Integer) i$.next()).intValue();
                        if ((c.getJob() >= job_val) && (job_val > finalJob)) {
                            finalJob = job_val;
                        }
                    }
                    if (finalJob == 0) {
                        c.gainSP(sp_val);
                    } else {
                        c.gainSP(sp_val, GameConstants.getSkillBook(finalJob));
                    }
                } else {
                    c.gainSP(sp_val);
                }
                break;
            /*case charmEXP:
            case charismaEXP:
            case craftEXP:
            case insightEXP:
            case senseEXP:
            case willEXP:
                status = c.getQuest(this.quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                c.getTrait(MapleTrait.MapleTraitType.getByQuestName(this.type.name())).addExp(this.intStore, c);
                break;*/
        }
    }

    public boolean checkEnd(MapleCharacter c, Integer extSelection) {
        switch (this.type) {
            case item:
                Map props = new HashMap();

                for (QuestItem item : this.items) {
                    if ((item.prop > 0) && (canGetItem(item, c))) {
                        for (int i = 0; i < item.prop; i++) {
                            props.put(Integer.valueOf(props.size()), Integer.valueOf(item.itemid));
                        }
                    }
                }
                int selection = 0;
                int extNum = 0;
                if (props.size() > 0) {
                    selection = ((Integer) props.get(Integer.valueOf(Randomizer.nextInt(props.size())))).intValue();
                }
                byte eq = 0;
                byte use = 0;
                byte setup = 0;
                byte etc = 0;
                byte cash = 0;

                for (QuestItem item : this.items) {
                    if (!canGetItem(item, c)) {
                        continue;
                    }
                    int id = item.itemid;
                    if ((item.prop != -2)
                            && (item.prop == -1
                                    ? (extSelection != null) || (extSelection.intValue() != extNum++)
                                    : id != selection)) {
                        continue;
                    }
                    short count = (short) item.count;
                    if (count < 0) {
                        if (!c.haveItem(id, count, false, true)) {
                            c.dropMessage(1, "You are short of some item to complete quest.");
                            return false;
                        }
                    } else {
                        if ((MapleItemInformationProvider.getInstance().isPickupRestricted(id)) && (c.haveItem(id, 1, true, false))) {
                            c.dropMessage(1, "You have this item already: " + MapleItemInformationProvider.getInstance().getName(id));
                            return false;
                        }
                        switch (GameConstants.getInventoryType(id)) {
                            case EQUIP:
                                eq++;
                                break;
                            case USE:
                                use++;
                                break;
                            case SETUP:
                                setup++;
                                break;
                            case ETC:
                                etc++;
                                break;
                            case CASH:
                                cash++;
                                break;
                        }
                    }
                }

                if (c.getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < eq) {
                    c.dropMessage(1, "装备栏空间不足.");
                    return false;
                }
                if (c.getInventory(MapleInventoryType.USE).getNumFreeSlot() < use) {
                    c.dropMessage(1, "消耗栏空间不足.");
                    return false;
                }
                if (c.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() < setup) {
                    c.dropMessage(1, "设置栏空间不足.");
                    return false;
                }
                if (c.getInventory(MapleInventoryType.ETC).getNumFreeSlot() < etc) {
                    c.dropMessage(1, "其他栏空间不足.");
                    return false;
                }
                if (c.getInventory(MapleInventoryType.CASH).getNumFreeSlot() < cash) {
                    c.dropMessage(1, "特殊栏空间不足.");
                    return false;
                }
                return true;
            case money:
                int meso = this.intStore;
                if (c.getMeso() + meso < 0) {
                    c.dropMessage(1, "携带金币数量已达限制.");
                    return false;
                }
                if ((meso < 0) && (c.getMeso() < Math.abs(meso))) {
                    c.dropMessage(1, "金币不足.");
                    return false;
                }
                return true;
        }

        return true;
    }

    public void runEnd(MapleCharacter c, Integer extSelection) {
        int selection;
        int extNum;
        switch (type) {
            case exp:
                c.gainExp(this.intStore, true, true, true);
                // c.gainExp(this.intStore * GameConstants.getExpRate_Quest(c.getLevel()) * c.getStat().questBonus * (c.getTrait(MapleTrait.MapleTraitType.sense).getLevel() * 3 / 10 + 100) / 100, true, true, true);
                break;
            case item:
                Map props = new HashMap();
                for (QuestItem item : this.items) {
                    if ((item.prop > 0) && (canGetItem(item, c))) {
                        for (int i = 0; i < item.prop; i++) {
                            props.put(Integer.valueOf(props.size()), Integer.valueOf(item.itemid));
                        }
                    }
                }
                selection = 0;
                extNum = 0;
                if (props.size() > 0) {
                    selection = ((Integer) props.get(Integer.valueOf(Randomizer.nextInt(props.size())))).intValue();
                }
                for (QuestItem item : this.items) {
                    if (!canGetItem(item, c)) {
                        continue;
                    }
                    int id = item.itemid;
                    if ((item.prop != -2)
                            && (item.prop == -1
                                    ? (extSelection != null) || (extSelection.intValue() != extNum++)
                                    : id != selection)) {
                        continue;
                    }
                    short count = (short) item.count;
                    if (count < 0) {
                        MapleInventoryManipulator.removeById(c.getClient(), GameConstants.getInventoryType(id), id, count * -1, true, false);
                        c.getClient().getSession().write(MaplePacketCreator.getShowItemGain(id, count, true));
                    } else {
                        int period = item.period / 1440;
                        String name = MapleItemInformationProvider.getInstance().getName(id);
                        if ((id / 10000 == 114) && (name != null) && (name.length() > 0)) {
                            String msg = "你获得了勋章 <" + name + ">";
                            c.dropMessage(-1, msg);
                            c.dropMessage(5, msg);
                        }
                        MapleInventoryManipulator.addById(c.getClient(), id, count, period + " 时间 " + FileoutputUtil.CurrentReadable_Date());
                        c.getClient().getSession().write(MaplePacketCreator.getShowItemGain(id, count, true));
                    }
                }
                break;
            case nextQuest:
                c.getClient().getSession().write(MaplePacketCreator.updateQuestFinish(this.quest.getId(), c.getQuest(this.quest).getNpc(), this.intStore));
                break;
            case money:
                c.gainMeso(this.intStore, true, true);
                break;
            case quest:
                for (Pair<Integer, Integer> q : this.state) {
                    c.updateQuest(new MapleQuestStatus(MapleQuest.getInstance(q.left), (q.right)));
                }
                break;
            case skill:
                for (Triple skills : this.skill) {
                    int skillid = ((Integer) skills.left).intValue();
                    int skillLevel = ((Integer) skills.mid).intValue();
                    int masterLevel = ((Integer) skills.right).intValue();
                    ISkill skillObject = SkillFactory.getSkill(skillid);
                    boolean found = false;
                    for (int applicableJob : applicableJobs) {
                        if (c.getJob() == applicableJob) {
                            found = true;
                            break;
                        }
                    }
                    if ((skillObject.isBeginnerSkill()) || (found)) {
                        c.changeSkillLevel(skillObject, (byte) Math.max(skillLevel, c.getSkillLevel(skillObject)), (byte) Math.max(masterLevel, c.getMasterLevel(skillObject)));
                    }
                }
                break;
            case pop:
                int fameGain = this.intStore;
                c.addFame(fameGain);
                c.updateSingleStat(MapleStat.FAME, c.getFame());
                c.getClient().getSession().write(MaplePacketCreator.getShowFameGain(fameGain));
                break;
            case buffItemID:
                int tobuff = this.intStore;
                if (tobuff <= 0) {
                    break;
                }
                MapleItemInformationProvider.getInstance().getItemEffect(tobuff).applyTo(c);
                break;
            case infoNumber:
                break;
            case sp:
                int sp_val = this.intStore;
                if (this.applicableJobs.size() > 0) {
                    int finalJob = 0;
                    for (Iterator i$ = this.applicableJobs.iterator(); i$.hasNext();) {
                        int job_val = ((Integer) i$.next()).intValue();
                        if ((c.getJob() >= job_val) && (job_val > finalJob)) {
                            finalJob = job_val;
                        }
                    }
                    if (finalJob == 0) {
                        c.gainSP(sp_val);
                    } else {
                        c.gainSP(sp_val, GameConstants.getSkillBook(finalJob));
                    }
                } else {
                    c.gainSP(sp_val);
                }
                break;
            //一些经验处理
            /* case charmEXP:
            case charismaEXP:
            case craftEXP:
            case insightEXP:
            case senseEXP:
            case willEXP:
                c.getTrait(MapleTrait.MapleTraitType.getByQuestName(this.type.name())).addExp(this.intStore, c);
                break;*/
        }
    }

    private static List<Integer> getJobBy5ByteEncoding(int encoded) {
        List ret = new ArrayList();
        if ((encoded & 0x1) != 0) {
            ret.add(Integer.valueOf(0));
        }
        if ((encoded & 0x2) != 0) {
            ret.add(Integer.valueOf(100));
        }
        if ((encoded & 0x4) != 0) {
            ret.add(Integer.valueOf(200));
        }
        if ((encoded & 0x8) != 0) {
            ret.add(Integer.valueOf(300));
        }
        if ((encoded & 0x10) != 0) {
            ret.add(Integer.valueOf(400));
        }
        if ((encoded & 0x20) != 0) {
            ret.add(Integer.valueOf(500));
        }
        if ((encoded & 0x400) != 0) {
            ret.add(Integer.valueOf(1000));
        }
        if ((encoded & 0x800) != 0) {
            ret.add(Integer.valueOf(1100));
        }
        if ((encoded & 0x1000) != 0) {
            ret.add(Integer.valueOf(1200));
        }
        if ((encoded & 0x2000) != 0) {
            ret.add(Integer.valueOf(1300));
        }
        if ((encoded & 0x4000) != 0) {
            ret.add(Integer.valueOf(1400));
        }
        if ((encoded & 0x8000) != 0) {
            ret.add(Integer.valueOf(1500));
        }
        if ((encoded & 0x20000) != 0) {
            ret.add(Integer.valueOf(2001));
            ret.add(Integer.valueOf(2200));
        }
        if ((encoded & 0x100000) != 0) {
            ret.add(Integer.valueOf(2000));
            ret.add(Integer.valueOf(2001));
        }
        if ((encoded & 0x200000) != 0) {
            ret.add(Integer.valueOf(2100));
        }
        if ((encoded & 0x400000) != 0) {
            ret.add(Integer.valueOf(2001));
            ret.add(Integer.valueOf(2200));
        }
        if ((encoded & 0x40000000) != 0) {
            ret.add(Integer.valueOf(3000));
            ret.add(Integer.valueOf(3200));
            ret.add(Integer.valueOf(3300));
            ret.add(Integer.valueOf(3500));
        }
        return ret;
    }

    private static List<Integer> getJobBySimpleEncoding(int encoded) {
        List ret = new ArrayList();
        if ((encoded & 0x1) != 0) {
            ret.add(Integer.valueOf(200));
        }
        if ((encoded & 0x2) != 0) {
            ret.add(Integer.valueOf(300));
        }
        if ((encoded & 0x4) != 0) {
            ret.add(Integer.valueOf(400));
        }
        if ((encoded & 0x8) != 0) {
            ret.add(Integer.valueOf(500));
        }
        return ret;
    }

    public MapleQuestActionType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }

    public List<Triple<Integer, Integer, Integer>> getSkills() {
        return this.skill;
    }

    public List<QuestItem> getItems() {
        return this.items;
    }

    public static class QuestItem {

        public int itemid;
        public int count;
        public int period;
        public int gender;
        public int job;
        public int jobEx;
        public int prop;

        public QuestItem(int itemid, int count, int period, int gender, int job, int jobEx, int prop) {//, int jobEx
            /*if (RandomRewards.getTenPercent().contains(itemid)) {
                count += Randomizer.nextInt(3);
            }*/
            this.itemid = itemid;
            this.count = count;
            this.period = period;
            this.gender = gender;
            this.job = job;
            this.jobEx = jobEx;
            this.prop = prop;
        }
    }
}
/*
package server.quest;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

import client.ISkill;
import constants.GameConstants;
import client.inventory.InventoryException;
import client.MapleCharacter;
import client.inventory.MapleInventoryType;
import client.MapleQuestStatus;
import client.MapleStat;
import client.SkillFactory;
import java.util.ArrayList;
import java.util.List;
import provider.MapleData;
import provider.MapleDataTool;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Randomizer;
import tools.MaplePacketCreator;
import tools.packet.UIPacket;

public class MapleQuestAction implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    private MapleQuestActionType type;
    private MapleData data;
    private MapleQuest quest;

 
    public MapleQuestAction(MapleQuestActionType type, MapleData data, MapleQuest quest) {
        this.type = type;
        this.data = data;
        this.quest = quest;
    }

    private static boolean canGetItem(MapleData item, MapleCharacter c) {
        if (item.getChildByPath("gender") != null) {
            final int gender = MapleDataTool.getInt(item.getChildByPath("gender"));
            if (gender != 2 && gender != c.getGender()) {
                return false;
            }
        }
        if (item.getChildByPath("job") != null) {
            final int job = MapleDataTool.getInt(item.getChildByPath("job"));
            final List<Integer> code = getJobBy5ByteEncoding(job);
            boolean jobFound = false;
            for (int codec : code) {
                if (codec / 100 == c.getJob() / 100) {
                    jobFound = true;
                    break;
                }
            }
            if (!jobFound && item.getChildByPath("jobEx") != null) {
                final int jobEx = MapleDataTool.getInt(item.getChildByPath("jobEx"));
                final List<Integer> codeEx = getJobBy5ByteEncoding(jobEx);
                for (int codec : codeEx) {
                    if (codec / 100 == c.getJob() / 100) {
                        jobFound = true;
                        break;
                    }
                }
            }
            return jobFound;
        }
        return true;
    }

    public final boolean RestoreLostItem(final MapleCharacter c, final int itemid) {
        if (type == MapleQuestActionType.item) {
            int retitem;

            for (final MapleData iEntry : data.getChildren()) {
                retitem = MapleDataTool.getInt(iEntry.getChildByPath("id"), -1);
                if (retitem == itemid) {
                    if (!c.haveItem(retitem, 1, true, false)) {
                        MapleInventoryManipulator.addById(c.getClient(), retitem, (short) 1, (byte) 0);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void runStart(MapleCharacter c, Integer extSelection) {
        MapleQuestStatus status;
        switch (type) {
            case exp:
                status = c.getQuest(quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                c.gainExp(MapleDataTool.getInt(data, 0) * GameConstants.getExpRate_Quest(c.getLevel()), true, true, true);
                break;
            case item:
                // first check for randomness in item selection
                Map<Integer, Integer> props = new HashMap<Integer, Integer>();
                MapleData prop;
                for (MapleData iEntry : data.getChildren()) {
                    prop = iEntry.getChildByPath("prop");
                    if (prop != null && MapleDataTool.getInt(prop) != -1 && canGetItem(iEntry, c)) {
                        for (int i = 0; i < MapleDataTool.getInt(iEntry.getChildByPath("prop")); i++) {
                            props.put(props.size(), MapleDataTool.getInt(iEntry.getChildByPath("id")));
                        }
                    }
                }
                int selection = 0;
                int extNum = 0;
                if (props.size() > 0) {
                    selection = props.get(Randomizer.nextInt(props.size()));
                }
                for (MapleData iEntry : data.getChildren()) {
                    if (!canGetItem(iEntry, c)) {
                        continue;
                    }
                    final int id = MapleDataTool.getInt(iEntry.getChildByPath("id"), -1);
                    if (iEntry.getChildByPath("prop") != null) {
                        if (MapleDataTool.getInt(iEntry.getChildByPath("prop")) == -1) {
                            if (extSelection != extNum++) {
                                continue;
                            }
                        } else if (id != selection) {
                            continue;
                        }
                    }
                    final short count = (short) MapleDataTool.getInt(iEntry.getChildByPath("count"), 1);
                    if (count < 0) { // remove items
                        try {
                            MapleInventoryManipulator.removeById(c.getClient(), GameConstants.getInventoryType(id), id, (count * -1), true, false);
                        } catch (InventoryException ie) {
                            // it's better to catch this here so we'll atleast try to remove the other items
                            System.err.println("[h4x] Completing a quest without meeting the requirements" + ie);
                        }
                        c.getClient().getSession().write(MaplePacketCreator.getShowItemGain(id, count, true));
                    } else { // add items
                        final int period = MapleDataTool.getInt(iEntry.getChildByPath("period"), 0) / 1440; //im guessing.
                        final String name = MapleItemInformationProvider.getInstance().getName(id);
                        if (id / 10000 == 114 && name != null && name.length() > 0) { //medal
                            final String msg = "你已獲得稱號 <" + name + ">";
                            c.dropMessage(5, msg);
                            c.dropMessage(5, msg);
                        }
                        MapleInventoryManipulator.addById(c.getClient(), id, count, "", null, period, (byte) 0);
                        c.getClient().getSession().write(MaplePacketCreator.getShowItemGain(id, count, true));
                    }
                }
                break;
            case nextQuest:
                status = c.getQuest(quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                c.getClient().getSession().write(MaplePacketCreator.updateQuestFinish(quest.getId(), status.getNpc(), MapleDataTool.getInt(data)));
                break;
            case money:
                status = c.getQuest(quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                c.gainMeso(MapleDataTool.getInt(data, 0), true, false, true);
                break;
            case quest:
                for (MapleData qEntry : data) {
                    c.updateQuest(
                            new MapleQuestStatus(MapleQuest.getInstance(MapleDataTool.getInt(qEntry.getChildByPath("id"))),
                                    (byte) MapleDataTool.getInt(qEntry.getChildByPath("state"), 0)));
                }
                break;
            case skill:
                //TODO needs gain/lost message?
                for (MapleData sEntry : data) {
                    final int skillid = MapleDataTool.getInt(sEntry.getChildByPath("id"));
                    int skillLevel = MapleDataTool.getInt(sEntry.getChildByPath("skillLevel"), 0);
                    int masterLevel = MapleDataTool.getInt(sEntry.getChildByPath("masterLevel"), 0);
                    final ISkill skillObject = SkillFactory.getSkill(skillid);

                    for (MapleData applicableJob : sEntry.getChildByPath("job")) {
                        if (skillObject.isBeginnerSkill() || c.getJob() == MapleDataTool.getInt(applicableJob)) {
                            c.changeSkillLevel(skillObject,
                                    (byte) Math.max(skillLevel, c.getSkillLevel(skillObject)),
                                    (byte) Math.max(masterLevel, c.getMasterLevel(skillObject)));
                            break;
                        }
                    }
                }
                break;
            case pop:
                status = c.getQuest(quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                final int fameGain = MapleDataTool.getInt(data, 0);
                c.addFame(fameGain);
                c.updateSingleStat(MapleStat.FAME, c.getFame());
                c.getClient().getSession().write(MaplePacketCreator.getShowFameGain(fameGain));
                break;
            case buffItemID:
                status = c.getQuest(quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                final int tobuff = MapleDataTool.getInt(data, -1);
                if (tobuff == -1) {
                    break;
                }
                MapleItemInformationProvider.getInstance().getItemEffect(tobuff).applyTo(c);
                break;
            case infoNumber: {
//		System.out.println("quest : "+MapleDataTool.getInt(data, 0)+"");
//		MapleQuest.getInstance(MapleDataTool.getInt(data, 0)).forceComplete(c, 0);
                break;
            }
            case sp: {
                status = c.getQuest(quest);
                if (status.getForfeited() > 0) {
                    break;
                }
                for (MapleData iEntry : data.getChildren()) {
                    final int sp_val = MapleDataTool.getInt(iEntry.getChildByPath("sp_value"), 0);
                    if (iEntry.getChildByPath("job") != null) {
                        int finalJob = 0;
                        for (MapleData jEntry : iEntry.getChildByPath("job").getChildren()) {
                            final int job_val = MapleDataTool.getInt(jEntry, 0);
                            if (c.getJob() >= job_val && job_val > finalJob) {
                                finalJob = job_val;
                            }
                        }
                        if (finalJob == 0) {
                            c.gainSP(sp_val);
                        } else {
                            c.gainSP(sp_val, GameConstants.getSkillBook(finalJob));
                        }
                    } else {
                        c.gainSP(sp_val);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    public boolean checkEnd(MapleCharacter c, Integer extSelection) {
        switch (type) {
            case item: {
                // first check for randomness in item selection
                final Map<Integer, Integer> props = new HashMap<Integer, Integer>();

                for (MapleData iEntry : data.getChildren()) {
                    final MapleData prop = iEntry.getChildByPath("prop");
                    if (prop != null && MapleDataTool.getInt(prop) != -1 && canGetItem(iEntry, c)) {
                        for (int i = 0; i < MapleDataTool.getInt(iEntry.getChildByPath("prop")); i++) {
                            props.put(props.size(), MapleDataTool.getInt(iEntry.getChildByPath("id")));
                        }
                    }
                }
                int selection = 0;
                int extNum = 0;
                if (props.size() > 0) {
                    selection = props.get(Randomizer.nextInt(props.size()));
                }
                byte eq = 0, use = 0, setup = 0, etc = 0, cash = 0;

                for (MapleData iEntry : data.getChildren()) {
                    if (!canGetItem(iEntry, c)) {
                        continue;
                    }
                    final int id = MapleDataTool.getInt(iEntry.getChildByPath("id"), -1);
                    if (iEntry.getChildByPath("prop") != null) {
                        if (MapleDataTool.getInt(iEntry.getChildByPath("prop")) == -1) {
                            if (extSelection != extNum++) {
                                continue;
                            }
                        } else if (id != selection) {
                            continue;
                        }
                    }
                    final short count = (short) MapleDataTool.getInt(iEntry.getChildByPath("count"), 1);
                    if (count < 0) { // remove items
                        if (!c.haveItem(id, count, false, true)) {
                            c.dropMessage(1, "You are short of some item to complete quest.");
                            return false;
                        }
                    } else { // add items
                        if (MapleItemInformationProvider.getInstance().isPickupRestricted(id) && c.haveItem(id, 1, true, false)) {
                            c.dropMessage(1, "You have this item already: " + MapleItemInformationProvider.getInstance().getName(id));
                            return false;
                        }
                        switch (GameConstants.getInventoryType(id)) {
                            case EQUIP:
                                eq++;
                                break;
                            case USE:
                                use++;
                                break;
                            case SETUP:
                                setup++;
                                break;
                            case ETC:
                                etc++;
                                break;
                            case CASH:
                                cash++;
                                break;
                        }
                    }
                }
                if (c.getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < eq) {
                    c.dropMessage(1, "请为您的装备栏腾出空间.");
                    return false;
                } else if (c.getInventory(MapleInventoryType.USE).getNumFreeSlot() < use) {
                    c.dropMessage(1, "请为您的消耗栏腾出空间.");
                    return false;
                } else if (c.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() < setup) {
                    c.dropMessage(1, "请为您的设置栏腾出空间.");
                    return false;
                } else if (c.getInventory(MapleInventoryType.ETC).getNumFreeSlot() < etc) {
                    c.dropMessage(1, "请为您的其他栏腾出空间.");
                    return false;
                } else if (c.getInventory(MapleInventoryType.CASH).getNumFreeSlot() < cash) {
                    c.dropMessage(1, "请为您的特殊栏腾出空间.");
                    return false;
                }
                return true;
            }
            case money: {
                final int meso = MapleDataTool.getInt(data, 0);
                if (c.getMeso() + meso < 0) { // Giving, overflow
                    c.dropMessage(1, "Meso exceed the max amount, 2147483647.");
                    return false;
                } else if (meso < 0 && c.getMeso() < Math.abs(meso)) { //remove meso
                    c.dropMessage(1, "Insufficient meso.");
                    return false;
                }
                return true;
            }
        }
        return true;
    }

    public void runEnd(MapleCharacter c, Integer extSelection) {
        switch (type) {
            case exp: {
                c.gainExp(MapleDataTool.getInt(data, 0) * GameConstants.getExpRate_Quest(c.getLevel()), true, true, true);
                break;
            }
            case item: {
                // first check for randomness in item selection
                Map<Integer, Integer> props = new HashMap<Integer, Integer>();

                for (MapleData iEntry : data.getChildren()) {
                    final MapleData prop = iEntry.getChildByPath("prop");
                    if (prop != null && MapleDataTool.getInt(prop) != -1 && canGetItem(iEntry, c)) {
                        for (int i = 0; i < MapleDataTool.getInt(iEntry.getChildByPath("prop")); i++) {
                            props.put(props.size(), MapleDataTool.getInt(iEntry.getChildByPath("id")));
                        }
                    }
                }
                int selection = 0;
                int extNum = 0;
                if (props.size() > 0) {
                    selection = props.get(Randomizer.nextInt(props.size()));
                }
                for (MapleData iEntry : data.getChildren()) {
                    if (!canGetItem(iEntry, c)) {
                        continue;
                    }
                    final int id = MapleDataTool.getInt(iEntry.getChildByPath("id"), -1);
                    if (iEntry.getChildByPath("prop") != null) {
                        if (MapleDataTool.getInt(iEntry.getChildByPath("prop")) == -1) {
                            if (extSelection != extNum++) {
                                continue;
                            }
                        } else if (id != selection) {
                            continue;
                        }
                    }
                    final short count = (short) MapleDataTool.getInt(iEntry.getChildByPath("count"), 1);
                    if (count < 0) { // remove items
                        MapleInventoryManipulator.removeById(c.getClient(), GameConstants.getInventoryType(id), id, (count * -1), true, false);
                        c.getClient().getSession().write(MaplePacketCreator.getShowItemGain(id, count, true));
                    } else { // add items
                        final int period = MapleDataTool.getInt(iEntry.getChildByPath("period"), 0) / 1440;
                        final String name = MapleItemInformationProvider.getInstance().getName(id);
                        if (id / 10000 == 114 && name != null && name.length() > 0) { //medal
                            final String msg = "You have attained title <" + name + ">";
                            c.dropMessage(5, msg);
                            c.dropMessage(5, msg);
                        }
                        MapleInventoryManipulator.addById(c.getClient(), id, count, "", null, period, (byte) 0);
                        c.getClient().getSession().write(MaplePacketCreator.getShowItemGain(id, count, true));
                    }
                }
                break;
            }
            case nextQuest: {
                c.getClient().getSession().write(MaplePacketCreator.updateQuestFinish(quest.getId(), c.getQuest(quest).getNpc(), MapleDataTool.getInt(data)));
                break;
            }
            case money: {
                c.gainMeso(MapleDataTool.getInt(data, 0), true, false, true);
                break;
            }
            case quest: {
                for (MapleData qEntry : data) {
                    c.updateQuest(
                            new MapleQuestStatus(MapleQuest.getInstance(MapleDataTool.getInt(qEntry.getChildByPath("id"))),
                                    (byte) MapleDataTool.getInt(qEntry.getChildByPath("state"), 0)));
                }
                break;
            }
            case skill: {
                for (MapleData sEntry : data) {
                    final int skillid = MapleDataTool.getInt(sEntry.getChildByPath("id"));
                    int skillLevel = MapleDataTool.getInt(sEntry.getChildByPath("skillLevel"), 0);
                    int masterLevel = MapleDataTool.getInt(sEntry.getChildByPath("masterLevel"), 0);
                    final ISkill skillObject = SkillFactory.getSkill(skillid);

                    for (MapleData applicableJob : sEntry.getChildByPath("job")) {
                        if (skillObject.isBeginnerSkill() || c.getJob() == MapleDataTool.getInt(applicableJob)) {
                            c.changeSkillLevel(skillObject,
                                    (byte) Math.max(skillLevel, c.getSkillLevel(skillObject)),
                                    (byte) Math.max(masterLevel, c.getMasterLevel(skillObject)));
                            break;
                        }
                    }
                }
                break;
            }
            case pop: {
                final int fameGain = MapleDataTool.getInt(data, 0);
                c.addFame(fameGain);
                c.updateSingleStat(MapleStat.FAME, c.getFame());
                c.getClient().getSession().write(MaplePacketCreator.getShowFameGain(fameGain));
                break;
            }
            case buffItemID: {
                final int tobuff = MapleDataTool.getInt(data, -1);
                if (tobuff == -1) {
                    break;
                }
                MapleItemInformationProvider.getInstance().getItemEffect(tobuff).applyTo(c);
                break;
            }
            case infoNumber: {
//		System.out.println("quest : "+MapleDataTool.getInt(data, 0)+"");
//		MapleQuest.getInstance(MapleDataTool.getInt(data, 0)).forceComplete(c, 0);
                break;
            }
            case sp: {
                for (MapleData iEntry : data.getChildren()) {
                    final int sp_val = MapleDataTool.getInt(iEntry.getChildByPath("sp_value"), 0);
                    if (iEntry.getChildByPath("job") != null) {
                        int finalJob = 0;
                        for (MapleData jEntry : iEntry.getChildByPath("job").getChildren()) {
                            final int job_val = MapleDataTool.getInt(jEntry, 0);
                            if (c.getJob() >= job_val && job_val > finalJob) {
                                finalJob = job_val;
                            }
                        }
                        c.gainSP(sp_val, GameConstants.getSkillBook(finalJob));
                    } else {
                        c.gainSP(sp_val);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    private static List<Integer> getJobBy5ByteEncoding(int encoded) {
        List<Integer> ret = new ArrayList<Integer>();
        if ((encoded & 0x1) != 0) {
            ret.add(0);
        }
        if ((encoded & 0x2) != 0) {
            ret.add(100);
        }
        if ((encoded & 0x4) != 0) {
            ret.add(200);
        }
        if ((encoded & 0x8) != 0) {
            ret.add(300);
        }
        if ((encoded & 0x10) != 0) {
            ret.add(400);
        }
        if ((encoded & 0x20) != 0) {
            ret.add(500);
        }
        if ((encoded & 0x400) != 0) {
            ret.add(1000);
        }
        if ((encoded & 0x800) != 0) {
            ret.add(1100);
        }
        if ((encoded & 0x1000) != 0) {
            ret.add(1200);
        }
        if ((encoded & 0x2000) != 0) {
            ret.add(1300);
        }
        if ((encoded & 0x4000) != 0) {
            ret.add(1400);
        }
        if ((encoded & 0x8000) != 0) {
            ret.add(1500);
        }
        if ((encoded & 0x20000) != 0) {
            ret.add(2001); //im not sure of this one
            ret.add(2200);
        }
        if ((encoded & 0x100000) != 0) {
            ret.add(2000);
            ret.add(2001); //?
        }
        if ((encoded & 0x200000) != 0) {
            ret.add(2100);
        }
        if ((encoded & 0x400000) != 0) {
            ret.add(2001); //?
            ret.add(2200);
        }

        if ((encoded & 0x40000000) != 0) { //i haven't seen any higher than this o.o
            ret.add(3000);
            ret.add(3200);
            ret.add(3300);
            ret.add(3500);
        }
        return ret;
    }

    public MapleQuestActionType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + ": " + data;
    }
}
 */
