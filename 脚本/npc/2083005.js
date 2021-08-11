/*
	Keroben - Leafre Cave of life - Entrance
*/

var morph;
var status = -1;

function action(mode, type, selection) {
	if (cm.haveItem(4031454)) {
	     cm.gainItem(4031454, -1);
             cm.gainItem(4031455, 1);
	     cm.dispose();
	} else {
	     cm.dispose();
    }
}