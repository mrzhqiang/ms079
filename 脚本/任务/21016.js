var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    qm.sendNext("您还没准备好猎杀 #o0100132#吗？ 最好把该准备的都准备好再去狩猎比较好。如果不好好准备，在途中一命呜呼了，那只会让人遗浅罢了！");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.askAcceptDecline("那么要继续基础体力锻炼吗？准备好了吗？请您在确认剑是否装备好了，技能和药是否已经放入快捷栏内，然后就开始吧！");
    } else if (status == 1) {
	qm.forceStartQuest();
	qm.AranTutInstructionalBubble("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow3");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.dispose();
}
