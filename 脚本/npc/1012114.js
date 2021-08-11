var status = -1;

function action(mode, type, selection) {
	if (mode == 0 && status == 0) {
		cm.dispose();
		return;
	}
	if (mode == 1)
		status++;
	else
		status--;
	if (status == 0) {
		cm.sendSimple("地方的老虎需要#b月妙的元宵#k...#b\r\n#L0#我带来10个月妙的元宵孝敬您了#l\r\n#L1#我要用20个月妙的年宵兑换潮帽#l#k");
	} else if (status == 1) {
		if (selection == 0) {
			if (!cm.isLeader()) {
				cm.sendNext("只有队长给的我才要吃");
		                cm.dispose();
			} else {
				if (cm.haveItem(4001101,10)) {
					cm.gainItem(4001101, -10);
					cm.showEffect(true, "quest/party/clear");
					cm.playSound(true, "Party1/Clear");
					cm.givePartyExp(1600);
					cm.endPartyQuest(1200);
					cm.warpParty(910010100);
		                        cm.dispose();
				} else {
					cm.sendNext("呜呜 我要吃拉");
		                        cm.dispose();
				}
			}
		} else if (selection == 1) {
	if (cm.haveItem(1002798,1)) {
	    cm.sendOk("你已经有了");
	} else if (!cm.canHold(1002798,1)) {
	    cm.sendOk("你已经有了");
	} else if (cm.haveItem(4001101,20)) {
	    cm.gainItem(4001101,-20); //should handle automatically for "have"
	    cm.gainItem(1002798,1);
	} else {
	    cm.sendOk("你需要20个月妙的元宵");
	}
		cm.dispose();
	}
	}
}