/*
Zakum Altar - Summons Zakum.
*/

function act() {
    rm.changeMusic("Bgm06/FinalFight");
	rm.getMap().spawnChaosZakum(-10, -215);
    rm.mapMessage("混沌殘暴炎魔被火焰之眼的力量召喚出來了。");
	if (!rm.getPlayer().isGM()) {
		rm.getMap().startSpeedRun();
	}
}
