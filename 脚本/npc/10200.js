var status = 0;
var job;

importPackage(net.sf.odinms.client);

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if ((mode == 0 && status == 2) || (mode == 0 && status == 13)) {
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendNext("弓手拥有高敏捷及力量,在战斗中负责远距离攻击,假如弓手职业能巧妙地运用地势的话,打猎可是非常轻松厉害。");
		} else if (status == 1) {
			cm.sendYesNo("怎么样？你想体验弓手吗？");
		} else if (status == 2) {
			cm.lockUI();
			cm.warp(1020300);
			cm.dispose();
		}
	}
}	