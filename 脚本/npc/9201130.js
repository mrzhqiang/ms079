var status = -1;
function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	cm.dispose();
	return;
    }
    if (status == 0) {
		if (cm.getPlayer().getLevel() < 40 && cm.haveItem(4032485)) {
			cm.sendYesNo("你想移动到隐藏地图?");
		} else {
			cm.sendOk("你需要小于40级，需要进入要有大型钱币模型.");
			cm.dispose();
		}
} else {
	cm.spawnMob_map(9400613, 677000009 , 33, 66);
	cm.warp(677000008,0);
	cm.dispose();
    }
}