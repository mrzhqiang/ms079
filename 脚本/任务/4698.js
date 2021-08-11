/*
	NPC Name: 		Asia
	Description: 		Quest - Asia
*/
var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
    } else {
        if (mode == 1) {
            status++;
        }
        if (status == 0) {
            qm.sendNext("在这里很危险！如果你打算挑战它，你需要更多的组队员，你想做什么么？想进去吗？？");
        } else if (status == 1) {
            qm.warp(802000109, 0);
            //qm.forceStartQuest();
            qm.dispose();
        }
    }
}

function end(mode, type, selection) {
}