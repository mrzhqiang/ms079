/* Konpei
	Showa ultra warp npc - armory
*/

function start() {
    if (cm.getPlayerCount(801040100) == 0) {
	cm.resetMap(801040100);
    }
    cm.warp(801040100);
    cm.dispose();
}

function action(mode, type, selection) {
}