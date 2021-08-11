/*
	Name: Gachapon13
	Place: Malaysia's Trend Zone Metropolis Gachapon
 */

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	if (cm.haveItem(5220000)) {
	    cm.sendYesNo("You have some #bGachapon Tickets#k there.\r\nWould you like to try your luck?");
	} else {
	    cm.sendOk("You don't have a single ticket with you. Please buy the ticket at the department store before coming back to me. Thank you.");
	    cm.safeDispose();
	}
    } else if (status == 1) {
	var item;
	if (Math.floor(Math.random() * 300) == 0) {
	    var rareList = new Array(3010022, 1372041, 1372042, 2340000, 2049100, 1082149);

	    item = cm.gainGachaponItem(rareList[Math.floor(Math.random() * rareList.length)], 1);
	} else {
	    var itemList = new Array(2000005, 2040105, 2040605, 2040609, 2040607, 2044505, 2041031, 2041037, 2041041, 2044705, 2043305, 2040309, 2040103, 2040811, 2040815, 2040015, 2040011,
		2040511, 2040509, 2040521, 2044405, 2040713, 2040717, 2043805, 2040407, 2040206, 2040106, 2040101, 2040606, 2041036, 2041034, 2044604, 2043304, 2040306, 2040814, 2040008, 2043006,
		2040508, 2040520, 2044404, 2040904, 2040908, 2040921, 2040916, 2040410, 2040404, 1102082, 1082145, 1082146, 1082147, 1082148, 1082150, 1012056, 1322027, 1002391,
		1432013, 1322022, 1102040, 1102043, 1422008, 1002037, 1002074, 1002274, 1372008, 1061094, 1002175, 1472054, 1472005, 1002170, 1051065, 2044901, 2044902, 2044803, 2044804);

	    item = cm.gainGachaponItem(itemList[Math.floor(Math.random() * itemList.length)], 1);
	}

	if (item != -1) {
	    cm.gainItem(5220000, -1);
	    cm.sendOk("You have obtained #b#t" + item + "##k.");
	} else {
	    cm.sendOk("Please check your item inventory and see if you have the ticket, or if the inventory is full.");
	}
	cm.safeDispose();
    }
}