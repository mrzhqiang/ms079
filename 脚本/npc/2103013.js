var status = 0;
var section = 0;
importPackage(java.lang);
//questid 29932, infoquest 7760
function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 99) {
            cm.dispose();
            return;
        }
        status--;
    }
    if (status == 1) {
        if (cm.getMapId() >= 926020001 && cm.getMapId() <= 926020004) {
            var itemid = 4001321 + (cm.getMapId() % 10);
            if (!cm.canHold(itemid)) {
                cm.sendOk("请空出一些其他栏。");
            } else {
                cm.gainItem(itemid, 1);
                cm.warp(cm.getMapId() - 10000, 0);
            }
            cm.dispose();
        } else if (cm.getMapId() >= 926010001 && cm.getMapId() <= 926010004) {
            cm.warp(926010000, 0);
            cm.dispose();
        } else if (cm.getMapId() >= 926010100 && cm.getMapId() <= 926013504) {
            cm.sendYesNo("你想要离开这里？？");
            status = 99;
        } else {
            cm.sendSimple("我的名字是#p2103013#\r\n#b#e#L1#进入金字塔副本#l#n\r\n#L2#进入法老小雪球副本#l\r\n#L3#兑换法老王腰带#l\r\n#L4#兑换勋章#l#k");
        }
    } else if (status == 2) {
        section = selection;
        if (selection == 1) {
            cm.sendSimple("你这个无知的傻瓜居然敢无视上帝的愤怒，选择一个命运吧！\r\n#L0# #v3994115# #l#L1# #v3994116# #l#L2# #v3994117# #l#L3# #v3994118# #l");
        } else if (selection == 2) {
            cm.sendSimple("你想要什么？？\r\n#L0##i4001322##t4001322##l\r\n#L1##i4001323##t4001323##l\r\n#L2##i4001324##t4001324##l\r\n#L3##i4001325##t4001325##l");
        } else if (selection == 3) {
            cm.sendSimple("你想要什么？？\r\n#L0##i1132012##t1132012##l\r\n#L1##i1132013##t1132013##l");
        } else if (selection == 4) {
            var record = cm.getQuestRecord(7760);
            var data = record.getCustomData();
            if (data == null) {
                record.setCustomData("0");
                data = record.getCustomData();
            }
            var mons = parseInt(data);
            if (mons < 50000) {
                cm.sendOk("请击杀 50,000 金字塔副本内的怪物再来找我 \r\n目前击杀了 : " + mons + "只");
            } else if (cm.canHold(1142142) && !cm.haveItem(1142142)) {
                cm.gainItem(1142142, 1);
                cm.forceStartQuest(29932);
                cm.forceCompleteQuest(29932);
            } else {
                cm.sendOk("请空出一些装备栏空间。");
            }
            cm.dispose();
        }
    } else if (status == 3) {
        if (section == 1) {
            var cont_ = false;
            if (selection == 0) { //easy; 40-45
                if (cm.getPlayer().getLevel() < 40) {
                    cm.sendOk("你的等级尚未达到40级。");
                } else if (cm.getPlayer().getLevel() > 60) {
                    cm.sendOk("你的等级高于60级。");
                } else {
                    cont_ = true;
                }
            } else if (selection == 1) { //normal; 46-50
                if (cm.getPlayer().getLevel() < 45) {
                    cm.sendOk("你的等级尚未达到45级。");
                } else if (cm.getPlayer().getLevel() > 60) {
                    cm.sendOk("你的等级高于60级。");
                } else {
                    cont_ = true;
                }
            } else if (selection == 2) { //hard; 51-60
                if (cm.getPlayer().getLevel() < 50) {
                    cm.sendOk("你的等级尚未达到50级。");
                } else if (cm.getPlayer().getLevel() > 60) {
                    cm.sendOk("你的等级高于60级。");
                } else {
                    cont_ = true;
                }
            } else if (selection == 3) { //hell; 61+
                if (cm.getPlayer().getLevel() < 61) {
                    cm.sendOk("你的等级尚未达到61级。");
                } else {
                    cont_ = true;
                }
            }
            if (cont_ && cm.isLeader()) {//todo
                if (!cm.start_PyramidSubway(selection)) {
                    cm.sendOk("目前金字塔副本满人，请稍后再尝试。");
                }
            } else if (cont_ && !cm.isLeader()) {
                cm.sendOk("请找您的队长来找我说话。");
            }
        } else if (section == 2) {
            var itemid = 4001322 + selection;
            if (!cm.haveItem(itemid, 1)) {
                cm.sendOk("你没有#b#t" + itemid + "##k");
            } else {
                if (cm.bonus_PyramidSubway(selection)) {
                    cm.gainItem(itemid, -1);
                } else {
                    cm.sendOk("目前金字塔副本满人，请稍后再尝试。");
                }
            }
        } else if (section == 3) {
            if (selection == 0) {
				if (cm.canHold(1132012)) {
                if (cm.haveItem(2022613, 150)) {
					cm.gainItem(2022613, -150);
					cm.gainItem(1132012, 1);
					cm.sendOk("来这是你的奖励。");
				} else {
					cm.sendOk("我需要#b#t2022613##k 150个。");
				}
					cm.sendOk("请空出一些空间。");
				}
            } else if (selection == 1) {
				if (cm.canHold(1132013)) {
                if (cm.haveItem(2022613, 400) && cm.haveItem(1132012)) {
					cm.gainItem(2022613, -400);
					cm.gainItem(1132012, -1);
					cm.gainItem(1132013, 1);
					cm.sendOk("来这是你的奖励。");
				} else {
					cm.sendOk("我需要#b#t2022613##k 400个 和一条 #i1132012#。");
				}
					cm.sendOk("请空出一些空间。");
            }
        }
        cm.dispose(); //todo
    } else if (status == 100) {
        cm.warp(926010000, 0);
        cm.dispose();
    }
}
}