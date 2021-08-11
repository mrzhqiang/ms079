function enter(pi) {
	if(pi.isQuestActive(22008)){
		pi.warp(100030103, "west00");
	} else {
		pi.playerMessage("You cannot go to the Back Yard without a reason");
    } 
	return true;
}  