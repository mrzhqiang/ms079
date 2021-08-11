var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
		cm.sendNextNoESC("W-What is that?");
    } else if (status == 1) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg2/1");
		cm.sendDirectionStatus(1, 2000);
		cm.sendPlayerToNpc("(...What's going on? My Fury is nearly gone! Did it take my power?)");
    } else if (status == 2) {
		cm.sendNextNoESC("This can't be happening...!");
	} else if (status == 3) {
		cm.sendPlayerToNpc("What did you do to me? Is this the Black Mage's energy?");
	} else if (status == 4) {
		cm.sendNextNoESC("We must capture you to avoid interrogation.");
	} else if (status == 5) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/16");
		cm.sendDirectionInfo("Skill/3112.img/skill/31121006/effect");
		cm.showMapEffect("demonSlayer/31121006");
		cm.sendDirectionInfo("Effect/Direction6.img/DemonTutorial/Scene3");
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/17");
		cm.showMapEffect("demonSlayer/31121006h");
		cm.removeNpc(2159340);
		cm.removeNpc(2159341);
		cm.sendDirectionStatus(4, 2159342);
		cm.sendDirectionStatus(3, 0);
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/13");
		cm.sendNextNoESC("(Who is that? I've never seen someone so powerful.)", 2159342);
	} else if (status == 6) {
		cm.sendPlayerToNpc("I need to get out of here. I wasted too much time and power.");
	} else if (status == 7) {
		cm.sendDirectionStatus(3, 2);
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/12");
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/4");
		cm.sendPlayerToNpc("No, I'm losing consciousness...!");
	} else if (status == 8) {
		cm.sendNextNoESC("Wait. I'm not part of them. You..who are you?", 2159342);
	} else if (status == 9) {
		cm.sendPlayerToNpc("Stay back!");
	} else if (status == 10) {
		cm.sendNextNoESC("C'mon.. You need help, you need it now. They, the Black Wings, were draining your power!", 2159342);
	} else if (status == 11) {
		cm.sendPlayerToNpc("...None of this makes any sense!");
	} else if (status == 12) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/13");
		cm.sendPlayerToNpc("Who are you? How do you know about these things?");
	} else if (status == 13) {
		cm.sendNextNoESC("I'm J, an Agent in the Resistance. We're working against the Black Wings. I don't know who you are, but I want to help you.", 2159342);
	} else if (status == 14) {
		cm.sendPlayerToNpc("No... I've lost all my energy...");
	} else if (status == 15) {
		cm.sendDirectionStatus(0, 373);
		cm.sendDirectionStatus(4, 0);
		if (cm.getPlayer().getGender() == 0) {
			cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/fallMale");
		} else {
			cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/fallFemale");
		}
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/13");
		cm.warp(931050030,0);
		cm.dispose();
	}
}