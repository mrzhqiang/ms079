/* 
 * Tylus
 */

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	if (cm.getQuestStatus(6192) == 1) {
	    cm.sendOk("Thank you for guarding me. I could do my mission thanks to you. Talk to me when you're out.");
	} else {
	    cm.warp(211000001, 0);
	    cm.dispose();
	}
    } else if (status == 1) {
	if (!cm.haveItem(4031495)) {
	    if (cm.canHold(4031495)) {
		cm.gainItem(4031495, 1);
		cm.warp(211000001, 0);
		cm.dispose();
	    } else {
		cm.sendOk("You're not given items as there's no blank in Others box. Make a blank and talk to me again.");
		cm.safeDispose();
	    }
	} else {
	    cm.warp(211000001, 0);
	    cm.dispose();
	}
    }
}