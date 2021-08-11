function enter(pi) {

    if (!pi.haveItem(4032649)) {
	pi.playerMessage("Please get the Bottle for Ancient Glacial Water first.");
    } else {
	pi.warp(921120700,0);
    } //2022698
}