var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (status >= 0 && mode == 0) {
			cm.sendOk("嗯... 我猜你还有什么别的事情要在这里做吧？");
			cm.dispose();
			return;
		}
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendYesNo("如果你想离开这里，你需要付给我 #e150 金币#n  我会带你去 #b维多利亚岛#k. 但关键的是，你一旦离开，你就可以再回到这里来。你是不是想要去维多利亚岛？");
		} else if (status == 1) {
			if (cm.haveItem(4031801)) {
				cm.sendNext("好, 现在给我 150 金币... 嗯, 那是什么？ 是路卡斯的推荐信？ 嘿, 你应该告诉我. 伟大的冒险家。我似乎已经看到你的将来。");
			} else {
				cm.sendNext("确定要离开吗? 那么... 先付我 #e150 金币#n 吧...");
			}
		} else if (status == 2) {
			if (cm.haveItem(4031801)) {
				cm.sendNextPrev("既然你有推荐信，我也不会向你收任何费用的。好了。我们现在就向维多利亚岛出发。船可能会有点动荡，坐好了...");
			} else {
				if (cm.getLevel() >= 7) {
					if (cm.getMeso() < 150) {
						cm.sendOk("什么？你想去维多利亚但你没有钱？你是一个怪人...");
						cm.dispose();
					} else {
						cm.sendNext("很好! #e150#n 金币! 那么，我们现在就向维多利亚岛出发吧!");
					}
				} else {
					cm.sendOk("来让我看看，我不认为你有足够的资格去维多利亚岛。你至少等级在7级或者7级以上。");
					cm.dispose();
				}
			}
		} else if (status == 3) {
			if (cm.haveItem(4031801)) {
				cm.gainItem(4031801, -1);
				//cm.getChar().sethp(0);
				cm.warp(104000000);
				cm.dispose();
			} else {
				cm.gainMeso(-150);
				//cm.getChar().sethp(0);
				cm.warp(104000000);
				cm.dispose();
			}
		}
	}
}