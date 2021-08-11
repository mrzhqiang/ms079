var eventmapid = 740000000;
var returnmap = 910000000;
var monster = Array(8820001,9400300);

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
   eim.setProperty("n_spawn", 0);
   eim.setProperty("f_spawn", 0);
   eim.setProperty("c_spawn", 0);
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
	if (monsterid == 0) {
		if (parseInt(eim.getProperty("n_spawn")) == 0) {
			monsterid = 8820015;
			eim.setProperty("n_spawn", "1");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("n_spawn")) == 1) {
			monsterid = 8820016;
			eim.setProperty("n_spawn", "2");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("n_spawn")) == 2) {
			monsterid = 8820017;
			eim.setProperty("n_spawn", "3");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("n_spawn")) == 3) {
			monsterid = 8820018;
			eim.setProperty("n_spawn", "4");
		}
	} else if (monsterid == 1) {
		if (parseInt(eim.getProperty("f_spawn")) == 0) {
			monsterid = 9400590;
			eim.setProperty("f_spawn", "1");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 1) {
			monsterid = 9400591;
			eim.setProperty("f_spawn", "2");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 2) {
			monsterid = 9400592;
			eim.setProperty("f_spawn", "3");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 3) {
			monsterid = 9400593;
			eim.setProperty("f_spawn", "4");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("f_spawn")) == 4) {
			monsterid = 9400589;
		}
	} else if (monsterid == 2) {
		if (parseInt(eim.getProperty("c_spawn")) == 0) {
			monsterid = 8850005; //stronger versions
			eim.setProperty("c_spawn", "1");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("c_spawn")) == 1) {
			monsterid = 8850006;
			eim.setProperty("c_spawn", "2");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("c_spawn")) == 2) {
			monsterid = 8850007;
			eim.setProperty("c_spawn", "3");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("c_spawn")) == 3) {
			monsterid = 8850008;
			eim.setProperty("c_spawn", "4");
			monsterSpawn(eim); //double spawn
		} else if (parseInt(eim.getProperty("c_spawn")) == 4) {
			monsterid = 8850009;
		}
	}
    var mob = em.getMonster(monsterid);
var modified = em.newMonsterStats();
modified.setOMp(mob.getMobMaxMp());
    switch (monsterid) {
	case 8820001:
	    modified.setOExp(mob.getMobExp() * 1.8);
	    modified.setOHp(mob.getMobMaxHp() * 3); //1b
		modified.setOMp(mob.getMobMaxMp() * 100);
	    break;
	case 8800100:
	case 8800101:
	case 8800102:
	case 9400289:
	    modified.setOExp(mob.getMobExp() * 2);
	    modified.setOHp(mob.getMobMaxHp() * 2.2); //1b
	    break;
	case 9400300:
	    modified.setOExp(mob.getMobExp() * 1.5); //goes stack overflow over 2.1b if too high
	    modified.setOHp(mob.getMobMaxHp() * 8); //1.2b
		modified.setOMp(mob.getMobMaxMp() * 100);
	    break;
	case 9400589:
	case 9400590:
	case 9400591:
	case 9400592:
	case 9400593:
	    modified.setOExp(mob.getMobExp() * 1.1);
	    modified.setOHp(mob.getMobMaxHp() * 1.2); //1.4b total
	    break;
	case 8850005:
	case 8850006:
	case 8850007:
	case 8850008:
	case 8850009:
	    modified.setOExp(mob.getMobExp() * 1.1);
	    modified.setOHp(mob.getMobMaxHp() * 2.3); //1.4b total
	    break;
	case 8820002:
	case 8820015:
	case 8820016:
	case 8820017:
	case 8820018:
	    modified.setOExp(mob.getMobExp() * 1.1);
	    modified.setOHp(mob.getMobMaxHp() * 1.9); //1.4b total
 	    break;
    }
	mob.setOverrideStats(modified);
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
    var num = mobnum * 600;
    var totalp = parseInt(eim.getProperty("points")) + num;

    eim.setProperty("points", totalp);

    eim.broadcastPlayerMsg(5, "你的隊伍獲得了 "+num+" 點數! 總共為 "+totalp+".");
    
    eim.saveBossQuest(num);

    if (mobnum < monster.length) {
	eim.broadcastPlayerMsg(6, "準備！下一隻的BOSS即將來臨。");
    } else {
	eim.saveBossQuest(15000);
	eim.saveNX(500);
	eim.broadcastPlayerMsg(5, "恭喜整隊挑戰普通模式成功額外獲得500 點GASH。");
	//eim.giveAchievement(22);
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
    if (mobid == 8820002 || mobid == 8820015 || mobid == 8820016 || mobid == 8820017 || mobid == 8820018) { //ariel
	eim.getMapInstance(0).killMonster(8820019);
    }
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