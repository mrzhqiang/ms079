/*
	NPC Name: 		Parwen
	Description: 		Quest - Verifying the password
*/
var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
	qm.dispose();
    } else {
	if (mode == 1) {
	    status++;
	} else {
	    qm.sendNext("Come on, hurry up. Get your pen and paper out if you're not that smart!");
	    qm.dispose();
	    return;
	}

	if (status == 0) {
	    qm.sendNext("Oh! Finally you have come! I'm glad you are here in time. I have the master key for you to open the secert passage! Hahahaha! Isn't it amazing? Say it amazing!");
	} else if (status == 1) {
	    qm.askAcceptDecline("All right, now, this key is very long and complex. I need you to memorize it very well. I won't say again, so you'd better write it down somewhere. Are you ready?");
	} else if (status == 2) {
	    var pass = generateString();
	    qm.sendOk("The key code is #b"+pass+"#k. Got that? Put the key into the door of the secret passage, and you will be able to walk around the passage freely.");
	    qm.forceStartQuest(pass);
	    qm.dispose();
	}
    }
}

function end(mode, type, selection) {
    if (mode == -1) {
	qm.dispose();
    } else {
	if (mode == 1)
	    status++;
	else
	    status--;
	if (status == 0) {
	    qm.dispose();
	}
    }
}

function generateString() {
    var thestring = "";
    var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    var rnum;
    for (var i = 0; i < 10; i++) {
	rnum = Math.floor(Math.random() * chars.length);
	thestring += chars.substring(rnum, rnum+1);
    }
    return thestring;
}