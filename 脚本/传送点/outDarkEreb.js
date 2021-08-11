function enter(pi) {
    if (pi.isQuestFinished(20407)) {
	pi.warp(130000000,0);
    } else {
	pi.playerMessage(5, "I have to defeat the Black Witch first!");
    }
}