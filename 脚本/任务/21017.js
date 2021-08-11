var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 5) {
	    qm.sendNext("#b(用害怕的心情按下拒绝。可是又不能这样逃走..镇定心情后再谈看看。)#k", 2);
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("现在筋骨应该都松开了吧！此时更严格的锻炼，才能拥有完美的基础体力。好！那么我们继续进行基础体力锻炼！", 8);
    } else if (status == 1) {
	qm.sendNextPrev("现在前往 #b#m140020200##k 击退 #r#o0100133#s#k 只要击退....#r20只#k 左右，就会锻炼体力有很大的帮助。好，快去吧...咦？你有话想说吗？", 8);
    } else if (status == 2) {
	qm.sendNextPrev(".....为什么数字愈来愈多.....", 2);
    } else if (status == 3) {
	qm.sendNextPrev("当然会增加。哎呀，您觉得 20只太少吗？ 那么去击退100只怎么样啊？不，不。既然要修炼，那么就效法奇幻村的某人要求去抓 999只怪物...", 8);
    } else if (status == 4) {
	qm.sendNextPrev("不，不用啦！这样就够了。", 2);
    } else if (status == 5) {
	qm.askAcceptDecline("哎呀哎呀，没有必要推辞。我完！全！的！了解英雄想快点变强的心情。英雄真的太伟大了...");
    } else if (status == 6) {
	qm.StartQuest();
	qm.sendNextS('#b(再听一次好像真的要我去击退 999只，干脆答应算了。)#k', 2);
    } else if (status == 7) {
	qm.sendNextPrev('那么请击退 #o0100133# 20只吧！', 8);
    } else if (status == 8) {
	qm.AranTutInstructionalBubble("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow3");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.dispose();
}
