/*
	任务: 不管多少次都得争取！
	描述: 我从重力网驾驶员……位于2102年商业区的#p9120033#处，获得了讨伐#o9400295#的任务，他说重力网使用的驱动组件目前出现在#o9400295#的周围，有了它就可以强化#o9400295#没时间了，如果不尽快地打倒#o9400295#的话…！
*/
var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        qm.sendNext("...What is it? Ah, I see that he's coming really close!");
        qm.dispose();
        return;
    }
    if (status == 0) {
        qm.askAcceptDecline("Watch out, because he seems... much more powerful than before. Do not underestimate him!");
    } else if (status == 1) {
        qm.forceStartQuest();
        qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.forceCompleteQuest();
    qm.dispose();
}