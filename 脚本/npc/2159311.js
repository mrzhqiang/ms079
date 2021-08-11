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
		cm.sendPlayerToNpc("....");
    } else if (status == 1) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/14");
		cm.sendDirectionStatus(1, 2000);
		cm.sendPlayerToNpc("(...I think I hear something.)");
    } else if (status == 2) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/15");
		cm.sendDirectionStatus(1, 2000);
		cm.sendPlayerToNpc("(...Am I still alive?)");
	} else if (status == 3) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/16");
		cm.sendDirectionStatus(1, 2000);
		cm.sendPlayerToNpc("(...Something's taking my energy!)");
	} else if (status == 4) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/17");
		cm.sendDirectionStatus(1, 2000);
		cm.sendPlayerToNpc("(...I have to escape!)");
	} else if (status == 5) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/guide1/0");
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/breakEgg/0");
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/7");
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/breakEgg/1");
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/1");
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/breakEgg/2");
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/2");
		cm.showMapEffect("demonSlayer/whiteOut");
		cm.warp(931050020,0);
		cm.dispose();
	}
}