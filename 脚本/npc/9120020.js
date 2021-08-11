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
	cm.sendYesNo("你想要回去了嘛?");
    } else if (status == 1) {
	cm.warp(193000000);
	cm.dispose();
    }
}