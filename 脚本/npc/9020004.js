
function action(mode, type, selection) {
		if (cm.getMapId() / 100 == 9230403) {
			if (cm.getMap().getAllMonstersThreadsafe().size() == 0) {
				cm.warpParty(923040400,0);
				cm.dispose();
			} else {
				cm.sendOk("Please, save me!");
				cm.safeDispose();
			}
		} else if (cm.getMapId() / 100 == 9230404) {
			if (cm.getMap().getAllMonstersThreadsafe().size() == 0) {
				if (!cm.canHold(4001535, 1)) {
					cm.sendOk("Please make ETC room.");
					cm.dispose();
					return;
				}
				cm.gainExp_PQ(200, 1.5);
				cm.gainItem(4001535, 1);
				//cm.gainNX(2000);
				cm.addTrait("will", 26);
				cm.addTrait("charm", 26);
				cm.warp(923040000,0);
				cm.dispose();
			} else {
				cm.sendOk("Please, destroy Pianus!");
				cm.safeDispose();
			}
		}
}