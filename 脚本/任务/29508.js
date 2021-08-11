var status = -1;

function start(mode, type, selection) {
	qm.dispose();
}
function end(mode, type, selection) {
	if (qm.getPlayer().getMarriageId() > 0 && qm.getPlayer().getGuildId() > 0 && qm.getPlayer().getJunior1() > 0 && qm.canHold(1142081,1)) {
		qm.sendNext("Wow. Here you are!");
		qm.forceCompleteQuest();
		qm.gainItem(1142081,1);
	} else {
		qm.sendNext("I don't think you fit the requirements. Get in a wedding, family, and guild.");
	}
	qm.dispose();
}
