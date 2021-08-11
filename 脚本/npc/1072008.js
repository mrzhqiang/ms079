/**
	Author: xQuasar
	NPC: Kyrin - Pirate Job Advancer
	Inside Test Room
**/

var status;

function start() {
    status = -1;
    action(1,0,0);
}

function action(mode,type,selection) {
    if (status == -1) {
	if (cm.getMapId() == 108000500) {
	    if (!(cm.haveItem(4031857,15))) {
		cm.sendNext("快去收集 15个 #b列风结晶#k 给我.");
		cm.dispose();
	    } else {
		status = 2;
		cm.sendNext("wow 果然是个大侠恭喜通过这次个考验 你已经是个强大的海盗了所以我将颁赠给你神秘的小礼物.");
	    }
	} else if (cm.getMapId() == 108000502) {
	    if (!(cm.haveItem(4031856,15))) {
		cm.sendNext("快去收集15个 #b强大力量结晶#k 给我.");
		cm.dispose();
	    } else {
		status = 2;
		cm.sendNext("wow 果然是个大侠恭喜通过这次个考验 你已经是个强大的海盗了所以我将颁赠给你神秘的小礼物.");
	    }
	} else {
	    cm.sendNext("错误请再尝试一次.");
	    cm.dispose();
	}
    } else if (status == 2) {
	cm.gainItem(4031012, 1);
	cm.warp(120000101,0);
	cm.dispose();
    }
}