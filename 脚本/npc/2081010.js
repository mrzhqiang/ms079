/*
Moose, Power of Shield
*/
var status = -1;

function action(mode, type, selection){
    if (mode == 1) {
	status++
    } else {
	cm.dispose();
	return;
    }
    if (status == 0) {
	if (cm.getMapId() == 924000002) { // At Exit Map
	    cm.warp(240010400, 0);
	    cm.dispose();
	} else if (cm.getMapId() == 924000000) { // At start map
	    cm.sendNext("I have to let you know one thing before sending you to the training field. You have to hold #b#t1092041##k that I gave you in shield training field. Otherwise, you're dead.");
	} else {
	    cm.warp(924000002, 0);
	    cm.dispose();
	}
    } else if (status == 1) {
	cm.sendSimple("Don't forget #rto hold shield#k before you get there! \r\n #b#L0# I want to get #t1092041#.#l \r\n #b#L1# Let me go in to #m924000001#.#l \r\n #b#L2# Let me out.#l");

    } else if (status == 2) {
	if (selection == 0) {
	    if (!cm.haveItem(1092041)) {
		if (cm.canHold(1092041)) {
		    cm.gainItem(1092041, 1);
		    cm.sendOk("I gave you #t1092041#. Check inventory. You have to be equipped with it!");
		} else {
		    cm.sendOk("I couldn...t give you #t1092041##k as there's no blank in Equipment box. Make a blank and try again." );
		}
	    } else {
		cm.sendOk("You already have #t1092041##k. No need more.");
	    }
	    cm.safeDispose();
	} else if (selection == 1) {
	    cm.warp(924000001, 0);
	    cm.dispose();
	} else {
	    cm.warp(240010400, 0);
	    cm.dispose();
	}
    }
}