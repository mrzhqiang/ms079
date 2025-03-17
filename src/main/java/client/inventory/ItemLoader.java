package client.inventory;

import com.github.mrzhqiang.maplestory.domain.*;
import com.github.mrzhqiang.maplestory.domain.query.QDCsItem;
import com.github.mrzhqiang.maplestory.domain.query.QDDueyItem;
import com.github.mrzhqiang.maplestory.domain.query.QDHiredMerchItem;
import com.github.mrzhqiang.maplestory.domain.query.QDInventoryItem;
import com.github.mrzhqiang.maplestory.domain.query.QDMtsItem;
import com.github.mrzhqiang.maplestory.domain.query.QDMtsTransfer;
import constants.GameConstants;
import tools.Pair;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public enum ItemLoader {
    ; // no instance

    // 0 INVENTORY("inventoryitems", "inventoryequipment", 0, "character_id")
    // 1 STORAGE("inventoryitems", "inventoryequipment", 1, "accountid")
    // 2 CASHSHOP_EXPLORER("csitems", "csequipment", 2, "accountid")
    // 3 CASHSHOP_CYGNUS("csitems", "csequipment", 3, "accountid")
    // 4 CASHSHOP_ARAN("csitems", "csequipment", 4, "accountid")
    // 5 HIRED_MERCHANT("hiredmerchitems", "hiredmerchequipment", 5, "packageid", "accountid", "character_id")
    // 6 DUEY("dueyitems", "dueyequipment", 6, "packageid")
    // 7 CASHSHOP_EVAN("csitems", "csequipment", 7, "accountid")
    // 8 MTS("mtsitems", "mtsequipment", 8, "packageid")
    // 9 MTS_TRANSFER("mtstransfer", "mtstransferequipment", 9, "character_id")
    // 10 CASHSHOP_DB("csitems", "csequipment", 10, "accountid")
    // 11 CASHSHOP_RESIST("csitems", "csequipment", 11, "accountid")
    public static Map<Integer, Pair<IItem, MapleInventoryType>> loadItems_hm(int value, int packageid, int accountid) {
        Map<Integer, Pair<IItem, MapleInventoryType>> items = new LinkedHashMap<>();
        new QDHiredMerchItem().type.eq(value).packageId.eq(packageid).account.id.eq(accountid).findStream()
                .forEach(it -> {
                    MapleInventoryType mit = MapleInventoryType.getByType(it.getInventoryType());
                    if (MapleInventoryType.EQUIP.equals(mit) || MapleInventoryType.EQUIPPED.equals(mit)) {
                        MapleHiredMerchEquip equip = new MapleHiredMerchEquip(it);
                        equip.setQuantity(1);
                        if (equip.getUniqueId() > -1) {
                            if (GameConstants.isEffectRing(it.getItemId())) {
                                MapleRing ring = MapleRing.loadFromDb(equip.getUniqueId(), mit.equals(MapleInventoryType.EQUIPPED));
                                if (ring != null) {
                                    equip.setRing(ring);
                                }
                            }
                        }
                        items.put(it.getId(), new Pair<>(equip.copy(), mit));
                    } else {
                        MapleHiredMerchItem item = new MapleHiredMerchItem(it);
                        if (GameConstants.isPet(item.getItemId())) {
                            if (item.getUniqueId() > -1) {
                                MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(), item.getPosition());
                                if (pet != null) {
                                    item.setPet(pet);
                                }
                            } else {
                                //O_O hackish fix
                                int new_unique = MapleInventoryIdentifier.getInstance();
                                item.setUniqueId(new_unique);
                                item.setPet(MaplePet.createPet(item.getItemId(), new_unique));
                            }
                        }
                        items.put(it.getId(), new Pair<>(item.copy(), mit));
                    }
                });
        return items;
    }
    //does not need connection con to be auto commit

    public static Map<Integer, Pair<IItem, MapleInventoryType>> loadItems(int value, boolean login, int... id) {
        Map<Integer, Pair<IItem, MapleInventoryType>> items = new LinkedHashMap<>();
        QDInventoryItem select = new QDInventoryItem().type.eq(value);
        switch (value) {
            case 0:
                // 0 INVENTORY("inventoryitems", "inventoryequipment", 0, "character_id")
                return loadInventoryItemByCharacter(login, id[0]);
            case 1:
                // 1 STORAGE("inventoryitems", "inventoryequipment", 1, "accountid")
                return loadInventoryItemByAccount(login, id[0]);
            case 2:
            case 3:
            case 4:
            case 7:
            case 10:
            case 11:
                // 2 CASHSHOP_EXPLORER("csitems", "csequipment", 2, "accountid")
                // 3 CASHSHOP_CYGNUS("csitems", "csequipment", 3, "accountid")
                // 4 CASHSHOP_ARAN("csitems", "csequipment", 4, "accountid")
                // 7 CASHSHOP_EVAN("csitems", "csequipment", 7, "accountid")
                // 10 CASHSHOP_DB("csitems", "csequipment", 10, "accountid")
                // 11 CASHSHOP_RESIST("csitems", "csequipment", 11, "accountid")
                return loadCsItem(value, login, id[0]);
            case 5:
                // 5 HIRED_MERCHANT("hiredmerchitems", "hiredmerchequipment", 5, "packageid", "accountid", "character_id")
                return loadHiredMerchItem(login, id[0], id[1], id[2]);
            case 6:
                // 6 DUEY("dueyitems", "dueyequipment", 6, "packageid")
                return loadDueyItem(login, id[0]);
            case 8:
                // 8 MTS("mtsitems", "mtsequipment", 8, "packageid")
                return loadMtsItem(login, id[0]);
            case 9:
                // 9 MTS_TRANSFER("mtstransfer", "mtstransferequipment", 9, "character_id")
                return loadMtsTransfer(login, id[0]);
        }
        if (login) {
            select.inventoryType.eq(MapleInventoryType.EQUIPPED.getType());
        }

        select.findEach(it -> {
            MapleInventoryType mit = MapleInventoryType.getByType(it.getInventoryType());
            if (mit == null) {
                return;
            }

            if (MapleInventoryType.EQUIP.equals(mit) || MapleInventoryType.EQUIPPED.equals(mit)) {
                Equip equip = new Equip(it);
                if (!login) {
                    equip.setQuantity(1);

                    if (equip.getUniqueId() > -1) {
                        if (GameConstants.isEffectRing(it.getItemId())) {
                            MapleRing ring = MapleRing.loadFromDb(
                                    equip.getUniqueId(), MapleInventoryType.EQUIPPED.equals(mit));
                            if (ring != null) {
                                equip.setRing(ring);
                            }
                        }
                    }
                }
                // copy ? 人才
                items.put(it.getId(), new Pair<>(equip.copy(), mit));
            } else {
                Item item = new Item(it);
                if (GameConstants.isPet(item.getItemId())) {
                    if (item.getUniqueId() > -1) {
                        MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(), item.getPosition());
                        if (pet != null) {
                            item.setPet(pet);
                        }
                    } else {
                        //O_O hackish fix
                        int new_unique = MapleInventoryIdentifier.getInstance();
                        item.setUniqueId(new_unique);
                        item.setPet(MaplePet.createPet(item.getItemId(), new_unique));
                    }
                }
                items.put(it.getId(), new Pair<>(item.copy(), mit));
            }
        });
        return items;
    }

    private static Map<Integer, Pair<IItem, MapleInventoryType>> loadMtsTransfer(boolean login, int id) {
        Map<Integer, Pair<IItem, MapleInventoryType>> items = new LinkedHashMap<>();

        QDMtsTransfer select = new QDMtsTransfer().type.eq(9).character.id.eq(id);
        if (login) {
            select.inventoryType.eq(MapleInventoryType.EQUIPPED.getType());
        }

        select.findEach(it -> {
            MapleInventoryType mit = MapleInventoryType.getByType(it.getInventoryType());
            if (mit == null) {
                return;
            }

            if (MapleInventoryType.EQUIP.equals(mit) || MapleInventoryType.EQUIPPED.equals(mit)) {
                MapleMtsTransferEquip equip = new MapleMtsTransferEquip(it);
                if (!login) {
                    equip.setQuantity(1);

                    if (equip.getUniqueId() > -1) {
                        if (GameConstants.isEffectRing(it.getItemId())) {
                            MapleRing ring = MapleRing.loadFromDb(
                                    equip.getUniqueId(), MapleInventoryType.EQUIPPED.equals(mit));
                            if (ring != null) {
                                equip.setRing(ring);
                            }
                        }
                    }
                }
                // copy ? 人才
                items.put(it.getId(), new Pair<>(equip.copy(), mit));
            } else {
                MapleMtsTransferItem item = new MapleMtsTransferItem(it);
                if (GameConstants.isPet(item.getItemId())) {
                    if (item.getUniqueId() > -1) {
                        MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(), item.getPosition());
                        if (pet != null) {
                            item.setPet(pet);
                        }
                    } else {
                        //O_O hackish fix
                        int new_unique = MapleInventoryIdentifier.getInstance();
                        item.setUniqueId(new_unique);
                        item.setPet(MaplePet.createPet(item.getItemId(), new_unique));
                    }
                }
                items.put(it.getId(), new Pair<>(item.copy(), mit));
            }
        });
        return items;
    }

    private static Map<Integer, Pair<IItem, MapleInventoryType>> loadMtsItem(boolean login, int id) {
        Map<Integer, Pair<IItem, MapleInventoryType>> items = new LinkedHashMap<>();

        QDMtsItem select = new QDMtsItem().type.eq(8).packageId.eq(id);
        if (login) {
            select.inventoryType.eq(MapleInventoryType.EQUIPPED.getType());
        }

        select.findEach(it -> {
            MapleInventoryType mit = MapleInventoryType.getByType(it.getInventoryType());
            if (mit == null) {
                return;
            }

            if (MapleInventoryType.EQUIP.equals(mit) || MapleInventoryType.EQUIPPED.equals(mit)) {
                MapleMtsEquip equip = new MapleMtsEquip(it);
                if (!login) {
                    equip.setQuantity(1);

                    if (equip.getUniqueId() > -1) {
                        if (GameConstants.isEffectRing(it.getItemId())) {
                            MapleRing ring = MapleRing.loadFromDb(
                                    equip.getUniqueId(), MapleInventoryType.EQUIPPED.equals(mit));
                            if (ring != null) {
                                equip.setRing(ring);
                            }
                        }
                    }
                }
                // copy ? 人才
                items.put(it.getId(), new Pair<>(equip.copy(), mit));
            } else {
                MapleMtsItem item = new MapleMtsItem(it);
                if (GameConstants.isPet(item.getItemId())) {
                    if (item.getUniqueId() > -1) {
                        MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(), item.getPosition());
                        if (pet != null) {
                            item.setPet(pet);
                        }
                    } else {
                        //O_O hackish fix
                        int new_unique = MapleInventoryIdentifier.getInstance();
                        item.setUniqueId(new_unique);
                        item.setPet(MaplePet.createPet(item.getItemId(), new_unique));
                    }
                }
                items.put(it.getId(), new Pair<>(item.copy(), mit));
            }
        });
        return items;
    }

    private static Map<Integer, Pair<IItem, MapleInventoryType>> loadDueyItem(boolean login, int id) {
        Map<Integer, Pair<IItem, MapleInventoryType>> items = new LinkedHashMap<>();

        QDDueyItem select = new QDDueyItem().type.eq(6).pack.id.eq(id);
        if (login) {
            select.inventoryType.eq(MapleInventoryType.EQUIPPED.getType());
        }

        select.findEach(it -> {
            MapleInventoryType mit = MapleInventoryType.getByType(it.getInventoryType());
            if (mit == null) {
                return;
            }

            if (MapleInventoryType.EQUIP.equals(mit) || MapleInventoryType.EQUIPPED.equals(mit)) {
                MapleDueyEquip equip = new MapleDueyEquip(it);
                if (!login) {
                    equip.setQuantity(1);

                    if (equip.getUniqueId() > -1) {
                        if (GameConstants.isEffectRing(it.getItemId())) {
                            MapleRing ring = MapleRing.loadFromDb(
                                    equip.getUniqueId(), MapleInventoryType.EQUIPPED.equals(mit));
                            if (ring != null) {
                                equip.setRing(ring);
                            }
                        }
                    }
                }
                // copy ? 人才
                items.put(it.getId(), new Pair<>(equip.copy(), mit));
            } else {
                MapleDueyItem item = new MapleDueyItem(it);
                if (GameConstants.isPet(item.getItemId())) {
                    if (item.getUniqueId() > -1) {
                        MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(), item.getPosition());
                        if (pet != null) {
                            item.setPet(pet);
                        }
                    } else {
                        //O_O hackish fix
                        int new_unique = MapleInventoryIdentifier.getInstance();
                        item.setUniqueId(new_unique);
                        item.setPet(MaplePet.createPet(item.getItemId(), new_unique));
                    }
                }
                items.put(it.getId(), new Pair<>(item.copy(), mit));
            }
        });
        return items;
    }

    private static Map<Integer, Pair<IItem, MapleInventoryType>> loadHiredMerchItem(boolean login,
                                                                                    int packageId,
                                                                                    int accountId,
                                                                                    int characterId) {
        Map<Integer, Pair<IItem, MapleInventoryType>> items = new LinkedHashMap<>();
        QDHiredMerchItem select = new QDHiredMerchItem().type.eq(5);
        if (packageId > 0) {
            select.packageId.eq(packageId);
        }
        if (accountId > 0) {
            select.account.id.eq(accountId);
        }
        if (characterId > 0) {
            select.character.id.eq(characterId);
        }
        if (login) {
            select.inventoryType.eq(MapleInventoryType.EQUIPPED.getType());
        }

        select.findEach(it -> {
            MapleInventoryType mit = MapleInventoryType.getByType(it.getInventoryType());
            if (mit == null) {
                return;
            }

            if (MapleInventoryType.EQUIP.equals(mit) || MapleInventoryType.EQUIPPED.equals(mit)) {
                MapleHiredMerchEquip equip = new MapleHiredMerchEquip(it);
                if (!login) {
                    equip.setQuantity(1);

                    if (equip.getUniqueId() > -1) {
                        if (GameConstants.isEffectRing(it.getItemId())) {
                            MapleRing ring = MapleRing.loadFromDb(
                                    equip.getUniqueId(), MapleInventoryType.EQUIPPED.equals(mit));
                            if (ring != null) {
                                equip.setRing(ring);
                            }
                        }
                    }
                }
                // copy ? 人才
                items.put(it.getId(), new Pair<>(equip.copy(), mit));
            } else {
                MapleHiredMerchItem item = new MapleHiredMerchItem(it);
                if (GameConstants.isPet(item.getItemId())) {
                    if (item.getUniqueId() > -1) {
                        MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(), item.getPosition());
                        if (pet != null) {
                            item.setPet(pet);
                        }
                    } else {
                        //O_O hackish fix
                        int new_unique = MapleInventoryIdentifier.getInstance();
                        item.setUniqueId(new_unique);
                        item.setPet(MaplePet.createPet(item.getItemId(), new_unique));
                    }
                }
                items.put(it.getId(), new Pair<>(item.copy(), mit));
            }
        });
        return items;
    }

    private static Map<Integer, Pair<IItem, MapleInventoryType>> loadCsItem(int value, boolean login, int id) {
        Map<Integer, Pair<IItem, MapleInventoryType>> items = new LinkedHashMap<>();

        QDCsItem select = new QDCsItem().type.eq(value).account.id.eq(id);
        if (login) {
            select.inventoryType.eq(MapleInventoryType.EQUIPPED.getType());
        }

        select.findEach(it -> {
            MapleInventoryType mit = MapleInventoryType.getByType(it.getInventoryType());
            if (mit == null) {
                return;
            }

            if (MapleInventoryType.EQUIP.equals(mit) || MapleInventoryType.EQUIPPED.equals(mit)) {
                MapleCsEquip equip = new MapleCsEquip(it);
                if (!login) {
                    equip.setQuantity(1);

                    if (equip.getUniqueId() > -1) {
                        if (GameConstants.isEffectRing(it.getItemId())) {
                            MapleRing ring = MapleRing.loadFromDb(
                                    equip.getUniqueId(), MapleInventoryType.EQUIPPED.equals(mit));
                            if (ring != null) {
                                equip.setRing(ring);
                            }
                        }
                    }
                }
                // copy ? 人才
                items.put(it.getId(), new Pair<>(equip.copy(), mit));
            } else {
                MapleCsItem item = new MapleCsItem(it);
                if (GameConstants.isPet(item.getItemId())) {
                    if (item.getUniqueId() > -1) {
                        MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(), item.getPosition());
                        if (pet != null) {
                            item.setPet(pet);
                        }
                    } else {
                        //O_O hackish fix
                        int new_unique = MapleInventoryIdentifier.getInstance();
                        item.setUniqueId(new_unique);
                        item.setPet(MaplePet.createPet(item.getItemId(), new_unique));
                    }
                }
                items.put(it.getId(), new Pair<>(item.copy(), mit));
            }
        });
        return items;
    }

    private static Map<Integer, Pair<IItem, MapleInventoryType>> loadInventoryItemByCharacter(boolean login, int id) {
        Map<Integer, Pair<IItem, MapleInventoryType>> items = new LinkedHashMap<>();
        QDInventoryItem select = new QDInventoryItem().type.eq(0).character.id.eq(id);
        if (login) {
            select.inventoryType.eq(MapleInventoryType.EQUIPPED.getType());
        }

        select.findEach(it -> {
            MapleInventoryType mit = MapleInventoryType.getByType(it.getInventoryType());
            if (mit == null) {
                return;
            }

            if (MapleInventoryType.EQUIP.equals(mit) || MapleInventoryType.EQUIPPED.equals(mit)) {
                Equip equip = new Equip(it);
                if (!login) {
                    equip.setQuantity(1);

                    if (equip.getUniqueId() > -1) {
                        if (GameConstants.isEffectRing(it.getItemId())) {
                            MapleRing ring = MapleRing.loadFromDb(
                                    equip.getUniqueId(), MapleInventoryType.EQUIPPED.equals(mit));
                            if (ring != null) {
                                equip.setRing(ring);
                            }
                        }
                    }
                }
                // copy ? 人才
                items.put(it.getId(), new Pair<>(equip.copy(), mit));
            } else {
                Item item = new Item(it);
                if (GameConstants.isPet(item.getItemId())) {
                    if (item.getUniqueId() > -1) {
                        MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(), item.getPosition());
                        if (pet != null) {
                            item.setPet(pet);
                        }
                    } else {
                        //O_O hackish fix
                        int new_unique = MapleInventoryIdentifier.getInstance();
                        item.setUniqueId(new_unique);
                        item.setPet(MaplePet.createPet(item.getItemId(), new_unique));
                    }
                }
                items.put(it.getId(), new Pair<>(item.copy(), mit));
            }
        });
        return items;
    }

    private static Map<Integer, Pair<IItem, MapleInventoryType>> loadInventoryItemByAccount(boolean login, int id) {
        Map<Integer, Pair<IItem, MapleInventoryType>> items = new LinkedHashMap<>();
        QDInventoryItem select = new QDInventoryItem().type.eq(1).account.id.eq(id);
        if (login) {
            select.inventoryType.eq(MapleInventoryType.EQUIPPED.getType());
        }

        select.findEach(it -> {
            MapleInventoryType mit = MapleInventoryType.getByType(it.getInventoryType());
            if (mit == null) {
                return;
            }

            if (MapleInventoryType.EQUIP.equals(mit) || MapleInventoryType.EQUIPPED.equals(mit)) {
                Equip equip = new Equip(it);
                if (!login) {
                    equip.setQuantity(1);

                    if (equip.getUniqueId() > -1) {
                        if (GameConstants.isEffectRing(it.getItemId())) {
                            MapleRing ring = MapleRing.loadFromDb(
                                    equip.getUniqueId(), MapleInventoryType.EQUIPPED.equals(mit));
                            if (ring != null) {
                                equip.setRing(ring);
                            }
                        }
                    }
                }
                // copy ? 人才
                items.put(it.getId(), new Pair<>(equip.copy(), mit));
            } else {
                Item item = new Item(it);
                if (GameConstants.isPet(item.getItemId())) {
                    if (item.getUniqueId() > -1) {
                        MaplePet pet = MaplePet.loadFromDb(item.getItemId(), item.getUniqueId(), item.getPosition());
                        if (pet != null) {
                            item.setPet(pet);
                        }
                    } else {
                        //O_O hackish fix
                        int new_unique = MapleInventoryIdentifier.getInstance();
                        item.setUniqueId(new_unique);
                        item.setPet(MaplePet.createPet(item.getItemId(), new_unique));
                    }
                }
                items.put(it.getId(), new Pair<>(item.copy(), mit));
            }
        });
        return items;
    }

    public static void deleteItems(int value, int... id) {
        switch (value) {
            case 0:
                // 0 INVENTORY("inventoryitems", "inventoryequipment", 0, "character_id")
                new QDInventoryItem().type.eq(value).character.id.eq(id[0]).delete();
                break;
            case 1:
                // 1 STORAGE("inventoryitems", "inventoryequipment", 1, "accountid")
                new QDInventoryItem().type.eq(value).account.id.eq(id[0]).delete();
                break;
            case 2:
            case 3:
            case 4:
            case 7:
            case 10:
            case 11:
                // 2 CASHSHOP_EXPLORER("csitems", "csequipment", 2, "accountid")
                // 3 CASHSHOP_CYGNUS("csitems", "csequipment", 3, "accountid")
                // 4 CASHSHOP_ARAN("csitems", "csequipment", 4, "accountid")
                // 7 CASHSHOP_EVAN("csitems", "csequipment", 7, "accountid")
                // 10 CASHSHOP_DB("csitems", "csequipment", 10, "accountid")
                // 11 CASHSHOP_RESIST("csitems", "csequipment", 11, "accountid")
                new QDCsItem().type.eq(value).account.id.eq(id[0]).delete();
                break;
            case 5:
                // 5 HIRED_MERCHANT("hiredmerchitems", "hiredmerchequipment", 5, "packageid", "accountid", "character_id")
                new QDHiredMerchItem().type.eq(value).packageId.eq(id[0])
                        .or().account.id.eq(id[1])
                        .or().character.id.eq(id[2]).delete();
                break;
            case 6:
                // 6 DUEY("dueyitems", "dueyequipment", 6, "packageid")
                new QDDueyItem().type.eq(value).pack.id.eq(id[0]).delete();
                break;
            case 8:
                // 8 MTS("mtsitems", "mtsequipment", 8, "packageid")
                new QDMtsItem().type.eq(value).packageId.eq(id[0]).delete();
                break;
            case 9:
                // 9 MTS_TRANSFER("mtstransfer", "mtstransferequipment", 9, "character_id")
                new QDMtsTransfer().type.eq(value).character.id.eq(id[0]).delete();
                break;
        }
    }

    public static void saveItems(List<Pair<IItem, MapleInventoryType>> items, DCharacter character) {
        if (items == null || items.isEmpty()) {
            return;
        }

        for (Pair<IItem, MapleInventoryType> pair : items) {
            IItem left = pair.left;
            MapleInventoryType right = pair.right;
            if (left instanceof Equip) {
                DInventoryItem item = ((Equip) left).item;
                if (character != null) {
                    item.setCharacter(character);
                }
                item.setInventoryType(right.getType());
                item.save();
                // todo 级联操作
                if (item.getEquipment() != null) {
                    item.getEquipment().save();
                }
            } else if (left instanceof Item) {
                DInventoryItem item = ((Item) left).item;
                if (character != null) {
                    item.setCharacter(character);
                }
                item.setInventoryType(right.getType());
                item.save();
                // todo 级联操作
                if (item.getEquipment() != null) {
                    item.getEquipment().save();
                }
            } else if (left instanceof MapleHiredMerchEquip) {
                DHiredMerchItem item = ((MapleHiredMerchItem) left).item;
                if (character != null) {
                    item.setCharacter(character);
                }
                item.setInventoryType(right.getType());
                item.save();
                // todo 级联操作
                if (item.getEquipment() != null) {
                    item.getEquipment().save();
                }
            } else if (left instanceof MapleHiredMerchItem) {
                DHiredMerchItem item = ((MapleHiredMerchItem) left).item;
                if (character != null) {
                    item.setCharacter(character);
                }
                item.setInventoryType(right.getType());
                item.save();
                // todo 级联操作
                if (item.getEquipment() != null) {
                    item.getEquipment().save();
                }
            } else if (left instanceof MapleCsEquip) {
                DCsItem item = ((MapleCsEquip) left).item;
                if (character != null) {
                    item.setCharacter(character);
                }
                item.setInventoryType(right.getType());
                item.save();
                // todo 级联操作
                if (item.getEquipment() != null) {
                    item.getEquipment().save();
                }
            } else if (left instanceof MapleCsItem) {
                DCsItem item = ((MapleCsItem) left).item;
                if (character != null) {
                    item.setCharacter(character);
                }
                item.setInventoryType(right.getType());
                item.save();
                // todo 级联操作
                if (item.getEquipment() != null) {
                    item.getEquipment().save();
                }
            } else if (left instanceof MapleDueyEquip) {
                DDueyItem item = ((MapleDueyEquip) left).item;
                if (character != null) {
                    item.setCharacter(character);
                }
                item.setInventoryType(right.getType());
                item.save();
                // todo 级联操作
                if (item.getEquipment() != null) {
                    item.getEquipment().save();
                }
            } else if (left instanceof MapleDueyItem) {
                DDueyItem item = ((MapleDueyItem) left).item;
                if (character != null) {
                    item.setCharacter(character);
                }
                item.setInventoryType(right.getType());

                item.save();
                // todo 级联操作
                if (item.getEquipment() != null) {
                    item.getEquipment().save();
                }
            } else if (left instanceof MapleMtsEquip) {
                DMtsItem item = ((MapleMtsEquip) left).item;
                if (character != null) {
                    item.setCharacter(character);
                }
                item.setInventoryType(right.getType());
                item.save();
                // todo 级联操作
                if (item.getEquipment() != null) {
                    item.getEquipment().save();
                }
            } else if (left instanceof MapleMtsItem) {
                DMtsItem item = ((MapleMtsItem) left).item;
                if (character != null) {
                    item.setCharacter(character);
                }
                item.setInventoryType(right.getType());
                item.save();
                // todo 级联操作
                if (item.getEquipment() != null) {
                    item.getEquipment().save();
                }
            } else if (left instanceof MapleMtsTransferEquip) {
                DMtsTransfer item = ((MapleMtsTransferEquip) left).item;
                if (character != null) {
                    item.setCharacter(character);
                }
                item.setInventoryType(right.getType());
                item.save();
                // todo 级联操作
                if (item.getEquipment() != null) {
                    item.getEquipment().save();
                }
            } else if (left instanceof MapleMtsTransferItem) {
                DMtsTransfer item = ((MapleMtsTransferItem) left).item;
                if (character != null) {
                    item.setCharacter(character);
                }
                item.setInventoryType(right.getType());
                item.save();
                // todo 级联操作
                if (item.getEquipment() != null) {
                    item.getEquipment().save();
                }
            }
        }
    }
}
