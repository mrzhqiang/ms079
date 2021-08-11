// Kerny - Pilot
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	if (cm.getMapId() == 540010002) {
		cm.sendOk("风景很美对吧?");
	    cm.dispose();
	} else if (cm.getMapId() == 540010101){
	cm.sendOk("风景很美对吧?");
	cm.dispose();
	} else {
	    cm.sendYesNo("这架飞机将在起飞不久，请问你现在离开？您将有再次购买飞机票到这里来.");
	}
    } else {
	cm.warp(540010000, 0);
	cm.dispose();
    }
}