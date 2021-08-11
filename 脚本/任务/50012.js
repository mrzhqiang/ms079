/*
	NPC Name: 		Asia
	Description: 		Quest - Magic, Science and space
*/

function start(mode, type, selection) {
    qm.dispose();
}

function end(mode, type, selection) {
    if (qm.getQuestStatus(50012) == 0) {
	qm.forceStartQuest();
    } else {
	qm.forceCompleteQuest(50015);
	qm.forceCompleteQuest();
    }
    qm.dispose();
}