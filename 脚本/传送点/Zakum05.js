/*
    Zakum Entrance
*/

function enter(pi) {
    if (pi.getQuestStatus(100200) != 2) {
	pi.playerMessage(5, "您好像还沒准备好面对BOSS。");
	return false;

    } else if (!pi.haveItem(4001017)) {
	pi.playerMessage(5, "由于你沒有火焰之眼，所以不能挑战扎昆。");
	return false;
    }
    
    pi.playPortalSE();
    pi.warp(pi.getPlayer().getMapId() + 100, "west00");
    return true;
}