var status = -1;
var coin = 4310018;
var baseid = 1142321;
var points = Array(500, 350, 250, 200, 150, 100, 75, 50, 30, 20, 10, 1);  

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	if (cm.haveItem(baseid, 1, true, true)) { //grand champion
	    cm.sendNext("You are the highest rank.");
	    cm.dispose();
	} else {
	    for (var i = 0; i < (points.length - 1); i++) {
		if (cm.haveItem(baseid - (i+1), 1, true, true)) {
		    cm.sendYesNo("You are at rank #i" + (baseid - (i+1)) + "#. \r\nWould you like to get the next rank? #i" + (baseid - i) + "#\r\nCost: " + points[i] + " coins");
		    return;
		}
	    } 
	    cm.sendYesNo("You do not have a medal. \r\nWould you like to get one? #i" + (baseid - (points.length - 1)) + "#\r\nCost: " + points[points.length - 1] + " coins");
 	}
    } else if (status == 1) {
	for (var i = 0; i < (points.length - 1); i++) {
	    if (cm.haveItem(baseid - (i+1), 1, true, true)) {
		giveItem(i);
		return;
	    }
	} 
	giveItem(points.length - 1);
    }
}

function giveItem(stat) {
     if (!cm.haveItem(coin, points[stat])) {
	cm.sendOk("You do not have " + points[stat] + " coins.");
     } else if (!cm.canHold(baseid - stat, 1)) {
	cm.sendOk("Please make some space.");
     } else if (cm.getPlayer().hasEquipped(baseid - (stat+1))) {
	cm.sendOk("Please take off your medal.");
     } else {
	cm.gainItem(coin, -points[stat]);
	cm.gainItem(baseid - stat, 1);
	if (cm.haveItem(baseid - (stat+1))) {
	    cm.gainItem(baseid - (stat+1), -1);
	}
     }
     cm.dispose();
}