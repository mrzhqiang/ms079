function start() {
    if (cm.isQuestActive(23121)) {
	cm.sendOk("Quest complete.");
	cm.forceCompleteQuest(23121);
	cm.dispose();
	return;
    }
    cm.sendSimple("#b\r\n#L0#Victoria Island#l\r\n#L1#Orbis#l#k");
}

function action(mode,type,selection) {
    if (mode == 1) { //or 931000400 + selection..?
	switch(selection) {
	    case 0:
		cm.warp(104020130,0);
		break;
	    case 1:
		cm.warp(200000170,0);
		break;
	}
    }
    cm.dispose();
}