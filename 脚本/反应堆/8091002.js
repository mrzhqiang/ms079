/*
	Ludibirum Maze PQ
*/

function act() {
    var rand = (Math.random() * 2) + 1;
    var q = 0;
    var q2 = 0;
    if (rand < 2) {
	q = 3;
	q2 = 4;
    } else {
	q = 4;
	q2 = 3;
    }
    if (rm.getMapId() == 809050002) {
	rm.spawnMonster(9400213, q);
	rm.spawnMonster(9400214, q2);
    } else if (rm.getMapId() == 809050003) {
	rm.spawnMonster(9400213, q);
	rm.spawnMonster(9400214, q2);
    } else if (rm.getMapId() == 809050008) {
	rm.spawnMonster(9400213, q);
	rm.spawnMonster(9400214, q2);

    }
}