var status = -1;

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendNextNoESC("So, why do you want to go to Maple Island, anyway? Not many people head that way these days. Judging by your clothes, you're not a tourist either.");
	} else if (status == 1) {
		cm.sendPlayerToNpc("I'm going to Maple Island for training... After that, I'm headed to Victoria Island to become a great adventurer! That's how it works, right?");
	} else if (status == 2) {
		cm.sendNextNoESC("It sure does! Maple Island is a great place to train, since there are no dangerous monsters there. Plus, you'll make plenty of friends and learn the basics. When you're ready, there's a big, wide world out there for you to explore!");
	} else if (status == 3) {
		cm.sendPlayerToNpc("Heh, I can't wait! I'm going to train really hard, and learn to take down all the powerful monsters. I'm completely prepared!");
	} else if (status == 4) {
		cm.sendNextNoESC("What a great attitude! That will help you succeed. You can't ever be sure of what will happen, though. Just remember, #beverything happens for a reason.#k");
	} else if (status == 5) {
		cm.sendPlayerToNpc("Hey, did you hear something?");
	} else if (status == 6) {
		cm.sendDirectionStatus(4, 0);
		cm.sendDirectionStatus(3, 2);
		cm.sendDirectionInfo("Effect/Summon.img/15");
		cm.sendDirectionStatus(1, 2000);
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/balog");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/npc/0");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/User/0");
		cm.sendDirectionStatus(1, 1000);
		cm.showWZEffect("Effect/Direction4.img/effect/cannonshooter/face02");
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/npc/1");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/User/1");
		cm.sendDirectionStatus(1, 1000);
		cm.showWZEffect("Effect/Direction4.img/effect/cannonshooter/face05");
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/balog/0");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Mob/8150000.img/attack2/info/effect");
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/User/2");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionStatus(3, 6);
		cm.sendDirectionInfo("Mob/8130100.img/attack1/info/effect");
		cm.sendDirectionInfo("Mob/8130100.img/attack1/info/hit");
		cm.showWZEffect("Effect/Direction4.img/effect/cannonshooter/face01");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionStatus(3, 2);
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/balog/1");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Effect/Direction4.img/effect/cannonshooter/User/3");
		cm.sendDirectionStatus(1, 1000);
		cm.sendDirectionInfo("Mob/8150000.img/attack2/info/hit");
		cm.warp(912060100,0);
		cm.dispose();
	}
}