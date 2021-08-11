var status = -1;
function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	cm.dispose();
	return;
    }
    if (status == 0) {
		if (cm.getPlayer().getLevel() < 40 && cm.haveItem(1452084)) {
			cm.sendYesNo("你想移动到隐藏地图?");
		} else {
			cm.sendOk("你需要小于40级，需要进入要有所罗门之弓.");
			cm.dispose();
		}
} else {
	cm.spawnMob_map(9400610, 677000003,34,35 );
	cm.warp(677000002,0);
	cm.dispose();
    }
}