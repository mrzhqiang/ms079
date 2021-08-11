
function enter(pi) {
	if (pi.getMapId() == 222020100) {
		pi.playPortalSE();
		pi.warp(222020200, "sp");
	} else { // 222020200
		pi.playPortalSE();
		pi.warp(222020100, "sp");
	}
}
