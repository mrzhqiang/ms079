var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
        qm.dispose();
    } else {
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            qm.sendNext("很不错啊，已经成功获取了狼神。但是乘骑的话还是需要特制的鞍子才能骑上去的。如果由于材料不够，我无发给你`你所需要的东西。");
        } else if (status == 1) {
            qm.sendAcceptDecline("别着急，虽然我这里没有足够的材料。但是也不是没办法了。这样吧，你帮我去搜索#b小白雪人的皮50个#k来吧！我就可以给你做了！");
        } else if (status == 2) {
            qm.forceStartQuest();
            qm.sendNext("快去快回啊，好冷！记好是 #b50个小白雪人的皮#k。");
        } else if (status == 3) {
            qm.dispose();
        }
    }
}