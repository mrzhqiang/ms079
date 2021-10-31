package tools;

import com.github.mrzhqiang.maplestory.domain.query.QDShopItem;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.MapleItemInformationProvider;

/**
 *
 * @author Pungin
 */
public class FixShopItemsPrice {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixShopItemsPrice.class);

    private List<Integer> loadFromDB() {
        return new QDShopItem().orderBy().itemid.asc()
                .findStream()
                .map(it -> it.itemid)
                // 去重
                .distinct()
                .collect(Collectors.toList());
    }

    private void changePrice(int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        new QDShopItem().itemid.eq(itemId).orderBy().price.asc().findEach(it -> {
            if (ii.getPrice(itemId) > it.price) {
                String name = ii.getName(itemId);
                LOGGER.debug("道具: " + name + "道具ID: " + itemId + " 商店: " + it.shopid + " 价格: " + it.price + " 新价格:" + (long) ii.getPrice(itemId));
                it.price = (int)ii.getPrice(itemId);
                it.save();
            }
        });
    }

    public static void main(String[] args) {
        System.setProperty("wzpath", System.getProperty("wzpath"));
        FixShopItemsPrice i = new FixShopItemsPrice();
        LOGGER.debug("正在加载道具数据......");
       // MapleItemInformationProvider.getInstance().load();
        MapleItemInformationProvider.getInstance().runEtc();
        MapleItemInformationProvider.getInstance().runItems();
        LOGGER.debug("正在读取商店内商品......");
        List<Integer> list = i.loadFromDB();
        LOGGER.debug("正在处理商店内商品价格......");
        for (int ii : list) {
            i.changePrice(ii);
        }
        LOGGER.debug("处理商品价格结束。");
    }
}
