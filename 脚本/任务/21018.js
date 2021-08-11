var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 1) {
	    qm.sendNext("什么，该不会是嫌5只太少了吧？如果是为了修炼多击退一些的话也是没关系。为了英雄，我虽然心痛，但是也会睁一只眼闭一只眼");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("好！那么现在要进行基础体力的测试。方法很简单。只要击败岛上最强最凶恶的怪物， #o0100134# 就可以了！如果可以击败 #r50只#k 那就太好了，可是...");
    } else if (status == 1) {
	qm.askAcceptDecline("如果将仅剩没几只的 #o0100134#全部击退，对生态界似乎会造成不好的影响，那么请击败5只。考虑到自然和环境的锻炼！啊！真太美了...");
    } else if (status == 2) {
	qm.forceStartQuest();
	qm.AranTutInstructionalBubble("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow1");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.dispose();
}
