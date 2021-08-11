/*
	NPC Name: 		Commander Grauda
	Description: 		Quest - Elliminate Dunas Squad
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
            qm.sendNext("好了，你即将挑战它了！谢谢……。只是让你知道，敌人可能是非常强大的。你准备好了吗？");
        } else if (status == 1) {
            qm.warp(802000409, 0);
            //qm.forceStartQuest();
            qm.dispose();
        }
    }
}

function end(mode, type, selection) {
}