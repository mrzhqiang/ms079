/* 
 * NPC :      Mihai
 * Map :      Timu's Forest
 */

function start() {
    cm.sendNext("呵呵...难道我发现了什么东西吗？那么只有一个办法了！出来吧！!");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.removeNpc(cm.getMapId(), cm.getNpc());
	cm.spawnMonster(9001009,1); // Transforming
    }
    cm.dispose();
}