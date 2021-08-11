function enter(pi) {
    if (pi.getPlayer().getInventory(pi.getInvType(-1)).findById(1003036) == null) {
	pi.playerMessage(5, "你未佩戴符咒独眼野种帽子.即将传送你出去");
	pi.warp(105050400);
	return true;
	}
	return false;
}