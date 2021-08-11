/*
Warp to Sharen III's Grave - Guild Quest
Give guild points if holding appropriate item and not gained already
Save location to return.

@Author Lerk
*/

function enter(pi) {
    if (pi.getPlayerStat("LVL") <= 30) {
	pi.playPortalSE();
        pi.warp(990000640, 1);
        return true;
    } else {
        pi.playerMessage("You cannot proceed past this point.");
        return false;
    }
}