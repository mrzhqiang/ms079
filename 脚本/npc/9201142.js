var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
		return;
	}
	status--;
    }
    	if (status == 0) {
	        cm.sendSimple("Hello~I am Witch Malady.#b\r\n\r\n#L0#Go to Defeat Olivia - Easy (Level 10)#l\r\n#L1#Go to Defeat Olivia - Medium (Level 30)#l\r\n#L2#Go to Defeat Olivia - Hard (Level 70)#l");
    	    } else if (status == 1) {
   		    var em = cm.getEventManager("Olivia");
    		    if (em == null) {
			cm.sendOk("Please try again later.");
			cm.dispose();
			return;
    		    }
		    if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
			cm.sendOk("The leader of the party must be here.");
		    } else {
			var s = selection;
			var party = cm.getPlayer().getParty().getMembers();
			var mapId = cm.getPlayer().getMapId();
			var next = true;
			var size = 0;
			var it = party.iterator();
			while (it.hasNext()) {
				var cPlayer = it.next();
				var ccPlayer = cm.getPlayer().getMap().getCharacterById(cPlayer.getId());
				if (ccPlayer == null || ccPlayer.getLevel() < (s == 0 ? 10 : (s == 1 ? 30 : 70))) {
					next = false;
					break;
				}
				size++;
			}	
			if (next && size >= 2) {
		    		if (em.getInstance("Olivia" + s) == null) {
					em.startInstance_Party("" + s, cm.getPlayer());
		    		} else {
					cm.sendOk("Another party quest has already entered this channel.");
		    		}
			} else {
				cm.sendOk("All members of your party must be here.");
			}
		    }
	        cm.dispose();
            }
			
}