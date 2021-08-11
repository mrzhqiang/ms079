/*
Leave the Cave of Life - Entrance Map and go back to the Peak of the Big Nest (240040600) should probably cancel the HT morph buff
*/

function enter(pi) {
    var morph = pi.getMorphState();
    if (morph == 2210003) {
	pi.cancelItem(2210003);
    }
    pi.playPortalSE();
    pi.warp(240040600, "east00");
}
