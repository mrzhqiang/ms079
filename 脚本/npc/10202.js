var status = 0;
var job;

//importPackage(net.sf.odinms.client);

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
			cm.sendNext("战士拥有很强的攻击力和体力,因此在战斗中处于非常重要的地位.因为基本攻击很强,所以学习高级技能的话可以发挥超强的力量。");
		} else if (status == 1) {
			cm.sendYesNo("怎么样？你想体验战士吗？");
		} else if (status == 2) {
			cm.lockUI();
			cm.warp(1020100);
			cm.dispose();
		}
	}
}	
