/* NPC : A pile of pink flower
 * Location : Sleepywood, forest of patient
 */

var itemSet = new Array(4010003, 4010000, 4010002, 4010005, 4010004, 4010001);
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
            
	if (cm.getMapId() == 105040300) {
	    if (cm.getQuestStatus(2052) == 1 && !cm.haveItem(4031025)) {
		cm.gainItem(4031025, 10);
	    }
	} else {
	    cm.gainItem(itemSet[rand], 2);
	}
	cm.dispose();
    }
}