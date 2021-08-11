var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendNext("嗯...用这个方法也不能恢复记忆吗？可是没试过也不晓得，您再考虑看看吧。");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("大英雄！您好！您说怎么知道您是英雄吗？前面有三个人大声的叫喊，当然知道啊。这个岛上已经流传着英雄回来的消息。");
    } else if (status == 1) {
	qm.sendNextPrev("总之，英雄的脸色怎么会这么难看呢？您有什么困难吗？您说您不晓得自己是不是真正的英雄吗？ 英雄丧失记忆了吗？怎么会这样...应该是数百年被困在冰雪之中的副作用。");
    } else if (status == 2) {
	qm.askAcceptDecline("啊！ 既然您是英雄 ，只要挥挥剑应该会想起什么吧！您想不想去#b猎捕怪物#k呢?");
    } else if (status == 3) {
	qm.forceStartQuest();
	qm.sendNext("正好这附近有很多 #r#o9300383#s#k 请您去击退 #r3只#k。	搞不好会想起些什么。");
    } else if (status == 4) {
	qm.sendNextPrevS("啊，该不会连技能使用方法都忘光了吧？ #b将技能放入快捷栏就可以轻松使用#k。 不只是技能，连消耗道具也可以放进去，请多加利用。", 1);
    } else if (status == 5) {
	qm.summonMsg(17);
	qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    qm.sendNext("什么？你不喜欢药水？");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendYesNo("嗯...看您的表情，似乎什么都没有想起来...可是请不要担心。总有一天会好起来的。来，请您喝下这些药水打起精神来: \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i2000022# 10 #t2000022# \r\n#i2000023# 10 #t2000023# \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 57 经验值");
    } else if (status == 1) {
	qm.gainItem(2000022, 10);
	qm.gainItem(2000023, 10);
	qm.gainExp(57);
	qm.forceCompleteQuest();
	qm.sendOkS("#b(就算我是真正的英雄...可是什么能力都没有的英雄还有用处吗？)#k", 2);
	qm.dispose();
    }
}