
function init() {
    em.setProperty("state", "0");
}

function setup(eim) {
    em.setProperty("state", "1");
    var eim = em.newInstance("DollHouse");
    var map = eim.setInstanceMap(922000010);
    map.shuffleReactors();
    eim.startEventTimer(600000);
    return eim;
}

function playerEntry(eim, player) {
    var map = em.getMapFactory().getMap(922000010);
    player.changeMap(map, map.getPortal(0));
}

function changedMap(eim, player, mapid) {
    switch (mapid) {
	case 103000800: // 1st Stage
	case 103000801: // 2nd Stage
	case 103000802: // 3rd Stage
	case 103000803: // 4th Stage
	case 103000804: // 5th Stage
    	case 103000805: // Bonus stage
	    return;
    }
    eim.unregisterPlayer(player);
}

function playerExit(eim, player) {
    clear(eim);
}

function scheduledTimeout(eim) {
    clear(eim);
}

function playerDisconnected(eim, player) {
    em.setProperty("state", "0");
    player.getMap().removePlayer(player);
    player.setMap(em.getChannelServer().getMapFactory().getMap(221024400));
    eim.unregisterPlayer(player);
    eim.dispose();
}

function clear(eim) {
    var map = eim.getChannelServer().getMapFactory().getMap(221024400);
    em.setProperty("state", "0");
    if( eim.getPlayers().legnth != 0) {
        try {
            var player = eim.getPlayers().get(0);
            player.changeMap(map, map.getPortal(0));
            eim.unregisterPlayer(player);
        } catch (e) {
        }
    }
    eim.dispose();
}

function cancelSchedule() {
}
