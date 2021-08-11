/* NPC : A pile of blue flower
 * Location : Sleepywood, forest of patient
 */

var itemSet = new Array(4020005, 4020006, 4020004, 4020001, 4020003, 4020000, 4020002);
var rand = Math.floor(Math.random() * itemSet.length);


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 2 && mode == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	cm.warp(105040300);
            
	if (cm.getQuestStatus(2053) == 1 && !cm.haveItem(4031026)) {
	    cm.gainItem(4031026, 20);
	} else {
	    cm.gainItem(itemSet[rand], 2);
	}
	cm.dispose();
    }
}