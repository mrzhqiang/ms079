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
    if (cm.getPlayer().getLevel() < 50) {
	cm.sendOk("在你受伤以前，赶快离开吧.");
	cm.dispose();
	return;
    }
    if (status == 0) {
	cm.sendYesNo("你看起来很强的样子，要不要去一趟巴洛古寺庙呢?");
    } else if (status == 1) {
	cm.warp(105100100);
	cm.dispose();
    }
}