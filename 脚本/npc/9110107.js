/*
	NPC Name: 		Palakeen
	Map(s): 		Zipangu - Mushroom Shrine
	Description: 		Kaede Castle teleporter
*/

var status = -1;
var cost = 3000;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 1 || status == 0 && mode == -1) {
	    cm.sendNext("需要的时候再来找我吧。");
	    cm.dispose();
	    return;
	}
	status--;
    }
    switch (cm.getMapId()) {
	case 800040000: {
	    if (status == 0) {
		cm.sendNext("是否要回去古代神社？ 一次 " + cost + "金币就好了。");
		} else if (status == 1) {
		cm.sendYesNo("您真的要回去吗??");
	    } else if (status == 2) {
		cm.sendNext("那我就带您回去啰！");
	    } else if (status == 3) {
		cm.gainMeso(-cost);
		cm.warp(800000000, 0);
		cm.dispose();
	    }
	    break;
	}
	default: {
	    if (status == 0) {
		cm.sendNext("是否要去枫叶古城？ 一次 " + cost + "金币就好了。");
		} else if (status == 1) {
		cm.sendYesNo("您真的要去吗??");	
	    } else if (status == 2) {
		cm.sendNext("那我就带您去啰！");
	    } else if (status == 3) {
		cm.gainMeso(-cost);
		cm.warp(800040000, 0);
		cm.dispose();
	    }
	    break;
	}
    }
}
