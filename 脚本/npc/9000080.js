var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode != 1) {
		cm.dispose();
	} else {
		status++;
		if(status == 0){
			cm.sendSimple("Hello. Welcome to the Monkey Temple Entrance. Where would you like to go? #rYou need a Golden Temple Ticket to enter.#k\r\n\r\n#b#L0#Monkey Temple 1 - Wild Monkey(250 HP/52 EXP)#l\r\n#L1#Monkey Temple 2 - Mama Monkey(350 HP/70 EXP)#l\r\n#L2#Monkey Temple 3 - White Baby Monkey(650 HP/120 EXP)#l\r\n#L3#Monkey Temple 4 - White Mama Monkey(1040 HP/200 EXP)#l#k");
		} else if (status == 1) {
			if (!cm.haveItem(4001431) && !cm.haveItem(4001432)) {
				cm.sendOk("You need a Golden Temple Ticket.");
			} else if (cm.getMap(950100100 + (selection * 100)).getCharactersSize() > 0) {
				cm.sendOk("There is already someone in the map.");
			} else {
				if (cm.haveItem(4001431) && !cm.haveItem(4001432)) {
					cm.gainItem(4001431, -1);
				}
				cm.warp(950100100 + (selection * 100),0);
			}
			cm.dispose();
		}
	}
}