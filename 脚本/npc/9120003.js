/*
	Hikari - Showa Town(801000000)
*/

var status = -1;

function start() {
    action(1,0,0);
}

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else {
	cm.sendOk("需要的时候再来找我。");
	cm.dispose();
	return;
    }
    if (status == 0) {
	cm.sendYesNo("你想进入澡堂？ "+300+" 金币");
    } else if (status == 1) {
	if (cm.getMeso() < 300) {
	    cm.sendOk("请确认是不是有 "+300+" 金币。");
	} else {
	    cm.gainMeso(-300);
	    if (cm.getPlayerStat("GENDER") == 0) {
		cm.warp(801000100);
	    } else {
		cm.warp(801000200);
	    }
	}
	cm.dispose();
    }
}
