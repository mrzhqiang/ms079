/**
-- Odin JavaScript --------------------------------------------------------------------------------
	Ludibirum Maze PQ
-- By ---------------------------------------------------------------------------------------------
	sadiq
-- Version Info -----------------------------------------------------------------------------------
	1.0 - First Version by sadiq [ thanks to RMZero123 for the original reactor ]
---------------------------------------------------------------------------------------------------
**/

function act() {
    var rand = (Math.random() * 2) + 1;
    var q = 0;
    var q2 = 0;
    if (rand < 2) {
	q = 3;
	q2 = 3;
    } else {
	q = 3;
	q2 = 3;
    }
    if (rm.getMapId() == 809050005) {
	rm.spawnMonster(9400217, q);
	rm.spawnMonster(9400218, q2);
    } else if (rm.getMapId() == 809050006) {
	rm.spawnMonster(9400217, q);
	rm.spawnMonster(9400218, q2);
    }
}