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
	cm.sendYesNo("Would you like to head to White Christmas Hill?");
    } else if (status == 1) {
	cm.saveLocation("CHRISTMAS");
	cm.warp(555000000);
	cm.dispose();
    }
}