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
	    cm.sendYesNo("请问你想要去参观结婚礼堂吗？？");
	} else {
	    cm.sendYesNo("请问你想要回去#m680000000#？？");
	}
    } else if (status == 1) {
	cm.warp(cm.getPlayer().getMapId() == 680000000 ? 680000100 : 680000000);
	cm.dispose();
    }
}