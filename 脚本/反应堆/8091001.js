/*
	Ludibirum Maze PQ
*/

function act() {
    var rand = (Math.random() * 2) + 1;
    var q = 0;
    var q2 = 0;
    if (rand < 2) {
	q = 4;
	q2 = 3;
    } else {
	q = 3;
	q2 = 4;
    }
    if (rm.getMapId() == 809050001) {
	rm.spawnMonster(9400211, q);
	rm.spawnMonster(9400212, q2);
    } else if (rm.getMapId() == 809050009) {
	rm.spawnMonster(9400211, q);
	rm.spawnMonster(9400212, q2);
    }
}