/*
Stage 2: Exit Door - Guild Quest

@Author Lerk
*/

function enter(pi) {
    if (pi.getMap().getReactorByName("speargate").getState() == 4) {
        pi.warp(990000401);
        return true;
    } else {
	var map = pi.getPlayer().getEventInstance().getMapFactory().getMap(990000440);
	if (map.getReactorByName("spear1").getState() >= 1 && map.getReactorByName("spear2").getState() >= 1 &&
		map.getReactorByName("spear3").getState() >= 1 && map.getReactorByName("spear4").getState() >= 1) {
		//reactors activated ....

        		pi.warp(990000401);
        		return true;
		//just in case, we aren't giving gp (:
	}
        pi.playerMessage("This way forward is not open yet.");
        return false;
    }
}