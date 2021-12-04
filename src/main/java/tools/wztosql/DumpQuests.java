package tools.wztosql;

import com.github.mrzhqiang.maplestory.domain.DWzQuestActData;
import com.github.mrzhqiang.maplestory.domain.DWzQuestActItemData;
import com.github.mrzhqiang.maplestory.domain.DWzQuestActQuestData;
import com.github.mrzhqiang.maplestory.domain.DWzQuestActSkillData;
import com.github.mrzhqiang.maplestory.domain.DWzQuestData;
import com.github.mrzhqiang.maplestory.domain.DWzQuestPartyData;
import com.github.mrzhqiang.maplestory.domain.DWzQuestReqData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzQuestActData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzQuestActItemData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzQuestActQuestData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzQuestActSkillData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzQuestData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzQuestPartyData;
import com.github.mrzhqiang.maplestory.domain.query.QDWzQuestReqData;
import com.github.mrzhqiang.maplestory.util.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.quest.MapleQuestActionType;
import server.quest.MapleQuestRequirementType;
import tools.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DumpQuests {

    private static final Logger LOGGER = LoggerFactory.getLogger(DumpQuests.class);

    protected boolean update;
    protected int id = 0;

    public DumpQuests(boolean update) {
        this.update = update;
    }

    //kinda inefficient
    public void dumpQuests() {
        if (!update) {
            new QDWzQuestData().delete();
            new QDWzQuestActData().delete();
            new QDWzQuestActItemData().delete();
            new QDWzQuestActSkillData().delete();
            new QDWzQuestActQuestData().delete();
            new QDWzQuestReqData().delete();
            new QDWzQuestPartyData().delete();
            LOGGER.debug("Deleted wz_questdata successfully.");
        }
        LOGGER.debug("Adding into wz_questdata.....");
        AtomicInteger uniqueid = new AtomicInteger();

        WzData.QUEST.directory().findFile("Check.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(qz -> {
                    this.id = Numbers.ofInt(qz.name());
                    if (update && new QDWzQuestData().id.eq(id).exists()) {
                        return;
                    }

                    for (int i = 0; i < 2; i++) {
                        WzElement<?> reqData = qz.find(String.valueOf(i));
                        int finalI = i;
                        if (reqData != null) {
                            reqData.childrenStream().forEach(req -> {
                                DWzQuestReqData reqdata = new DWzQuestReqData();
                                reqdata.setQuestData(new QDWzQuestData().id.eq(id).findOne());
                                reqdata.setType(finalI);
                                if (MapleQuestRequirementType.getByWZName(req.name()) == MapleQuestRequirementType.UNDEFINED) {
                                    return; //un-needed
                                }
                                reqdata.setName(req.name());
                                if (req.name().equals("fieldEnter")) { //diff
                                    reqdata.setStringStore(String.valueOf(Elements.findInt(req, "0")));
                                } else if (req.name().equals("end") || req.name().equals("startscript") || req.name().equals("endscript")) {
                                    reqdata.setStringStore(Elements.ofString(req));
                                } else {
                                    reqdata.setStringStore(String.valueOf(Elements.ofInt(req)));
                                }
                                StringBuilder intStore1 = new StringBuilder();
                                StringBuilder intStore2 = new StringBuilder();
                                List<Pair<Integer, Integer>> dataStore = new LinkedList<>();
                                if (req.name().equals("job")) {
                                    req.childrenStream().forEach(element -> {
                                        int right = Elements.ofInt(element, -1);
                                        Pair<Integer, Integer> pair = new Pair<>(finalI, right);
                                        dataStore.add(pair);
                                    });
                                } else if (req.name().equals("skill")) {
                                    req.childrenStream().forEach(element -> {
                                        int id = Elements.findInt(element, "id");
                                        int acquire = Elements.findInt(element, "acquire");
                                        Pair<Integer, Integer> pair = new Pair<>(id, acquire);
                                        dataStore.add(pair);
                                    });
                                } else if (req.name().equals("quest")) {
                                    req.childrenStream().forEach(element -> {
                                        int id = Elements.findInt(element, "id");
                                        int state = Elements.findInt(element, "state");
                                        Pair<Integer, Integer> pair = new Pair<>(id, state);
                                        dataStore.add(pair);
                                    });
                                } else if (req.name().equals("item") || req.name().equals("mob")) {
                                    req.childrenStream().forEach(element -> {
                                        int id = Elements.findInt(element, "id");
                                        int count = Elements.findInt(element, "count");
                                        Pair<Integer, Integer> pair = new Pair<>(id, count);
                                        dataStore.add(pair);
                                    });
                                } else if (req.name().equals("mbcard")) {
                                    req.childrenStream().forEach(element -> {
                                        int id = Elements.findInt(element, "id");
                                        int min = Elements.findInt(element, "min");
                                        Pair<Integer, Integer> pair = new Pair<>(id, min);
                                        dataStore.add(pair);
                                    });
                                } else if (req.name().equals("pet")) {
                                    req.childrenStream().forEach(element -> {
                                        int id = Elements.findInt(element, "id");
                                        Pair<Integer, Integer> pair = new Pair<>(finalI, id);
                                        dataStore.add(pair);
                                    });
                                }
                                for (Pair<Integer, Integer> data : dataStore) {
                                    if (intStore1.length() > 0) {
                                        intStore1.append(", ");
                                        intStore2.append(", ");
                                    }
                                    intStore1.append(data.getLeft());
                                    intStore2.append(data.getRight());
                                }
                                reqdata.setIntStoresFirst(intStore1.toString());
                                reqdata.setIntStoresSecond(intStore2.toString());
                                reqdata.save();
                            });
                        }
                        WzData.QUEST.directory().findFile("Act.img")
                                .map(WzFile::content)
                                .map(element -> element.find(id + "/" + finalI))
                                .ifPresent(actData -> actData.childrenStream().forEach(act -> {
                                    if (MapleQuestActionType.getByWZName(act.name()) == MapleQuestActionType.UNDEFINED) {
                                        return; //un-needed
                                    }
                                    DWzQuestActData actdata = new DWzQuestActData();
                                    actdata.setQuestData(new QDWzQuestData().id.eq(id).findOne());
                                    actdata.setType(finalI);
                                    actdata.setName(act.name());
                                    if (act.name().equals("sp")) {
                                        actdata.setIntStore(Elements.findInt(act, "0/sp_value"));
                                    } else {
                                        actdata.setIntStore(Elements.ofInt(act));
                                    }
                                    StringBuilder applicableJobs = new StringBuilder();
                                    if (act.name().equals("sp") || act.name().equals("skill")) {
                                        int index = 0;
                                        while (true) {
                                            Optional<WzElement<?>> optional = act.findByName(index + "/job");
                                            if (!optional.isPresent()) {
                                                break;
                                            }
                                            optional.map(WzElement::childrenStream)
                                                    .ifPresent(elementStream -> elementStream.forEach(d -> {
                                                        if (applicableJobs.length() > 0) {
                                                            applicableJobs.append(", ");
                                                        }
                                                        applicableJobs.append(Elements.ofInt(d));
                                                    }));
                                            index++;
                                        }
                                    } else if (act.find("job") != null) {
                                        act.findByName("job").map(WzElement::childrenStream)
                                                .ifPresent(elementStream -> elementStream.forEach(d -> {
                                                    if (applicableJobs.length() > 0) {
                                                        applicableJobs.append(", ");
                                                    }
                                                    applicableJobs.append(Elements.ofInt(d));
                                                }));
                                    }
                                    actdata.setApplicableJobs(applicableJobs.toString());
                                    actdata.setUniqueId(-1);
                                    if (act.name().equals("item")) { //prop, job, gender, id, count
                                        uniqueid.getAndIncrement();
                                        actdata.setUniqueId(uniqueid.get());
                                        act.childrenStream().forEach(iEntry -> {
                                            DWzQuestActItemData itemData = new DWzQuestActItemData();
                                            itemData.setUniqueId(uniqueid.get());
                                            itemData.setItemId(Elements.findInt(iEntry, "id"));
                                            itemData.setCount(Elements.findInt(iEntry, "count"));
                                            itemData.setPeriod(Elements.findInt(iEntry, "period"));
                                            itemData.setGender(Elements.findInt(iEntry, "gender"));
                                            itemData.setJob(Elements.findInt(iEntry, "job", -1));
                                            itemData.setJobEx(Elements.findInt(iEntry, "jobEx", -1));
                                            if (iEntry.find("prop") == null) {
                                                itemData.setProp(-2);
                                            } else {
                                                itemData.setProp(Elements.findInt(iEntry, "prop", -1));
                                            }
                                            itemData.save();
                                        });
                                    } else if (act.name().equals("skill")) {
                                        uniqueid.getAndIncrement();
                                        actdata.setUniqueId(uniqueid.get());
                                        act.childrenStream().forEach(sEntry -> {
                                            DWzQuestActSkillData skillData = new DWzQuestActSkillData();
                                            skillData.setUniqueId(uniqueid.get());
                                            skillData.setId(Elements.findInt(sEntry, "id"));
                                            skillData.setSkillLevel(Elements.findInt(sEntry, "skillLevel"));
                                            skillData.setMasterLevel(Elements.findInt(sEntry, "masterLevel"));
                                            skillData.save();
                                        });
                                    } else if (act.name().equals("quest")) {
                                        uniqueid.getAndIncrement();
                                        actdata.setUniqueId(uniqueid.get());
                                        act.childrenStream().forEach(sEntry -> {
                                            DWzQuestActQuestData questData = new DWzQuestActQuestData();
                                            questData.setUniqueId(uniqueid.get());
                                            questData.setId(Elements.findInt(sEntry, "id"));
                                            questData.setState(Elements.findInt(sEntry, "state"));
                                            questData.save();
                                        });
                                    }
                                    actdata.save();
                                }));
                    }

                    WzData.QUEST.directory().findFile("QuestInfo.img")
                            .map(WzFile::content)
                            .ifPresent(infoData -> {
                                DWzQuestData data = new DWzQuestData();
                                data.setId(id);
                                data.setName(Elements.findString(infoData, "name"));
                                data.setAutoStart(Elements.findInt(infoData, "autoStart") > 0);
                                data.setAutoPreComplete(Elements.findInt(infoData, "autoPreComplete") > 0);
                                data.setViewMedalItem(Elements.findInt(infoData, "viewMedalItem"));
                                data.setSelectedSkillId(Elements.findInt(infoData, "selectedSkillID"));
                                data.setBlocked(Elements.findInt(infoData, "blocked") > 0);
                                data.setAutoAccept(Elements.findInt(infoData, "autoAccept") > 0);
                                data.setAutoComplete(Elements.findInt(infoData, "autoComplete") > 0);
                                data.save();
                            });

                    WzData.QUEST.directory().findFile("PQuest.img")
                            .map(WzFile::content)
                            .map(element -> element.find(String.valueOf(id)))
                            .ifPresent(pinfoData -> {
                                if (pinfoData.findByName("rank").isPresent()) {
                                    pinfoData.findByName("rank").map(WzElement::childrenStream)
                                            .ifPresent(elementStream -> elementStream.forEach(d ->
                                                    d.childrenStream().forEach(c ->
                                                            c.childrenStream().forEach(b -> {
                                                                DWzQuestPartyData partyData = new DWzQuestPartyData();
                                                                partyData.setQuestData(new QDWzQuestData().id.eq(id).findOne());
                                                                partyData.setRank(d.name());
                                                                partyData.setMode(c.name());
                                                                partyData.setProperty(b.name());
                                                                partyData.setValue(Elements.ofInt(b));
                                                                partyData.save();
                                                            }))));
                                }
                            });
                    LOGGER.debug("Added quest: " + id);
                }));
        LOGGER.debug("任务数据提取完成!");
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
            final DumpQuests dq = new DumpQuests(update);
            LOGGER.debug("Dumping quests");
            dq.dumpQuests();
//            hadError |= dq.isHadError();
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
}
