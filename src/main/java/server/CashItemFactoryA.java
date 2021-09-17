package server;

import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.github.mrzhqiang.maplestory.wz.element.ImgdirElement;

import java.util.*;
import java.util.stream.Collectors;

public class CashItemFactoryA {

    private static final Map<Integer, CashItemInfoA> ITEM_STATS = new HashMap<>();
    private static final Map<Integer, Integer> SN_LOOKUP = new HashMap<>();
    private static final Map<Integer, Integer> ID_LOOKUP = new HashMap<>();
    private static final Map<Integer, List<CashItemInfoA>> CASH_PACKAGES = new HashMap<>();

    public static CashItemInfoA getItem(int sn) {
        CashItemInfoA stats = ITEM_STATS.get(sn);
        if (stats != null) {
            return stats;
        }

        String cid = String.valueOf(getCommodityFromSN(sn));
        return WzData.ETC.directory()
                .findFile("Commodity.img")
                .map(WzFile::content)
                .map(element -> {
                    int itemId = Elements.findInt(element, cid + "/ItemId");
                    int count = Elements.findInt(element, cid + "/Count", 1);
                    int price = Elements.findInt(element, cid + "/Price");
                    int period = Elements.findInt(element, cid + "/Period");
                    int gender = Elements.findInt(element, cid + "/Gender", 2);
                    boolean onSale = Elements.findInt(element, cid + "/OnSale") == 1;
                    CashItemInfoA info = new CashItemInfoA(sn, itemId, count, price, period, gender, onSale);
                    ITEM_STATS.put(sn, info);
                    return info;
                }).orElse(null);
    }

    private static int getCommodityFromSN(int sn) {
        Integer cid = SN_LOOKUP.get(sn);
        if (cid != null) {
            return cid;
        }

        int curr = 0;
        int currSN = 0;
        Optional<ImgdirElement> optional = WzData.ETC.directory()
                .findFile("Commodity.img")
                .map(WzFile::content);
        if (SN_LOOKUP.isEmpty()) {
            currSN = optional.map(element -> Elements.findInt(element, "0/SN")).orElse(0);
            SN_LOOKUP.put(currSN, curr);
        }

        for (int i = SN_LOOKUP.size() - 1; currSN != sn; i++) {
            curr = i;
            String name = String.valueOf(curr);
            currSN = optional.map(element -> Elements.findInt(element, name + "/SN")).orElse(0);
            SN_LOOKUP.put(currSN, curr);
        }
        return curr;
    }

    public static List<CashItemInfoA> getPackageItems(int itemId) {
        List<CashItemInfoA> info = CASH_PACKAGES.get(itemId);
        if (info != null) {
            return info;
        }

        List<CashItemInfoA> packageItems = WzData.ETC.directory()
                .findFile("CashPackage.img")
                .map(WzFile::content)
                .map(it -> it.find(String.valueOf(itemId)))
                .map(WzElement::childrenStream)
                .map(stream -> stream.map(Elements::ofInt)
                        .map(CashItemFactoryA::getItem)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        CASH_PACKAGES.put(itemId, packageItems);
        return packageItems;
    }

    public static int getSnFromId(int id) {
        Integer cid = ID_LOOKUP.get(id);
        Optional<ImgdirElement> optional = WzData.ETC.directory()
                .findFile("Commodity.img")
                .map(WzFile::content);
        if (cid == null) {
            int curr = 0;
            int currSN = 0;
            if (ID_LOOKUP.isEmpty()) {
                currSN = optional.map(element -> Elements.findInt(element, "0/ItemId")).orElse(0);
                ID_LOOKUP.put(currSN, curr);
            }

            for (int i = ID_LOOKUP.size() - 1; currSN != id; i++) {
                curr = i;
                String name = String.valueOf(curr);
                currSN = optional.map(element -> Elements.findInt(element, name + "/ItemId")).orElse(0);
                ID_LOOKUP.put(currSN, curr);
            }
            cid = curr;
        }

        String name = String.valueOf(cid);
        return optional.map(element -> Elements.findInt(element, name + "/SN")).orElse(0);
    }
}