var status = -1;
function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	cm.dispose();
	return;
    }
    if (status == 0) {
		if (cm.getPlayer().getLevel() < 40 && cm.haveItem(4032495)) {
			cm.sendYesNo("你想移动到隐藏地图?");
		} else {
			cm.sendOk("你需要小于40级，需要进入牛魔王勋章.");
			cm.dispose();
		}
} else {
	cm.spawnMob_map(9400612, 677000001,-10,60 );
	cm.warp(677000000,0);
	cm.dispose();
    }
}