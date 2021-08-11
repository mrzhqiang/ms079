
function enter(pi) {
    if (pi.getQuestStatus(21728) == 1) {
        pi.warp(910510001, 0);
        return true;
    } else {
        pi.playerMessage(5, "因不明的力量，而無法進入此洞穴。");
        return false;
    }

}