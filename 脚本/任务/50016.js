/*
	NPC Name: 		Dida
	Description: 		Quest - Break of Blaze
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
	    qm.sendNext("Okay, so you are going to the battle as well. Thanks... Just letting you know, the enemy is probably more powerful than anything you've ever faced, Are you ready?");
	} else if (status == 1) {
	    qm.warp(802000800, 0);
	    qm.dispose();
	}
    }
}

function end(mode, type, selection) {
}