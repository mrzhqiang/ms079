/*
	Marr - Tokyo Park 2095
*/

function start() {
    if (cm.getMapId() != 802000310) {
	cm.sendSimple("呜呜呜呜! \r\n#b#L0#我想要用#t4032192#兑换奖励。#l \r\n#b#L1#我想要跑走!!#l");
    } else {
	cm.sendOk("没事不要打扰我!");
	cm.dispose();
}
}

function action(mode, type, selection) {
    if (mode == 1) {
	if (selection == 0) {
	    if (cm.haveItem(4032192, 10)) {
		cm.removeAll(4032192);
		cm.warp(802000313, 0);
	    } else {
		cm.sendOk("请给我#t4032192#。");
	    }
	} else if (selection == 1) {
	    cm.warp(802000310, 0);
	}
    }
    cm.dispose();
}