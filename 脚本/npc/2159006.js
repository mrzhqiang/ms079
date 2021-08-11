var status = -1;
function action(mode, type, selection) {
    if (mode == 1) 
        status++;
    else 
	status--;
    if (cm.getPlayer().getMapId() == 931000011) {
	cm.dispose();
	return;
    }
    if (cm.getInfoQuest(23007).indexOf("vel00=1") == -1 && cm.getInfoQuest(23007).indexOf("vel01=1") == -1) {
    	if (status == 0) {
    	    cm.sendNext("Stay back!");
    	} else if (status == 1) {
	    cm.sendNextPrevS("Whos talking?! Where are you?!", 2);
        } else if (status == 2) {
	    cm.sendNextPrev("Look up.");
        } else if (status == 3) {
	    cm.updateInfoQuest(23007, "vel00=1");
	    cm.showWZEffect("Effect/Direction4.img/Resistance/ClickVel");
	    cm.dispose();
	}
    } else if (cm.getInfoQuest(23007).indexOf("vel00=1") != -1 && cm.getInfoQuest(23007).indexOf("vel01=1") == -1) {
    	if (status == 0) {
    	    cm.sendNext("My name is #bVita#k. I''m one of #rDoctor Gelimer''s#k test subjects. But that''s not important right now. You have to get out of here before someone sees you!");
    	} else if (status == 1) {
	    cm.sendNextPrevS("Wait, what are you talking about? Someone''s doing experiments on you?! And who''s Gelimer?", 2);
        } else if (status == 2) {
	    cm.sendNextPrev("Shhh! Did you hear that? Someone''s coming! It''s got to be Doctor Gelimer! Oh no!");
        } else if (status == 3) {
	    cm.updateInfoQuest(23007, "vel00=2");
	    cm.warp(931000011,0);
	    cm.dispose();
	}
    } else if (cm.getInfoQuest(23007).indexOf("vel01=1") != -1) {
    	if (status == 0) {
    	    cm.sendNext("Whew, something must have distracted them. Now''s your chance. GO!");
    	} else if (status == 1) {
	    cm.sendNextPrevS("#b(Vita closes her eyes like she''s given up. What should you do? How about trying to break open the vat?)#k", 2);
        } else if (status == 2) {
	    cm.sendNextPrev("#b(You tried to hit the vat with all your might, but your hand slipped!)#k");
        } else if (status == 3) {
	    cm.gainExp(60);
	    cm.warp(931000013,0);
	    cm.dispose();
	}
    }
}