function start() {
    cm.sendSimple("移动到#b#m106021500##k 要前往到哪里呢？ #b\r\n#L0#挑战雪人三兄弟 (需要组队)#l\r\n#L1#拯救菲欧娜#l#k");
}

function action(mode,type,selection) {
    if (mode == 1) {
	switch(selection) {
	    case 0:
	    if (cm.getPlayer().getParty() == null || !cm.isLeader()) {
		cm.sendOk("请找你的队长来和我说话。");
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
		if (next && (cm.getPlayer().isGM() || size >= 3)) {
	    	    for(var i = 0; i < 10; i++) {
			if (cm.getMap(106021500 + i).getCharactersSize() == 0) {
		    		cm.warpParty(106021500 + i);
				cm.dispose();
		    		return;
			}
	    	    }
			cm.sendOk("已经有另一个队伍在挑战，请稍后再尝试。");
		} else {
			cm.sendOk("队伍里需要三个人以上。");
		}
	    }
		break;
	    case 1:
		cm.warp(106021401,0);
		break;
	}
    }
    cm.dispose();
}