/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import client.inventory.MapleInventoryType;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import database.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.CashItemFactory;
import server.CashItemInfo.CashModInfo;
import server.MapleItemInformationProvider;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Flower
 */
public class CashShopDumper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CashShopDumper.class);

    public static CashModInfo getModInfo(int sn) {
        CashModInfo ret = null;

        Connection con = DatabaseConnection.getConnection();
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM cashshop_modified_items WHERE serial = ?")) {
            ps.setInt(1, sn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret = new CashModInfo(sn, rs.getInt("discount_price"), rs.getInt("mark"), rs.getInt("showup") > 0, rs.getInt("itemid"), rs.getInt("priority"), rs.getInt("package") > 0, rs.getInt("period"), rs.getInt("gender"), rs.getInt("count"), rs.getInt("meso"), rs.getInt("unk_1"), rs.getInt("unk_2"), rs.getInt("unk_3"), rs.getInt("extra_flags"));

                }
            }

        } catch (Exception ex) {
            FilePrinter.printError("CashShopDumper.txt", ex);
        }

        return ret;
    }

    public static void main(String[] args) {
        CashModInfo m = getModInfo(20000393);
        CashItemFactory.getInstance().initialize();
        Collection<CashModInfo> list = CashItemFactory.getInstance().getAllModInfo();
        Connection con = DatabaseConnection.getConnection();

        final List<Integer> itemids = new ArrayList<>();
        List<Integer> qq = new ArrayList<>();

        Map<Integer, List<String>> dics = new HashMap<>();

        WzData.ETC.directory().findFile("Commodity.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .map(stream -> stream.flatMap(WzElement::childrenStream))
                .ifPresent(stream -> stream.forEach(element -> {
                    try {
                        int itemId = Elements.findInt(element, "ItemId");
                        if (itemId == 0) {
                            return;
                        }
                        int sn = Elements.findInt(element, "SN");
                        int count = Elements.findInt(element, "Count");
                        int price = Elements.findInt(element, "Price");
                        int priority = Elements.findInt(element, "Priority");
                        int period = Elements.findInt(element, "Period");
                        int gender = Elements.findInt(element, "Gender");
                        int meso = Elements.findInt(element, "Meso");

                        int cat = itemId / 10000;
                        dics.computeIfAbsent(cat, k -> new ArrayList<>());
                        boolean check = false;
                        if (meso > 0) {
                            check = true;
                        }
                        if (MapleItemInformationProvider.getInstance().getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                            if (!MapleItemInformationProvider.getInstance().isCashItem(itemId)) {
                                check = true;
                            }
                        }
                        if (MapleItemInformationProvider.getInstance().getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                            if (period > 0) {
                                check = true;
                            }
                        }

                        if (check) {
                            LOGGER.info("MapleItemInformationProvider.getInstance().getName: {}", MapleItemInformationProvider.getInstance().getName(itemId));
                            return;
                        }
                        PreparedStatement ps = con.prepareStatement("INSERT INTO cashshop_modified_items (serial, showup,itemid,priority,period,gender,count,meso,discount_price,mark, unk_1, unk_2, unk_3,name  ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        ps.setInt(1, sn);
                        ps.setInt(2, 1);
                        ps.setInt(3, 0);
                        ps.setInt(4, 0);
                        ps.setInt(5, period);
                        ps.setInt(6, gender);
                        ps.setInt(7, count > 1 ? count : 0);
                        ps.setInt(8, meso);
                        if ((1000000 <= itemId || itemId <= 1003091) && sn >= 20000000) {
                            ps.setInt(9, price);
                        } else {
                            ps.setInt(9, 0);
                        }
                        qq.add(itemId);
                        ps.setInt(10, 0);
                        ps.setInt(11, 0);
                        ps.setInt(12, 0);
                        ps.setInt(13, 0);
                        ps.setString(14, MapleItemInformationProvider.getInstance().getName(itemId));

                        String sql = ps.toString().split(":")[1].trim() + ";";
                        ps.executeUpdate();
                        dics.get(cat).add("-- " + MapleItemInformationProvider.getInstance().getName(itemId) + "\n" + sql);
                        ps.close();

                    } catch (SQLException ex) {
                        FilePrinter.printError("CashShopDumper.txt", ex);
                    }

                }));

        for (Integer key : dics.keySet()) {
            File fout = new File("cashshopItems/" + key.toString() + ".sql");
            List<String> l = dics.get(key);
            FileOutputStream fos = null;
            try {
                if (!fout.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    fout.createNewFile();
                }
                fos = new FileOutputStream(fout);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                for (String s : l) {
                    bw.write(s);
                    bw.newLine();
                }

                bw.close();

            } catch (IOException ex) {
                FilePrinter.printError("CashShopDumper.txt", ex);
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException ex) {
                    FilePrinter.printError("CashShopDumper.txt", ex);
                }
            }

        }

    }
}
