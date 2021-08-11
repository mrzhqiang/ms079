function action(mode, type, selection) {
	if (cm.getPlayer().getLevel() < 20) {	
		if (cm.getPlayer().getSubcategory() != 1) {
			cm.sendOk("You must have selected Dual Blader in character selection to talk to me.");
		} else {
			cm.sendOk("You must have accepted the quests at level 2 and 9 to talk to me.");
		}
	} else {
		cm.sendOk("I guard the entrance to the Secret Garden... oops, not so secret anymore, is it?");
	}
	cm.safeDispose();
}