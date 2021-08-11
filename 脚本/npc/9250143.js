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
	        cm.sendSimple("Welcome.#b\r\n\r\n#L0#Go to Mothership - Easy (Level 30+)#l\r\n#L1#Go to Mothership - Medium (Level 60+)#l\r\n#L2#Go to Mothership - Hard(Level 120+)#l");
    	    } else if (status == 1) {
	        if (selection == 0 || selection == 1 || selection == 2) {
   		    var em = cm.getEventManager("Visitor");
    		    if (em == null) {
			cm.sendOk("Please try again later.");
			cm.dispose();
			return;
    		    }
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
				if (ccPlayer == null || ccPlayer.getLevel() < (selection == 0 ? 30 : (selection == 1 ? 60 : 120))) {
					next = false;
					break;
				}
				size += (ccPlayer.isGM() ? 2 : 1);
			}	
			if (next && size >= 2) {
		    		if (em.getInstance("Visitor" + selection) == null) {
					em.startInstance_Party("" + selection, cm.getPlayer());
		    		} else {
					cm.sendOk("Another party quest has already entered this channel.");
		    		}
			} else {
				cm.sendOk("All members of your party must be here. At least two people are needed to enter the Mothership.");
			}
		    }
		}
	        cm.dispose();
            }
}