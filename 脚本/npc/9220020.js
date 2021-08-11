var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (!cm.isLeader()) {
	cm.sendNext("请找队长来和我说话。");
	cm.dispose();
	return;
    }
    if (cm.haveItem(4001190,17)) {
	cm.warpParty(674030200);
	cm.gainItem(4001190,-17);
    } else {
	cm.sendOk("欢迎来到盖福斯副本 我需要地图上岩块堆 中的\r\n#r17个 #b#v4001190##k。");
    }
    cm.dispose();
}