var status = -1;

function start(mode, type, selection) {
    if (mode > 0)
        status++;
    else {
        qm.dispose();
        return;
    }
    if (status == 0)
        qm.sendAcceptDecline("唉唉，你回来了。我可以看到你在10级了。它看起来像你闪烁的一丝希望努力成为一个骑士。基本的训练已经结束，现在是时候为你做的决定。");
    else if (status == 1) {
        qm.sendOk("左边有五个人他们就是骑士团的领导人在等着你。");
        qm.forceStartQuest();
        qm.forceCompleteQuest();
        qm.dispose();
    }
}