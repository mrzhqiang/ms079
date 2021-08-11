var status = -1;
var items = Array(1102041, 1082149, 5220000);
var itemsp = Array(2000, 2000, 500);
var itemsu = Array(0, 2, 0); // extra slots, not set.
var itemsq = Array(1, 1, 150);
var itemse = Array(7, -1, -1);
var chairs = Array(3010025, 3010045, 3010054, 3012002, 3010014, 3010068, 3010022, 3010023, 3010041);
var chairsp = Array(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000);
var fame = 100;
var famep = 500;
var acashp = 10000;
var sel = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }
    status++;
    if (status == 0) {
        cm.sendSimple("嗨， #r#h ##k! 我的名字是 #r#p9000039##k. 如果你有 #b赞助点数#k. 可以找我换东西哦。 好了你需要什么？？\r\n#b#L0#什么是赞助点数？#l\r\n#b#L1#换一些赞助商品#l \r\n#b#L2#换一些椅子#l \r\n#b#L3#换一些名声#l\r\n#L4#换一些点数#l\r\n#L6#我目前有多少赞助点数?\r\n#L5#红色福袋换1千赞助点#l \r\n#L7#1千赞助点换红色福袋#l#k");
    } else if (status == 1) {
        sel = selection;
        if (selection == 0) {
            cm.sendNext("赞助点数可以买一些物品，至于怎么样有赞助点数...这要问\r\nGM呢=)");
            status = -1;
        } else if (selection == 1) {
            var selStr = "你想要什么？？#b\r\n\r\n";
            for (var i = 0; i < items.length; i++) {
                selStr += "#L" + i + "##v" + items[i] + "##t" + items[i] + "#" + (itemsu[i] > 0 ? "(附赠 " + itemsu[i] + " 卷)" : "") + " x " + itemsq[i] + " 为 #e" + itemsp[i] + "#n 赞助点#n" + (itemse[i] > 0 ? (" ...期限 #r#e" + itemse[i] + "#n#b天") : "") + "#l\r\n";
            }
            cm.sendSimple(selStr + "#k");
        } else if (selection == 2) {
            var selStr = "你想要什么？？#b\r\n\r\n";
            for (var i = 0; i < chairs.length; i++) {
                selStr += "#L" + i + "##v" + chairs[i] + "##t" + chairs[i] + "# 为 #e" + chairsp[i] + "#n 赞助点#n#l\r\n";
            }
            cm.sendSimple(selStr + "#k");
        } else if (selection == 3) {
            cm.sendYesNo("你需要名声?? 我可以跟你交换 #b#e" + fame + " 名声为 " + famep + " 赞助点.#n#k. 请问你接受吗??");
        } else if (selection == 4) {
            cm.sendYesNo("你需要GASH对吧?? 我可以跟你交换 #r#e1000 GASH 为 " + acashp + " 赞助点.#n#k. 请问你接受吗??");
        } else if (selection == 5) {
            if (!cm.getPlayer().haveItem(3993003)) {
                cm.sendOk("你需要有一个#b#t3993003##k。");
                cm.dispose();
            } else {
                cm.sendGetNumber("你想要用多少#r#t3993003##k换赞助点数？？ \r\n比值: (1 #t3993003# = 1000 赞助点) \r\n(目前你有: " + cm.getPlayer().itemQuantity(3993003) + "个 #r#t3993003##k) \r\n(目前你有: " + cm.getPlayer().getPoints() + "赞助点)", cm.getPlayer().itemQuantity(3993003), 1, cm.getPlayer().itemQuantity(3993003));
            }
        } else if (selection == 7) {
            if (cm.getPlayer().getPoints() < 1000) {
                cm.sendOk("你需要有#b一千赞助点#k。");
                cm.dispose();
            } else {
                cm.sendGetNumber("你想要用多少#r赞助点数#k换#t3993003#？？ \r\n比值: (1 #t3993003# = 1000 赞助点) \r\n(目前你有: " + cm.getPlayer().getPoints() + "赞助点) (目前你有:  " + cm.getPlayer().itemQuantity(3993003) + "个 #r#t3993003##k)", cm.getPlayer().getPoints() / 1000, 1, cm.getPlayer().getPoints() / 1000);
            }
        } else if (selection == 6) {
            cm.sendOk("目前你有 #e" + cm.getPlayer().getPoints() + "#n 赞助点数。");
            cm.dispose();
        }
    } else if (status == 2) {
        if (sel == 1) {
            var it = items[selection];
            var ip = itemsp[selection];
            var iu = itemsu[selection];
            var iq = itemsq[selection];
            var ie = itemse[selection];
            if (cm.getPlayer().getPoints() < ip) {
                cm.sendOk("很抱歉，你没有足够的赞助点数 你目前有 " + cm.getPlayer().getPoints() + " 我需要 " + ip + ".");
            } else if (!cm.canHold(it, iq)) {
                cm.sendOk("请空出一些栏位。");
            } else {
                if (iu > 0) {
                    cm.gainItem(it, iq, false, ie, iu);
                } else {
                    cm.gainItemPeriod(it, iq, ie);
                }
                cm.getPlayer().setPoints(cm.getPlayer().getPoints() - ip);
                cm.sendOk("感谢！！");
            }
        } else if (sel == 2) {
            var it = chairs[selection];
            var cp = chairsp[selection];
            if (cm.getPlayer().getPoints() < cp) {
                cm.sendOk("很抱歉，你没有足够的赞助点数 你目前有 " + cm.getPlayer().getPoints() + " 我需要 " + cp + ".");
            } else if (!cm.canHold(it)) {
                cm.sendOk("请空出一些栏位。");
            } else {
                cm.gainItem(it, 1);
                cm.getPlayer().setPoints(cm.getPlayer().getPoints() - cp);
                cm.sendOk("感谢！！");
            }
        } else if (sel == 3) {
            if (cm.getPlayer().getPoints() < famep) {
                cm.sendOk("很抱歉，你没有足够的赞助点数 你目前有 " + cm.getPlayer().getPoints() + " 我需要 " + famep + ".");
            } else if (cm.getPlayer().getFame() > (30000 - fame)) {
                cm.sendOk("你已经有太多的名声了。");
            } else {
                cm.getPlayer().setPoints(cm.getPlayer().getPoints() - famep);
                cm.getPlayer().addFame(fame);
                cm.getPlayer().updateSingleStat(client.MapleStat.FAME, cm.getPlayer().getFame());
                cm.sendOk("感谢！！");
            }
        } else if (sel == 4) {
            if (cm.getPlayer().getPoints() < acashp) {
                cm.sendOk("很抱歉，你没有足够的赞助点数 你目前有 " + cm.getPlayer().getPoints() + " 我需要 " + acashp + ".");
            } else if (cm.getPlayer().getCSPoints(1) > (java.lang.Integer.MAX_VALUE - 100000)) {
                cm.sendOk("你已经有太多GASH了。");
            } else {
                cm.getPlayer().setPoints(cm.getPlayer().getPoints() - acashp);
                cm.getPlayer().modifyCSPoints(1, 100, true);
                cm.sendOk("感谢！！");
            }
        } else if (sel == 5) {
            if (selection >= 1 && selection <= cm.getPlayer().itemQuantity(3993003)) {
                if (cm.getPlayer().getPoints() > (2147483647 - (selection * 1000))) {
                    cm.sendOk("你有太多赞助点了。");
                } else {
                    cm.gainItem(3993003, -selection);
                    cm.getPlayer().setPoints(cm.getPlayer().getPoints() + (selection * 1000));
                    cm.sendOk("你失去了 " + selection + " #r个#t3993003##k 和 获得了 " + (selection * 1000) + " 赞助点. \r\n目前赞助点: " + cm.getPlayer().getPoints());
                }
            }
        } else if (sel == 7) {
            if (selection >= 1) {
                if (selection > (cm.getPlayer().getPoints() / 1000)) {
                    cm.sendOk("你最多只能得到 " + (cm.getPlayer().getPoints() / 1000) + ". 1 道具 = 1000 赞助点.");
                } else if (!cm.canHold(3993003, selection)) {
                    cm.sendOk("请空出一些装饰栏。");
                } else {
                    cm.gainItem(3993003, selection);
                    cm.getPlayer().setPoints(cm.getPlayer().getPoints() - (selection * 1000));
                    cm.sendOk("你获得了 " + selection + " #r个#t3993003##k 和 失去了 " + (selection * 1000) + " 赞助点. \r\n目前赞助点: " + cm.getPlayer().getPoints());
                }
            }
        }
        cm.dispose();
    }
}