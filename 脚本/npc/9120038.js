var minlv = 140;

function start() {
	var level = cm.getPlayerStat("LVL");
	var trainMap = cm.getMap(802000311);
    if (cm.canHold(4032202)) {
		if (trainMap.playerCount() == 0) {
		trainMap.resetFully();
		}
        if (!(cm.haveItem(4032202)) && level >= minlv) {
			cm.gainItem(4032202,1);
			cm.warp(802000311,0);
		} else if (cm.haveItem(4032202)) {
			cm.warp(802000311,0);
		} else {
			cm.sendOk("你貌似等级不够...")
		}
		cm.sendOk("走啰!");
	} else {
		cm.sendOk("请检查你的装备拦....");
	}
		cm.dispose();
}