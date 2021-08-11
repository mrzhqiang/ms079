var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    qm.sendNext("不行啊！ 狂郎勇士... 抛弃那些孩子， 只剩下我们苟且偷生... 那人生还有什么意义！我知道对您来说只是很大的负担...可是请您再考虑看看吧！");
	    qm.dispose();
	    return;
	}
	status--
    }
    if (status == 0) {
	qm.askAcceptDecline("真是的！ 好像还有几个孩子留在森林内！ 我们不可能丢下孩子们逃走。 狂狼勇士... 请你去救救那些孩子吧！ 我知道对受的您说这些话真的很厚颜无耻， 可是... 只能拜托您了！");
    } else if (status == 1) {
	qm.forceStartQuest(21000, "..w?PG??."); // Idk what data lol..
	qm.forceStartQuest(21000, "..w?PG??."); // Twice, intended..
	qm.sendNext("#b孩子们应该在森林深处#k。 在黑魔法师追来这里之前，方舟要赶紧出发，请尽快救回那些孩子吧！");
    } else if (status == 1) {
	qm.sendNextPrev("最重要的是不要惊慌。 狂狼勇士。诺您想要确认任务进行情况，请按下 #bQ按键#k 确认任务栏位。");
    } else if (status == 2) {
	qm.sendNextPrev("拜托您了！狂狼勇士！请救救那些孩子！我不希望有人再牺牲于黑魔法师的魔掌之下。");
    } else if (status == 3) {
	qm.AranTutInstructionalBubble("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow1");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    qm.dispose();
}