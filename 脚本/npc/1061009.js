/*
 重新改写打教官脚本by:Kodan
 */
function start() {
    var nextmap1 = cm.getMapFactory().getMap(108010201);
    var nextmap2 = cm.getMapFactory().getMap(108010301);
    var nextmap3 = cm.getMapFactory().getMap(108010101);
    var nextmap4 = cm.getMapFactory().getMap(108010401);
    var nextmap5 = cm.getMapFactory().getMap(108010501);
    var nextmap11 = cm.getMapFactory().getMap(108010200);
    var nextmap22 = cm.getMapFactory().getMap(108010300);
    var nextmap33 = cm.getMapFactory().getMap(108010100);
    var nextmap44 = cm.getMapFactory().getMap(108010400);
    var nextmap55 = cm.getMapFactory().getMap(108010500);
	if (cm.getPlayer().getLevel() >= 70) {
		if (cm.canHold(4031059)) {
			if (!(cm.haveItem(4031059))) {
				if (nextmap1.mobCount() > 0 && nextmap1.playerCount() == 0 && nextmap11.playerCount() == 0) {
					nextmap1.killAllMonsters(true);
					cm.dispose();
				}
				if (nextmap2.mobCount() > 0 && nextmap2.playerCount() == 0 && nextmap22.playerCount() == 0) {
					nextmap2.killAllMonsters(true);
					cm.dispose();
				}
				if (nextmap3.mobCount() > 0 && nextmap3.playerCount() == 0 && nextmap33.playerCount() == 0) {
					nextmap3.killAllMonsters(true);
					cm.dispose();
				}
				if (nextmap4.mobCount() > 0 && nextmap4.playerCount() == 0 && nextmap44.playerCount() == 0) {
					nextmap4.killAllMonsters(true);
					cm.dispose();
				}
				if (nextmap5.mobCount() > 0 && nextmap5.playerCount() == 0 && nextmap55.playerCount() == 0) {
					nextmap5.killAllMonsters(true);
					cm.dispose();
				}
				if (cm.getPlayer().getMapId() == 100040106 && nextmap11.playerCount() == 0 && nextmap1.playerCount() == 0 && cm.getJob() == 210 || cm.getJob() == 220 || cm.getJob() == 230) {
					cm.warp(108010200, 0);
					cm.spawnMobOnMap(9001001, 1, -276, -3, 108010201);
					cm.sendOk("法师3转的试炼即将开始!!");
					cm.dispose();
				} else if (cm.getPlayer().getMapId() == 105070001 && nextmap22.playerCount() == 0 && nextmap2.playerCount() == 0 && cm.getJob() == 110 || cm.getJob() == 120 || cm.getJob() == 130 || cm.getJob() == 2110) {
					cm.warp(108010300, 0);
					cm.spawnMobOnMap(9001000, 1, -276, -3, 108010301);
					cm.sendOk("剑士3转的试炼即将开始!!");
					cm.dispose();
				} else if (cm.getPlayer().getMapId() == 105040305 && nextmap33.playerCount() == 0 && nextmap3.playerCount() == 0 && cm.getJob() == 310 || cm.getJob() == 320) {
					cm.warp(108010100, 0);
					cm.spawnMobOnMap(9001002, 1, -276, -3, 108010101);
					cm.sendOk("弓箭手3转的试炼即将开始!!");
					cm.dispose();
				} else if (cm.getPlayer().getMapId() == 107000402 && nextmap44.playerCount() == 0 && nextmap4.playerCount() == 0 && cm.getJob() == 410 || cm.getJob() == 420) {
					cm.warp(108010400, 0);
					cm.spawnMobOnMap(9001003, 1, -276, -3, 108010401);
					cm.sendOk("盗贼3转的试炼即将开始!!");
					cm.dispose();
				} else if (cm.getPlayer().getMapId() == 105070200 && nextmap55.playerCount() == 0 && nextmap5.playerCount() == 0 && cm.getJob() == 510 || cm.getJob() == 520) {
					cm.warp(108010500, 0);
					cm.spawnMobOnMap(9001004, 1, -276, -3, 108010501);
					cm.sendOk("海盗3转的试炼即将开始!!");
					cm.dispose();
				} else {
					cm.sendOk("里面已经有人在挑战了0.0");
				}
			} else {
				cm.sendOk("你貌似已经有了#t4031059#。");
			}
		} else {
			cm.sendOk("请确认是否有足够的空间。");
		}
	} else {
		cm.sendOk("等级好像不正确。");
	}
    cm.dispose();
}