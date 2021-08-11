/**
	Witch tower - Easy Mode
**/

function init() {
    em.setProperty("goldkey", "0");
}

function playerEntry(eim, player) {    
    for (var i = 980041000; i <= 980041009; i++) {
	var map = em.getMapFactory().getMap(i);
	if (map.getCharactersSize() == 0) {
	    eim = em.getInstance(player.getName());
	    eim.startEventTimer(180000);

	    map.shuffleReactors();
	    player.changeMap(map, map.getPortal(0));
	    return;
	}
    }
    playerExit(eim, player);
}

function playerExit(eim, player) {
    //    eim.unregisterPlayer(player);
    var returnmap = em.getMapFactory().getMap(980040000);
    player.changeMap(returnmap, returnmap.getPortal(0));

    eim.dispose();
}

function scheduledTimeout(eim) {
    eim.disposeIfPlayerBelow(100, 980040000);
}

function changedMap(eim, player, mapid) {
    switch (mapid) {
	case 980041000:
	case 980041001:
	case 980041002:
	case 980041003:
	case 980041004:
	case 980041005:
	case 980041006:
	case 980041007:
	case 980041008:
	case 980041009:
	case 980041100:
	case 980041101:
	case 980041102:
	case 980041103:
	case 980041104:
	case 980041105:
	case 980041106:
	case 980041107:
	case 980041108:
	case 980041109:
	case 980041200:
	case 980041201:
	case 980041202:
	case 980041203:
	case 980041204:
	case 980041205:
	case 980041206:
	case 980041207:
	case 980041208:
	case 980041209:
	    return;
    }
    //    eim.unregisterPlayer(player);
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

function disbandParty(eim) {}