package tools;

import client.inventory.MapleInventoryType;
import com.github.mrzhqiang.maplestory.domain.DCashShopModifiedItem;
import com.github.mrzhqiang.maplestory.domain.query.QDCashShopModifiedItem;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.CashItemFactory;
import server.CashItemInfo.CashModInfo;
import server.MapleItemInformationProvider;

import java.io.*;
import java.util.*;

/**
 * @author Flower
 */
public class CashShopDumper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CashShopDumper.class);

    public static CashModInfo getModInfo(int sn) {
        DCashShopModifiedItem one = new QDCashShopModifiedItem().id.eq(sn).findOne();
        if (one != null) {
            return new CashModInfo(sn, one);
        }
        return null;
    }

    public static void main(String[] args) {
        CashModInfo m = getModInfo(20000393);
        CashItemFactory.getInstance().initialize();
        Collection<CashModInfo> list = CashItemFactory.getInstance().getAllModInfo();
        List<Integer> itemids = new ArrayList<>();
        List<Integer> qq = new ArrayList<>();

        Map<Integer, List<String>> dics = new HashMap<>();

        WzData.ETC.directory().findFile("Commodity.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .map(stream -> stream.flatMap(WzElement::childrenStream))
                .ifPresent(stream -> stream.forEach(element -> {
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

                    DCashShopModifiedItem item = new DCashShopModifiedItem();
                    item.id = sn;
                    item.showup = true;
                    item.itemid = 0;
                    item.priority = priority;
                    item.period = period;
                    item.gender = gender;
                    item.count = count > 1 ? count : 0;
                    item.meso = meso;
                    qq.add(itemId);
                    if ((1000000 <= itemId || itemId <= 1003091) && sn >= 20000000) {
                        item.discountPrice = price;
                    } else {
                        item.discountPrice = 0;
                    }
                    item.mark = 0;
                    item.unk1 = 0;
                    item.unk2 = 0;
                    item.unk3 = 0;
                    item.name = MapleItemInformationProvider.getInstance().getName(itemId);
                    dics.get(cat).add("-- " + MapleItemInformationProvider.getInstance().getName(itemId) + "\n");
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
