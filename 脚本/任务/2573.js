var status = -1;

function start(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		qm.dispose();
		return;
	}
	if (status == 0) {
		qm.sendNext("Greetings! Isn't this just the perfect weather for a journey? I'm SKipper, the captain of this fine ship. You must be a new Explorer, eh? Nice to meet you.");
	} else if (status == 1) {
		qm.sendAcceptDecline("We're not quite ready to leave, so feel free to look around the ship while we're waiting.");
	} else if (status == 2) {
		qm.forceCompleteQuest();
		qm.warp(3000000,0);
		qm.dispose();
	}
}
function end(mode, type, selection) {
	qm.dispose();
}
