package server;

import com.github.mrzhqiang.maplestory.domain.DCashShopModifiedItem;
import com.github.mrzhqiang.maplestory.domain.query.QDCashShopModifiedItem;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.CashItemInfo.CashModInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CashItemFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CashItemFactory.class);

    private final static CashItemFactory instance = new CashItemFactory();
    private final static int[] BEST_ITEMS = new int[]{50100010, 50100010, 50100010, 50100010, 50100010};

    private static final Map<Integer, List<CashItemInfo>> CASH_PACKAGES = new HashMap<>();

    private boolean initialized = false;

    private final Map<Integer, CashItemInfo> itemStats = new HashMap<>();
    private final Map<Integer, List<CashItemInfo>> itemPackage = new HashMap<>();
    private final Map<Integer, CashModInfo> itemMods = new HashMap<>();
    private final Map<Integer, Integer> idLookup = new HashMap<>();

    public static CashItemFactory getInstance() {
        return instance;
    }

    protected CashItemFactory() {
    }

    public void initialize() {
        List<Integer> itemids = WzData.ETC.directory()
                .findFile("Commodity.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .map(stream -> stream.map(this::cashItemInfoOf)
                        .peek(this::handleCashItemInfo)
                        .map(CashItemInfo::getId)
                        .filter(integer -> integer > 0)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        for (int i : itemids) {
            getPackageItems(i);
        }
        for (int i : itemStats.keySet()) {
            getModInfo(i);
            getItem(i); //init the modinfo's citem
        }
        initialized = true;
    }

    private void handleCashItemInfo(CashItemInfo info) {
        int sn = info.getSN();
        int id = info.getId();
        if (sn > 0) {
            itemStats.put(sn, info);
            idLookup.put(id, sn);
        }
    }

    private CashItemInfo cashItemInfoOf(WzElement<?> element) {
        int snValue = Elements.findInt(element, "SN");
        int itemIdValue = Elements.findInt(element, "ItemId", 1);
        int countValue = Elements.findInt(element, "Count");
        int priceValue = Elements.findInt(element, "Price");
        int periodValue = Elements.findInt(element, "Period");
        int genderValue = Elements.findInt(element, "Gender", 2);
        int onsaleValue = Elements.findInt(element, "OnSale");
        return new CashItemInfo(itemIdValue, countValue, priceValue, snValue, periodValue, genderValue, onsaleValue > 0);
    }

    public final CashItemInfo getItem(int sn) {
        final CashItemInfo stats = itemStats.get(sn);
        // final CashItemInfo stats = itemStats.get(Integer.valueOf(sn));
        final CashModInfo z = getModInfo(sn);
        if (z != null && z.item.isShowUp()) {
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
        List<CashItemInfo> list = CASH_PACKAGES.get(itemId);
        if (list != null) {
            return list;
        }

        List<CashItemInfo> packageItems = new ArrayList<>();
        WzData.ETC.directory()
                .findFile("CashPackage.img")
                .map(WzFile::content)
                .map(it -> it.find(String.valueOf(itemId)))
                .map(WzElement::childrenStream)
                .map(stream -> stream.flatMap(WzElement::childrenStream))
                .ifPresent(stream -> stream.forEach(element -> {
                    // fixme 这一段代码有问题，需要看看要不要删除
//                    Integer sn = ((IntElement) element).value();
                    // packageItems.add(getItem(sn));
                    CASH_PACKAGES.put(itemId, packageItems);
                }));
        CASH_PACKAGES.put(itemId, packageItems);
        return packageItems;
    }

    public final CashModInfo getModInfo(int sn) {
        CashModInfo ret = itemMods.get(sn);
        //  LOGGER.debug(itemMods.toString());
        if (ret == null) {
            if (initialized) {
                return null;
            }
            ret = new QDCashShopModifiedItem()
                    .id.eq(sn)
                    .findOneOrEmpty()
                    .map(item -> new CashModInfo(sn, item))
                    .orElse(null);
            CashItemInfo cashItemInfo =itemStats.get(sn);
            // 数据库里面itemid不对导致购买不了东西
            if (cashItemInfo != null && ret != null) {
                ret.setItemId(cashItemInfo.getId());
            }
            itemMods.put(sn, ret);
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
        return BEST_ITEMS;
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
