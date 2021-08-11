/*
	Egnet - Before Takeoff To Ariant(200000152)
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	cm.sendOk("这是好的选择！！");
	cm.safeDispose();
	return;
    }
    if (status == 0) {
	cm.sendYesNo("你要离开船上??");
    } else if (status == 1) {
	cm.warp(200000151);
	cm.dispose();
    }
}