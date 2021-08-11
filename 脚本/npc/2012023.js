
function action(mode, type, selection) {
    if (cm.getQuestStatus(6230) == 1) {
	if (!cm.haveItem(4031456)) {
	    if (cm.haveItem(4031476)) {
		if (cm.canHold(4031456)) {
		    cm.gainItem(4031456, 1);
		    cm.gainItem(4031476, -1);
		    cm.sendOk("Maple leaves were absorbed into sparkling glass marble." );
		    cm.safeDispose();
		} else {
		    cm.sendOk("Maple Marble can...t be earned as there's no blank on Others window. Make a blank and try again." );
		    cm.safeDispose();
		}
	    } else {
		cm.dispose();
	    }
	} else {
	    cm.dispose();
	}
    } else {
	cm.dispose();
    }
}