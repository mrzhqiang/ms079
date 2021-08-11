/*
Return from Free Market Script
*/

function enter(pi) {
    var returnMap = pi.getSavedLocation("FREE_MARKET");
    pi.clearSavedLocation("FREE_MARKET");

    if (returnMap < 0) {
	returnMap = 102000000; // to fix people who entered the fm trough an unconventional way
    }
    var target = pi.getMap(returnMap);
    var portal;

    if (returnMap == 230000000) { // aquaroad has a different fm portal - maybe we should store the used portal too?
	portal = target.getPortal("market01");
    } else {
	portal = target.getPortal("market00");
    }
    if (portal == null) {
	portal = target.getPortal(0);
    }
    if (pi.getMapId() != target) {
	pi.getPlayer().changeMap(target, portal);
    }
}