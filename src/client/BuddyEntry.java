/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

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
package client;

import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 纗ね虫
 *
 * @author Flower
 */
public class BuddyEntry {

    /**
     * ね嘿
     */
    private final String name;

    /**
     * ね┮竤舱
     */
    private String group;

    /**
     * ねID
     */
    private final int characterId;

    /**
     * ね单
     */
    private final int level;

    /**
     * ね戮穨
     */
    private final int job;

    /**
     * ねǎ
     */
    private boolean visible;

    /**
     * ね繵笵
     */
    private int channel;

    /**
     * 篶
     *
     * @param name ねà︹嘿
     * @param characterId ねà︹ID
     * @param group ね┮竤舱
     * @param channel à︹┮繵笵瞒絬玥 -1
     * @param visible ね琌ǎ
     * @param job ねà︹戮穨
     * @param level ねà︹单
     */
    public BuddyEntry(String name, int characterId, String group, int channel, boolean visible, int level, int job) {
        super();
        this.name = name;
        this.characterId = characterId;
        this.group = group;
        this.channel = channel;
        this.visible = visible;
        this.level = level;
        this.job = job;
    }

    /**
     * @return 肚ねà︹┮繵笵狦瞒絬杠玥 -1 returns -1.
     */
    public int getChannel() {
        return channel;
    }

    /**
     * 砞﹚ね┮繵笵
     *
     * @param channel 稱璶砞﹚繵笵
     */
    public void setChannel(int channel) {
        this.channel = channel;
    }

    /**
     * ね琌絬
     *
     * @return 肚ね琌ぃ琌絬
     */
    public boolean isOnline() {
        return channel >= 0;
    }

    /**
     * 砞﹚ね竒瞒絬
     */
    public void setOffline() {
        channel = -1;
    }

    /**
     * 眔ね嘿
     *
     * @return ね嘿
     */
    public String getName() {
        return name;
    }

    /**
     * 眔ねà︹ID
     *
     * @return ねà︹ID
     */
    public int getCharacterId() {
        return characterId;
    }

    /**
     * 眔ね单
     *
     * @return ね单
     */
    public int getLevel() {
        return level;
    }

    /**
     * 眔ね戮穨
     *
     * @return ね戮穨
     */
    public int getJob() {
        return job;
    }

    /**
     * 砞﹚ね琌ぃ琌ǎ
     *
     * @param visible 陪ボ絬籔
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * 肚ね琌ぃ琌陪ボ
     *
     * @return ね琌ǎ
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * 砞﹚ね┮竤舱
     *
     * @return 竤舱嘿
     */
    public String getGroup() {
        return group;
    }

    /**
     * 砞﹚ね┮竤舱
     *
     * @param newGroup 穝竤舱嘿
     */
    public void setGroup(String newGroup) {
        this.group = newGroup;
    }

    public static BuddyEntry getByNameFromDB(String buddyName) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id, name, level, job FROM characters WHERE name = ?");
            ps.setString(1, buddyName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new BuddyEntry(
                        rs.getString("name"),
                        rs.getInt("id"),
                        BuddyList.DEFAULT_GROUP,
                        -1,
                        false,
                        rs.getInt("level"),
                        rs.getInt("job"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            //FilePrinter.printError("BuddyEntry.txt", ex, "getByNameFromDB has SQLException");
            return null;
        }
    }

    public static BuddyEntry getByIdfFromDB(int buddyCharId) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT id, name, level, job FROM characters WHERE id = ?");
            ps.setInt(1, buddyCharId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new BuddyEntry(
                        rs.getString("name"),
                        rs.getInt("id"),
                        BuddyList.DEFAULT_GROUP,
                        -1,
                        true,
                        rs.getInt("level"),
                        rs.getInt("job"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            //FilePrinter.printError("BuddyEntry.txt", ex, "getByNameFromDB has SQLException");
            return null;
        }
    }

    /**
     * 
     *
     * @return 俱计
     */
    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + characterId;
        return result;
    }

    /**
     * 耞琌ね
     *
     * @param obj 饼肚ン
     * @return 琌妓
     */
    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        final BuddyEntry other = (BuddyEntry) obj;
        return characterId == other.characterId;
    }
}
