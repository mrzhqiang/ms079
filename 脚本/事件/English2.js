var letters = Array("What is Lirin's favourite animal?", "What is the main colour of the Yeti Pyramid Area?", "Please bring me the letters [REINDEER]", "Which Valentine Rose is level 48?", "How much EXP does it take for level 1-2?", "Who exchanges Vote Points in FM?", "Who is the owner of this server (HINT: A _ _ _ _ _ _) ?", "What level does a Beginner become a Magician?", "What town does an Evan start in?", "What town is the home to the Black Wings?", "What are the Wild Hunters, Battle Mages, and Mechanics?", "What type of dragon is Mir?", "Who is Mir's ancestor?", "What weapon does the Aran use?", "Who is the job instructor for Mechanic?", "What is the item needed for 3rd job advancement?", "What NPC gives you a Pokemon starter?", "What are the animals that the Wild Hunter rides?", "Complete this job class: Knights of ?", "The ranks of the potentials are: Rare, Epic, ?", "What is the name of the top statue of Pink Bean?", "What town is closest to Horntail?", "What level can you go to Zakum?");
var answers = Array("WOLF", "YELLOW", "REINDEER", "BLUE", "FIFTEEN", "PHOENIX", "AWESOME", "EIGHT", "HENESYS", "EDELSTEIN", "RESISTANCE", "ONYX", "AFRIEN", "POLEARM", "CHECKY", "DARKCRYSTAL", "GAGA", "JAGUAR", "CYGNUS", "UNIQUE", "ARIEL", "LEAFRE", "FIFTY");

function init() {
    em.setProperty("state", "0");
}

function monsterValue(eim, mobId) {
    return 1;
}

function setup() {
    em.setProperty("state", "1");

    var eim = em.newInstance("English0");
    eim.setInstanceMap(702090301).resetFully();
    eim.setInstanceMap(702090302).resetFully();
    eim.setInstanceMap(702090303).resetFully();
    var ee = java.lang.Math.floor(java.lang.Math.random() * letters.length);
    eim.setProperty("question", letters[ee]);
    eim.setProperty("answer", answers[ee]);
    eim.startEventTimer(10 * 60000); //10 mins 

    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
    player.sendEnglishQuiz(eim.getProperty("question"));
}

function playerDead(eim, player) {
}

function changedMap(eim, player, mapid) {
    switch (mapid) {
	case 702090301: // 1st Stage
	case 702090302: // 2nd Stage
	case 702090303: // 3rd Stage
	    return; // Everything is fine
    }
    eim.unregisterPlayer(player);

    if (eim.disposeIfPlayerBelow(2, 702090400)) {
	em.setProperty("state", "0");
    }
}

function playerRevive(eim, player) {
}

function playerDisconnected(eim, player) {
    return -2;
}

function leftParty(eim, player) {			
    // If only 2 players are left, uncompletable
    if (eim.disposeIfPlayerBelow(2, 702090400)) {
	em.setProperty("state", "0");
    } else {
	playerExit(eim, player);
    }
}

function disbandParty(eim) {
    // Boot whole party and end
    eim.disposeIfPlayerBelow(100, 702090400);

    em.setProperty("state", "0");
}


function scheduledTimeout(eim) {
    clearPQ(eim);
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);

    var exit = eim.getMapFactory().getMap(702090400);
    player.changeMap(exit, exit.getPortal(0));
    if (eim.disposeIfPlayerBelow(2, 702090400)) {
	em.setProperty("state", "0");
    }
}

function clearPQ(eim) {
    // KPQ does nothing special with winners
    eim.disposeIfPlayerBelow(100, 702090400);

    em.setProperty("state", "0");
}

function allMonstersDead(eim) {
}

function cancelSchedule() {
}