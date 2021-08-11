var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	if (cm.getPlayer().getMapId() == 680000000) {
	    cm.sendYesNo("你要去结婚礼堂吗?");
	} else {
	    cm.sendYesNo("你要回去结婚小镇吗?");
	}
    } else if (status == 1) {
	var WeddingMap = cm.getMap(680000400);
	WeddingMap.resetFully();
	cm.warp(cm.getPlayer().getMapId() == 680000000 ? 680000200 : 680000000);
	cm.dispose();
    }
}