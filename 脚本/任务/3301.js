/**
	NPC Name: 		Han the Broker
	Map(s): 		Magatia
	Description: 	Quest - Test from the Head of Zenumist Society
*/

var status = -1;

function start(mode, type, selection) {
    qm.dispose();
}

function end(mode, type, selection) {
	    	qm.sendNext("Then wait for awhile. I'll go and get the stuff to help you pass the test of Chief Zenumist.");
	    	qm.forceCompleteQuest();
	    qm.dispose();
}