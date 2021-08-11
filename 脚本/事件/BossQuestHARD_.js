var eventmapid = 740000000;
var returnmap = 910000000;

var monster = new Array(
    8220004, // Dodo
    8220005, // Lillinof
    8220006, // Raika
    9400121, // Female Boss
    9400405, // Samurai
    9420549, // furious Scarlion boss
    9420544, // Furious Targa
    8800002 // Zakum 3
    );

function init() {
// After loading, ChannelServer
}

function setup(partyid) {
    var instanceName = "BossQuest" + partyid;

    var eim = em.newInstance(instanceName);
    // If there are more than 1 map for this, you'll need to do mapid + instancename
    var map = eim.createInstanceMapS(eventmapid);
    map.toggleDrops();
    map.spawnNpc(9209101, new java.awt.Point(854, -24));

    eim.setProperty("points", 0);
    eim.setProperty("monster_number", 0);

    beginQuest(eim);
    return eim;
}

function beginQuest(eim) { // Custom function
    if (eim != null) {
    	eim.startEventTimer(5000); // After 5 seconds -> scheduledTimeout()
    }
}
function monsterSpawn(eim) { // Custom function
    var monsterid = monster[parseInt(eim.getProperty("monster_number"))];
    var mob = em.getMonster(monsterid);

    switch (monsterid) {
	case 8220004: // Dodo
	case 8220005: // Lillinof
	case 8220006: // Rakia
	    var modified = em.newMonsterStats();
	    modified.setOExp(mob.getMobExp() * 7);
	    modified.setOHp(mob.getMobMaxHp() * 4);
	    modified.setOMp(mob.getMobMaxMp());

	    mob.setOverrideStats(modified);
	    break;
	case 8300006: // Dragonoir
	    var modified = em.newMonsterStats();
	    modified.setOExp(mob.getMobExp() * 0.5);
	    modified.setOHp(mob.getMobMaxHp() * 0.7);
	    modified.setOMp(mob.getMobMaxMp());

	    mob.setOverrideStats(modified);
	    break;
	case 9400121: // Anego
	    var modified = em.newMonsterStats();
	    modified.setOExp(mob.getMobExp() * 1.4);
	    modified.setOHp(mob.getMobMaxHp() * 2);
	    modified.setOMp(mob.getMobMaxMp());

	    mob.setOverrideStats(modified);
	    break;
	case 9400405: // Samurai
	    var modified = em.newMonsterStats();
	    modified.setOExp(mob.getMobExp() * 1.4);
	    modified.setOHp(mob.getMobMaxHp() * 3);
	    modified.setOMp(mob.getMobMaxMp());

	    mob.setOverrideStats(modified);
	    break;
	case 9420549: // Scarlion
	case 9420544: // Targa
	    var modified = em.newMonsterStats();
	    modified.setOExp(mob.getMobExp() * 0.8);
	    modified.setOHp(mob.getMobMaxHp() * 1.8);
	    modified.setOMp(mob.getMobMaxMp());

	    mob.setOverrideStats(modified);
	    break;
	case 8800002: // Zakum 3
	    var modified = em.newMonsterStats();
	    modified.setOExp(mob.getMobExp() * 1.2);
	    modified.setOHp(mob.getMobMaxHp() * 3);
	    modified.setOMp(mob.getMobMaxMp());

	    mob.setOverrideStats(modified);
	    break;
    }
    eim.registerMonster(mob);

    var map = eim.getMapInstance(0);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(-191, 261));
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function changedMap(eim, player, mapid) {
    if (mapid != eventmapid) {
	eim.unregisterPlayer(player);

	eim.disposeIfPlayerBelow(0, 0);
    }
}

function scheduledTimeout(eim) {
    var num = parseInt(eim.getProperty("monster_number"));
    if (num < monster.length) {
	monsterSpawn(eim);
	eim.setProperty("monster_number", num + 1);
    } else {
	eim.disposeIfPlayerBelow(100, returnmap);
    }
// When event timeout..

// restartEventTimer(long time)
// stopEventTimer()
// startEventTimer(long time)
// isTimerStarted()
}

function allMonstersDead(eim) {
    eim.restartEventTimer(3000);

    var mobnum = parseInt(eim.getProperty("monster_number"));
    var num = mobnum * 85;
    var totalp = parseInt(eim.getProperty("points")) + num;

    eim.setProperty("points", totalp);

    eim.broadcastPlayerMsg(5, "你的隊伍獲得了 "+num+" 點數! 總共為 "+totalp+".");
    
    eim.saveBossQuest(num);

    if (mobnum < monster.length) {
	eim.broadcastPlayerMsg(6, "準備！下一隻的BOSS即將來臨。");
} else {
	eim.saveBossQuest(3000);
	eim.saveNX(300);
	eim.broadcastPlayerMsg(5, "恭喜整隊挑戰普通模式成功額外獲得300 GASH點。");
    }
// When invoking unregisterMonster(MapleMonster mob) OR killed
// Happens only when size = 0
}

function playerDead(eim, player) {
// Happens when player dies
}

function playerRevive(eim, player) {
    return true;
// Happens when player's revived.
// @Param : returns true/false
}

function playerDisconnected(eim, player) {
    return 0;
// return 0 - Deregister player normally + Dispose instance if there are zero player left
// return x that is > 0 - Deregister player normally + Dispose instance if there x player or below
// return x that is < 0 - Deregister player normally + Dispose instance if there x player or below, if it's leader = boot all
}

function monsterValue(eim, mobid) {
    return 0;
// Invoked when a monster that's registered has been killed
// return x amount for this player - "Saved Points"
}

function leftParty(eim, player) {
    // Happens when a player left the party
    eim.unregisterPlayer(player);

    var map = em.getMapFactory().getMap(returnmap);
    player.changeMap(map, map.getPortal(0));

    eim.disposeIfPlayerBelow(0, 0);
}

function disbandParty(eim, player) {
    // Boot whole party and end
    eim.disposeIfPlayerBelow(100, returnmap);
}

function clearPQ(eim) {
// Happens when the function EventInstanceManager.finishPQ() is invoked by NPC/Reactor script
}

function removePlayer(eim, player) {
    eim.dispose();
// Happens when the funtion NPCConversationalManager.removePlayerFromInstance() is invoked
}

function registerCarnivalParty(eim, carnivalparty) {
// Happens when carnival PQ is started. - Unused for now.
}

function onMapLoad(eim, player) {
// Happens when player change map - Unused for now.
}

function cancelSchedule() {
}