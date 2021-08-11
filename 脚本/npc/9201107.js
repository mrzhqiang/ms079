/* @Author SharpAceX
*/

function start() {
if (cm.getPlayer().getMap().getId() == 610030500) {
        cm.sendOk("Unbelievable strength and power, anyone can be achieve. But what makes a warrior special is their iron will. No matter the odds, a true warrior pushes through until victory is assured. Thus, the Warrior Chamber is a brutal road where the room itself is against you, as well as the ultra-strong monsters within. Use your skills to shake off the effects and defeat the monsters within to reach the Warrior Statue and claim the Master Sword. Good luck!");
cm.dispose();
} else if (cm.getPlayer().getMap().getId() == 610030000) {
cm.sendOk("A legendary family of heroes, the de Vrisiens are the original founders of the Stormcasters. The family is unique, as each son or daughter inherits the full fighting techniques of their ancestors. This ability has proven to be immensely useful; as it allows for nearly unlimited strategy, improvisation and tactics to defeat all enemies. A true family for the generations.");
cm.dispose();
} else if (cm.getPlayer().getMapId() == 610030510) {
	if (cm.getPlayer().getMap().getAllMonstersThreadsafe().size() == 0) {
		if (!cm.haveItem(4001259,1)) {
			cm.gainItem(4001259,1);
		}
		cm.warp(610030500,0);
	} else {
		cm.sendOk("Eliminate all of the Crimson Guardians.");
	}
	cm.dispose();
}
}