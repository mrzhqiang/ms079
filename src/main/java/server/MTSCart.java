package server;

import client.inventory.IItem;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryType;
import com.github.mrzhqiang.maplestory.domain.DMtsCart;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDMtsCart;
import constants.GameConstants;
import tools.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MTSCart implements Serializable {

    private static final long serialVersionUID = 231541893513373578L;
    private int characterId, tab = 1, type = 0, page = 0;
    //tab; 1 = buy now, 2 = wanted, 3 = auction, 4 = cart
    //type = inventorytype; 0 = anything
    //page = whatever
    private List<IItem> transfer = new ArrayList<IItem>();
    private List<Integer> cart = new ArrayList<Integer>();
    private List<Integer> notYetSold = new ArrayList<Integer>(10);
    private int owedNX = 0;

    public MTSCart(int characterId) {
        this.characterId = characterId;
        for (Pair<IItem, MapleInventoryType> item : ItemLoader.loadItems(9, false, characterId).values()) {
            transfer.add(item.getLeft());
        }
        loadCart();
        loadNotYetSold();
    }

    public List<IItem> getInventory() {
        return transfer;
    }

    public void addToInventory(IItem item) {
        transfer.add(item);
    }

    public void removeFromInventory(IItem item) {
        transfer.remove(item);
    }

    public List<Integer> getCart() {
        return cart;
    }

    public boolean addToCart(final int car) {
        if (!cart.contains(car)) {
            cart.add(car);
            return true;
        }
        return false;
    }

    public void removeFromCart(final int car) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i) == car) {
                cart.remove(i);
            }
        }
    }

    public List<Integer> getNotYetSold() {
        return notYetSold;
    }

    public void addToNotYetSold(final int car) {
        notYetSold.add(car);
    }

    public void removeFromNotYetSold(final int car) {
        for (int i = 0; i < notYetSold.size(); i++) {
            if (notYetSold.get(i) == car) {
                notYetSold.remove(i);
            }
        }
    }

    public final int getSetOwedNX() {
        final int on = owedNX;
        owedNX = 0;
        return on;
    }

    public void increaseOwedNX(final int newNX) {
        owedNX += newNX;
    }

    public void save() {
        List<Pair<IItem, MapleInventoryType>> itemsWithType = new ArrayList<>();

        for (IItem item : getInventory()) {
            itemsWithType.add(new Pair<>(item, GameConstants.getInventoryType(item.getItemId())));
        }

        ItemLoader.saveItems(itemsWithType,
                new QDCharacter().id.eq(characterId).findOne());
        new QDMtsCart().characterId.eq(characterId).delete();

        for (int i : cart) {
            DMtsCart cart = new DMtsCart();
            cart.setCharacterId(characterId);
            cart.setItemId(i);
            cart.save();
        }
        if (owedNX > 0) {
            DMtsCart cart = new DMtsCart();
            cart.setCharacterId(characterId);
            cart.setItemId(-owedNX);
            cart.save();
        }
        //not Yet Sold 不应该保存在这里
    }

    public void loadCart() {
        new QDMtsCart().characterId.eq(characterId).findEach(it -> {
            if (it.getItemId() < 0) {
                owedNX -= it.getItemId();
            } else if (MTSStorage.getInstance().check(it.getItemId())) {
                cart.add(it.getItemId());
            }
        });
    }

    public void loadNotYetSold() {
        /*new QDMtsitem().characterid.eq(characterId).findEach(it -> {
            if (MTSStorage.getInstance().check(it.id)) {
                notYetSold.add(it.id);
            }
        });*/
    }

    public void changeInfo(final int tab, final int type, final int page) {
        this.tab = tab;
        this.type = type;
        this.page = page;
    }

    public int getTab() {
        return tab;
    }

    public int getType() {
        return type;
    }

    public int getPage() {
        return page;
    }
}
