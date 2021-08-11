/*
	Cody
*/

function start() {
    cm.sendSimple("Would you like to go somewhere?\r\n#b#L0#Beer Tent#l\r\n#L1#Mal Volence#l#k");
}

function action(mode,type,selection) {
    if (mode == 1) {
	cm.saveReturnLocation("CHRISTMAS");
	cm.warp(selection == 0 ? 674020000 : 674030100,0);
    }
    cm.dispose();
}