package tools.wztosql;

import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import database.DatabaseConnection;
import server.quest.MapleQuestActionType;
import server.quest.MapleQuestRequirementType;
import tools.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DumpQuests {

    protected boolean update;
    protected int id = 0;
    private Connection con = DatabaseConnection.getConnection();

    public DumpQuests(boolean update) {
        this.update = update;
    }

    public void dumpQuests() throws Exception {
        PreparedStatement psai = con.prepareStatement("INSERT INTO wz_questactitemdata(uniqueid, itemid, count, period, gender, job, jobEx, prop) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        PreparedStatement psas = con.prepareStatement("INSERT INTO wz_questactskilldata(uniqueid, skillid, skillLevel, masterLevel) VALUES (?, ?, ?, ?)");
        PreparedStatement psaq = con.prepareStatement("INSERT INTO wz_questactquestdata(uniqueid, quest, state) VALUES (?, ?, ?)");
        PreparedStatement ps = con.prepareStatement("INSERT INTO wz_questdata(questid, name, autoStart, autoPreComplete, viewMedalItem, selectedSkillID, blocked, autoAccept, autoComplete) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        PreparedStatement psr = con.prepareStatement("INSERT INTO wz_questreqdata(questid, type, name, stringStore, intStoresFirst, intStoresSecond) VALUES (?, ?, ?, ?, ?, ?)");
        PreparedStatement psq = con.prepareStatement("INSERT INTO wz_questpartydata(questid, rank, mode, property, value) VALUES(?,?,?,?,?)");
        PreparedStatement psa = con.prepareStatement("INSERT INTO wz_questactdata(questid, type, name, intStore, applicableJobs, uniqueid) VALUES (?, ?, ?, ?, ?, ?)");
        try {
            dumpQuests(psai, psas, psaq, ps, psr, psq, psa);
        } catch (Exception e) {
            System.out.println(id + " quest.");
            e.printStackTrace();
        } finally {
            psai.executeBatch();
            psai.close();
            psas.executeBatch();
            psas.close();
            psaq.executeBatch();
            psaq.close();
            psa.executeBatch();
            psa.close();
            psr.executeBatch();
            psr.close();
            psq.executeBatch();
            psq.close();
            ps.executeBatch();
            ps.close();
        }
    }

    public void delete(String sql) throws Exception {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
    }

    public boolean doesExist(String sql) throws Exception {
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        boolean ret = rs.next();
        rs.close();
        ps.close();
        return ret;
    }

    //kinda inefficient
    public void dumpQuests(PreparedStatement psai, PreparedStatement psas, PreparedStatement psaq, PreparedStatement ps, PreparedStatement psr, PreparedStatement psq, PreparedStatement psa) throws Exception {
        if (!update) {
            delete("DELETE FROM wz_questdata");
            delete("DELETE FROM wz_questactdata");
            delete("DELETE FROM wz_questactitemdata");
            delete("DELETE FROM wz_questactskilldata");
            delete("DELETE FROM wz_questactquestdata");
            delete("DELETE FROM wz_questreqdata");
            delete("DELETE FROM wz_questpartydata");
            System.out.println("Deleted wz_questdata successfully.");
        }
        System.out.println("Adding into wz_questdata.....");
        AtomicInteger uniqueid = new AtomicInteger();
        WzData.QUEST.directory().findFile("Check.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(qz -> {
                    this.id = Numbers.ofInt(qz.name());
                    try {
                        if (update && doesExist("SELECT * FROM wz_questdata WHERE questid = " + id)) {
                            return;
                        }

                        ps.setInt(1, id);
                        for (int i = 0; i < 2; i++) {
                            WzElement<?> reqData = qz.find(String.valueOf(i));
                            int finalI = i;
                            if (reqData != null) {
                                psr.setInt(1, id);
                                psr.setInt(2, i); //0 = start
                                reqData.childrenStream().forEach(req -> {
                                    try {
                                        if (MapleQuestRequirementType.getByWZName(req.name()) == MapleQuestRequirementType.UNDEFINED) {
                                            return; //un-needed
                                        }
                                        psr.setString(3, req.name());
                                        if (req.name().equals("fieldEnter")) { //diff
                                            psr.setString(4, String.valueOf(Elements.findInt(req, "0")));
                                        } else if (req.name().equals("end") || req.name().equals("startscript") || req.name().equals("endscript")) {
                                            psr.setString(4, Elements.ofString(req));
                                        } else {
                                            psr.setString(4, String.valueOf(Elements.ofInt(req)));
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
                                        psr.setString(5, intStore1.toString());
                                        psr.setString(6, intStore2.toString());
                                        psr.addBatch();
                                    } catch (Exception ignore) {
                                    }
                                });
                            }
                            WzData.QUEST.directory().findFile("Act.img")
                                    .map(WzFile::content)
                                    .map(element -> element.find(id + "/" + finalI))
                                    .ifPresent(actData -> {
                                        try {
                                            psa.setInt(1, id);
                                            psa.setInt(2, finalI); //0 = start
                                            actData.childrenStream().forEach(act -> {
                                                if (MapleQuestActionType.getByWZName(act.name()) == MapleQuestActionType.UNDEFINED) {
                                                    return; //un-needed
                                                }
                                                try {
                                                    psa.setString(3, act.name());
                                                    if (act.name().equals("sp")) {
                                                        psa.setInt(4, Elements.findInt(act, "0/sp_value"));
                                                    } else {
                                                        psa.setInt(4, Elements.ofInt(act));
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
                                                    psa.setString(5, applicableJobs.toString());
                                                    psa.setInt(6, -1);
                                                    if (act.name().equals("item")) { //prop, job, gender, id, count
                                                        uniqueid.getAndIncrement();
                                                        psa.setInt(6, uniqueid.get());
                                                        psai.setInt(1, uniqueid.get());
                                                        act.childrenStream().forEach(iEntry -> {
                                                            try {
                                                                psai.setInt(2, Elements.findInt(iEntry, "id"));
                                                                psai.setInt(3, Elements.findInt(iEntry, "count"));
                                                                psai.setInt(4, Elements.findInt(iEntry, "period"));
                                                                psai.setInt(5, Elements.findInt(iEntry, "gender"));
                                                                psai.setInt(6, Elements.findInt(iEntry, "job", -1));
                                                                psai.setInt(7, Elements.findInt(iEntry, "jobEx", -1));
                                                                if (iEntry.find("prop") == null) {
                                                                    psai.setInt(8, -2);
                                                                } else {
                                                                    psai.setInt(8, Elements.findInt(iEntry, "prop", -1));
                                                                }
                                                                psai.addBatch();
                                                            } catch (SQLException ignore) {
                                                            }
                                                        });
                                                    } else if (act.name().equals("skill")) {
                                                        uniqueid.getAndIncrement();
                                                        psa.setInt(6, uniqueid.get());
                                                        psas.setInt(1, uniqueid.get());
                                                        act.childrenStream().forEach(sEntry -> {
                                                            try {
                                                                psas.setInt(2, Elements.findInt(sEntry, "id"));
                                                                psas.setInt(3, Elements.findInt(sEntry, "skillLevel"));
                                                                psas.setInt(4, Elements.findInt(sEntry, "masterLevel"));
                                                                psas.addBatch();
                                                            } catch (SQLException ignore) {
                                                            }
                                                        });
                                                    } else if (act.name().equals("quest")) {
                                                        uniqueid.getAndIncrement();
                                                        psa.setInt(6, uniqueid.get());
                                                        psaq.setInt(1, uniqueid.get());
                                                        act.childrenStream().forEach(sEntry -> {
                                                            try {
                                                                psaq.setInt(2, Elements.findInt(sEntry, "id"));
                                                                psaq.setInt(3, Elements.findInt(sEntry, "state"));
                                                                psaq.addBatch();
                                                            } catch (SQLException ignore) {
                                                            }
                                                        });
                                                    }
                                                    psa.addBatch();
                                                } catch (SQLException ignore) {
                                                }
                                            });
                                        } catch (SQLException ignore) {
                                        }
                                    });
                        }

                        ps.setString(2, "");
                        ps.setInt(3, 0);
                        ps.setInt(4, 0);
                        ps.setInt(5, 0);
                        ps.setInt(6, 0);
                        ps.setInt(7, 0);
                        ps.setInt(8, 0);
                        ps.setInt(9, 0);
                        WzData.QUEST.directory().findFile("QuestInfo.img")
                                .map(WzFile::content)
                                .ifPresent(infoData -> {
                                    try {
                                        ps.setString(2, Elements.findString(infoData, "name"));
                                        ps.setInt(3, Elements.findInt(infoData, "autoStart") > 0 ? 1 : 0);
                                        ps.setInt(4, Elements.findInt(infoData, "autoPreComplete") > 0 ? 1 : 0);
                                        ps.setInt(5, Elements.findInt(infoData, "viewMedalItem"));
                                        ps.setInt(6, Elements.findInt(infoData, "selectedSkillID"));
                                        ps.setInt(7, Elements.findInt(infoData, "blocked"));
                                        ps.setInt(8, Elements.findInt(infoData, "autoAccept"));
                                        ps.setInt(9, Elements.findInt(infoData, "autoComplete"));
                                    } catch (SQLException ignore) {
                                    }
                                });
                        ps.addBatch();

                        WzData.QUEST.directory().findFile("PQuest.img")
                                .map(WzFile::content)
                                .map(element -> element.find(String.valueOf(id)))
                                .ifPresent(pinfoData -> {
                                    if (pinfoData.findByName("rank").isPresent()) {
                                        try {
                                            psq.setInt(1, id);
                                        } catch (SQLException ignore) {
                                        }
                                    }
                                    pinfoData.findByName("rank").map(WzElement::childrenStream)
                                            .ifPresent(elementStream -> elementStream.forEach(d -> {
                                                try {
                                                    psq.setString(2, d.name());
                                                } catch (SQLException ignore) {
                                                }
                                                d.childrenStream().forEach(c -> {
                                                    try {
                                                        psq.setString(3, c.name());
                                                    } catch (SQLException ignore) {
                                                    }
                                                    c.childrenStream().forEach(b -> {
                                                        try {
                                                            psq.setString(4, b.name());
                                                            psq.setInt(5, Elements.ofInt(b));
                                                            psq.addBatch();
                                                        } catch (SQLException ignore) {
                                                        }
                                                    });
                                                });

                                            }));
                                });
                        System.out.println("Added quest: " + id);
                    } catch (Exception ignore) {
                    }
                }));
        System.out.println("任务数据提取完成!");
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
            System.out.println("Dumping quests");
            dq.dumpQuests();
//            hadError |= dq.isHadError();
            currentQuest = dq.currentId();
        } catch (Exception e) {
            hadError = true;
            e.printStackTrace();
            System.out.println(currentQuest + " quest.");
        }
        long endTime = System.currentTimeMillis();
        double elapsedSeconds = (endTime - startTime) / 1000.0;
        int elapsedSecs = (((int) elapsedSeconds) % 60);
        int elapsedMinutes = (int) (elapsedSeconds / 60.0);

        String withErrors = "";
        if (hadError) {
            withErrors = " with errors";
        }
        System.out.println("Finished" + withErrors + " in " + elapsedMinutes + " minutes " + elapsedSecs + " seconds");
    }
}
