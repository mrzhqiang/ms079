var status = -1;

function start(mode, type, selection) {
	qm.dispose();
}
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    qm.dispose();
	}
	status--;
    }
    if (status == 0) {
		qm.forceCompleteQuest();
		qm.teachSkill(30010166, -1, 0);
		qm.teachSkill(30011167, -1, 0);
		qm.teachSkill(30011168, -1, 0);
		qm.teachSkill(30011169, -1, 0);
		qm.teachSkill(30011170, -1, 0);
		qm.EnableUI(1);
		qm.sendDirectionStatus(3, 2);
		qm.sendDirectionStatus(1, 30);
		qm.sendDirectionStatus(4, 2159308);
		qm.sendNextNoESC("You're powerful, aren't you? I think it's time to settle things!", 2159308);
    } else if (status == 1) {
		qm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/arkyrimAttack");
		qm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/8");
		qm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/11");
		qm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/guide1/0");
		qm.sendDirectionStatus(1, 1500);
		qm.sendNextNoESC("You're stronger than I thought! How amusing!");
    } else if (status == 2) {
		qm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/10");
		qm.sendDirectionStatus(1, 1500);
		qm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/11");
		qm.sendDirectionStatus(1, 1500);
		qm.sendDirectionInfo("Skill/3112.img/skill/31121005/effect");
		qm.sendDirectionInfo("Skill/3112.img/skill/31121005/effect0");
		qm.sendDirectionStatus(0, 364);
		qm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/gateOpen/0");
		qm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/gateLight/0");
		qm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/gateStair/0");
		qm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/0");
		qm.sendDirectionStatus(1, 1500);
		qm.sendNextNoESC("Ah, it seems the Black Mage wishes to see you after all! I'll expect to see you again!", 2159308);
	} else if (status == 3) {
		qm.removeNpc(2159308);
		qm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg3/2");
		qm.sendDirectionStatus(3, 2);
		qm.sendDirectionStatus(4, 0);
		qm.showMapEffect("demonSlayer/whiteOut");
		qm.warp(931050300,0);
		qm.dispose();
	}
}