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
	cm.sendSimple("#b#L0#Give me Bottle of Ancient Glacial Water.#l\r\n#L1#Go to Ice Ravine by myself.#l\r\n#L2#Go to Ice Ravine with a party.#l#k");
    } else if (status == 1) {
	if (selection == 0) {
	    if (!cm.haveItem(4032649) && !cm.haveItem(2022698)) {
		cm.gainItem(4032649,1);
	    } else {
		cm.sendOk("You have this already.");
	    }
	} else if (selection == 1){
	    cm.warp(921120705,0);
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
			if (ccPlayer == null || ccPlayer.getLevel() < 75) {
				next = false;
				break;
			}
			size += (ccPlayer.isGM() ? 4 : 1);
		}	
		if (next && size >= 3) {
			var em = cm.getEventManager("Rex");
			if (em == null) {
				cm.sendOk("I don't wanna see Rex at the moment. Please try again later.");
			} else {
				em.startInstance(cm.getPlayer().getParty(), cm.getPlayer().getMap());
			}
		} else {
			cm.sendOk("All 3+ members of your party must be here and level 75 or greater.");
		}
	    }
	}
	cm.dispose();
    }
}