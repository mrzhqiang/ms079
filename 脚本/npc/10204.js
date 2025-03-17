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
			cm.sendNext("海盗凭借高敏捷及力量给与敌人发射百发百中的短枪或者可以使用瞬间制约敌人的格斗术.使用短枪的海盗可以选择属性子弹更有效地攻击敌人或者坐在船上攻击敌人,使用格拳甲的海盗可以变身后发挥更强的力量。");
		} else if (status == 1) {
			cm.sendYesNo("怎么样？你想体验海盗吗？");
		} else if (status == 2) {
			cm.lockUI();
			cm.warp(1020500);
			cm.dispose();
		}
	}
}	
