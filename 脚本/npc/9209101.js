var status;

function start() {
    status = -1;
    action(1,0,0);
}

function action(mode, type, selection){
    if (mode == 0 && status == 0) {
	cm.dispose();
	return;
    } else {
	if (mode == 1)
	    status++;
	else
	    status--;
		var mapId = cm.getMapId();
	    var outText;
	    if (mapId == 740000000) {
		outText = "你确定要离开BOSS PQ？？";
	    }
	    if (status == 0) {
		cm.sendYesNo(outText);
	    } else if (mode == 1) {
		cm.warp(910000000, 0); // 回自由
		cm.dispose();
	    }
    }
}