/**
-- Odin JavaScript --------------------------------------------------------------------------------
	Mark the Toy Soldier - Doll's House(922000010)
-- By ---------------------------------------------------------------------------------------------
	Information
-- Version Info -----------------------------------------------------------------------------------
	1.0 - First Version by Information
---------------------------------------------------------------------------------------------------
**/

var havePendulum = false;
var complete = false;
var inQuest = false;

function start() {
    if(cm.getQuestStatus(3230) == 1) {
	inQuest = true;
    } else {
	inQuest = false;
    }
    dh = cm.getEventManager("DollHouse");
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if(mode == 0 && status == 0) {
	cm.dispose();
	return;
    } else if(mode == 0 && status == 1) {
	cm.sendNext("I knew you'd stay. It's important that you finish what you've started! Now please go locate the different-looking dollhouse, break it, and bring #b#t4031094##k to me!");
	cm.dispose();
	return;
    }
    if(mode == 1) {
	status++;
    } else {
	status--;
    }
    if(inQuest == true) {
	if(status == 0) {
	    if(cm.haveItem(4031094)) {
		cm.sendNext("Oh wow, you did locate the different-looking dollhouse and find #b#t4031094##k! That was just incredible!! With this, the Ludibrium Clocktower will be running again! Thank you for your work and here's a little reward for your effort. Before that, through, please check your inventory and see if it's full or not.");
		havePendulum = true;
	    } else {
		cm.sendSimple("Hello, there. I'm #b#p2040028##k, in charge of protecting this room. Inside, you'll see a bunch of dollhouses, and you may find one that looks a little bit different from the others. Your job is to locate it, breaks it's door, and find the #b#t4031094##k, which is an integral part of the Ludibrium Clocktower. You'll have a time limit on this, and if you break the wrong dollhouse, you'll be forced back outside, so please be careful.\r\n#L0##bI want to get out of here.#k#l");
	    }
	} else if(status == 1) {
	    if(havePendulum == true) {
		if(!cm.canHold(2000010)) {
		    cm.sendNext("YOU CANNOT HOLD THE ITEM???");
		}
		cm.sendNextPrev("What do you think? Do you like the #b100 #t2000010#s#k that I gave you? Thank you so much for helping us out. The clocktower will be running again thanks to your heroic effort, and the monsters from the other dimension seems to have disappeared, too. I'll let you out now. I'll see you around!");
		if(complete == false) {
		    cm.completeQuest(3230);
		    cm.gainExp(2400);
		    cm.gainItem(4031094, -1);
		    cm.gainItem(2000010, 100);
		    complete = true;
		}
	    } else {
		cm.sendYesNo("Are you sure you want to give up now? Alright then... but please remember that the next time you visit this place, the dollhouses will switch places, and you'll have to look through each and every one of them carefully again. What do you think? Would you still like to leave this place?");
	    }
	} else if(status == 2) {
	    if( cm.getPlayer().getEventInstance() != null)
	        cm.getPlayer().getEventInstance().removePlayer(cm.getChar());
	    cm.dispose();
	}
    } else {
	if(status == 0) {
	    cm.sendNext("What the... we have been forbidding people from entering this room due to the fact that a monster from another dimension is hiding out here. I don't know how you got in here, but I'll have to ask you to leave immediately, for it's dangerous to be inside this room.");
	} else if(status == 1) {
	    cm.warp(221024400, 4);
	    cm.dispose();
	}
    }
}
