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
	cm.sendSimple("#b#L2#Go protect Kenta with a party.#l\r\n#L3#Kenta Goggles (50 Pianus Scale)#l\r\n#L4#Random Pet Equipment Scroll (5 Pianus Scale)#l#k");
    } else if (status == 1) {
	if (selection == 2) {
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
			var em = cm.getEventManager("Kenta");
			if (em == null) {
				cm.sendOk("Kenta is fine at the moment. Please try again later.");
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
		if (!cm.canHold(1022123,1)) {
			cm.sendOk("Make room in Equip.");
		} else if (cm.haveItem(4001535,50)) { //TODO JUMP
			cm.gainItem(1022123, 1);
			cm.gainItem(4001535, -50);
		} else {
			cm.sendOk("Come back with 50 Pianus Scale.");
		}
	} else if (selection == 4) {
		if (!cm.canHold(2048010,1) || !cm.canHold(2048011,1) || !cm.canHold(2048012,1) || !cm.canHold(2048013,1)) {
			cm.sendOk("Make room in Use.");
		} else if (cm.haveItem(4001535,5)) { //TODO JUMP
			cm.gainItem(2048010 + java.lang.Math.floor(java.lang.Math.random() * 4) | 0, 1);
			cm.gainItem(4001535, -5);
		} else {
			cm.sendOk("Come back with 5 Pianus Scale.");
		}
	}
	cm.dispose();
    }
}