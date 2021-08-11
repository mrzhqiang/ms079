/* ===========================================================
			Resonance
	NPC Name: 		Dragon's Nest
	Map(s): 		Hidden Street: Lost Forest(900020220)
	Description: 	Obtain Dragon Egg
=============================================================
Version 1.0 - Script Done.(4/6/2010)
=============================================================
*/

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
			cm.forceStartQuest(22011);
			cm.playerMessage("你已经获得了龙蛋。");
			cm.warp(900090103);
			cm.dispose();
		}
	}
}