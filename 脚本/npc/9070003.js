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
		cm.sendSimple((cm.getPlayer().getMapId() != 960000000 ? "\r\n#L5#Go to Battle Square#l" : "\r\n#L5#Go back to town#l"));
    } else if (status == 1) {
	cm.warp(cm.getPlayer().getMapId() != 960000000 ? 960000000 : 100000000);
	cm.dispose();
    }
}