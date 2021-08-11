/*
	Dolphin - Pier on the Beach(251000100)
*/

var status = -1;
var menu;
var cost = 10000;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	cm.sendNext("需要的时候再来找我。");
	cm.safeDispose();
	return;
    }
    if (status == 0) {
	cm.sendYesNo("你现在想要前往 #b#m230000000##k ? 费用是 #b"+cost+" 金币#k.");
    } else if (status == 1) {
	if (cm.getMeso() < cost) {
	    cm.sendOk("很抱歉，您没有足够的金币...");
	    cm.safeDispose();
	} else {
	    cm.gainMeso(-cost);
	    cm.warp(230000000);
	    cm.dispose();
	}
    }
}
