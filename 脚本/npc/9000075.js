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
			cm.sendSimple("Hello. Welcome to the Goblin Temple Entrance. Where would you like to go? #rYou need a Golden Temple Ticket to enter. All these monsters drop Sunbursts, required to get into the boss Ravana.#k\r\n\r\n#b#L0#Goblin Temple 1 - Blue Goblin(2200 HP/170 EXP)#l\r\n#L1#Goblin Temple 2 - Red Goblin(4150 HP/336 EXP)#l\r\n#L2#Goblin Temple 3 - Stone Goblin(9300 HP/501 EXP)#l#k");
		} else if (status == 1) {
			if (!cm.haveItem(4001431) && !cm.haveItem(4001432)) {
				cm.sendOk("You need a Golden Temple Ticket.");
			} else if (cm.getMap(950100500 + (selection * 100)).getCharactersSize() > 0) {
				cm.sendOk("There is already someone in the map.");
			} else {
				if (cm.haveItem(4001431) && !cm.haveItem(4001432)) {
					cm.gainItem(4001431, -1);
				}
				cm.warp(950100500 + (selection * 100),0);
			}
			cm.dispose();
		}
	}
}