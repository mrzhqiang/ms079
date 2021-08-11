/* 
 *  Dallier - King Medal
 *  Lith Habor = 104000000
 *  Sleepywood = 105040300
 */

var status = -1;

function start(mode, type, selection) {
    if (mode == 0) {
	if (status == 0) {
	    qm.sendNext("Come back when you feel like you are fully prepared for this.");
	    qm.dispose();
	    return;
	} else if (status == 2) {
	    status--;
	} else {
	    qm.dispose();
	    return;
	}
    } else {
	status++;
    }

    if (status == 0) {
	qm.askAcceptDecline("#v1142030# #e#b#t1142030##k\n\r\n\r - Time Limit: 1 hr\n\r - Donate the Most for this town....#nDo you want to test yourself and see if this Medal is for you?");
    } else if (status == 1) {
	qm.sendNext("Current Rank \n\r\n\r1. #bMintLovePep#k : ???,???,??? mesos\n\r2. #bKelviinXD#k : 68,000,000 mesos\n\r3. #bxBabyRence#k : 49,999,999 mesos\n\r4. #bXxTrIStaArxx#k : 29,999,999 mesosn\n\r5. #bxBabyRence#k : 14,000,000 mesos\n\r\n\rUnderstand that we cannot divulge the exact number donated by the current King of Donation. \n\rAlso remember that all records will be reset every first of the month...");
    } else if (status == 2) {
	qm.sendNextPrev("Best of luck to you. There's no real set date for this, so if you feel like you qualify for this, then feel free to come see me so I can determine whether you qualify for it. And remember that you will not be able to challenge other Titles unless you either forfeit this challenge or complete it...");
	qm.dispose();
    }
}

function end(mode, type, selection) {
}

/*function getMedalType(ids) {
    var thestring = "#b";
    var extra;
    for (x = 0; x < ids.length; x++) {
	extra = "#L" + x + "##t" + ids[x] + "##l\r\n";
	thestring += extra;
    }
    thestring += "#k";
    return thestring;
}*/