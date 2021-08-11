var status = -1;

//member of resistance.

function end(mode, type, selection) {
    qm.sendNext("Your first mission is to eliminate the Patrol Robots.");
    qm.forceStartQuest(23129, "1");
    qm.forceStartQuest(23110);
    qm.forceStartQuest();
    qm.forceCompleteQuest();
    qm.dispose();
}