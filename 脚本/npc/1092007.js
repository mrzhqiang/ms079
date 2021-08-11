function start() {
	if (cm.isQuestActive(2175)) {
	cm.sendOk("你准备干黑魔法师的手下了吗?? 我将把你传送过去...");
    } else {
    cm.sendOk("这黑魔师真它妈的该死!!");
    cm.dispose();
}
}

function action(mode, type, selection) {
    cm.warp(912000000,0);
    cm.dispose();
}
