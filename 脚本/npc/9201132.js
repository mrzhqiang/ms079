var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
		
		if(cm.getPlayerCount(677000006) <= 0 && cm.getPlayerCount(677000007) <= 0 ){
            cm.sendYesNo("你想移动到血之猫女所在地？?");
        } else {
            cm.sendOk("里面有人,无法进入.");
            cm.dispose();
        }
		
    } else {
       if (cm.getPlayer().getLevel() >= 40) {
        cm.warp(677000006, 0);
		cm.getMap(677000007).resetFully();
		cm.spawnMobOnMap(9400611,1,150,73,677000007);
        cm.dispose();
	} else {
        cm.sendOk("你等级未足40级,无法进入.");
        cm.dispose();
    }
}
}
