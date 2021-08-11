/**
	Advancement - Cygnus 3rd job
**/

var instanceId;
var returnmap;

function init() {
}

function monsterValue(eim, mobId) {
    return 1;
}

function setup() {
    returnmap = em.getChannelServer().getMapFactory().getMap(130010100);
    var instanceName = "enterDisguise3" + instanceId;
    var eim = em.newInstance(instanceName);
    eim.createInstanceMap(108010620);

    instanceId++;

    var eventtime = 600000;
    em.schedule("timeOut", eventtime);
//    eim.startEventTimer(eventtime);

    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(108010620, 0);
    map.addMapTimer(3 * 60);
    player.changeMap(map, map.getPortal(0));
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));

    if (eim.getPlayerCount() == 0) {
	eim.dispose();
    }
}

function timeOut() {
    var iter = em.getInstances().iterator();
    while (iter.hasNext()) {
	var eim = iter.next();
	if (eim.getPlayerCount() > 0) {
	    var pIter = eim.getPlayers().iterator();
	    while (pIter.hasNext()) {
		playerExit(eim, pIter.next());
	    }
	}
	eim.dispose();
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function clear(eim) {
    var player = eim.getPlayers().get(0);
    player.changeMap(returnMap, returnMap.getPortal(0));
    eim.unregisterPlayer(player);
    em.cancel();
    em.disposeInstance("enterDisguise3");
    eim.dispose();
}

function cancelSchedule() {
}