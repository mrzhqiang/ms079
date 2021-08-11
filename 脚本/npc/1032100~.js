/**
	Arwen the Fairy - Victoria Road : Ellinia (101000000)
**/

var status = 0;
var item;
var selected;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status == 1 && mode == 0) {
	cm.dispose();
	return;
    } else if (status == 2 && mode == 0) {
	cm.sendNext("这个 " + item + " 不好制作，准备好材料再来找我。");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (cm.getPlayerStat("LVL") >= 40) {
	    cm.sendNext("是啊...我是仙女的炼金大师。但是，仙女不应该在一个人的时间很长一段时间的接触......。如果你得到了我的资料，我会让你成为一个特殊的道具。");
	} else {
	    cm.sendOk("我可以做出稀有，贵重物品，但不幸的是，我不能让它像你这样的陌生人。");
	    cm.dispose();
	}
    } else if (status == 1) {
	cm.sendSimple("你想要做什么#b\r\n#L0#月石#l\r\n#L1#星石#l\r\n#L2#黑羽毛#l");
    } else if (status == 2) {
	selected = selection;
	if (selection == 0) {
	    item = "月石";
	    cm.sendYesNo("所以，你想要做" + item + "? 那么你需要的材料有: #b#t4011000##k, #b#t4011001##k,\r\n#b#t4011002##k, #b#t4011003##k, #b#t4011004##k, #b#t4011005##k, 和 #b#t4011006##k. 然后还有 10,000 金币");
	} else if (selection == 1) {
	    item = "星石";
	    cm.sendYesNo("所以，你想要做" + item + "? 那么你需要的材料有: #b#t4021000##k, #b#t4021001##k, #b#t4021002##k, #b#t4021003##k, #b#t4021004##k, #b#t4021005##k, #b#t4021006##k, #b#t4021007##k 和 #b#t4021008##k. 然后还有 15,000 金币");
	} else if (selection == 2) {
	    item = "黑羽毛";
	    cm.sendYesNo("所以，你想要做" + item + "? 那么你需要的材料有: #b1 #t4001006##k, #b1 #t4001007##k 和 #b1 #t4001008##k. 然后还有 30,000 金币");
	}
    } else if (status == 3) {
	if (selected == 0) {
	    if (cm.haveItem(4011000) && cm.haveItem(4011001) && cm.haveItem(4011002) && cm.haveItem(4011003) && cm.haveItem(4011004) && cm.haveItem(4011005) && cm.haveItem(4011006) && cm.getMeso() > 10000) {
		cm.gainMeso(-10000);
		cm.gainItem(4011000, -1);
		cm.gainItem(4011001, -1);
		cm.gainItem(4011002, -1);
		cm.gainItem(4011003, -1);
		cm.gainItem(4011004, -1);
		cm.gainItem(4011005, -1);
		cm.gainItem(4011006, -1);
		cm.gainItem(4011007, 1);
		cm.sendNext("做好了你要的 " + item + "。");
	    } else {
		cm.sendNext("请准备好材料和钱再来找我。");
	    }
	} else if (selected == 1) {
	    if (cm.haveItem(4021000) && cm.haveItem(4021001) && cm.haveItem(4021002) && cm.haveItem(4021003) && cm.haveItem(4021004) && cm.haveItem(4021005) && cm.haveItem(4021006) && cm.haveItem(4021007) && cm.haveItem(4021008) && cm.getMeso() > 15000) {
		cm.gainMeso(-15000);
		cm.gainItem(4021000, -1);
		cm.gainItem(4021001, -1);
		cm.gainItem(4021002, -1);
		cm.gainItem(4021003, -1);
		cm.gainItem(4021004, -1);
		cm.gainItem(4021005, -1);
		cm.gainItem(4021006, -1);
		cm.gainItem(4021007, -1);
		cm.gainItem(4021008, -1);
		cm.gainItem(4021009, 1);
		cm.sendNext("做好了你要的 " + item + "。");
	    } else {
		cm.sendNext("请准备好材料和钱再来找我。");
	    }
	} else if (selected == 2) {
	    if (cm.haveItem(4001006) && cm.haveItem(4011007) && cm.haveItem(4021008) && cm.getMeso() > 30000) {
		cm.gainMeso(-30000);
		cm.gainItem(4001006, -1);
		cm.gainItem(4011007, -1);
		cm.gainItem(4021008, -1);
		cm.gainItem(4031042, 1);
		cm.sendNext("做好了你要的 " + item + "。");
	    } else {
		cm.sendNext("请准备好材料和钱再来找我。");
	    }
	}
	cm.dispose();
    }
}
