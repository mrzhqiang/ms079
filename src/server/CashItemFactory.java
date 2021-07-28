package server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.CashItemInfo.CashModInfo;
import tools.wztosql.AddCashItemToDB;

public class CashItemFactory {
    
    private final static CashItemFactory instance = new CashItemFactory();
    private final static int[] bestItems = new int[]{50100010, 50100010, 50100010, 50100010, 50100010};
    private boolean initialized = false;
    private final Map<Integer, CashItemInfo> itemStats = new HashMap<Integer, CashItemInfo>();
    private final Map<Integer, List<CashItemInfo>> itemPackage = new HashMap<Integer, List<CashItemInfo>>();
    private final Map<Integer, CashModInfo> itemMods = new HashMap<Integer, CashModInfo>();
    private final MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Etc.wz"));
    //是这个目录把，嗯
    private final MapleDataProvider itemStringInfo = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/String.wz"));
    private Map<Integer, Integer> idLookup = new HashMap();
        private static Map<Integer, List<CashItemInfo>> cashPackages = new HashMap();
    public static final CashItemFactory getInstance() {
        return instance;
    }
    
    protected CashItemFactory() {
    }
    
    public void initialize() {
        //System.out.println("商城 :::");
        final List<Integer> itemids = new ArrayList<Integer>();
        for (MapleData field : data.getData("Commodity.img").getChildren()) {
            final int SN = MapleDataTool.getIntConvert("SN", field, 0);
            final int itemId = MapleDataTool.getIntConvert("ItemId", field, 0);
            final CashItemInfo stats = new CashItemInfo(itemId,
                    MapleDataTool.getIntConvert("Count", field, 1),
                    MapleDataTool.getIntConvert("Price", field, 0), SN,
                    MapleDataTool.getIntConvert("Period", field, 0),
                    MapleDataTool.getIntConvert("Gender", field, 2),
                    MapleDataTool.getIntConvert("OnSale", field, 0) > 0);
            
         /*   String name = "";
            MapleData nameData = null;
            int id = 0;
            for (MapleData Eqp : itemStringInfo.getData("Eqp.img").getChildByPath("Eqp").getChildren()) {
                nameData = Eqp.getChildByPath("name");
                id = Integer.parseInt(Eqp.getName().substring(0, Eqp.getName().length() - 4));
            }
            for (MapleData Cash : itemStringInfo.getData("Cash.img").getChildren()) {
                 nameData = Cash.getChildByPath("name");
                 id = Integer.parseInt(Cash.getName());
            }
            for (MapleData Item : itemStringInfo.getData("Item.img").getChildren()) {
                 nameData = Item.getChildByPath("name");
                 id = Integer.parseInt(Item.getName());
            }
            for (MapleData Pet : itemStringInfo.getData("Pet.img").getChildren()) {
                 nameData = Pet.getChildByPath("name");
                 id = Integer.parseInt(Pet.getName());
            }
            for (MapleData Ins : itemStringInfo.getData("Ins.img").getChildren()) {
                 nameData = Ins.getChildByPath("name");
                 id = Integer.parseInt(Ins.getName());
            }
            for (MapleData Etc : itemStringInfo.getData("Etc.img").getChildByPath("Etc").getChildren()) {
                 nameData = Etc.getChildByPath("name");
                 id = Integer.parseInt(Etc.getName());
            }
            for (MapleData Consume : itemStringInfo.getData("Consume.img").getChildren()) {
                 nameData = Consume.getChildByPath("name");
                 id = Integer.parseInt(Consume.getName());
            }
            if (itemId == id && id != 0) {
                if (nameData != null) {
                    name = (String) nameData.getData();
                }
            }*/
            /*try {
                AddCashItemToDB.addItem(stats.getId(), stats.getCount(), stats.getPrice(), stats.getSN(), stats.getExpire(), stats.getGender(), stats.getOnSale());
            } catch (Exception ex) {
                Logger.getLogger(CashItemFactory.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            if (SN > 0) {
                itemStats.put(SN, stats);
                idLookup.put(itemId, SN);
            }
            
            if (itemId > 0) {
                itemids.add(itemId);
            }
        }
        for (int i : itemids) {
            getPackageItems(i);
        }
        for (int i : itemStats.keySet()) {
            getModInfo(i);
            getItem(i); //init the modinfo's citem
        }
        initialized = true;
    }
    
    public final CashItemInfo getItem(int sn) {
        final CashItemInfo stats = itemStats.get(sn);
        // final CashItemInfo stats = itemStats.get(Integer.valueOf(sn));
        final CashModInfo z = getModInfo(sn);
        if (z != null && z.showUp) {
            return z.toCItem(stats); //null doesnt matter
        }
        if (stats == null || !stats.onSale()) {
            return null;
        }
        //hmm
        return stats;
    }
    
   /* public final List<CashItemInfo> getPackageItems(int itemId) {
        return itemPackage.get(itemId);
    }*/
        public static List<CashItemInfo> getPackageItems(int itemId) {
        if (cashPackages.containsKey(itemId)) {
            return cashPackages.get(itemId);
        }
        List<CashItemInfo> packageItems = new ArrayList<CashItemInfo>();
        MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/" + "Etc.wz"));
        MapleData a = dataProvider.getData("CashPackage.img");
        for (MapleData b : a.getChildren()) {
            if (itemId == Integer.parseInt(b.getName())) {
                for (MapleData c : b.getChildren()) {
                    for (MapleData d : c.getChildren()) {
                        int SN = MapleDataTool.getIntConvert("" + Integer.parseInt(d.getName()), c);
                        //packageItems.add(getItem(SN));
                        cashPackages.put(itemId, packageItems);
                    }
                }
                break;
            }
        }
        cashPackages.put(itemId, packageItems);
        return packageItems;
    }
   /* public final List<CashItemInfo> getPackageItems(int itemId) {
        if (itemPackage.get(itemId) != null) {
            return itemPackage.get(itemId);
        }
        final List<CashItemInfo> packageItems = new ArrayList<CashItemInfo>();
        
        final MapleData b = data.getData("CashPackage.img");

        if (b == null || b.getChildByPath(itemId + "/SN") == null) {
            return null;
        }
        for (MapleData d : b.getChildByPath(itemId + "/SN").getChildren()) {
            packageItems.add(itemStats.get(Integer.valueOf(MapleDataTool.getIntConvert(d))));
        }
        itemPackage.put(itemId, packageItems);
        return packageItems;
    }*/
    
    public final CashModInfo getModInfo(int sn) {
        CashModInfo ret = itemMods.get(sn);
      //  System.out.println(itemMods.toString());
        if (ret == null) {
            if (initialized) {
                return null;
            }
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM cashshop_modified_items WHERE serial = ?");
                ps.setInt(1, sn);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ret = new CashModInfo(sn, rs.getInt("discount_price"), rs.getInt("mark"), rs.getInt("showup") > 0, rs.getInt("itemid"), rs.getInt("priority"), rs.getInt("package") > 0, rs.getInt("period"), rs.getInt("gender"), rs.getInt("count"), rs.getInt("meso"), rs.getInt("unk_1"), rs.getInt("unk_2"), rs.getInt("unk_3"), rs.getInt("extra_flags"));
                    itemMods.put(sn, ret);
                }
                rs.close();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    
    public final Collection<CashModInfo> getAllModInfo() {
        if (!initialized) {
            initialize();
        }
        return itemMods.values();
    }
    
    public final int[] getBestItems() {
        return bestItems;
    }
    
    public int getSnFromId(int itemId) {
        return idLookup.get(itemId);
    }
    public final void clearCashShop() {
        itemStats.clear();
        itemPackage.clear();
        itemMods.clear();
        idLookup.clear();
        initialized = false;
        initialize();
    }

    public final int getItemSN(int itemid) {
        for (Map.Entry<Integer, CashItemInfo> ci : itemStats.entrySet()) {
            if (ci.getValue().getId() == itemid) {
                return ci.getValue().getSN();
            }
        }
        return 0;
    }
}
