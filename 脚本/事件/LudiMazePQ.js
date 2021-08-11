/**
-- Odin JavaScript --------------------------------------------------------------------------------
	Ludibirum Maze PQ
-- By ---------------------------------------------------------------------------------------------
	Raz
-- Version Info -----------------------------------------------------------------------------------
	1.0 - First Version by Raz
---------------------------------------------------------------------------------------------------
**/

var exitMap;
var instanceId;
var finishMap;

function init() {
    instanceId = 1;
    em.setProperty("shuffleReactors", "true");
}

function monsterValue(eim, mobId) {
    return 1;
}

function setup() {
    exitMap = em.getChannelServer().getMapFactory().getMap(809050017);
    finishMap = em.getChannelServer().getMapFactory().getMap(809050016);
	
    var instanceName = "LudiMazePQ" + instanceId;
    var eim = em.newInstance(instanceName);
	
    instanceId++;
	
    var eventTime = 900000;
    em.schedule("timeOut", eventTime);
    eim.startEventTimer(eventTime);

    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(809050000);
    player.changeMap(map, map.getPortal(0));
	
}

function playerDead(eim, player) {
    if (player.isAlive()) { //don't trigger on death, trigger on manual revive
	if (eim.isLeader(player)) { //check for party leader
	    //boot whole party and end
	    var party = eim.getPlayers();
	    for (var i = 0; i < party.size(); i++) {
		playerExit(eim, party.get(i));
	    }
	    eim.dispose();
	} else { //boot dead player
	    playerExit(eim, player);
	}
    }
}

function playerDisconnected(eim, player) {
    if (eim.isLeader(player)) { //check for party leader
	//boot whole party and end
	var party = eim.getPlayers();
	for (var i = 0; i < party.size(); i++) {
	    if (party.get(i).equals(player)) {
		removePlayer(eim, player);
	    }
	    else {
		playerExit(eim, party.get(i));
	    }
	}
	eim.dispose();
    } else { //boot d/ced player
	removePlayer(eim, player);
    }
}

function leftParty(eim, player) {
    playerExit(eim, player);
}

function disbandParty(eim) {
    //boot whole party and end
    var party = eim.getPlayers();
    for (var i = 0; i < party.size(); i++) {
	playerExit(eim, party.get(i));
    }
    eim.dispose();
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));
}


function playerFinish(eim, player) {
    eim.unregisterPlayer(player);
    player.changeMap(finishMap, finishMap.getPortal(0));
}

//for offline players
function removePlayer(eim, player) {
    eim.unregisterPlayer(player);
    player.getMap().removePlayer(player);
    player.setMap(exitMap);
}

function clearPQ(eim) {
    var party = eim.getPlayers();
    for (var i = 0; i < party.size(); i++) {
	playerFinish(eim, party.get(i));
    }
    eim.dispose();
}

function allMonstersDead(eim) {
//do nothing; LMPQ has nothing to do with monster killing
}

function cancelSchedule() {
}

function playerRevive(eim, player) {
     
}