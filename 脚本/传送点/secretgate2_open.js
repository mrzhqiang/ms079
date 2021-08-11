/*
Stage 4: Mark of Evil Door - Guild Quest

@Author Lerk
*/

function enter(pi) {
    if (pi.getMap().getReactorByName("secretgate2").getState() == 1) {
	pi.playPortalSE();
	pi.warp(990000631,1);
	return true;
    } else {
	pi.playerMessage("This door is closed.");
	return false;
    }
}
