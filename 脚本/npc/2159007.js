var status = -1;
function action(mode, type, selection) {
    if (mode == 1) 
        status++;
    else 
	status--;
    if (cm.getPlayer().getMapId() == 931000011 || cm.getPlayer().getMapId() == 931000030) {
	cm.dispose();
	return;
    }
    if (cm.getInfoQuest(23007).indexOf("vel01=2") == -1) {
    	if (status == 0) {
    	    cm.sendNext("Whoa. Wh-what happened? The glass is broken... Did that vibration earlier break it?");
    	} else if (status == 1) {
	    cm.sendNextPrevS("Now, there''s nothing stopping you right? Let''s get out of here!", 2);
        } else if (status == 2) {
	    cm.sendNextPrevS("Then hurry up! Let''s go!", 2);
        } else if (status == 3) {
	    cm.updateInfoQuest(23007, "vel00=2;vel01=2");
	    cm.warp(931000020,1);
	    cm.dispose();
	}
    } else if (cm.getInfoQuest(23007).indexOf("vel01=2") != -1) {
    	if (status == 0) {
    	    cm.sendNext("It's been...a really long time since I''ve been outside the laboratory. Where are we?");
    	} else if (status == 1) {
	    cm.sendNextPrevS("This is the road that leads to Edelstein, where I live! Let''s get out of here before the Black Wings follow us.", 2);
        } else if (status == 2) {
	    cm.updateInfoQuest(23007, "vel00=2;vel01=3");
	    cm.ShowWZEffect("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow1");
	    cm.dispose();
	}
    } else {
	cm.sendOk("It's been...a really long time since I've been outside the laboratory.");
    	cm.dispose();
    }
}