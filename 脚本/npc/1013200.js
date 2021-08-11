/* ===========================================================
			Resonance
	NPC Name: 		Piglet
	Map(s): 		Hidden Street: Lush Forest(900020110)
	Description: 	Obtain Piglet
=============================================================
Version 1.0 - Script Done.(4/6/2010)
=============================================================
*/

importPackage(Packages.client);

var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.forceCompleteQuest(22015);
			cm.playerMessage("You have rescued the Piglet.");
			cm.gainItem(4032449, 1);
			cm.dispose();
		}
	}
}