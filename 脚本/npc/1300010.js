/* ===========================================================
			Resonance
	NPC Name: 		Killer Mushroom Spore
	Map(s): 		Mushroom Castle: Deep inside Mushroom Forest(106020300)
	Description: 	Breaking the Barrier
=============================================================
Version 1.0 - Script Done.(18/7/2010)
=============================================================
*/

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
		if(status == 0 && mode == 0){
			cm.sendNext("You have canceled the use of the item.");
			cm.gainItem(2430014, 1);
			cm.dispose();
		}
		if (mode == 1)
            status++;
        else
            status--;
		}
	if(status == 0){
		cm.sendYesNo("Are you going to use the #bKiller Mushroom Spore#k?....#e#r* Take Note#n..Please do not apply directly on the body!..If swallowed, please see the nearest doctor!");
	}if(status == 1)
		cm.PlayerToNpc("Awesome, the barrier is broken!!!");
	if(status == 2){
		cm.playerMessage("The Mushroom Forest Barrier has been removed, and penetrated.");
		cm.dispose();
	}
}
			