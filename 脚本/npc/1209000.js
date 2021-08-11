/* global cm */

var status = -1;


function action(mode, type, selection) {
    if (cm.getQuestStatus(21002) == 0) {
	if (mode == 1) {
	    status++;
	} else {
	    status--;
	}
	if (status == 0) {
	    cm.sendNext("您醒了，#b小乖乖#k 受伤的伤口还好吗？，什么？你说现在情况吗？");
	} else if (status == 1) {
	    cm.sendNextPrev("逃难的准备几乎都做好了。可以搭载的人全部都坐上了方舟了。逃生船飞行期间会由神兽守护，没有什么好担心的。现在只要收拾好就会立刻前往维多利亚港。");
	} else if (status == 2) {
	    cm.sendNextPrev("狂狼勇士的同伴吗...? 他们... 去找黑魔法师了。 在我们逃难的期间会阻止黑法师. 什么? 连你也要去找黑魔法师？ 不行！ 你不是受了伤吗？ 跟我们一起逃亡吧！");
	} else if (status == 3) {
	    cm.forceStartQuest(21002, "1");
	    // Ahh, Oh No. The kid is missing
	    //cm.showWZEffect2("Effect/Direction1.img/aranTutorial/Trio");
	    cm.dispose();
	}
    } else {
	if (mode == 1) {
	    status++;
	} else {
	    status--;
	}
	if (status == 0) {
	    cm.sendSimple("情况很紧急。你想知道什么？ \r #b#L0#黑魔法师？#l \r #b#L1#逃难准备？#l \r #b#L2#同伴们？#l");
	} else if (status == 1) {
	    switch (selection) {
		case 0:
		    cm.sendOk("听说黑魔法师就在不远处。因为成为黑魔法师布下的龙群阻挡，根本无法通过森林。所以我们打造了逃生船。现在只能飞往维多利亚岛逃难...");
		    break;
		case 1:
		    cm.sendOk("逃难几乎都做好了。可以搭载的人全部都上方舟了。现在只剩下几个人搭乘后就可以出发前往维多利亚岛。神兽答应在逃生船飞行的其间阻挡空中的攻击...没有人会留下来守护这里...");
		    break;
		case 2:
		    cm.sendOk("你的同伴... 他们已经去找黑魔法师了。 他们说要带我们去逃难的期间阻止黑魔法师...还说因为您受伤了，所以不带您去。等孩子都救出来后，您也跟我们一起逃走吧！ 狂狼勇士！");
		    break;
	    }
	    cm.safeDispose();
	}
    }
}