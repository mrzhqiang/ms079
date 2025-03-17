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
			cm.sendNext("魔法师可以使用华丽效果的属性魔法,并可以在组队打猎中使用非常有用的辅助魔法.2转后学习的属性魔法可以给相反属性的敌人致命的伤害哦。");
		} else if (status == 1) {
			cm.sendYesNo("怎么样？你想体验魔法师吗？");
		} else if (status == 2) {
			cm.lockUI();
			cm.warp(1020200);
			cm.dispose();
		}
	}
}	
