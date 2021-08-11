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
	cm.sendSimple("#b#L2#Go to Ice Ravine with a party.#l\r\n#L1#Go to Ice Ravine myself. (Quest)#l\r\n\r\n#L3#Upgrade Red Rex Earring#l\r\n#L4#Upgrade Blue Rex Earring#l\r\n#L5#Upgrade Green Rex Earring#l#k");
    } else if (status == 1) {
	if (selection == 1) {
		cm.warp(921120000, 0);
	} else if (selection == 2) {
	    if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
		cm.sendOk("The leader of the party must be here.");
	    } else {
		var party = cm.getPlayer().getParty().getMembers();
		var mapId = cm.getPlayer().getMapId();
		var next = true;
		var size = 0;
		var it = party.iterator();
		while (it.hasNext()) {
			var cPlayer = it.next();
			var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
			if (ccPlayer == null || ccPlayer.getLevel() < 120) {
				next = false;
				break;
			}
			size += (ccPlayer.isGM() ? 4 : 1);
		}	
		if (next && size >= 2) {
			var em = cm.getEventManager("Rex");
			if (em == null) {
				cm.sendOk("I don't wanna see Rex at the moment. Please try again later.");
			} else {
		    var prop = em.getProperty("state");
		    if (prop.equals("0") || prop == null) {
			em.startInstance(cm.getPlayer().getParty(), cm.getPlayer().getMap(), 200);
		    } else {
			cm.sendOk("Another party quest has already entered this channel.");
		    }
			}
		} else {
			cm.sendOk("All 2+ members of your party must be here and level 120 or greater.");
		}
	    }
	} else if (selection == 3) {
	if (cm.haveItem(1032078,1)) {
		if (!cm.canHold(1032103,1)) {
			cm.sendOk("Make room in Equip.");
		} else if (cm.haveItem(4001530,20) && cm.isGMS()) { //TODO JUMP
			cm.gainItem(1032103, 1);
			cm.gainItem(4001530, -20);
		} else {
			cm.sendOk("Come back with 20 Hobb Warrior Proof.");
		}
	} else {
	    cm.sendOk("Come back when you have Red Rex Earring.");
	}
	} else if (selection == 4) {
	if (cm.haveItem(1032079,1)) {
		if (!cm.canHold(1032104,1)) {
			cm.sendOk("Make room in Equip.");
		} else if (cm.haveItem(4001530,20) && cm.isGMS()) { //TODO JUMP
			cm.gainItem(1032104, 1);
			cm.gainItem(4001530, -20);
		} else {
			cm.sendOk("Come back with 20 Hobb Warrior Proof.");
		}
	} else {
	    cm.sendOk("Come back when you have Blue Rex Earring.");
	}
	} else if (selection == 5) {
	if (cm.haveItem(1032077,1)) {
		if (!cm.canHold(1032102,1)) {
			cm.sendOk("Make room in Equip.");
		} else if (cm.haveItem(4001530,20) && cm.isGMS()) { //TODO JUMP
			cm.gainItem(1032102, 1);
			cm.gainItem(4001530, -20);
		} else {
			cm.sendOk("Come back with 20 Hobb Warrior Proof.");
		}
	} else {
	    cm.sendOk("Come back when you have Green Rex Earring.");
	}
	}
	cm.dispose();
    }
}