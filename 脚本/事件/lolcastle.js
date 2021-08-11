var i = 5;
var mapId = 270050100;
var returnMap;
var setupTask;

function init() {
    em.setProperty("entryPossible", "false");
    scheduleNew();
}

function scheduleNew() {
    var cal = java.util.Calendar.getInstance();
    cal.set(java.util.Calendar.HOUR, 3);
    cal.set(java.util.Calendar.MINUTE, 50);
    cal.set(java.util.Calendar.SECOND, 0);
    var nextTime = cal.getTimeInMillis();
    while (nextTime <= java.lang.System.currentTimeMillis()) {
	nextTime += 1000 * 60 * 60 * 4; // 4 hours
    }
    setupTask = em.scheduleAtTimestamp("setup", nextTime);
}

function cancelSchedule() {
    setupTask.cancel(true);
}

function setup() {
    em.setProperty("entryPossible", "false");
    returnMap = em.getChannelServer().getMapFactory().getMap(101000000);
    var createInstances = new Array(	"lolcastle1",
	"lolcastle2",
	"lolcastle3",
	"lolcastle4",
	"lolcastle5");
    var x;
    for (x = 0; x < createInstances.length; x++) {
	var eim = em.newInstance(createInstances[x]);
	var map = eim.createInstanceMap(mapId);
	map.toggleDrops();
    }
    for (x = 0; x < 5; x++) {
	em.schedule("announce", 5 * 60000 + x * 60000);
    }
    em.schedule("mesoDistribution", 5 * 60000 + x * 60000);
    em.schedule("start", 5 * 60000 + (x + 1) * 60000);
    em.schedule("timeOut", 11 * 60000 + 1200000);
}

function announce() {
    em.setProperty("entryPossible", "true");
    if (i == 0) i = 5;
    em.getChannelServer().broadcastPacket(tools.MaplePacketCreator.serverNotice(6, "[Event] Field of Judgement will open in " + i + " minutes"));
    i--;
}

function mesoDistribution() {
    em.setProperty("entryPossible", "false");
    var iter = em.getInstances().iterator();
    while (iter.hasNext()) {
	var eim = iter.next();
	if (eim.getPlayerCount() > 0) {
	    var meso = eim.getPlayerCount() * 500000;
	    var randWinner = Math.floor(Math.random() * eim.getPlayerCount());
	    var winner = eim.getPlayers().get(randWinner);
	    var map = eim.getMapInstance(mapId);
	    map.broadcastMessage(tools.MaplePacketCreator.serverNotice(6, "[Event] " + winner.getName() + " wins " + meso + " meso"));
	    winner.gainMeso(meso, true, true, true);
	}
    }
}

function start() {
    scheduleNew();
    em.getChannelServer().broadcastPacket(
	tools.MaplePacketCreator.serverNotice(6, "[Event] Field of Judgement is now open"));
    var iter = em.getInstances().iterator();
    while (iter.hasNext()) {
	var eim = iter.next();
	if (eim.getPlayerCount() > 0) {
	    startInstance(eim);
	}
    }
}

function randX() {
    return -600 + Math.floor(Math.random() * 1800);
}

function startInstance(eim) {
    if (eim.getPlayerCount() > 0) {
	var iter = eim.getPlayers().iterator();
	while (iter.hasNext()) {
	    var player = iter.next();
	    player.getClient().getSession().write(tools.MaplePacketCreator.getClock(1200));
	}
	var map = eim.getMapInstance(mapId);
	if (eim.getName().equals("lolcastle1")) {
	    for (var x = 0; x < 100; x++) {
		var mob = em.getMonster(9500169);
		var overrideStats = new server.life.MapleMonsterStats();
		overrideStats.setHp(mob.getHp() * 3);
		overrideStats.setExp(mob.getExp() / 4);
		overrideStats.setMp(mob.getMaxMp());
		mob.setOverrideStats(overrideStats);
		mob.setHp(mob.getHp() * 3);
		eim.registerMonster(mob);

		map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(randX(), 100));
	    }
	    for (var x = 0; x < 10; x++) {
		var mob = em.getMonster(9300136);
		var overrideStats = new server.life.MapleMonsterStats();
		overrideStats.setHp(15000);
		overrideStats.setExp(mob.getExp());
		overrideStats.setMp(mob.getMaxMp());
		mob.setOverrideStats(overrideStats);
		mob.setHp(15000);
		eim.registerMonster(mob);

		map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(randX(), 100));
	    }
	} else if (eim.getName().equals("lolcastle2")) {
	    for (var x = 0; x < 100; x++) {
		var mob = em.getMonster(5120000);
		var overrideStats = new server.life.MapleMonsterStats();
		overrideStats.setHp(30000);
		overrideStats.setExp(mob.getExp() / 4);
		overrideStats.setMp(mob.getMaxMp());
		mob.setOverrideStats(overrideStats);
		mob.setHp(30000);
		eim.registerMonster(mob);

		map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(randX(), 100));
	    }
	    for (var x = 0; x < 3; x++) {
		var mob = em.getMonster(9300012);
		var overrideStats = new server.life.MapleMonsterStats();
		overrideStats.setHp(mob.getHp());
		overrideStats.setExp(mob.getExp());
		overrideStats.setMp(mob.getMaxMp());
		mob.setOverrideStats(overrideStats);
		eim.registerMonster(mob);

		map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(randX(), 100));
	    }
	} else if (eim.getName().equals("lolcastle3")) {
	    for (var x = 0; x < 100; x++) {
		var mobId;
		if (x < 33) {
		    mobId = 7130400;
		} else if (x < 66) {
		    mobId = 7130401;
		} else {
		    mobId = 7130402;
		}
		var mob = em.getMonster(mobId);
		var overrideStats = new server.life.MapleMonsterStats();
		overrideStats.setHp(mob.getHp() * 2);
		overrideStats.setExp(mob.getExp() / 4);
		overrideStats.setMp(mob.getMaxMp());
		mob.setOverrideStats(overrideStats);
		eim.registerMonster(mob);

		map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(randX(), 100));
	    }
	    for (var x = 0; x < 3; x++) {
		var mob = em.getMonster(9300105);
		var overrideStats = new server.life.MapleMonsterStats();
		overrideStats.setHp(mob.getHp());
		overrideStats.setExp(mob.getExp() / 4);
		overrideStats.setMp(mob.getMaxMp());
		mob.setOverrideStats(overrideStats);
		eim.registerMonster(mob);

		map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(randX(), 100));
	    }
	} else if (eim.getName().equals("lolcastle4")) {
	    for (var x = 0; x < 78; x++) {
		var mobId;
		if (x < 25) {
		    mobId = 7130010;
		} else if (x < 50) {
		    mobId = 7130001;
		} else if (x < 75) {
		    mobId = 9500134;
		} else {
		    mobId = 9300039;
		}
		var mob = em.getMonster(mobId);
		var overrideStats = new server.life.MapleMonsterStats();
		if (x >= 75) {
		    overrideStats.setExp(mob.getExp());
		    overrideStats.setHp(mob.getHp());
		} else {
		    overrideStats.setExp(mob.getExp() / 4);
		    overrideStats.setHp(mob.getHp() * 2);
		}
		overrideStats.setMp(mob.getMaxMp());
		mob.setOverrideStats(overrideStats);
		if (x < 75) {
		    mob.setHp(mob.getHp() * 2);
		} else {
		    mob.setHp(mob.getHp());
		}
		eim.registerMonster(mob);

		map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(randX(), 100));
	    }
	} else if (eim.getName().equals("lolcastle5")) {
	    for (var x = 0; x < 90; x++) {
		var mobId;
		if (x < 30) {
		    mobId = 8141100;
		} else if (x < 60) {
		    mobId = 8140600;
		} else {
		    mobId = 8150100;
		}
		var mob = em.getMonster(mobId);
		var overrideStats = new server.life.MapleMonsterStats();
		overrideStats.setHp(mob.getHp());
		overrideStats.setExp(mob.getExp() / 4);
		overrideStats.setMp(mob.getMaxMp());
		mob.setOverrideStats(overrideStats);
		eim.registerMonster(mob);

		map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(randX(), 100));
	    }
	    for (var x = 0; x < 3; x++) {
		var mob = em.getMonster(9300152);
		var overrideStats = new server.life.MapleMonsterStats();
		overrideStats.setHp(2000000);
		overrideStats.setExp(mob.getExp());
		overrideStats.setMp(mob.getMaxMp());
		mob.setOverrideStats(overrideStats);
		mob.setHp(2000000);
		eim.registerMonster(mob);

		map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(randX(), 100));
	    }
	}

    }
}

function monsterValue(eim, mobId) {
    switch(mobId) {
	case 9300152: // Angry Franken Lloyd
	    return 15;
	case 9300039: // papa pixie (lolcastle4)
	    return 15;
	case 9300105: // Angry Lord Pirate
	    return 15;
	case 9300012: // Alishar
	    return 15;
	case 9300136: //rombot
	    return 5;
	default:
	    return 1;
    }
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(mapId);
    player.changeMap(map, map.getPortal(0));
}

function playerDead(eim, player) {
    player.setHp(1);
    player.changeMap(returnMap, returnMap.getPortal(0));
    eim.unregisterPlayer(player);
    player.getClient().getSession().write(tools.MaplePacketCreator.serverNotice(1, "You lost with " + eim.getKillCount(player) + " points."));
}

function playerDisconnected(eim, player) {
    eim.unregisterPlayer(player);
    player.getMap().removePlayer(player);
    player.setMap(returnMap);
}

function allMonstersDead(eim) {
    if (eim.getPlayerCount() > 1) {
	var maxKC = 0;
	var maxPlayer = null;
	var iter = eim.getPlayers().iterator();
	while (iter.hasNext()) {
	    var curPlayer = iter.next();
	    if (eim.getKillCount(curPlayer) <= maxKC) {
		playerDead(eim, curPlayer);
	    } else if (eim.getKillCount(curPlayer) > maxKC) {
		if (maxPlayer != null) {
		    playerDead(eim, maxPlayer);
		}
		maxPlayer = curPlayer;
		maxKC = eim.getKillCount(maxPlayer);
	    }
	}
    }
    // lolo nur noch der gewinner da
    var map = eim.getMapInstance(mapId);
    var ii = server.MapleItemInformationProvider.getInstance();
    var priceRand = 1 + Math.floor(Math.random() * 100);
    var price;
    if (priceRand <= 35) {
	// cash weapon
	var weaponId;
	do {
	    weaponId = 1702000 + Math.floor(Math.random() * 176);
	} while(ii.getSlotMax(weaponId) == 0);
	price = ii.getEquipById(weaponId);
    } else if (priceRand > 35 && priceRand <= 37) {
	// uberweapons
	var uberWeapons;
	if (eim.getName().equals("lolcastle1") || eim.getName().equals("lolcastle2")) {
	    // not so uber (lv50 weapons)
	    uberWeapons = new Array(1452008,
		1472021,
		1462007,
		1332054,
		1312008,
		1322021,
		1302010,
		1442046,
		1432004,
		1382011,
		1412003,
		1422005,
		1402013,
		1372011);
	} else {
	    // lv100 weapons
	    uberWeapons = new Array(1452019,
		1472053,
		1462015,
		1332051,
		1312030,
		1322045,
		1302056,
		1442044,
		1432030,
		1382035,
		1412021,
		1422027,
		1402037,
		1372010,
		1302026, // black umbrella
		1102041 //pink adventurer cape
		);
	}
	var uberRand = Math.floor(Math.random() * uberWeapons.length);
	var weaponId = uberWeapons[uberRand];
	price = ii.randomizeStats(ii.getEquipById(weaponId));
    } else if (priceRand > 37 && priceRand <= 45) {
	// 30% scroll
	var scrolls30;
	/*
			none of these are available yet
			2040103, // face accessory hp
			2040108, // face accessory avoid
			2040203, // eye accessory acc
			2040208, // eye accessory int
		*/
	if (eim.getName().equals("lolcastle1") || eim.getName().equals("lolcastle2")) {
	    scrolls30 = new Array(
		2040009, // helmet def
		2040011, // helmet hp
		2040309, // earring def
		2040405, // topwear def
		2040409, // topwear hp
		2040511, // overall def
		2040605, // bottomwear def
		2040607, // bottomwear jump
		2040609, // bottomwear hp
		2040611, // bottomwear dex
		2040713, // shoes dex
		2040717, // shoed speed
		2040809, // gloves dex
		2040813, // gloves hp
		2040905, // shield def
		2040909, // shield hp
		2041027, // cape mdef
		2041029, // cape def
		2041031, // cape hp
		2041033, // cape mp
		// 70% scrolls that don't suck
		2041034, // cape str
		2041036, // cape int
		2041038, // cape dex
		2041040, // cape luk
		2043004, // 1h-sword att
		2043104, // 1h-axe att
		2043204, // 1h-bw att
		2043304, // dagger att
		2043704, // wand matt
		2043804, // staff matt
		2044004, // 2h-sword att
		2044104, // 2h-axe att
		2044204, // 2h-bw att
		2044304, // spear att
		2044404, // pole arm att
		2044504, // bow att
		2044604, // crossbow att
		2044704 // claw att
		);
	} else {
	    scrolls30 = new Array(
		2040013, // helmet int
		2040015, // helmet acc
		2040307, // earring dex
		2044705, // claw att
		2044505, // bow att
		2043305, // dagger att
		2044605, // crossbow att
		2040407, // topwear str
		2040411, // topwear luk
		2040907, // shield luk
		2041035, // cape str
		2041037, // cape int
		2041039, // cape dex
		2041041, // cape luk
		2043105, // one-handed axe att
		2044105, // two-handed axe att
		2043205, // one-handed bw att
		2044205, // two-handed bw att
		2043005, // one-handed sword att
		2044005, // two-handed sword att
		2044405, // pole arm att
		2044305, // spear att
		2043805, // staff matt
		2043705, // wand matt
		2040715, // shoes jump
		2040509, // overall dex
		2040519, // overall int
		2040521, // overall luk
		2040811, // gloves att
		2040815, // gloves matt
		2040305, // earring int
		2040917, // shield att
		2040922, // shield matt
		2043007 // sword matt
		);
	}
	var scrollRand = Math.floor(Math.random() * scrolls30.length);
	price = new client.Item(scrolls30[scrollRand], 0, 1, 0);
    } else if (priceRand > 45 && priceRand <= 70) {
	// powerups
	var powerUps = new Array(new Array(2022273, 3),
	    new Array(2022245, 3),
	    new Array(2022179, 1));
	var powerRand = Math.floor(Math.random() * powerUps.length);
	price = new client.Item(powerUps[powerRand][0], 0, powerUps[powerRand][1], 0);
    } else if (priceRand > 70 && priceRand <= 75) {
	// throwing stars
	var starId = 2070003 + Math.floor(Math.random() * 14);
	price = new client.Item(starId, 0, 1, 0);
    } else if (priceRand > 75 && priceRand <= 95) {
	// 60% scroll
	var scrolls60 = new Array(	2044701, // claw att
	    2044501, // bow att
	    2043301, // dagger att
	    2044601, // crossbow att
	    2043101, // one-handed axe att
	    2044101, // two-handed axe att
	    2043201, // one-handed bw att
	    2044201, // two-handed bw att
	    2043001, // one-handed sword att
	    2044001, // two-handed sword att
	    2044401, // pole arm att
	    2044301, // spear att
	    2043801, // staff matt
	    2043701, // wand matt
	    2040704, // shoes jump
	    2040501, // overall dex
	    2040513, // overall int
	    2040516, // overall luk
	    2040804, // gloves att
	    2040801, // gloves dex
	    2040301 // earring int
	    );
	var scrollRand = Math.floor(Math.random() * scrolls60.length);
	price = new client.Item(scrolls60[scrollRand], 0, 1, 0);
    } else {
	// 40% scroll
	var scrolls40 = new Array(
	    2040315, // earring int
	    2040912, // shield def
	    2040313, // one-handed sword att
	    2043108, // one-handed axe att
	    2043208, // one-handed bw att
	    2043308, // dagger att
	    2043708, // wand matt
	    2043808, // staff matt
	    2044008, // two-handed sword att
	    2044108, // two-handed axe att
	    2044208, // two-handed bw att
	    2044308, // spear att
	    2044408, // pole arm att
	    2044508, // bow att
	    2044608, // crossbow att
	    2044708 // claw att
	    );
	var scrollRand = Math.floor(Math.random() * scrolls40.length);
	price = new client.Item(scrolls40[scrollRand], 0, 1, 0);
    }
	
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
	var winner = iter.next();
	winner.getClient().getSession().write(tools.MaplePacketCreator.serverNotice(1, "You win with " + eim.getKillCount(winner) + " points. You will be warped out in 2 minutes."));
	winner.getClient().getSession().write(tools.MaplePacketCreator.getClock(120));
	eim.saveWinner(winner);
    }
    var winner = eim.getPlayers().get(0);
    map.spawnItemDrop(winner, winner, price, winner.getPosition(), true, false);

    eim.schedule("warpWinnersOut", 120000);
}

function timeOut() {
    var iter = em.getInstances().iterator();
    while (iter.hasNext()) {
	var eim = iter.next();
	if (eim.getPlayerCount() > 0) {
	    var pIter = eim.getPlayers().iterator();
	    while (pIter.hasNext()) {
		playerDead(eim, pIter.next());
	    }
	}
	eim.dispose();
    }
}

function warpWinnersOut(eim) {
    var iter = eim.getPlayers().iterator();
    while (iter.hasNext()) {
	var player = iter.next();
	player.changeMap(returnMap, returnMap.getPortal(0));
	eim.unregisterPlayer(player);
    }
    eim.dispose();
}

function leftParty(eim, player) {
	
}

function disbandParty(eim, player) {

}

// can't happen
function playerRevive(eim, player) {

}