function enter(pi) {
	    if (pi.getPlayer().getParty() == null || !pi.isLeader()) {
		pi.playerMessage("The leader of the party must be here.");
	    } else {
		var party = pi.getPlayer().getParty().getMembers();
		var mapId = pi.getPlayer().getMapId();
		var next = true;
		var size = 0;
		var it = party.iterator();
		while (it.hasNext()) {
			var cPlayer = it.next();
			var ccPlayer = pi.getPlayer().getMap().getCharacterById(cPlayer.getId());
			if (ccPlayer == null) {
				next = false;
				break;
			}
			size += (ccPlayer.isGM() ? 4 : 1);
		}	
		if (next && (pi.getPlayer().isGM() || size >= 2)) {
	    	    for(var i = 0; i < 7; i++) {
			if (pi.getMap(pi.getMapId() + 1 + i) != null && pi.getMap(pi.getMapId() + 1 + i).getCharactersSize() == 0) {
		    		pi.warpParty(pi.getMapId() + 1 + i);
				pi.dispose();
		    		return;
			}
	    	    }
			pi.playerMessage("Another party quest has already entered this channel.");
		} else {
			pi.playerMessage("All 2+ members of your party must be here.");
		}
	    }
}