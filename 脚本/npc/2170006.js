var status = -1;

function action(mode, type, selection) {
	    if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
		cm.playerMessage("The leader of the party must be here.");
	    } else {
		var party = cm.getPlayer().getParty().getMembers();
		var mapId = cm.getPlayer().getMapId();
		var next = true;
		var size = 0;
		var it = party.iterator();
		while (it.hasNext()) {
			var cPlayer = it.next();
			var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
			if (ccPlayer == null) {
				next = false;
				break;
			}
			size += (ccPlayer.isGM() ? 4 : 1);
		}	
		if (next && (cm.getPlayer().isGM() || size >= 2)) {
	    	    for(var i = 0; i < 10; i++) {
			if (cm.getMap(200101500 + i) != null && cm.getMap(200101500 + i).getCharactersSize() == 0) {
		    		cm.warpParty(200101500 + i);
				cm.dispose();
		    		return;
			}
	    	    }
			cm.playerMessage("Another party quest has already entered this channel.");
		} else {
			cm.playerMessage("All 2+ members of your party must be here.");
		}
	    }
	cm.dispose();
}