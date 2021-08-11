/*
 * The return of the Hero
 * Rien Cold Forest 1
 */

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 3) {
	    qm.sendNext("哎呀，不用客气啦！送英雄一瓶药水也不是什么了不起的事。倘若您改变心意在告诉我吧！");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("咦？ 这个岛上的什么人？ 喔， 您认识 #p1201000#吗？ #p1201000#到这里有什么事情...啊，这位是不是#p1201000#大人认识的人呢？神么？你说这位是英雄吗？");
    } else if (status == 1) {
	qm.sendNextPrev("     #i4001170#");
    } else if (status == 2) {
	qm.sendNextPrev("这位正是 #p1201000#家族数百年等待的英雄！喔喔！难怪看起来不是什么平凡的人物...");
    } else if (status == 3) {
	qm.askAcceptDecline("但是，因为黑魔法师的诅咒而在巨冰里沉睡着，所以，好像英雄的体力都消耗掉了的样子。#b我给你一个恢复体力用的药水，赶紧喝喝看#k。");
    } else if (status == 4) { // TODO HP set to half
	qm.sendNext("您先大口喝下，我再继续说下去~");
	qm.gainItem(2000022, 1);
	qm.forceStartQuest();
    } else if (status == 5) {
	qm.sendNextPrevS("#b(药水该怎么喝呢？...想不起来了...)#k", 3);
    } else if (status == 6) {
	qm.summonMsg(0xE);
	qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	qm.sendNext("长久以来寻找英雄的痕迹，在冰雪中挖掘，果然真正的英雄出现了！预言果真是真的！#p1201000#大人做了正确的选择英雄回来了，现在没有必要再畏惧黑魔法师了！");
    } else if (status == 1) {
	qm.sendNextPrev("啊！真是的，我耽误英雄太久了。先收拾起快乐的心情...其他企鹅应该也有同样的想法。我知道您很忙，不过在前往村庄的路上 #b也请您和其他企鹅们谈一谈#k。可以和英雄谈话，大家应该会很兴奋！ \n\r #fUI/UIWindow.img/QuestIcon/4/0# \r #i2000022# #t2000022# 5 \r #i2000023# #t2000023# 5 \n\r #fUI/UIWindow.img/QuestIcon/8/0# 16 经验值");
    } else if (status == 2) {
	qm.sendNextPrev("想要升级吗？不晓得您有没有获得技能点数。在枫之谷内透过转职之后每上升1级就会获得3点的技能点数。按下 #bK键#k 就能栏位就能确认。");
	if (qm.getQuestStatus(21010) == 1) {
	    qm.gainExp(16);
	    qm.gainItem(2000022, 5);
	    qm.gainItem(2000023, 5);
	    qm.forceCompleteQuest();
	}
    } else if (status == 3) {
	qm.sendNextPrevS("#b(这么亲切的说明，可是我什么都想不起来。我真的是英雄吗？那么先确认技能看看...可是我该怎么确认呢？)#k");
    } else if (status == 4) {
	qm.summonMsg(0xF);
	qm.dispose();
    }
}