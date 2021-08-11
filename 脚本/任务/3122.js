var status = -1;

function start(mode, type, selection) {
	qm.sendNext("Go to Rex, but don't put your hands on the seal. Got it? You can enter Ice Ravine through the entrance in Ice Valley II.");
    	qm.forceStartQuest(3122, "0");
	qm.dispose();
}

function end(mode, type, selection) {
	qm.sendNext("Thank you.");
	qm.forceCompleteQuest();
	qm.dispose();
}