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
	q2 = 4;
    } else {
	q = 4;
	q2 = 3;
    }
    if (rm.getMapId() == 809050000) {
	rm.spawnMonster(9400209, q);
	rm.spawnMonster(9400210, q2);
    } else if (rm.getMapId() == 809050010) {
	rm.spawnMonster(9400209, q);
	rm.spawnMonster(9400210, q2);
    }
}