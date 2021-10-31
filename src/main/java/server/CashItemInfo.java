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
        return onSale || (CashItemFactory.getInstance().getModInfo(sn) != null && CashItemFactory.getInstance().getModInfo(sn).item.showup);
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

            if (item.id > 0) {
                flags |= 0x1;
            }
            if (item.count > 0) {
                flags |= 0x2;
            }
            if (item.discountPrice > 0) {
                flags |= 0x4;
            }
            if (item.unk1 > 0) {
                flags |= 0x8;
            }
            if (item.priority >= 0) {
                flags |= 0x10;
            }
            if (item.period > 0) {
                flags |= 0x20;
            }
            //0x40 = ?
            if (item.meso > 0) {
                flags |= 0x80;
            }
            if (item.unk2 > 0) {
                flags |= 0x100;
            }
            if (item.gender >= 0) {
                flags |= 0x200;
            }
            if (item.showup) {
                flags |= 0x400;
            }
            if (item.mark >= -1 || item.mark <= 3) {
                flags |= 0x800;
            }
            if (item.unk3 > 0) {
                flags |= 0x1000;
            }
            //0x2000, 0x4000, 0x8000 - ?
            if (item.packageField) {
                flags |= 0x10000;
            }
        }

        public CashItemInfo toCItem(CashItemInfo backup) {
            if (cii != null) {
                return cii;
            }
            final int item, c, price, expire, gen;
            final boolean onSale;
            if (this.item.id <= 0) {
                item = (backup == null ? 0 : backup.getId());
            } else {
                item = this.item.id;
            }
            if (this.item.count <= 0) {
                c = (backup == null ? 0 : backup.getCount());
            } else {
                c = this.item.count;
            }
            if (this.item.meso <= 0) {
                if (this.item.discountPrice <= 0) {
                    price = (backup == null ? 0 : backup.getPrice());
                } else {
                    price = this.item.discountPrice;
                }
            } else {
                price = this.item.meso;
            }
            if (this.item.period <= 0) {
                expire = (backup == null ? 0 : backup.getPeriod());
            } else {
                expire = this.item.period;
            }
            if (this.item.gender < 0) {
                gen = (backup == null ? 0 : backup.getGender());
            } else {
                gen = this.item.gender;
            }
            if (!this.item.showup) {
                onSale = (backup != null && backup.onSale());
            } else {
                onSale = this.item.showup;
            }

            cii = new CashItemInfo(item, c, price, sn, expire, gen, onSale);
            return cii;
        }
    }
}
