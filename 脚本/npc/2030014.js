/* 
 * Ancient Icy Stone - Ice Demon
 */

function start() {
    if (cm.haveItem(4031450)) {
	if (cm.canHold(2280011)) {
	    cm.gainItem(2280011, 1);
	    cm.gainItem(4031450, -1);
	} else {
	    cm.sendOk("Your inventory is full.");
	}
    }
    cm.dispose();
}