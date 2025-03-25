
var Vector = Java.type("com.github.mrzhqiang.maplestory.wz.element.data.Vector");
function action(mode, type, selection) {
	if (cm.getPlayerCount(913030000) == 0) {
		cm.removeNpc(913030000, 1104002);
		var map = cm.getMap(913030000);
		map.killAllMonsters(false);
		map.spawnNpc(1104002, Vector.of(-430, 88));
		cm.warp(913030000, 0);
	} else {
	    cm.playerMessage("黑女巫正在与其他人战斗中。");
	}
	cm.dispose();
}