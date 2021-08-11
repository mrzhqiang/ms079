function start() {
    cm.sendYesNo("请问你是否要离开呢??");
}

function action(mode, type, selection) {
    if (mode == 1) {
	var map = cm.getMapId();
	var kill = cm.getMap().killAllMonsters(true);
	var tomap;

	if (map == 108010101) {
		kill;
	    tomap = 100000000;
	} else if (map == 108010201) {
		kill;
	    tomap = 101000000;
	} else if (map == 108010301) {
		kill;
	    tomap = 102000000;
	} else if (map == 108010401) {
		kill;
	    tomap = 103000000;
	} else if (map == 108010501) {
		kill;
	    tomap = 120000000;
	}
	cm.warp(tomap);
    }
    cm.dispose();
}
