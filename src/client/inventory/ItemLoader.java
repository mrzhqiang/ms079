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
package client.inventory;

import constants.GameConstants;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import database.DatabaseConnection;
import java.sql.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import tools.Pair;

public enum ItemLoader {

    INVENTORY("inventoryitems", "inventoryequipment", 0, "characterid"),
    STORAGE("inventoryitems", "inventoryequipment", 1, "accountid"),
    CASHSHOP_EXPLORER("csitems", "csequipment", 2, "accountid"),
    CASHSHOP_CYGNUS("csitems", "csequipment", 3, "accountid"),
    CASHSHOP_ARAN("csitems", "csequipment", 4, "accountid"),
    HIRED_MERCHANT("hiredmerchitems", "hiredmerchequipment", 5, "packageid", "accountid", "characterid"),
    DUEY("dueyitems", "dueyequipment", 6, "packageid"),
    CASHSHOP_EVAN("csitems", "csequipment", 7, "accountid"),
    MTS("mtsitems", "mtsequipment", 8, "packageid"),
    MTS_TRANSFER("mtstransfer", "mtstransferequipment", 9, "characterid"),
    CASHSHOP_DB("csitems", "csequipment", 10, "accountid"),
    CASHSHOP_RESIST("csitems", "csequipment", 11, "accountid");
    private int value;
    private String table, table_equip;
    private List<String> arg;

    private ItemLoader(String table, String table_equip, int value, String... arg) {
        this.table = table;
        this.table_equip = table_equip;
        this.value = value;
        this.arg = Arrays.asList(arg);
    }

    public int getValue() {
        return value;
    }
    //does not need connection con to be auto commit

    public Map<Integer, Pair<IItem, MapleInventoryType>> loadItems_hm(int packageid, int accountid) throws SQLException {
        Map<Integer, Pair<IItem, MapleInventoryType>> items = new LinkedHashMap<>();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `hiredmerchitems` LEFT JOIN `hiredmerchequipment` USING(`inventoryitemid`) WHERE `type` = ? AND `packageid` = ? AND `accountid` = ? ");
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query.toString());
        ps.setInt(1, value);
        ps.setInt(2, packageid);
        ps.setInt(3, accountid);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            MapleInventoryType mit = MapleInventoryType.getByType(rs.getByte("inventorytype"));
            if (mit.equals(MapleInventoryType.EQUIP) || mit.equals(MapleInventoryType.EQUIPPED)) {
                Equip equip = new Equip(rs.getInt("itemid"), rs.getShort("position"), rs.getInt("uniqueid"), rs.getByte("flag"));
                equip.setQuantity((short) 1);
                equip.setOwner(rs.getString("owner"));
                equip.setExpiration(rs.getLong("expiredate"));
                equip.setUpgradeSlots(rs.getByte("upgradeslots"));
                equip.setLevel(rs.getByte("level"));
                equip.setStr(rs.getShort("str"));
                equip.setDex(rs.getShort("dex"));
                equip.setInt(rs.getShort("int"));
                equip.setLuk(rs.getShort("luk"));
                equip.setHp(rs.getShort("hp"));
                equip.setMp(rs.getShort("mp"));
                equip.setWatk(rs.getShort("watk"));
                equip.setMatk(rs.getShort("matk"));
                equip.setWdef(rs.getShort("wdef"));
                equip.setMdef(rs.getShort("mdef"));
                equip.setAcc(rs.getShort("acc"));
                equip.setAvoid(rs.getShort("avoid"));
                equip.setHands(rs.getShort("hands"));
                equip.setSpeed(rs.getShort("speed"));
                equip.setJump(rs.getShort("jump"));
                equip.setViciousHammer(rs.getByte("ViciousHammer"));
                equip.setItemEXP(rs.getInt("itemEXP"));
                equip.setGMLog(rs.getString("GM_Log"));
                equip.setDurability(rs.getInt("durability"));
                equip.setEnhance(rs.getByte("enhance"));
                equip.setPotential1(rs.getShort("potential1"));
                equip.setPotential2(rs.getShort("potential2"));
                equip.setPotential3(rs.getShort("potential3"));
                equip.setHpR(rs.getShort("hpR"));
                equip.setMpR(rs.getShort("mpR"));
                equip.setGiftFrom(rs.getString("sender"));
                equip.setEquipLevel(rs.getByte("itemlevel"));
                if (equip.getUniqueId() > -1) {
                    if (GameConstants.isEffectRing(rs.getInt("itemid"))) {
                        MapleRing ring = MapleRing.loadFromDb(equip.getUniqueId(), mit.equals(MapleInventoryType.EQUIPPED));
                        if (ring != null) {
                            equip.setRing(ring);
                        }
                    }
                }
                items.put(rs.getInt("inventoryitemid"), new Pair<IItem, MapleInventoryType>(equip.copy(), mit));
            } else {
                Item item = new Item(rs.getInt("itemid"), rs.getShort("position"), rs.getShort("quantity"), rs.getByte("flag"));
                item.setUniqueId(rs.getInt("uniqueid"));
                item.setOwner(rs.getString("owner"));
                item.setExpiration(rs.getLong("expiredate"));
                item.setGMLog(rs.getString("GM_Log"));
                item.setGiftFrom(rs.getString("sender"));
                if (GameConstants.isPet(item.getItemId())) {
                    if (item.getUniqueId() > -1) {
                        MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(), item.getPosition());
                        if (pet != null) {
                            item.setPet(pet);
                        }
                    } else {
                        //O_O hackish fix
                        final int new_unique = MapleInventoryIdentifier.getInstance();
                        item.setUniqueId(new_unique);
                        item.setPet(MaplePet.createPet(item.getItemId(), new_unique));
                    }
                }
                items.put(rs.getInt("inventoryitemid"), new Pair<IItem, MapleInventoryType>(item.copy(), mit));
            }
        }

        rs.close();
        ps.close();
        return items;
    }
    //does not need connection con to be auto commit

    public Map<Integer, Pair<IItem, MapleInventoryType>> loadItems(boolean login, Integer... id) throws SQLException {
        List<Integer> lulz = Arrays.asList(id);
        Map<Integer, Pair<IItem, MapleInventoryType>> items = new LinkedHashMap<Integer, Pair<IItem, MapleInventoryType>>();
        if (lulz.size() != arg.size()) {
            return items;
        }
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM `");
        query.append(table);
        query.append("` LEFT JOIN `");
        query.append(table_equip);
        query.append("` USING(`inventoryitemid`) WHERE `type` = ?");
        for (String g : arg) {
            query.append(" AND `");
            query.append(g);
            query.append("` = ?");
        }

        if (login) {
            query.append(" AND `inventorytype` = ");
            query.append(MapleInventoryType.EQUIPPED.getType());
        }
        PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query.toString());
        ps.setInt(1, value);
        for (int i = 0; i < lulz.size(); i++) {
            ps.setInt(i + 2, lulz.get(i));
        }
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            MapleInventoryType mit = MapleInventoryType.getByType(rs.getByte("inventorytype"));

            if (mit.equals(MapleInventoryType.EQUIP) || mit.equals(MapleInventoryType.EQUIPPED)) {
                Equip equip = new Equip(rs.getInt("itemid"), rs.getShort("position"), rs.getInt("uniqueid"), rs.getByte("flag"));
                if (!login) {
                    equip.setQuantity((short) 1);
                    equip.setOwner(rs.getString("owner"));
                    equip.setExpiration(rs.getLong("expiredate"));
                    equip.setUpgradeSlots(rs.getByte("upgradeslots"));
                    equip.setLevel(rs.getByte("level"));
                    equip.setStr(rs.getShort("str"));
                    equip.setDex(rs.getShort("dex"));
                    equip.setInt(rs.getShort("int"));
                    equip.setLuk(rs.getShort("luk"));
                    equip.setHp(rs.getShort("hp"));
                    equip.setMp(rs.getShort("mp"));
                    equip.setWatk(rs.getShort("watk"));
                    equip.setMatk(rs.getShort("matk"));
                    equip.setWdef(rs.getShort("wdef"));
                    equip.setMdef(rs.getShort("mdef"));
                    equip.setAcc(rs.getShort("acc"));
                    equip.setAvoid(rs.getShort("avoid"));
                    equip.setHands(rs.getShort("hands"));
                    equip.setSpeed(rs.getShort("speed"));
                    equip.setJump(rs.getShort("jump"));
                    equip.setViciousHammer(rs.getByte("ViciousHammer"));
                    equip.setItemEXP(rs.getInt("itemEXP"));
                    equip.setGMLog(rs.getString("GM_Log"));
                    equip.setDurability(rs.getInt("durability"));
                    equip.setEnhance(rs.getByte("enhance"));
                    equip.setPotential1(rs.getShort("potential1"));
                    equip.setPotential2(rs.getShort("potential2"));
                    equip.setPotential3(rs.getShort("potential3"));
                    equip.setHpR(rs.getShort("hpR"));
                    equip.setMpR(rs.getShort("mpR"));
                    equip.setGiftFrom(rs.getString("sender"));
                    equip.setEquipLevel(rs.getByte("itemlevel"));
                    if (equip.getUniqueId() > -1) {
                        if (GameConstants.isEffectRing(rs.getInt("itemid"))) {
                            MapleRing ring = MapleRing.loadFromDb(equip.getUniqueId(), mit.equals(MapleInventoryType.EQUIPPED));
                            if (ring != null) {
                                equip.setRing(ring);
                            }
                        }
                    }
                }
                items.put(rs.getInt("inventoryitemid"), new Pair<IItem, MapleInventoryType>(equip.copy(), mit));
            } else {
                Item item = new Item(rs.getInt("itemid"), rs.getShort("position"), rs.getShort("quantity"), rs.getByte("flag"));
                item.setUniqueId(rs.getInt("uniqueid"));
                item.setOwner(rs.getString("owner"));
                item.setExpiration(rs.getLong("expiredate"));
                item.setGMLog(rs.getString("GM_Log"));
                item.setGiftFrom(rs.getString("sender"));
                if (GameConstants.isPet(item.getItemId())) {
                    if (item.getUniqueId() > -1) {
                        MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(), item.getPosition());
                        if (pet != null) {
                            item.setPet(pet);
                        }
                    } else {
                        //O_O hackish fix
                        final int new_unique = MapleInventoryIdentifier.getInstance();
                        item.setUniqueId(new_unique);
                        item.setPet(MaplePet.createPet(item.getItemId(), new_unique));
                    }
                }
                items.put(rs.getInt("inventoryitemid"), new Pair<IItem, MapleInventoryType>(item.copy(), mit));
            }
        }

        rs.close();
        ps.close();
        return items;
    }

    public void saveItems(List<Pair<IItem, MapleInventoryType>> items, Integer... id) throws SQLException {
        Connection con = DatabaseConnection.getConnection();
        /*
         * try {
         *
         * con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
         * con.setAutoCommit(false);
         */
        saveItems(items, con, id);
        /*
         * con.commit(); } catch (Exception e) { e.printStackTrace();
         * System.err.println("[charsave] Error saving inventory" + e); try {
         * con.rollback(); } catch (SQLException ex) {
         * System.err.println("[charsave] Error Rolling Back inventory" + e); }
         * } finally { try { con.setAutoCommit(true);
         * con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
         * } catch (SQLException e) { System.err.println("[charsave] Error going
         * back to autocommit mode inventory" + e); } }
         */
    }

    public void saveItems(List<Pair<IItem, MapleInventoryType>> items, final Connection con, Integer... id) throws SQLException {
        List<Integer> lulz = Arrays.asList(id);
        if (lulz.size() != arg.size()) {
            return;
        }
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM `");
        query.append(table);
        query.append("` WHERE `type` = ? AND (`");
        query.append(arg.get(0));
        query.append("` = ?");
        if (arg.size() > 1) {
            for (int i = 1; i < arg.size(); i++) {
                query.append(" OR `");
                query.append(arg.get(i));
                query.append("` = ?");
            }
        }
        query.append(")");

        PreparedStatement ps = con.prepareStatement(query.toString());
        ps.setInt(1, value);
        for (int i = 0; i < lulz.size(); i++) {
            ps.setInt(i + 2, lulz.get(i));
        }
        ps.executeUpdate();
        ps.close();
        if (items == null) {
            return;
        }
        StringBuilder query_2 = new StringBuilder("INSERT INTO `");
        query_2.append(table);
        query_2.append("` (");
        for (String g : arg) {
            query_2.append(g);
            query_2.append(", ");
        }
        query_2.append("itemid, inventorytype, position, quantity, owner, GM_Log, uniqueid, expiredate, flag, `type`, sender) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?");
        for (String g : arg) {
            query_2.append(", ?");
        }
        query_2.append(")");
        ps = con.prepareStatement(query_2.toString(), Statement.RETURN_GENERATED_KEYS);
        try {
            PreparedStatement pse = con.prepareStatement("INSERT INTO " + table_equip + " VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            final Iterator<Pair<IItem, MapleInventoryType>> iter = items.iterator();
            Pair<IItem, MapleInventoryType> pair;

            while (iter.hasNext()) {
                pair = iter.next();
                IItem item = pair.getLeft();
                MapleInventoryType mit = pair.getRight();
                try {

                    int i = 1;
                    for (int x = 0; x < lulz.size(); x++) {
                        ps.setInt(i, lulz.get(x));
                        i++;
                    }
                    ps.setInt(i, item.getItemId());
                    ps.setInt(i + 1, mit.getType());
                    ps.setInt(i + 2, item.getPosition());
                    ps.setInt(i + 3, item.getQuantity());
                    ps.setString(i + 4, item.getOwner());

                    ps.setString(i + 5, item.getGMLog());
                    ps.setInt(i + 6, item.getUniqueId());
                    ps.setLong(i + 7, item.getExpiration());
                    ps.setByte(i + 8, item.getFlag());
                    ps.setByte(i + 9, (byte) value);
                    ps.setString(i + 10, item.getGiftFrom());
                    ps.executeUpdate();
                } catch (Exception ex) {
                    System.err.println("GMLOG : " + item.getGMLog() + " Table_equip : " + table + " " + ex);
                }
                if (mit.equals(MapleInventoryType.EQUIP) || mit.equals(MapleInventoryType.EQUIPPED)) {
                    ResultSet rs = ps.getGeneratedKeys();

                    if (!rs.next()) {
                        throw new RuntimeException("Inserting item failed.");
                    }

                    pse.setInt(1, rs.getInt(1));
                    rs.close();
                    IEquip equip = (IEquip) item;
                    pse.setInt(2, equip.getUpgradeSlots());
                    pse.setInt(3, equip.getLevel());
                    pse.setInt(4, equip.getStr());
                    pse.setInt(5, equip.getDex());
                    pse.setInt(6, equip.getInt());
                    pse.setInt(7, equip.getLuk());
                    pse.setInt(8, equip.getHp());
                    pse.setInt(9, equip.getMp());
                    pse.setInt(10, equip.getWatk());
                    pse.setInt(11, equip.getMatk());
                    pse.setInt(12, equip.getWdef());
                    pse.setInt(13, equip.getMdef());
                    pse.setInt(14, equip.getAcc());
                    pse.setInt(15, equip.getAvoid());
                    pse.setInt(16, equip.getHands());
                    pse.setInt(17, equip.getSpeed());
                    pse.setInt(18, equip.getJump());
                    pse.setInt(19, equip.getViciousHammer());
                    pse.setInt(20, equip.getItemEXP());
                    pse.setInt(21, equip.getDurability());
                    pse.setByte(22, equip.getEnhance());
                    pse.setInt(23, equip.getPotential1());
                    pse.setInt(24, equip.getPotential2());
                    pse.setInt(25, equip.getPotential3());
                    pse.setInt(26, equip.getHpR());
                    pse.setInt(27, equip.getMpR());
                    pse.setByte(28, equip.getEquipLevel());
                    pse.executeUpdate();
                }
            }

            pse.close();
            ps.close();
        } catch (Exception ex) {
            System.err.println("table_equip: " + table_equip + " " + ex);
        }
    }
}
