// Captain Battleship quest

function enter(pi) {
    var pt = pi.getEventManager("KyrinTrainingGroundC");
    if (pt == null) {
	pi.warp(120000101, 0);
    } else {
	if (pt.getInstance("KyrinTrainingGroundC").getTimeLeft() < 120000) { // 2 minutes left
		pi.gainItem(4031059, 1);
	    pi.warp(912010200, 0);
		return true;
	} else {
	    pi.playerMessage("必須再忍耐一下子!");
	    return false;
	}
    }
    return true;
}