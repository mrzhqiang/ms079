/**
	Konpei - Near the Hideout(801040000)
*/

function start() {
    cm.sendYesNo("你害怕了？ 想回去 #m801000000#？");
}

function action(mode, type, selection) {
    if (mode == 0) {
	cm.sendOk("如果你想回去 #m801000000#, 告诉我");
    } else {
	cm.warp(801000000,0);
    }
    cm.dispose();
}