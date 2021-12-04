package tools;

import com.github.mrzhqiang.maplestory.domain.DShopItem;
import com.github.mrzhqiang.maplestory.domain.query.QDShopItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.MapleItemInformationProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Pungin
 */
public class FixShopItemsPrice {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixShopItemsPrice.class);

    private List<Integer> loadFromDB() {
        return new QDShopItem().orderBy().itemId.asc()
                .findStream()
                .map(DShopItem::getItemId)
                // 去重
                .distinct()
                .collect(Collectors.toList());
    }

    private void changePrice(int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        new QDShopItem().itemId.eq(itemId).orderBy().price.asc().findEach(it -> {
            if (ii.getPrice(itemId) > it.getPrice()) {
                String name = ii.getName(itemId);
                LOGGER.debug("道具: " + name + "道具ID: " + itemId + " 商店: " + it.getShopId() + " 价格: " + it.getPrice() + " 新价格:" + (long) ii.getPrice(itemId));
                it.setPrice((int) ii.getPrice(itemId));
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
