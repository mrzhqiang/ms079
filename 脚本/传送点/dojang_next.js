function enter(pi) {
    if (!pi.haveMonster(9300216)) {
	pi.playerMessage("請先把怪物殺光。");
    } else {
	pi.dojoAgent_NextMap(true, false);
    }
}