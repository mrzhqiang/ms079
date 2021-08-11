/*
	Pokedex
*/

var status = -1;
var sel = 0;
var byName = false;

function action(mode, type, selection) {
	var pokeEntries = cm.getAllPokedex();
	if (mode != 1) {
    		cm.dispose();
	} else {
		status++;
		if (status == 0) {
			if (cm.getPlayer().getBattler(0) == null && cm.getPlayer().getBoxed().size() <= 0) {
				cm.sendOk("You have no monsters and therefore have no entries in the Pokedex.");
				cm.safeDispose();
				return;
			}
			
			var selStr = "Seen: #r#e" + cm.getPlayer().getMonsterBook().getSeen() + "#n#k, Caught: #b#e" + cm.getPlayer().getMonsterBook().getCaught() + "#n#k\r\nWhich entry would you like to see?\r\n\r\n#b#L0#Search By Name#l\r\n";
			for (var i = 0; i < pokeEntries.size(); i++) {
				selStr += "#L" + (i+1) + "##" + cm.getLeftPadded("" + (i + 1), '0', 3) + ": ";
				if (cm.getPlayer().getMonsterBook().getLevelByCard(pokeEntries.get(i).id) == 0) {
					selStr += "???";
				} else {
					selStr += "#o" + pokeEntries.get(i).id + "#";
					if (cm.getPlayer().getMonsterBook().getLevelByCard(pokeEntries.get(i).id) == 1) {
						selStr += "#r(Seen)#b";
					}
				}
				selStr += "#l\r\n";
			}
			cm.sendSimple(selStr);
		} else if (status == 1) {
			if (selection == 0) {
				byName = true;
				cm.sendGetText("Please enter the name of your monster here. (NOT case sensitive)");
				return;
			}
			selection--;
			if (selection < 0 || selection >= pokeEntries.size()) {
				cm.dispose();
				return;
			}
			var theEntry = pokeEntries.get(selection);
			if (cm.getPlayer().getMonsterBook().getLevelByCard(theEntry.id) == 0) {
				cm.sendOk("#" + cm.getLeftPadded("" + (selection + 1), '0', 3) + " - Unknown data.");
				cm.safeDispose();
				return;
			}
			sel = selection;
			var info = "#e#" + cm.getLeftPadded("" + (selection + 1), '0', 3) + " - #o" + theEntry.id + "##n\r\n";
			info += "#fMob/" + cm.getLeftPadded(theEntry.id + "", '0', 7) + ".img/stand/0#\r\n";
			if (cm.getPlayer().getMonsterBook().getLevelByCard(theEntry.id) == 1) {
				info += "Level ??\r\n";
				info += "HP ???\r\n";
				info += "EXP ???, Pokemon EXP ???\r\n";
				info += "ATK: ???, DEF: ??\r\n";
				info += "Sp.ATK: ???, Sp.DEF: ??\r\n";
				info += "Speed: ??, Evasion: ??, Accuracy: ???\r\n";
				info += "Element: ???\r\n";
				info += "EXP Type: ???\r\n";
				info += "First Ability: ???\r\n";
				info += "Second Ability: ???\r\n";
				info += "Attacks: ?\r\n";
				info += "Pre-evolutions: ???\r\n";
				info += "Evolutions: ???\r\n";
				info += "Found in: ???\r\n";
				cm.sendOk(info);
				cm.safeDispose();
				return;
			}
			info += "Level " + theEntry.dummyBattler.getLevel() + "\r\n";
			info += "HP " + theEntry.dummyBattler.calcHP() + "\r\n";
			info += "EXP " + theEntry.dummyBattler.getStats().getExp() + ", Pokemon EXP " + theEntry.dummyBattler.getOurExp() + "\r\n";
			info += "ATK: " + theEntry.dummyBattler.getATK(0) +  ", DEF: " + theEntry.dummyBattler.getDEF() + "%\r\n";
			info += "Sp.ATK: " + theEntry.dummyBattler.getSpATK(0) +  ", Sp.DEF: " + theEntry.dummyBattler.getSpDEF() + "%\r\n";
			info += "Speed: " + theEntry.dummyBattler.getSpeed() +  ", Evasion: " + theEntry.dummyBattler.getEVA() + ", Accuracy: " + theEntry.dummyBattler.getACC() + "\r\n";
			info += "Element: " + theEntry.dummyBattler.getElementString() + "\r\n";
			info += "EXP Type: " + theEntry.dummyBattler.getExpString() + "\r\n";
			info += "First Ability: " + theEntry.dummyBattler.getFamily().ability1 + " - " + theEntry.dummyBattler.getFamily().ability1.desc + "\r\n";
			info += "Second Ability: " + theEntry.dummyBattler.getFamily().ability2 + " - " + theEntry.dummyBattler.getFamily().ability2.desc + "\r\n";
			info += "Attacks: " + (theEntry.dummyBattler.getStats().getMobAttacks().size() + 1) + "\r\n";
			info += "Pre-evolutions: " + (theEntry.pre.size() == 0 ? "None" : "");
			var pre = theEntry.getPre();
			for (var xx = 0; xx < pre.size(); xx++) {
				var pr = pre.get(xx);
				switch (cm.getPlayer().getMonsterBook().getLevelByCard(pr.getKey())) {
					case 0:
						info += "??? by ???,";
						break;
					case 1:
						info += "#o" + pr.getKey() + "# by ???.";
						break;
					case 2:
						info += "#o" + pr.getKey() + "#";
						if (pr.getValue() >= 1000000) {
							info += " by using #v" + pr.getValue() + "##z" + pr.getValue() + "#, ";
						} else {
							info += " by level " + pr.getValue() + ", ";
						}
						break;
				}
			}
			info += "\r\nEvolutions: " + (theEntry.evo.size() == 0 ? "None" : "");
			var evo = theEntry.getEvo();
			for (var xx = 0; xx < evo.size(); xx++) {
				var pr = evo.get(xx);
				switch (cm.getPlayer().getMonsterBook().getLevelByCard(pr.getKey())) {
					case 0:
						info += "??? by ???, ";
						break;
					case 1:
						info += "#o" + pr.getKey() + "# by ???, ";
						break;
					case 2:
						info += "#o" + pr.getKey() + "#";
						if (pr.getValue() >= 1000000) {
							info += " by using #v" + pr.getValue() + "##z" + pr.getValue() + "#, ";
						} else {
							info += " by level " + pr.getValue() + ", ";
						}
						break;
				}
			}
			info += "\r\nFound in: ";
			if (theEntry.maps == null || theEntry.maps.size() <= 0) {
				info += "No Location";
			} else {
				for (var xx = 0; xx < theEntry.maps.size(); xx++) {
					var pr = theEntry.maps.get(xx);
					info += "#m" + pr.left + "# - " + (pr.right / 100) + "% chance\r\n";
				}
			}
			info += "\r\n#b\r\n";
			info += "#L0#View drops of this monster.#l\r\n";
			cm.sendSimple(info);
		} else if (status == 2) {
			if (byName && cm.getText() != null && cm.getText().length() > 0) {
				var found = false;
				var selStr = "Seen: #r#e" + cm.getPlayer().getMonsterBook().getSeen() + "#n#k, Caught: #b#e" + cm.getPlayer().getMonsterBook().getCaught() + "#n#k\r\nWhich entry would you like to see?\r\n\r\n#b";
				for (var i = 0; i < pokeEntries.size(); i++) {
					if (pokeEntries.get(i).dummyBattler.getName().toLowerCase().contains(cm.getText().toLowerCase())) {
						found = true;
						selStr += "#L" + (i+1) + "##" + cm.getLeftPadded("" + (i + 1), '0', 3) + ": ";
						if (cm.getPlayer().getMonsterBook().getLevelByCard(pokeEntries.get(i).id) == 0) {
							selStr += "???";
						} else {
							selStr += "#o" + pokeEntries.get(i).id + "#";
							if (cm.getPlayer().getMonsterBook().getLevelByCard(pokeEntries.get(i).id) == 1) {
								selStr += "#r(Seen)#b";
							}
						}
						selStr += "#l\r\n";
					}
				}
				if (found) {
					cm.sendSimple(selStr);
					status = 0;
				} else {
					cm.sendNext("No entries were found.");
					status = -1;
				}
				byName = false;
				return;
			}
			cm.sendNext(cm.checkDrop(pokeEntries.get(sel).id));
			cm.dispose();
		}
	}
}