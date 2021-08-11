var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 6) {
	    qm.sendNext("我需要修炼这种东西需要意志和实力，超越您的老师是非常惊人的，但你不能让你自己坠落下去，你必须不断的修炼才能获得强大的力量，同时找回失去的记忆。");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("……现在你的能力是什么程度，我大概了解了……呵呵……没想到我这把老骨头还能有今天……真是感动得要流眼泪……不，是鼻涕……");
    } else if (status == 1) {
	qm.sendNextPrevS("#b(……也没怎么修炼嘛……)#k", 2);
    } else if (status == 2) {
	qm.sendNextPrev("好，现在让我们开始第3阶段的最后一阶段的锻炼。这次修炼的对象是……#r#o9300343##k！猪猪！你了解他们吗？");
    } else if (status == 3) {
	qm.sendNextPrevS("一点点……", 2);
    } else if (status == 4) {
	qm.sendNextPrev("他们是天生的战士！从出生的那一刻起，对食物就充满了无穷的愤怒，凡是他们经过的地方都不会留下任何食物。很可怕吧？");
    } else if (status == 5) {
	qm.sendNextPrevS("#b(他不是在开玩笑吧？)#k", 2);
    } else if (status == 6) {
	qm.askAcceptDecline("来，快点#b再次进入修炼场#k，去和那些天生的战士们修炼用的猪中战斗吧，打倒#r30只#k后，你的能力将会有一个质的飞跃。全力以赴地去战斗吧！超越我这个老师！");
    } else if (status == 7) {
	qm.forceStartQuest();
	qm.sendOk("快走吧！去打倒那些#o9300343#！");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendNext("你舍不得离开老师？ 嗅嗅闻闻.... 我太感动了，但你不能到此为止！");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("这么快就打倒了30只#o9300343#……我这老头子果然没有看错。虽然你失去了曾经的记忆，失去了曾经的能力，但你仍然是个英雄！只要手上的长矛还在！");
    } else if (status == 1) {
	qm.sendNextPrevS("#b(这么说是为了安慰我吗？)#k", 2);
    } else if (status == 2) {
	qm.sendYesNo("我已经没什么可继续教你的了。你已经超越了我这个老头子。你可以下山了……唉，没什么好忧郁的。我这老头子能够有机会指导你，已经很满足了。");
    } else if (status == 3) {
	if (qm.getQuestStatus(21703) == 1) {
	    qm.forceCompleteQuest();
	    qm.teachSkill(21000000, qm.getPlayer().getSkillLevel(21000000), 10);   // Combo Ability Skill
	    qm.gainExp(2800);
	}
	qm.sendNextS("我想起了技能#b连击能力#k！ 我还想跟着有点痴呆的老头子训练有没有效果呢，没想到真的有效！)", 2);
	qm.AranTutInstructionalBubble("Effect/BasicEff.img/AranGetSkill");
    } else if (status == 4) {
	qm.sendPrev("现在你回去找#p1201000#吧。他看到你的进步会很高兴的！");
	qm.dispose();
    }
}