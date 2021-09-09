package tools.wztosql;

import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DumpMobSkills {

    protected boolean hadError = false;
    protected boolean update;
    protected int id = 0;
    private Connection con = DatabaseConnection.getConnection();

    public DumpMobSkills(boolean update) {
        this.update = update;
    }

    public boolean isHadError() {
        return hadError;
    }

    public void dumpMobSkills() throws Exception {
        if (!hadError) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO wz_mobskilldata(skillid, `level`, hp, mpcon, x, y, time, prop, `limit`, spawneffect,`interval`, summons, ltx, lty, rbx, rby, once) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            try {
                dumpMobSkills(ps);
            } catch (Exception e) {
                System.out.println(id + " skill.");
                System.out.println(e);
                hadError = true;
            } finally {
                ps.executeBatch();
                ps.close();
            }
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
    public void dumpMobSkills(PreparedStatement ps) throws Exception {
        if (!update) {
            delete("DELETE FROM wz_mobskilldata");
            System.out.println("Deleted wz_mobskilldata successfully.");
        }
        System.out.println("Adding into wz_mobskilldata.....");
        WzData.SKILL.directory().findFile("MobSkill.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element -> {
                    this.id = Numbers.ofInt(element.name());
                    element.findByName("level").map(WzElement::childrenStream)
                            .ifPresent(wzElementStream -> wzElementStream.forEach(lvlz -> {
                                int lvl = Numbers.ofInt(lvlz.name());
                                try {
                                    if (update && doesExist("SELECT * FROM wz_mobskilldata WHERE skillid = " + id + " AND level = " + lvl)) {
                                        return;
                                    }
                                    ps.setInt(1, id);
                                    ps.setInt(2, lvl);
                                    ps.setInt(3, Elements.findInt(lvlz, "hp", 100));
                                    ps.setInt(4, Elements.findInt(lvlz, "mpCon", 0));
                                    ps.setInt(5, Elements.findInt(lvlz, "x", 1));
                                    ps.setInt(6, Elements.findInt(lvlz, "y", 1));
                                    ps.setInt(7, Elements.findInt(lvlz, "time", 0)); // * 1000
                                    ps.setInt(8, Elements.findInt(lvlz, "prop", 100)); // / 100.0
                                    ps.setInt(9, Elements.findInt(lvlz, "limit", 0));
                                    ps.setInt(10, Elements.findInt(lvlz, "summonEffect", 0));
                                    ps.setInt(11, Elements.findInt(lvlz, "interval", 0)); // * 1000

                                    StringBuilder summ = new StringBuilder();
                                    List<Integer> toSummon = new ArrayList<>();
                                    for (int i = 0; i < lvlz.childrenStream().count(); i++) {
                                        WzElement<?> wzElement = lvlz.find(String.valueOf(i));
                                        if (wzElement == null) {
                                            break;
                                        }
                                        toSummon.add(Elements.ofInt(wzElement));
                                    }
                                    for (Integer summon : toSummon) {
                                        if (summ.length() > 0) {
                                            summ.append(", ");
                                        }
                                        summ.append(summon);
                                    }
                                    ps.setString(12, summ.toString());
                                    if (lvlz.find("lt") != null) {
                                        Vector lt = Elements.findVector(lvlz, "lt");
                                        ps.setInt(13, lt.x);
                                        ps.setInt(14, lt.y);
                                    } else {
                                        ps.setInt(13, 0);
                                        ps.setInt(14, 0);
                                    }
                                    if (lvlz.find("rb") != null) {
                                        Vector rb = Elements.findVector(lvlz, "rb");
                                        ps.setInt(15, rb.x);
                                        ps.setInt(16, rb.y);
                                    } else {
                                        ps.setInt(15, 0);
                                        ps.setInt(16, 0);
                                    }
                                    ps.setByte(17, (byte) (Elements.findInt(lvlz, "summonOnce") > 0 ? 1 : 0));
                                    System.out.println("Added skill: " + id + " level " + lvl);
                                    ps.addBatch();
                                } catch (Exception ignore) {
                                }
                            }));
                }));
        System.out.println("Done wz_mobskilldata...");
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
            final DumpMobSkills dq = new DumpMobSkills(update);
            System.out.println("Dumping mobskills");
            dq.dumpMobSkills();
            hadError |= dq.isHadError();
            currentQuest = dq.currentId();
        } catch (Exception e) {
            hadError = true;
            System.out.println(e);
            System.out.println(currentQuest + " skill.");
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
