/* Crumbling Statue
	1061007
*/

var status = 0;
var zones = 0;
var selectedMap = -1;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 1 && mode == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
        
    if (status == 0) {
	cm.sendNext("The crumbling statue makes you sad :(");
    } else if (status == 1) {
	cm.sendYesNo("Would you like to escape the sadness?");
    } else if (status == 2) {
	cm.warp(105040300);
	cm.dispose();
    }
}	