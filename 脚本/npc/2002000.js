var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.sendNext("噢噢~ 你还想待在这里的话，我随时都在哦 >_^");
        cm.safeDispose();
    }

    if (status == 0) {
        cm.sendYesNo("嗨~~你在这里没事做了吗? 你想要现在回去 #b自由市场#k? 我随时都能送你回去。你想要现在回去吗？");
    } else if (status == 1) {
        cm.warp(910000000);
        cm.dispose();
    }
}
