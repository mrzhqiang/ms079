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
			cm.sendNext("飞侠 拥有运气和一些敏捷及力量,在战场中可以突袭敌人或者使用隐身等特殊技能.飞侠拥有非常敏捷的移动及回避,配合自身的多样化技能可以充分享受操作的乐趣。");
		} else if (status == 1) {
			cm.sendYesNo("怎么样？你想体验飞侠吗？");
		} else if (status == 2) {
			cm.lockUI();
			cm.warp(1020400);
			cm.dispose();
		}
	}
}	
