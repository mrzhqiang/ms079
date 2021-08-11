/**
	Witch tower - Hard Mode
**/

function init() {
    em.setProperty("goldkey", "0");
}

function playerEntry(eim, player) {
    for (var i = 980043000; i <= 980043009; i++) {
	var map = em.getMapFactory().getMap(i);
	if (map.getCharactersSize() == 0) {
	    eim = em.getInstance(player.getName());
	    eim.startEventTimer(560000);

	    map.shuffleReactors();
	    player.changeMap(map, map.getPortal(0));
	    return;
	}
    }
    playerExit(eim, player);
}

function playerExit(eim, player) {
    //    eim.unregisterPlayer(player);
    var returnmap = eim.getMapFactory().getMap(980040000);
    player.changeMap(returnmap, returnmap.getPortal(0));

    eim.dispose();
}

function scheduledTimeout(eim) {
    eim.disposeIfPlayerBelow(100, 980040000);
}

function changedMap(eim, player, mapid) {
    switch (mapid) {
	case 980043000:
	case 980043001:
	case 980043002:
	case 980043003:
	case 980043004:
	case 980043005:
	case 980043006:
	case 980043007:
	case 980043008:
	case 980043009:
	case 980043100:
	case 980043101:
	case 980043102:
	case 980043103:
	case 980043104:
	case 980043105:
	case 980043106:
	case 980043107:
	case 980043108:
	case 980043109:
	case 980043200:
	case 980043201:
	case 980043202:
	case 980043203:
	case 980043204:
	case 980043205:
	case 980043206:
	case 980043207:
	case 980043208:
	case 980043209:
	    return;
    }
    eim.unregisterPlayer(player);
    eim.dispose();
}

function playerDisconnected(eim, player) {
    return 0;
}

function clear(eim) {
    eim.dispose();
}

function cancelSchedule() {
}

function playerDead() {
}

function playerRevive(eim, player) {
}

function monsterValue(eim, mobId) {
    return 1;
}

function leftParty(eim, player) {}
function disbandParty(eim) {}