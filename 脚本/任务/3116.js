var status = -1;

function start(mode, type, selection) {
	qm.sendNext("Please come see me, I am in the Chief's Residence of Elnath.");
    	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.dispose();
}