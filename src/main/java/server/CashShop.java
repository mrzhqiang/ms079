package server;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.Equip;
import client.inventory.IItem;
import client.inventory.Item;
import client.inventory.ItemLoader;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import client.inventory.MapleRing;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DGift;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDGift;
import constants.GameConstants;
import tools.Pair;
import tools.packet.MTSCSPacket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CashShop implements Serializable {

    private static final long serialVersionUID = 231541893513373579L;

    private int accountId, characterId;
    private final List<IItem> inventory = new ArrayList<>();
    private final List<Integer> uniqueids = new ArrayList<>();

    public CashShop(int accountId, int characterId, int jobType) {
        this.accountId = accountId;
        this.characterId = characterId;

        Map<Integer, Pair<IItem, MapleInventoryType>> items;
        if (jobType / 1000 == 1) {
            // CASHSHOP_CYGNUS == 3
            items = ItemLoader.loadItems(3, false, accountId);
        } else if ((jobType / 100 == 21 || jobType / 100 == 20) && jobType != 2001) {
            // CASHSHOP_ARAN == 4
            items = ItemLoader.loadItems(4, false, accountId);
        } else if (jobType == 2001 || jobType / 100 == 22) {
            // CASHSHOP_EVAN == 7
            items = ItemLoader.loadItems(7, false, accountId);
        } else if (jobType >= 3000) {
            // CASHSHOP_RESIST == 11
            items = ItemLoader.loadItems(11, false, accountId);
        } else if (jobType / 10 == 43) {
            // CASHSHOP_DB == 10
            items = ItemLoader.loadItems(10, false, accountId);
        } else {
            // CASHSHOP_EXPLORER == 2
            items = ItemLoader.loadItems(2, false, accountId);
        }
        items.forEach((integer, item) -> inventory.add(item.getLeft()));
    }

    public int getItemsSize() {
        return inventory.size();
    }

    public List<IItem> getInventory() {
        return inventory;
    }

    public IItem findByCashId(int cashId) {
        for (IItem item : inventory) {
            if (item.getUniqueId() == cashId) {
                return item;
            }
        }
        return null;
    }

    public void checkExpire(MapleClient c) {
        List<IItem> toberemove = new ArrayList<IItem>();
        for (IItem item : inventory) {
            if (item != null && !GameConstants.isPet(item.getItemId()) && item.getExpiration() > 0 && item.getExpiration() < System.currentTimeMillis()) {
                toberemove.add(item);
            }
        }
        if (toberemove.size() > 0) {
            for (IItem item : toberemove) {
                removeFromInventory(item);
                c.getSession().write(MTSCSPacket.cashItemExpired(item.getUniqueId()));
            }
            toberemove.clear();
        }
    }

    public IItem toItemA(CashItemInfoA cItem) {
        return toItemA(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), "");
    }

    public IItem toItemA(CashItemInfoA cItem, String gift) {
        return toItemA(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), gift);
    }

    public IItem toItemA(CashItemInfoA cItem, int uniqueid) {
        return toItemA(cItem, uniqueid, "");
    }

    public IItem toItemA(CashItemInfoA cItem, int uniqueid, String gift) {
        if (uniqueid <= 0) {
            uniqueid = MapleInventoryIdentifier.getInstance();
        }
        long period = cItem.getPeriod();
        if (period <= 0 || GameConstants.isPet(cItem.getId())) {
            period = 45;
        }
        IItem ret = null;
        if (GameConstants.getInventoryType(cItem.getId()) == MapleInventoryType.EQUIP) {
            Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(cItem.getId());
            eq.setUniqueId(uniqueid);
            eq.setExpiration(System.currentTimeMillis() + (period * 24 * 60 * 60 * 1000));
            eq.setGiftFrom(gift);
            if (GameConstants.isEffectRing(cItem.getId()) && uniqueid > 0) {
                MapleRing ring = MapleRing.loadFromDb(uniqueid);
                if (ring != null) {
                    eq.setRing(ring);
                }
            }
            ret = eq.copy();
        } else {
            Item item = new Item(cItem.getId(), (byte) 0, (short) cItem.getCount(), (byte) 0, uniqueid);
            item.setExpiration((long) (System.currentTimeMillis() + (long) (period * 24 * 60 * 60 * 1000)));
            item.setGiftFrom(gift);
            if (GameConstants.isPet(cItem.getId())) {
                final MaplePet pet = MaplePet.createPet(cItem.getId(), uniqueid);
                if (pet != null) {
                    item.setPet(pet);
                }
            }
            ret = item.copy();
        }
        return ret;
    }

    public IItem toItem(CashItemInfo cItem) {
        return toItem(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), "");
    }

    public IItem toItem(CashItemInfo cItem, String gift) {
        return toItem(cItem, MapleInventoryManipulator.getUniqueId(cItem.getId(), null), gift);
    }

    public IItem toItem(CashItemInfo cItem, int uniqueid) {
        return toItem(cItem, uniqueid, "");
    }

    public IItem toItem(CashItemInfo cItem, int uniqueid, String gift) {
        if (uniqueid <= 0) {
            uniqueid = MapleInventoryIdentifier.getInstance();
        }
        long period = cItem.getPeriod();
        if (GameConstants.isPet(cItem.getId())) {
            period = 90;
        } else if (cItem.getId() >= 5210000 && cItem.getId() <= 5360099 && cItem.getId() != 5220007 && cItem.getId() != 5220008) {
        } else {
            period = 0;
        }
        IItem ret = null;
        if (GameConstants.getInventoryType(cItem.getId()) == MapleInventoryType.EQUIP) {
            Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(cItem.getId());
            eq.setUniqueId(uniqueid);
            if (GameConstants.isPet(cItem.getId()) || period > 0) {
                eq.setExpiration((long) (System.currentTimeMillis() + (long) (period * 24 * 60 * 60 * 1000)));
            }
            // eq.setExpiration((long) (System.currentTimeMillis() + (long) (period * 24 * 60 * 60 * 1000)));
            eq.setGiftFrom(gift);
            if (GameConstants.isEffectRing(cItem.getId()) && uniqueid > 0) {
                MapleRing ring = MapleRing.loadFromDb(uniqueid);
                if (ring != null) {
                    eq.setRing(ring);
                }
            }
            ret = eq.copy();
        } else {
            Item item = new Item(cItem.getId(), (byte) 0, (short) cItem.getCount(), (byte) 0, uniqueid);
            if (period > 0) {
                item.setExpiration((long) (System.currentTimeMillis() + (long) (period * 24 * 60 * 60 * 1000)));
            }
            if (cItem.getId() == 5211047 || cItem.getId() == 5360014) {
                item.setExpiration((long) (System.currentTimeMillis() + (long) (3 * 60 * 60 * 1000)));
            }
            //  LOGGER.debug(new Date(System.currentTimeMillis() + (long) (3 * 60 * 60 * 1000)));
            //item.setExpiration((long) (System.currentTimeMillis() + (long) (period * 24 * 60 * 60 * 1000)));
            item.setGiftFrom(gift);
            if (GameConstants.isPet(cItem.getId())) {
                final MaplePet pet = MaplePet.createPet(cItem.getId(), uniqueid);
                if (pet != null) {
                    item.setPet(pet);
                }
            }
            ret = item.copy();
        }
        return ret;
    }

    public void addToInventory(IItem item) {
        inventory.add(item);
    }

    public void removeFromInventory(IItem item) {
        inventory.remove(item);
    }

    public void gift(int recipient, String from, String message, int sn) {
        gift(recipient, from, message, sn, 0);
    }

    public void gift(int recipient, String from, String message, int sn, int uniqueid) {
        DGift gift = new DGift();
        gift.setRecipient(recipient);
        gift.setFrom(from);
        gift.setMessage(message);
        gift.setSn(sn);
        gift.setUniqueId(uniqueid);
        gift.save();
    }

    public List<Pair<IItem, String>> loadGifts() {
        List<Pair<IItem, String>> gifts = new QDGift().recipient.eq(characterId).findStream()
                .map(it -> {
                    CashItemInfo cItem = CashItemFactory.getInstance().getItem(it.getSn());
                    IItem item = toItem(cItem, it.getUniqueId(), it.getFrom());
                    uniqueids.add(item.getUniqueId());
                    List<CashItemInfo> packages = CashItemFactory.getInstance().getPackageItems(cItem.getId());
                    if (packages != null && packages.size() > 0) {
                        for (CashItemInfo packageItem : packages) {
                            addToInventory(toItem(packageItem, it.getFrom()));
                        }
                    } else {
                        addToInventory(item);
                    }
                    return new Pair<>(item, it.getMessage());
                })
                .collect(Collectors.toList());

        new QDGift().recipient.eq(characterId).delete();
        save(new QDCharacter().id.eq(characterId).findOne());
        return gifts;
    }

    public boolean canSendNote(int uniqueid) {
        return uniqueids.contains(uniqueid);
    }

    public void sendedNote(int uniqueid) {
        Iterator<Integer> iterator = uniqueids.iterator();
        if (iterator.hasNext()) {
            Integer next = iterator.next();
            if (next.equals(uniqueid)) {
                iterator.remove();
            }
        }
    }

    public void save(DCharacter character) {
        List<Pair<IItem, MapleInventoryType>> itemsWithType = new ArrayList<>();

        for (IItem item : inventory) {
            itemsWithType.add(new Pair<>(item, GameConstants.getInventoryType(item.getItemId())));
        }

        ItemLoader.saveItems(itemsWithType, character);
    }

    public IItem toItem(CashItemInfo cItem, MapleCharacter chr, int uniqueid, String gift) {
        if (uniqueid <= 0) {
            uniqueid = MapleInventoryIdentifier.getInstance();
        }

        IItem ret = null;
        if (GameConstants.getInventoryType(cItem.getId()) == MapleInventoryType.EQUIP) {
            Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(cItem.getId());
            eq.setUniqueId(uniqueid);
            eq.setGiftFrom(gift);
            if (GameConstants.isEffectRing(cItem.getId()) && uniqueid > 0) {
                MapleRing ring = MapleRing.loadFromDb(uniqueid);
                if (ring != null) {
                    eq.setRing(ring);
                }
            }
            ret = eq.copy();
        } else {
            Item item = new Item(cItem.getId(), (byte) 0, (short) cItem.getCount(), (byte) 0, uniqueid);
            item.setGiftFrom(gift);
            if (GameConstants.isPet(cItem.getId())) {
                final MaplePet pet = MaplePet.createPet(cItem.getId(), uniqueid);
                if (pet != null) {
                    item.setPet(pet);
                }
            }
            ret = item.copy();
        }
        return ret;
    }
}
