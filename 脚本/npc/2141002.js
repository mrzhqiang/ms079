function start() {
    cm.sendYesNo("如果你现在离开，你将不得不重新开始。你确定要离开这里到外面去吗？");
}

function action(mode, type, selection) {
    if (mode == 1) {
        if (!cm.isLeader()) {
            cm.deleteboss();
            cm.warpParty(270050000);
        }
        cm.dispose();
    }
}