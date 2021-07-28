/*
 This file is part of the ZeroFusion MapleStory Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 ZeroFusion organized by "RMZero213" <RMZero213@hotmail.com>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tools.wztosql;

import database.DatabaseConnection;
import java.awt.Point;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;

public class DumpMobSkills {

    private MapleDataProvider skill;
    protected boolean hadError = false;
    protected boolean update = false;
    protected int id = 0;
    private Connection con = DatabaseConnection.getConnection();

    public DumpMobSkills(boolean update) throws Exception {
        this.update = update;
        this.skill = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Skill.wz"));
        if (skill == null) {
            hadError = true;
        }
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
        final MapleData skillz = skill.getData("MobSkill.img");
        System.out.println("Adding into wz_mobskilldata.....");

        for (MapleData ids : skillz.getChildren()) {
            for (MapleData lvlz : ids.getChildByPath("level").getChildren()) {
                this.id = Integer.parseInt(ids.getName());
                int lvl = Integer.parseInt(lvlz.getName());
                if (update && doesExist("SELECT * FROM wz_mobskilldata WHERE skillid = " + id + " AND level = " + lvl)) {
                    continue;
                }
                ps.setInt(1, id);
                ps.setInt(2, lvl);
                ps.setInt(3, MapleDataTool.getInt("hp", lvlz, 100));
                ps.setInt(4, MapleDataTool.getInt("mpCon", lvlz, 0));
                ps.setInt(5, MapleDataTool.getInt("x", lvlz, 1));
                ps.setInt(6, MapleDataTool.getInt("y", lvlz, 1));
                ps.setInt(7, MapleDataTool.getInt("time", lvlz, 0)); // * 1000
                ps.setInt(8, MapleDataTool.getInt("prop", lvlz, 100)); // / 100.0
                ps.setInt(9, MapleDataTool.getInt("limit", lvlz, 0));
                ps.setInt(10, MapleDataTool.getInt("summonEffect", lvlz, 0));
                ps.setInt(11, MapleDataTool.getInt("interval", lvlz, 0)); // * 1000

                StringBuilder summ = new StringBuilder();
                List<Integer> toSummon = new ArrayList<Integer>();
                for (int i = 0; i > -1; i++) {
                    if (lvlz.getChildByPath(String.valueOf(i)) == null) {
                        break;
                    }
                    toSummon.add(Integer.valueOf(MapleDataTool.getInt(lvlz.getChildByPath(String.valueOf(i)), 0)));
                }
                for (Integer summon : toSummon) {
                    if (summ.length() > 0) {
                        summ.append(", ");
                    }
                    summ.append(String.valueOf(summon));
                }
                ps.setString(12, summ.toString());
                if (lvlz.getChildByPath("lt") != null) {
                    Point lt = (Point) lvlz.getChildByPath("lt").getData();
                    ps.setInt(13, lt.x);
                    ps.setInt(14, lt.y);
                } else {
                    ps.setInt(13, 0);
                    ps.setInt(14, 0);
                }
                if (lvlz.getChildByPath("rb") != null) {
                    Point rb = (Point) lvlz.getChildByPath("rb").getData();
                    ps.setInt(15, rb.x);
                    ps.setInt(16, rb.y);
                } else {
                    ps.setInt(15, 0);
                    ps.setInt(16, 0);
                }
                ps.setByte(17, (byte) (MapleDataTool.getInt("summonOnce", lvlz, 0) > 0 ? 1 : 0));
                System.out.println("Added skill: " + id + " level " + lvl);
                ps.addBatch();
            }
        }
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
