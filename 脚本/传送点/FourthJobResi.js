function enter(pi) {
    if (pi.getQuestStatus(23043) == 1) {
	if (pi.getQuestStatus(23046) == 2 || pi.haveItem(4032743,1)) {
		pi.warp(931000300, 0);
		return true;
	} else {
	    pi.playerMessage("Need a key card from Gelimer.");
	}
    } else if (pi.getQuestStatus(23044) == 1) {
	if (pi.getQuestStatus(23047) == 2 || pi.haveItem(4032743,1)) {
		pi.warp(931000301, 0);
		return true;
	} else {
	    pi.playerMessage("Need a key card from Gelimer.");
	}
    } else if (pi.getQuestStatus(23045) == 1) {
	if (pi.getQuestStatus(23048) == 2 || pi.haveItem(4032743,1)) {
		pi.warp(931000302, 0);
		return true;
	} else {
	    pi.playerMessage("Need a key card from Gelimer.");
	}
    }
    //if not done yet
    pi.warp(310060220,0);
}