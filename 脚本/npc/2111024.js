var status = -1;

function start() {
    if (cm.getQuestStatus(3360) == 2) {
	if (cm.getMapId() == 261010000) {
	    cm.playerMessage("Your name is on the list. You'll now be transported to the secret tunnel.");
	    cm.warp(261030000, "sp_jenu");
	} else {
	    cm.playerMessage("Your name is on the list. You'll now be transported to the secret tunnel.");
	    cm.warp(261030000, "sp_alca");
	}
	cm.dispose();
    } else if (cm.getQuestStatus(3360) == 1) {
	cm.sendGetText("Please enter the password.");
    } else {
	cm.dispose();
    }
}

function action(mode, type, selection) {
    var pw = cm.getText();
    if (cm.getQuestRecord(3360).getCustomData().equals(pw)) {
	cm.forceCompleteQuest(3360);
	cm.playerMessage("The security device has been disengaged.");
    } else {
	cm.sendOk("Invalid password.");
    }
    cm.dispose();
}