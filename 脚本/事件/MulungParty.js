importPackage(java.lang);

function init() {
    // After loading, ChannelServer
}

function setup(eim, leaderid) {
    for (var i = 925020100; i <= 925020114; i++) {
	var prop = em.getProperty(String.valueOf(i));
	if (prop == null || prop.equals("not")) {
	    em.setProperty("" + i, "entered");

	    eim = em.newInstance("dojoparty" + leaderid);

	    eim.setProperty("select", String.valueOf(i));
	    return eim;
	}
    }
    eim.disposeIfPlayerBelow(100, 0);
    return eim;
}

function playerEntry(eim, player) {
    // Warp player in etc..
    var map = em.getMapFactory().getMap(parseInt(eim.getProperty("select")));
    player.changeMap(map, map.getPortal(0));
}

function changedMap(eim, player, mapid) {
    // What to do when player've changed map, based on the mapid
}

function scheduledTimeout(eim) {
    // When event timeout..

    // restartEventTimer(long time)
    // stopEventTimer()
    // startEventTimer(long time)
    // isTimerStarted()
}

function allMonstersDead(eim) {
    // When invoking unregisterMonster(MapleMonster mob) OR killed
    // Happens only when size = 0
}

function playerDead(eim, player) {
    // Happens when player dies
}

function playerRevive(eim, player) {
    // Happens when player's revived.
    // @Param : returns true/false
}

function playerDisconnected(eim, player) {
    // return 0 - Deregister player normally + Dispose instance if there are zero player left
    // return x that is > 0 - Deregister player normally + Dispose instance if there x player or below
    // return x that is < 0 - Deregister player normally + Dispose instance if there x player or below, if it's leader = boot all
}

function monsterValue(eim, mobid) {
    // Invoked when a monster that's registered has been killed
    // return x amount for this player - "Saved Points"
}

function leftParty(eim, player) {
    // Happens when a player left the party
}

function disbandParty(eim, player) {
    // Happens when the party is disbanded by the leader.
}

function clearPQ(eim) {
    // Happens when the function EventInstanceManager.finishPQ() is invoked by NPC/Reactor script
}

function removePlayer(eim, player) {
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