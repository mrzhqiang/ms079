/* Author: Xterminator
	Map(s): 		Hidden Street : Pirate Test Room (108000502)
	Description: 		
*/
function init() {
}

function playerEntry(eim, player) {
    var map = eim.setInstanceMap(912040000);
    player.changeMap(map, map.getPortal(0));
    player.getClient().getSession().write(tools.MaplePacketCreator.getClock(600));
    eim.schedule("warpOut", 600000);
}

function warpOut(eim) {
    var map = em.getChannelServer().getMapFactory().getMap(120000101);
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
	player = iter.next();
	player.changeMap(map, map.getPortal(0));
	eim.unregisterPlayer(player);
    }
    eim.dispose();
}

function playerDisconnected(eim, player) {
    return 0;
}

function playerDead(eim, player) {
}

function playerRevive(eim, player) {
    eim.unregisterPlayer(player);
    eim.dispose();
}

function leftParty(eim, player) {			
}

function disbandParty(eim) {
}

function cancelSchedule() {
}