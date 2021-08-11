/*
	NPC Name: 		Nineheart
	Description: 		Quest - Do you know the black Magician?
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 8) {
	    qm.sendNext("Oh, do you still have some questions? Talk to me again and I'll explain it to you from the very beginning.");
	    qm.safeDispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("嗨, #h0#. 来迎来到 #p1101000# 骑士团. 我的名字是 #p1101002# 而我目前作为年轻慈禧的战术家。哈哈！");
    } else if (status == 1) {
	qm.sendNextPrev("我敢肯定，你有很多的问题，因为一切都发生得太快。我会解释这一切，一个接一个，从那里你是你在这里做什么。");
    } else if (status == 2) {
	qm.sendNextPrev("这个岛被称为耶雷弗。");
    } else if (status == 3) {
	qm.sendNextPrev("这位年轻的女皇是枫之谷世界的统治者。什么？这是你听说过她的第一次？啊，是的。嗯，她是枫之谷世界的统治者，但她不喜欢来控制它。她从远处观看，以确保一切都很好。好吧，至少这是她一贯的作用。");
    } else if (status == 4) {
	qm.sendNextPrev("但是，这不是这种情况现在。我们一直在寻找的标牌立在枫的世界，预示黑法师的复兴。我们不能让黑法师回来恐吓枫之谷，因为他在过去!");
    } else if (status == 5) {
	qm.sendNextPrev("但是，这是很久以前的今天，人们不要为实现黑法师是有多吓人的。我们都成了被宠坏和平枫之谷世界我们今天所享有和遗忘是如何混乱和可怕的枫之谷世界曾经是。如果我们不做些什么，黑法师将再次统治枫之谷世界！");
    } else if (status == 6) {
	qm.askAcceptDecline("以上是我的解释，有问题吗？ \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#fUI/UIWindow.img/QuestIcon/8/0# 380 经验值");
    } else if (status == 7) {
	if (qm.getQuestStatus(20016) == 0) {
	    qm.gainExp(380);
	    qm.forceCompleteQuest();
	}
	qm.sendNext("我很高兴你清楚我们目前的情况，但你知道在你目前的能力，你甚至没有强大到足以面对黑法师的爪牙，更别说黑魔导士本人。连自己的奴才'奴才，作为一个事实问题。你将如何保护枫之谷世界在你目前的等级？");
    } else if (status == 10) {
	qm.sendNextPrev("Although you've been accepted into the knighthood, you cannot be recognized as a knight yet. You are not an Official Knight because you're not even a Knight-in-Training. If you remain at your current level, you'll be nothing more than the handyman of #p1101000# Knights.");
    } else if (status == 11) {
	qm.sendNextPrev("But no one starts as a strong Knight on day one. The Empress didn''t want someone strong. She wanted someone with courage whom she could develop into a strong Knight through rigorous training. So, you should first become a Knight-in-Training. We'll talk about your missions when you get to that point.");
    } else if (status == 12) {
	qm.sendPrev("Take the portal on the left to reach the Training Forest. There, you will find #p1102000#, the Training Instructor, who will teach you how to become stronger. I don''t want to find you wandering around aimlessly until you reach Lv. 10, you hear?");
	qm.safeDispose();
    }
}

function end(mode, type, selection) {
}