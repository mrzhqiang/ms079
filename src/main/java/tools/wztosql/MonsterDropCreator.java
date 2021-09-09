package tools.wztosql;

import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.google.common.base.Strings;
import tools.Pair;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonsterDropCreator {

    private static final int COMMON_ETC_RATE = 600000;
    private static final int SUPER_BOSS_ITEM_RATE = 300000;
    private static final int POTION_RATE = 20000;
    private static final int ARROWS_RATE = 25000;
    private static int lastmonstercardid = 2388070;
    private static boolean addFlagData = false;
    protected static String monsterQueryData = "drop_data";
    protected static List<Pair<Integer, String>> itemNameCache = new ArrayList<>();
    protected static List<Pair<Integer, MobInfo>> mobCache = new ArrayList<>();
    protected static Map<Integer, Boolean> bossCache = new HashMap<>();
//    protected static final MapleDataProvider data = MapleDataProviderFactory.getDataProvider(WzManage.STRING_DIR);
//    protected static final MapleDataProvider mobData = MapleDataProviderFactory.getDataProvider(WzManage.MOB_DIR);

    public static void main(String[] args) throws FileNotFoundException, IOException, NotBoundException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException {
        //  MapleData data = MapleDataProviderFactory.getDataProvider(new File(new StringBuilder().append(System.getProperty("wzpath")).append("String.wz").toString())).getData("MonsterBook.img");
        System.out.println("準備提取數據!");
        System.out.println("按任意鍵繼續...");
        System.console().readLine();

        long currtime = System.currentTimeMillis();
        // addFlagData = Boolean.parseBoolean(args[0]);
        addFlagData = false;
        System.out.println("載入: 物品名稱.");
        getAllItems();
        System.out.println("載入: 怪物數據.");
        getAllMobs();

        StringBuilder sb = new StringBuilder();
        FileOutputStream out = new FileOutputStream("mobDrop.sql", true);

        for (Map.Entry e : getDropsNotInMonsterBook().entrySet()) {
            boolean first = true;

            sb.append("INSERT INTO ").append(monsterQueryData).append(" VALUES ");
            for (Integer monsterdrop : (List<Integer>) e.getValue()) {
                int itemid = monsterdrop.intValue();
                int monsterId = ((Integer) e.getKey()).intValue();
                int rate = getChance(itemid, monsterId, bossCache.containsKey(Integer.valueOf(monsterId)));

                if (rate <= 100000) {
                    switch (monsterId) {
                        case 9400121:
                            rate *= 5;
                            break;
                        case 9400112:
                        case 9400113:
                        case 9400300:
                            rate *= 10;
                    }
                }

                for (int i = 0; i < multipleDropsIncrement(itemid, monsterId); i++) {
                    if (first) {
                        sb.append("(DEFAULT, ");
                        first = false;
                    } else {
                        sb.append(", (DEFAULT, ");
                    }
                    sb.append(monsterId).append(", ");
                    if (addFlagData) {
                        sb.append("'', ");
                    }
                    sb.append(itemid).append(", ");
                    sb.append("1, 1,");
                    sb.append("0, ");
                    int num = IncrementRate(itemid, i);
                    sb.append(num == -1 ? rate : num);
                    sb.append(")");
                    first = false;
                }
                sb.append("\n");
                sb.append("-- Name : ");
                retriveNLogItemName(sb, itemid);
                sb.append("\n");
            }
            sb.append(";");
            sb.append("\n");

            out.write(sb.toString().getBytes());
            sb.delete(0, 2147483647);
        }

        System.out.println("載入: Drops from String.wz/MonsterBook.img.");
        WzData.STRING.directory().findFile("MonsterBook.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(dataz -> {
                    int monsterId = Numbers.ofInt(dataz.name());
                    int idtoLog = monsterId;

                    if (monsterId == 9400408) {
                        idtoLog = 9400409;
                    }
                    final boolean[] first = {true};
                    int finalIdtoLog = idtoLog;
                    dataz.findByName("reward").map(WzElement::childrenStream)
                            .ifPresent(elementStream -> {
                                sb.append("INSERT INTO ").append(monsterQueryData).append(" VALUES ");
                                elementStream.forEach(drop -> {
                                    int itemid = Elements.ofInt(drop);
                                    int rate = getChance(itemid, finalIdtoLog, bossCache.containsKey(finalIdtoLog));
                                    for (Pair<Integer, MobInfo> Pair : mobCache) {
                                        if (Pair.getLeft() == monsterId) {
                                            if ((Pair.getRight().getBoss() <= 0)
                                                    || (rate > 100000)) {
                                                break;
                                            }
                                            if (Pair.getRight().rateItemDropLevel() == 2) {
                                                rate *= 10;
                                                break;
                                            }
                                            if (Pair.getRight().rateItemDropLevel() == 3) {
                                                switch (monsterId) {
                                                    case 8810018:
                                                        rate *= 48;
                                                    case 8800002:
                                                        rate *= 45;
                                                        break;
                                                    default:
                                                        rate *= 30;
                                                        break;
                                                }
                                            }
                                            switch (monsterId) {
                                                case 8860010:
                                                case 9400265:
                                                case 9400270:
                                                case 9400273:
                                                    rate *= 10;
                                                    break;
                                                case 9400294:
                                                    rate *= 24;
                                                    break;
                                                case 9420522:
                                                    rate *= 29;
                                                    break;
                                                case 9400409:
                                                    rate *= 35;
                                                    break;
                                                case 9400287:
                                                    rate *= 60;
                                                    break;
                                                default:
                                                    rate *= 10;
                                                    break;
                                            }
                                        }
                                    }

                                    for (int i = 0; i < multipleDropsIncrement(itemid, finalIdtoLog); i++) {
                                        if (first[0]) {
                                            sb.append("(DEFAULT, ");
                                            first[0] = false;
                                        } else {
                                            sb.append(", (DEFAULT, ");
                                        }
                                        sb.append(finalIdtoLog).append(", ");
                                        if (addFlagData) {
                                            sb.append("'', ");
                                        }
                                        sb.append(itemid).append(", ");
                                        sb.append("1, 1,");
                                        sb.append("0, ");
                                        int num = IncrementRate(itemid, i);
                                        sb.append(num == -1 ? rate : num);
                                        sb.append(")");
                                        first[0] = false;
                                    }
                                    sb.append("\n");
                                    sb.append("-- Name : ");
                                    retriveNLogItemName(sb, itemid);
                                    sb.append("\n");
                                });
                                sb.append(";");
                            });
                    sb.append("\n");
                    try {
                        out.write(sb.toString().getBytes());
                    } catch (IOException ignore) {
                    }
                    sb.delete(0, 2147483647);
                }));
        System.out.println("載入: 怪物書數據.");
        StringBuilder SQL = new StringBuilder();
        StringBuilder bookName = new StringBuilder();
        for (Pair<Integer, String> Pair : itemNameCache) {
            if ((Pair.getLeft() >= 2380000) && (Pair.getLeft() <= lastmonstercardid)) {
                bookName.append(Pair.getRight());

                if (bookName.toString().contains(" Card")) {
                    bookName.delete(bookName.length() - 5, bookName.length());
                }
                for (Pair<Integer, MobInfo> Pair_ : mobCache) {
                    if (Pair_.getRight().getName().equalsIgnoreCase(bookName.toString())) {
                        int rate = 1000;
                        if (Pair_.getRight().getBoss() > 0) {
                            rate *= 25;
                        }
                        SQL.append("INSERT INTO ").append(monsterQueryData).append(" VALUES ");
                        SQL.append("(DEFAULT, ");
                        SQL.append(Pair_.getLeft()).append(", ");
                        if (addFlagData) {
                            sb.append("'', ");
                        }
                        SQL.append(Pair.getLeft()).append(", ");
                        SQL.append("1, 1,");
                        SQL.append("0, ");
                        SQL.append(rate);
                        SQL.append(");\n");
                        SQL.append("-- 物品名 : ").append(Pair.getRight()).append("\n");
                        break;
                    }
                }
                bookName.delete(0, 2147483647);
            }
        }
        System.out.println("載入: 怪物卡數據.");
        SQL.append("\n");
        int i = 1;
        int lastmonsterbookid = 0;
        for (Pair<Integer, String> Pair : itemNameCache) {
            if ((Pair.getLeft() >= 2380000) && (Pair.getLeft() <= lastmonstercardid)) {
                bookName.append(Pair.getRight());

                if (bookName.toString().contains(" Card")) {
                    bookName.delete(bookName.length() - 5, bookName.length());
                }
                if (Pair.getLeft() != lastmonsterbookid) {
                    for (Pair<Integer, MobInfo> Pair_ : mobCache) {
                        if (Pair_.getRight().getName().equalsIgnoreCase(bookName.toString())) {
                            SQL.append("INSERT INTO ").append("monstercarddata").append(" VALUES (");
                            SQL.append(i).append(", ");
                            SQL.append(Pair.getLeft());
                            SQL.append(", ");
                            SQL.append(Pair_.getLeft()).append(");\n");
                            lastmonsterbookid = Pair.getLeft();
                            i++;
                            break;
                        }
                    }
                    bookName.delete(0, 2147483647);
                }
            }
        }
        out.write(SQL.toString().getBytes());
        out.close();
        long time = System.currentTimeMillis() - currtime;
        time /= 1000L;

        System.out.println("Time taken : " + time);
    }

    private static void retriveNLogItemName(StringBuilder sb, int id) {
        for (Pair<Integer, String> Pair : itemNameCache) {
            if (Pair.getLeft() == id) {
                sb.append(Pair.getRight());
                return;
            }
        }
        sb.append("MISSING STRING, ID : ");
        sb.append(id);
    }

    private static int IncrementRate(int itemid, int times) {
        if (times == 0) {
            if ((itemid == 1002357) || (itemid == 1002926) || (itemid == 1002927)) {
                return 999999;
            }
            if (itemid == 1122000) {
                return 999999;
            }
            if (itemid == 1002972) {
                return 999999;
            }
        } else if (times == 1) {
            if ((itemid == 1002357) || (itemid == 1002926) || (itemid == 1002927)) {
                return 999999;
            }
            if (itemid == 1122000) {
                return 999999;
            }
            if (itemid == 1002972) {
                return 300000;
            }
        } else if (times == 2) {
            if ((itemid == 1002357) || (itemid == 1002926) || (itemid == 1002927)) {
                return 300000;
            }
            if (itemid == 1122000) {
                return 300000;
            }
        } else if (times == 3) {
            if ((itemid == 1002357) || (itemid == 1002926) || (itemid == 1002927)) {
                return 300000;
            }
        } else if ((times == 4) && ((itemid == 1002357) || (itemid == 1002926) || (itemid == 1002927))) {
            return 300000;
        }

        return -1;
    }

    private static int multipleDropsIncrement(int itemid, int mobid) {
        switch (itemid) {
            case 1002357:
            case 1002390:
            case 1002430:
            case 1002926:
            case 1002927:
                return 5;
            case 1122000:
                return 4;
            case 4021010:
                return 7;
            case 1002972:
                return 2;
            case 4000172:
                if (mobid == 7220001) {
                    return 8;
                }
                return 1;
            case 4000000:
            case 4000003:
            case 4000005:
            case 4000016:
            case 4000018:
            case 4000019:
            case 4000021:
            case 4000026:
            case 4000029:
            case 4000031:
            case 4000032:
            case 4000033:
            case 4000043:
            case 4000044:
            case 4000073:
            case 4000074:
            case 4000113:
            case 4000114:
            case 4000115:
            case 4000117:
            case 4000118:
            case 4000119:
            case 4000166:
            case 4000167:
            case 4000195:
            case 4000268:
            case 4000269:
            case 4000270:
            case 4000283:
            case 4000284:
            case 4000285:
            case 4000289:
            case 4000298:
            case 4000329:
            case 4000330:
            case 4000331:
            case 4000356:
            case 4000364:
            case 4000365:
                if ((mobid == 2220000) || (mobid == 3220000) || (mobid == 3220001) || (mobid == 4220000) || (mobid == 5220000) || (mobid == 5220002) || (mobid == 5220003) || (mobid == 6220000) || (mobid == 4000119) || (mobid == 7220000) || (mobid == 7220002) || (mobid == 8220000) || (mobid == 8220002) || (mobid == 8220003)) {
                    return 3;
                }
                return 1;
        }
        return 1;
    }

    private static int getChance(int id, int mobid, boolean boss) {
        switch (id / 10000) {
            case 100:
                switch (id) {
                    case 1002357:
                    case 1002390:
                    case 1002430:
                    case 1002905:
                    case 1002906:
                    case 1002926:
                    case 1002927:
                    case 1002972:
                        return 300000;
                }
                return 1500;
            case 103:
                switch (id) {
                    case 1032062:
                        return 100;
                }
                return 1000;
            case 105:
            case 109:
                switch (id) {
                    case 1092049:
                        return 100;
                }
                return 700;
            case 104:
            case 106:
            case 107:
                switch (id) {
                    case 1072369:
                        return 300000;
                }
                return 800;
            case 108:
            case 110:
                return 1000;
            case 112:
                switch (id) {
                    case 1122000:
                        return 300000;
                    case 1122011:
                    case 1122012:
                        return 800000;
                }
            case 130:
            case 131:
            case 132:
            case 137:
                switch (id) {
                    case 1372049:
                        return 999999;
                }
                return 700;
            case 138:
            case 140:
            case 141:
            case 142:
            case 144:
                return 700;
            case 133:
            case 143:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
                return 500;
            case 204:
                switch (id) {
                    case 2049000:
                        return 150;
                }
                return 300;
            case 205:
                return 50000;
            case 206:
                return 30000;
            case 228:
                return 30000;
            case 229:
                switch (id) {
                    case 2290096:
                        return 800000;
                    case 2290125:
                        return 100000;
                }
                return 500;
            case 233:
                switch (id) {
                    case 2330007:
                        return 50;
                }
                return 500;
            case 400:
                switch (id) {
                    case 4000021:
                        return 50000;
                    case 4001094:
                        return 999999;
                    case 4001000:
                        return 5000;
                    case 4000157:
                        return 100000;
                    case 4001023:
                    case 4001024:
                        return 999999;
                    case 4000244:
                    case 4000245:
                        return 2000;
                    case 4001005:
                        return 5000;
                    case 4001006:
                        return 10000;
                    case 4000017:
                    case 4000082:
                        return 40000;
                    case 4000446:
                    case 4000451:
                    case 4000456:
                        return 10000;
                    case 4000459:
                        return 20000;
                    case 4000030:
                        return 60000;
                    case 4000339:
                        return 70000;
                    case 4000313://黄金枫叶
                        // case 2022034://粽子
                    case 4007000:
                    case 4007001:
                    case 4007002:
                    case 4007003:
                    case 4007004:
                    case 4007005:
                    case 4007006:
                    case 4007007:
                    case 4031456:
                        return 100000;
                    case 4001126:
                        return 500000;
                }
                switch (id / 1000) {
                    case 4000:
                    case 4001:
                        return 600000;
                    case 4003:
                        return 200000;
                    case 4004:
                    case 4006:
                        return 10000;
                    case 4005:
                        return 1000;
                    case 4002:
                }
            case 401:
            case 402:
                switch (id) {
                    case 4020009:
                        return 5000;
                    case 4021010:
                        return 300000;
                }
                return 9000;
            case 403:
                switch (id) {
                    case 4032024:
                        return 50000;
                    case 4032181:
                        return boss ? 999999 : 300000;
                    case 4032025:
                    case 4032155:
                    case 4032156:
                    case 4032159:
                    case 4032161:
                    case 4032163:
                        return 600000;
                    case 4032166:
                    case 4032167:
                    case 4032168:
                        return 10000;
                    case 4032151:
                    case 4032158:
                    case 4032164:
                    case 4032180:
                        return 2000;
                    case 4032152:
                    case 4032153:
                    case 4032154:
                        return 4000;
                }
                return 300;
            case 413:
                return 6000;
            case 416:
                return 6000;
        }
        switch (id / 1000000) {
            case 1:
                return 999999;
            case 2:
                switch (id) {
                    case 2000004:
                    case 2000005:
                        return boss ? 999999 : 20000;
                    case 2000006:
                        return mobid == 9420540 ? 50000 : boss ? 999999 : 20000;
                    case 2022345:
                        return boss ? 999999 : 3000;
                    case 2012002:
                        return 6000;
                    case 2020013:
                    case 2020015:
                        return boss ? 999999 : 20000;
                    case 2060000:
                    case 2060001:
                    case 2061000:
                    case 2061001:
                        return 25000;
                    case 2070000:
                    case 2070001:
                    case 2070002:
                    case 2070003:
                    case 2070004:
                    case 2070008:
                    case 2070009:
                    case 2070010:
                        return 500;
                    case 2070005:
                        return 400;
                    case 2070006:
                    case 2070007:
                        return 200;
                    case 2070012:
                    case 2070013:
                        return 1500;
                    case 2070019:
                        return 100;
                    case 2210006:
                        return 999999;
                }
                return 20000;
            case 3:
                switch (id) {
                    case 3010007:
                    case 3010008:
                        return 500;
                }
                return 2000;
        }
        System.out.println("未處理的數據, ID : " + id);
        return 999999;
    }

    private static Map<Integer, List<Integer>> getDropsNotInMonsterBook() {
        Map<Integer, List<Integer>> drops = new HashMap<>();

        List<Integer> IndiviualMonsterDrop = new ArrayList<>();

        IndiviualMonsterDrop.add(4000139);
        IndiviualMonsterDrop.add(2002011);
        IndiviualMonsterDrop.add(2002011);
        IndiviualMonsterDrop.add(2002011);
        IndiviualMonsterDrop.add(2000004);
        IndiviualMonsterDrop.add(2000004);

        drops.put(9400112, IndiviualMonsterDrop);

        IndiviualMonsterDrop = new ArrayList<>();

        IndiviualMonsterDrop.add(4000140);
        IndiviualMonsterDrop.add(2022027);
        IndiviualMonsterDrop.add(2022027);
        IndiviualMonsterDrop.add(2000004);
        IndiviualMonsterDrop.add(2000004);
        IndiviualMonsterDrop.add(2002008);
        IndiviualMonsterDrop.add(2002008);

        drops.put(9400113, IndiviualMonsterDrop);

        IndiviualMonsterDrop = new ArrayList<>();

        IndiviualMonsterDrop.add(4000141);
        IndiviualMonsterDrop.add(2000004);
        IndiviualMonsterDrop.add(2040813);
        IndiviualMonsterDrop.add(2041030);
        IndiviualMonsterDrop.add(2041040);
        IndiviualMonsterDrop.add(1072238);
        IndiviualMonsterDrop.add(1032026);
        IndiviualMonsterDrop.add(1372011);

        drops.put(9400300, IndiviualMonsterDrop);

        IndiviualMonsterDrop = new ArrayList<>();

        IndiviualMonsterDrop.add(4000225);
        IndiviualMonsterDrop.add(2000006);
        IndiviualMonsterDrop.add(2000004);
        IndiviualMonsterDrop.add(2070013);
        IndiviualMonsterDrop.add(2002005);
        IndiviualMonsterDrop.add(2022018);
        IndiviualMonsterDrop.add(2040306);
        IndiviualMonsterDrop.add(2043704);
        IndiviualMonsterDrop.add(2044605);
        IndiviualMonsterDrop.add(2041034);
        IndiviualMonsterDrop.add(1032019);
        IndiviualMonsterDrop.add(1102013);
        IndiviualMonsterDrop.add(1322026);
        IndiviualMonsterDrop.add(1092015);
        IndiviualMonsterDrop.add(1382016);
        IndiviualMonsterDrop.add(1002276);
        IndiviualMonsterDrop.add(1002403);
        IndiviualMonsterDrop.add(1472027);

        drops.put(9400013, IndiviualMonsterDrop);

        IndiviualMonsterDrop = new ArrayList<>();

        IndiviualMonsterDrop.add(1372049);

        drops.put(8800002, IndiviualMonsterDrop);

        IndiviualMonsterDrop = new ArrayList<>();

        IndiviualMonsterDrop.add(4001094);
        IndiviualMonsterDrop.add(2290125);

        drops.put(8810018, IndiviualMonsterDrop);

        IndiviualMonsterDrop = new ArrayList<>();

        IndiviualMonsterDrop.add(4000138);
        IndiviualMonsterDrop.add(4010006);
        IndiviualMonsterDrop.add(2000006);
        IndiviualMonsterDrop.add(2000011);
        IndiviualMonsterDrop.add(2020016);
        IndiviualMonsterDrop.add(2022024);
        IndiviualMonsterDrop.add(2022026);
        IndiviualMonsterDrop.add(2043705);
        IndiviualMonsterDrop.add(2040716);
        IndiviualMonsterDrop.add(2040908);
        IndiviualMonsterDrop.add(2040510);
        IndiviualMonsterDrop.add(1072239);
        IndiviualMonsterDrop.add(1422013);
        IndiviualMonsterDrop.add(1402016);
        IndiviualMonsterDrop.add(1442020);
        IndiviualMonsterDrop.add(1432011);
        IndiviualMonsterDrop.add(1332022);
        IndiviualMonsterDrop.add(1312015);
        IndiviualMonsterDrop.add(1382010);
        IndiviualMonsterDrop.add(1372009);
        IndiviualMonsterDrop.add(1082085);
        IndiviualMonsterDrop.add(1332022);
        IndiviualMonsterDrop.add(1472033);

        drops.put(9400121, IndiviualMonsterDrop);

        IndiviualMonsterDrop = new ArrayList<>();

        IndiviualMonsterDrop.add(4032024);
        IndiviualMonsterDrop.add(4032025);
        IndiviualMonsterDrop.add(4020006);
        IndiviualMonsterDrop.add(4020008);
        IndiviualMonsterDrop.add(4010001);
        IndiviualMonsterDrop.add(4004001);
        IndiviualMonsterDrop.add(2070006);
        IndiviualMonsterDrop.add(2044404);
        IndiviualMonsterDrop.add(2044702);
        IndiviualMonsterDrop.add(2044305);
        IndiviualMonsterDrop.add(1102029);
        IndiviualMonsterDrop.add(1032023);
        IndiviualMonsterDrop.add(1402004);
        IndiviualMonsterDrop.add(1072210);
        IndiviualMonsterDrop.add(1040104);
        IndiviualMonsterDrop.add(1060092);
        IndiviualMonsterDrop.add(1082129);
        IndiviualMonsterDrop.add(1442008);
        IndiviualMonsterDrop.add(1072178);
        IndiviualMonsterDrop.add(1050092);
        IndiviualMonsterDrop.add(1002271);
        IndiviualMonsterDrop.add(1051053);
        IndiviualMonsterDrop.add(1382008);
        IndiviualMonsterDrop.add(1002275);
        IndiviualMonsterDrop.add(1051082);
        IndiviualMonsterDrop.add(1050064);
        IndiviualMonsterDrop.add(1472028);
        IndiviualMonsterDrop.add(1072193);
        IndiviualMonsterDrop.add(1072172);
        IndiviualMonsterDrop.add(1002285);

        drops.put(9400545, IndiviualMonsterDrop);

        IndiviualMonsterDrop = new ArrayList<>();

        return drops;
    }

    private static void getAllItems() {
        // MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(new StringBuilder().append(System.getProperty("wzpath")).append("/String.wz").toString()));

        List<Pair<Integer, String>> itemPairs = new ArrayList<>();

        WzData.STRING.directory().findFile("Cash.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(itemFolder -> {
                    int itemId = Numbers.ofInt(itemFolder.name());
                    String itemName = Elements.findString(itemFolder, "name", "NO-NAME");
                    itemPairs.add(new Pair<>(itemId, itemName));
                }));

        WzData.STRING.directory().findFile("Consume.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(itemFolder -> {
                    int itemId = Numbers.ofInt(itemFolder.name());
                    String itemName = Elements.findString(itemFolder, "name", "NO-NAME");
                    itemPairs.add(new Pair<>(itemId, itemName));
                }));

        WzData.STRING.directory().findFile("Eqp.img")
                .map(WzFile::content)
                .map(element -> element.find("Eqp"))
                .map(WzElement::childrenStream)
                .map(stream -> stream.flatMap(WzElement::childrenStream))
                .ifPresent(stream -> stream.forEach(itemFolder -> {
                    int itemId = Numbers.ofInt(itemFolder.name());
                    String itemName = Elements.findString(itemFolder, "name", "NO-NAME");
                    itemPairs.add(new Pair<>(itemId, itemName));
                }));

        WzData.STRING.directory().findFile("Etc.img")
                .map(WzFile::content)
                .map(element -> element.find("Etc"))
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(itemFolder -> {
                    int itemId = Numbers.ofInt(itemFolder.name());
                    String itemName = Elements.findString(itemFolder, "name", "NO-NAME");
                    itemPairs.add(new Pair<>(itemId, itemName));
                }));

        WzData.STRING.directory().findFile("Ins.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(itemFolder -> {
                    int itemId = Numbers.ofInt(itemFolder.name());
                    String itemName = Elements.findString(itemFolder, "name", "NO-NAME");
                    itemPairs.add(new Pair<>(itemId, itemName));
                }));

        WzData.STRING.directory().findFile("Pet.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(itemFolder -> {
                    int itemId = Numbers.ofInt(itemFolder.name());
                    String itemName = Elements.findString(itemFolder, "name", "NO-NAME");
                    itemPairs.add(new Pair<>(itemId, itemName));
                }));

        itemNameCache.addAll(itemPairs);
    }

    public static void getAllMobs() {
        List<Pair<Integer, MobInfo>> itemPairs = new ArrayList<>();
        // MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(new StringBuilder().append(System.getProperty("wzpath")).append("/String.wz").toString()));
        //   MapleDataProvider mobData = MapleDataProviderFactory.getDataProvider(new File(new StringBuilder().append(System.getProperty("wzpath")).append("/Mob.wz").toString()));

        WzData.STRING.directory().findFile("Mob.img")
                .map(WzFile::content)
                .map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(itemFolder -> {
                    int id = Numbers.ofInt(itemFolder.name());
                    try {
                        String name = Strings.padStart(String.valueOf(id), 7, '0');
                        WzData.MOB.directory().findFile(name)
                                .map(WzFile::content)
                                .ifPresent(element -> {
                                    int boss = id == 8810018 ? 1 : Elements.findInt(element, "info/boss");

                                    if (boss > 0) {
                                        bossCache.put(id, Boolean.TRUE);
                                    }

                                    MobInfo mobInfo = new MobInfo(boss, Elements.findInt(element, "info/rareItemDropLevel"),
                                            Elements.findString(itemFolder, "name", "NO-NAME"));

                                    itemPairs.add(new Pair<>(id, mobInfo));
                                });
                    } catch (Exception ignore) {
                    }
                }));
        mobCache.addAll(itemPairs);
    }

    public static class MobInfo {

        public int boss;
        public int rareItemDropLevel;
        public String name;

        public MobInfo(int boss, int rareItemDropLevel, String name) {
            this.boss = boss;
            this.rareItemDropLevel = rareItemDropLevel;
            this.name = name;
        }

        public int getBoss() {
            return this.boss;
        }

        public int rateItemDropLevel() {
            return this.rareItemDropLevel;
        }

        public String getName() {
            return this.name;
        }
    }
}
