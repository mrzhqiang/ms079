var status = -1;
function action(mode, type, selection) {
    status++;
    if (cm.getInfoQuest(23999).indexOf("exp1=1") != -1) {
	cm.sendNext("Did you find Ulrika and Von yet? Von is really, really good at hiding.");
	cm.dispose();
	return;
    }
    if (status == 0) {
    	cm.sendNext("Did you find Ulrika and Von yet? Von is really, really good at hiding. \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 5 exp");
    } else if (status == 1) {
	cm.gainExp(5);
	if (cm.getInfoQuest(23999).equals("")) {
	    cm.updateInfoQuest(23999, "exp1=1");
	} else {
	    cm.updateInfoQuest(23999, cm.getInfoQuest(23999) + ";exp1=1");
	}
    	cm.dispose();
    }
}