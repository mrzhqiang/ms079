var status = -1;
function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	cm.dispose();
	return;
    }
    if (status == 0) {
		if (cm.getPlayer().getLevel() < 40 && cm.haveItem(4032491)) {
			cm.sendYesNo("你想移动到隐藏地图?");
		} else {
			cm.sendOk("你需要小于40级，需要进入印第安老斑鸠勋章.");
			cm.dispose();
		}
} else {
	cm.spawnMob_map(9400609, 677000005 , 89, 85);
	cm.warp(677000004,0);
	cm.dispose();
    }
}