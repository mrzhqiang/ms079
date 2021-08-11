function enter(pi) {
	if(pi.isQuestActive(22005)){
		pi.playPortalSE();
		pi.warp(900020100);
	} else{
		pi.warp(100030301);
    }
	return true;
}  