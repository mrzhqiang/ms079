package server;

import com.github.mrzhqiang.maplestory.domain.DCashShopModifiedItem;

public class CashItemInfo {

    private final int itemId;
    private final int count;
    private final int price;
    private final int sn;
    private final int expire;
    private final int gender;
    private final boolean onSale;
    private String name;

    public CashItemInfo(int itemId, int count, int price, int sn, int expire, int gender, boolean sale) {
        this.itemId = itemId;
        this.count = count;
        this.price = price;
        this.sn = sn;
        this.expire = expire;
        this.gender = gender;
        this.onSale = sale;
    }

    public CashItemInfo(int itemId, int count, int price, int sn, int expire, int gender, boolean sale, String name) {
        this.itemId = itemId;
        this.count = count;
        this.price = price;
        this.sn = sn;
        this.expire = expire;
        this.gender = gender;
        this.onSale = sale;
        this.name = name;
    }

    public int getId() {
        return itemId;
    }

    public int getOnSale() {
        if (onSale) {
            return 1;
        }
        return 0;

    }

    public int getExpire() {
        return expire;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }

    public int getSN() {
        return sn;
    }

    public int getPeriod() {
        return expire;
    }

    public int getGender() {
        return gender;
    }

    public boolean onSale() {
        return onSale || (CashItemFactory.getInstance().getModInfo(sn) != null && CashItemFactory.getInstance().getModInfo(sn).item.isShowUp());
    }

    public boolean genderEquals(int g) {
        return g == this.gender || this.gender == 2;
    }

    public static class CashModInfo {

        public final DCashShopModifiedItem item;
        public int sn, flags;
        private CashItemInfo cii;

        public CashModInfo(int sn, DCashShopModifiedItem item) {
            this.sn = sn;
            // mark: 0 = new, 1 = sale, 2 = hot, 3 = event
            // unk_1: 0 = doesn't have, 1 = has, but false, 2 = has and true
            this.item = item;

            if (item.getId() > 0) {
                flags |= 0x1;
            }
            if (item.getCount() > 0) {
                flags |= 0x2;
            }
            if (item.getDiscountPrice() > 0) {
                flags |= 0x4;
            }
            if (item.getUnk1() > 0) {
                flags |= 0x8;
            }
            if (item.getPriority() >= 0) {
                flags |= 0x10;
            }
            if (item.getPeriod() > 0) {
                flags |= 0x20;
            }
            //0x40 = ?
            if (item.getMeso() > 0) {
                flags |= 0x80;
            }
            if (item.getUnk2() > 0) {
                flags |= 0x100;
            }
            if (item.getGender() >= 0) {
                flags |= 0x200;
            }
            if (item.isShowUp()) {
                flags |= 0x400;
            }
            if (item.getMark() >= -1 || item.getMark() <= 3) {
                flags |= 0x800;
            }
            if (item.getUnk3() > 0) {
                flags |= 0x1000;
            }
            //0x2000, 0x4000, 0x8000 - ?
            if (item.isPackageField()) {
                flags |= 0x10000;
            }
        }

        public CashItemInfo toCItem(CashItemInfo backup) {
            if (cii != null) {
                return cii;
            }
            final int item, c, price, expire, gen;
            final boolean onSale;
            if (this.item.getItemId() <= 0) {
                item = (backup == null ? 0 : backup.getId());
            } else {
                item = this.item.getItemId();
            }
            if (this.item.getCount() <= 0) {
                c = (backup == null ? 0 : backup.getCount());
            } else {
                c = this.item.getCount();
            }
            if (this.item.getMeso() <= 0) {
                if (this.item.getDiscountPrice() <= 0) {
                    price = (backup == null ? 0 : backup.getPrice());
                } else {
                    price = this.item.getDiscountPrice();
                }
            } else {
                price = this.item.getMeso();
            }
            if (this.item.getPeriod() <= 0) {
                expire = (backup == null ? 0 : backup.getPeriod());
            } else {
                expire = this.item.getPeriod();
            }
            if (this.item.getGender() < 0) {
                gen = (backup == null ? 0 : backup.getGender());
            } else {
                gen = this.item.getGender();
            }
            if (!this.item.isShowUp()) {
                onSale = (backup != null && backup.onSale());
            } else {
                onSale = this.item.isShowUp();
            }

            cii = new CashItemInfo(item, c, price, sn, expire, gen, onSale);
            return cii;
        }

        public void setItemId(int itemId) {
            this.item.setItemId(itemId);

        }
    }
}
