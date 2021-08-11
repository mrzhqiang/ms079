/* ===========================================================
			Resonance
	Map(s): 		Queen's Path : Forest of the Start 1(130030000)
	Description: 	Warp to Queen's Path : Forest of the Start 2
=============================================================
Version 1.0 - Script Done.(11/5/2010)
=============================================================
*/

function enter(pi) {
	pi.playPortalSE();
	pi.warp(130030001, "east00");
	return true;
}