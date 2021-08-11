/*
	Description: 	Quest -  Strange Farm
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendOk("Huh? Are you scare of the Pigs? They are jumping around like crazy, but you shouldn't be scare of them...");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.askAcceptDecline("Forget about the Cunning Foxes. Since you're here, want to help me out again? I think the only way to calm the Pigs is by disciplining them. Why don't you go take care of a few of the #rPigs#k?");
    } else if (status == 1) {
	qm.forceStartQuest();
	qm.sendOk("The crazy pigs can be found staring at the #bHuge Path#k. Head over and take care of just #r20#k of them. Hey, kiddo, you've really become a huge help to me.");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	qm.sendOk("Oh, you disciplined the Pigs. Good job! Thank you.\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#i2022621# Declicious Milk 30\r\n#i2022622#Declicious Juice 30\r\n#fUI/UIWindow.img/QuestIcon/8/0#980 exp");
    } else if (status == 1) {
	qm.gainExp(980);
	qm.gainItem(2022621, 30);
	qm.gainItem(2022622, 30);
	qm.sendOk("Now I'll just get back to work");
	qm.forceCompleteQuest();
	qm.dispose();
    }
}