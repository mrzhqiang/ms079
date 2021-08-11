function start() {
    var status = cm.getQuestStatus(20706);
    
    if (status == 0) {
        cm.sendNext("它看起来像有什么可疑的地方.");
    } else if (status == 1) {
        cm.forceCompleteQuest(20706);
        cm.sendNext("你已经发现的影子！最好是报告给 #p1103001#.");
    } else if (status == 2) {
        cm.sendNext("阴影已经被发现。最好是报告给 #p1103001#.");
    }
    cm.dispose();
}
function action(mode, type, selection) {
    cm.dispose();
}