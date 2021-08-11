var status = 0;
var section = 0;
importPackage(java.lang);
//questid 29931, infoquest 7662
function action(mode, type, selection) {
    if (status == 99 || mode == -1){
	cm.sendOk("需要的时候可以来找我。");
	cm.dispose();
	}
	if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 1) {
	if (cm.getMapId() == 910320001) {
		cm.warp(910320000, 0);
		cm.dispose();
	} else if (cm.getMapId() == 910330001) {
		var itemid = 4001321;
		if (!cm.canHold(itemid)) {
			cm.sendOk("请空出一些其他栏。");
		} else {
			cm.gainItem(itemid,1);
			cm.warp(910320000, 0);
		}
		cm.dispose();
	} else if (cm.getMapId() >= 910320100 && cm.getMapId() <= 910320304) {
		cm.sendYesNo("你想要离开？？");
		status = 99;
	} else {
		cm.sendSimple("您好，我是#p1052115# 有什么可以帮忙的吗？？\r\n#b#e#L1#进去挑战。#l#n\r\n#L2#火车训练 999.#l\r\n#L3#领取勋章 <#t4001321#>.#l#k");
	}
    } else if (status == 2) {
		section = selection;
		if (selection == 1) {
			if (cm.getPlayer().getLevel() < 25 || cm.getPlayer().getLevel() > 30 || !cm.isLeader()) {
				cm.sendOk("你需要等级25-30之内，并找队长找我。");
			} else {
				if (!cm.start_PyramidSubway(-1)) {
					cm.sendOk("目前是满的。");
				}
			}
			//todo
		} else if (selection == 2) {
			if (cm.haveItem(4001321)) {
				if (cm.bonus_PyramidSubway(-1)) {
					cm.gainItem(4001321, -1);
				} else {
					cm.sendOk("里面已经满了。");
				}
			} else {
				cm.sendOk("你没有#b#t4001321##k。");
			}
		} else if (selection == 3) {
			var record = cm.getQuestRecord(7662);
			var data = record.getCustomData();
			if (data == null) {
				record.setCustomData("0");
				data = record.getCustomData();
			}
			var mons = parseInt(data);
			if (mons < 10000) {
				cm.sendOk("至少要杀死1万只怪物，目前 : " + mons);
			} else if (cm.canHold(1142141) && !cm.haveItem(1142141)){
				cm.gainItem(1142141,1);
				cm.forceStartQuest(29931);
				cm.forceCompleteQuest(29931);
			} else {
				cm.sendOk("请空出一些空间。");
			}
		}
		cm.dispose();
	} else if (status == 100) {
		cm.warp(910320000,0);
		cm.dispose();
	}
}