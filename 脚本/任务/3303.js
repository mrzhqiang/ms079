/*
	NPC Name: 		Han the Broker
	Map(s): 		Magatia
	Description: 	Quest - Test from the Head of Alcadno Society
*/

var status = -1;
var oreArray;

function start(mode, type, selection) {
}

function end(mode, type, selection) {
	    qm.sendNext("Then wait for awhile. I'll go and get the stuff to help you pass the test of Chief Alcadno.");
	    qm.forceCompleteQuest();
	    qm.dispose();
}