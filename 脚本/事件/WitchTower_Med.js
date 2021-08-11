/**
	Witch tower - Med Mode
**/

function init() {
    em.setProperty("goldkey", "0");
}

function playerEntry(eim, player) {
    for (var i = 980042000; i <= 980042009; i++) {
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

function scheduledTimeout_chr(eim, chr) {
    var returnmap = em.getMapFactory().getMap(980040000);

    eim.unregisterPlayer(chr);
    chr.changeMap(returnmap, returnmap.getPortal(0));
    eim.dispose();
}

function changedMap(eim, player, mapid) {
    switch (mapid) {
	case 980042000:
	case 980042001:
	case 980042002:
	case 980042003:
	case 980042004:
	case 980042005:
	case 980042006:
	case 980042007:
	case 980042008:
	case 980042009:
	case 980042100:
	case 980042101:
	case 980042102:
	case 980042103:
	case 980042104:
	case 980042105:
	case 980042106:
	case 980042107:
	case 980042108:
	case 980042109:
	case 980042200:
	case 980042201:
	case 980042202:
	case 980042203:
	case 980042204:
	case 980042205:
	case 980042206:
	case 980042207:
	case 980042208:
	case 980042209:
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