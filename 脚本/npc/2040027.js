/*
	Fourth Eos Rock - Ludibrium : Eos Tower 1st Floor (221020000)
*/

var status = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 0 && mode == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (cm.haveItem(4001020)) {
	    cm.sendYesNo("你是否要使用#t4001020# 传送到#m221021700# 呢？？");
	} else {
	    cm.sendOk("你需要有#t4001020# 才可以启动。");
	    cm.dispose();
	}
    } else if (status == 1) {
	cm.gainItem(4001020, -1);
	cm.warp(221021700, 3);
	cm.dispose();
    }
}