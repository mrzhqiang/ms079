
var Vector = Java.type("com.github.mrzhqiang.maplestory.wz.element.data.Vector");
function act() {
	rm.mapMessage(5, "小男孩的魅力!!!!");
	rm.getMap().spawnMonsterOnGroundBelow(em.getMonster(9400261), Vector.of(751, 137));
}