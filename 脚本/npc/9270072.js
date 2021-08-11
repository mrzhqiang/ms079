/* 
	NPC Name: 		Maple Administrator
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	cm.sendYesNo("Would you like to head back?");
    } else if (status == 1) {
	var returnMap = cm.getSavedLocation("CHRISTMAS");
	if (returnMap < 0) {
		returnMap = 100000000; // to fix people who entered the fm trough an unconventional way
	}
	cm.clearSavedLocation("CHRISTMAS");
	cm.warp(returnMap,0);
	cm.dispose();
    }
}