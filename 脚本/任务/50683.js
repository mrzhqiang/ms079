var status = -1;

function start(mode, type, selection) {
 	qm.sendOk("Come to Ludibrium.");
	qm.forceCompleteQuest();
	qm.dispose();
}
function end(mode, type, selection) {
	qm.dispose();
}
