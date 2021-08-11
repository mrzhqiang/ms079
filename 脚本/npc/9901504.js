var status = -1;
var gp = Array(6000, 5000, 5000, 5000, 2000, 2000);
var names = Array("2x EXP for Guild (2 hours)", "2x DROP for Guild (2 hours)", "2x MESO for Guild (2 hours)", "2x A-CASH for Guild (2 hours)", "Eye of Fire x 10", "Piece of Cracked Dimension x 10");
var buffs = Array(2022332, 2022463, 2022461, 2022333, 4001017, 4031179);
var q = Array(1, 1, 1, 1, 10, 10);


function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode != 1) {
		cm.dispose();
		return;
	}
	status++;
	if (cm.getGuild() == null || cm.getPlayer().getGuildId() <= 0 || cm.getPlayer().getGuildRank() > 1) {
		cm.sendOk("Only the Leader of a Guild may talk to me.");
		cm.safeDispose();
		return;
	} else {
		if (status == 0) {
			var selStr = "Hello, Guild Leader! You have currently " + cm.getGP() + " GP. What would you like to do?#eAll of the buffs are auto-use and will expire upon logout.#n\r\n";
			for (var i = 0; i < gp.length; i++) {
				selStr += "\r\n#b#L" + i + "#" + names[i] + "#k#r for " + gp[i] + " GP#l#k";
			}
			cm.sendSimple(selStr);
		} else if (status == 1 && selection >= 0) {
			if (cm.getGP() < gp[selection]) {
				cm.sendOk("You don't have enough GP.");
			} else {
				cm.gainGP(-gp[selection]);
				if (q[selection] == 1 || buffs[selection] / 1000000 == 2) {
					cm.buffGuild(buffs[selection], 14400000, names[selection]); //2 hrs
				} else {
					cm.gainItem(buffs[selection], q[selection]);
				}
			}
			cm.dispose();
		} else {
			cm.dispose();
		}
	}

}