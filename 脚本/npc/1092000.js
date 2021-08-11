/**
	Tangyoon - Nautilus Cook
**/

function start() {
	if (cm.getQuestStatus(2180) == 1 && !cm.haveItem(4031847, 1)) {
		cm.gainItem(4031847, 1);
		cm.warp(912000100, 0);
		cm.dispose();
	} else {
		cm.sendOk("我怕我不能让你走到那里。");
		cm.dispose();
}
}