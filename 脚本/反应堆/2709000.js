/*
	Reactor: 		PinkBeenPower
	Map(s): 		Twilight of the gods
	Description:		Summons Pink Bean
*/

function act() {
 /*   rm.killMob(8820020);
    rm.killMob(8820021);
    rm.killMob(8820022);
    rm.killMob(8820023);*/
	//rm.scheduleWarp(3600, 270050300);
    rm.killAllMob();
    rm.spawnMonster(8820008);
	if (!rm.getPlayer().isGM()) {
		rm.getMap().startSpeedRun();
	}
}