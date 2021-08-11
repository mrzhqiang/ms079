/*
	NPC Name: 		Hera
	Map(s): 		Towns
	Description: 		Wedding Village Entrance
*/

var status = -1;

function start() {
    cm.sendSimple("啊~今天真是个好日子！这世界太美好了~！你不觉得这世界充满了爱吗？满溢婚礼村的爱意都流淌到这里来了~！ \n\r #b#L0# 我想回去结婚小镇.#l \r\n #L1#我已经结婚了我想要领恋爱之心~");
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        switch (selection) {
            case 0:
		cm.sendNext("哦！多么美好的一天！这个世界是多么的美好～！这个世界似乎是充满爱的，不是吗？我可以从这里感受到爱的精神填补了婚礼!");
                break;
            case 1:
	        var marr = cm.getQuestRecord(160001);
	        var data = marr.getCustomData();
	        if (data == null) {
		    marr.setCustomData("0");
	            data = "0";
	        }
		if (cm.getPlayer().getMarriageId() <= 0 || !data.equals("3")) {
                    cm.sendOk("我很抱歉如果您想要得到这个椅子的话请先结婚~~");
		} else if (cm.canHold(3012004,1) && !cm.haveItem(3012004,1) && !cm.isQuestFinished(52013)) {
		    cm.gainItem(3012004,1);
			cm.forceCompleteQuest(52013);
			cm.sendOk("结婚后多一份喜悦送你吧，但机会只有一次!");
		} else {
		    cm.sendOk("请确定是否装备栏满了或者您已经有相同的椅子了... 或者你领过了....");
		}
                cm.dispose();
                break;
        }
    } else if (status == 1) {
        cm.sendYesNo("你曾经去过的婚礼村庄？这是一个了不起的地方，爱情是无极限的。恩爱夫妻可以结婚还有，如何浪漫它是什么？如果你想在那里，我会告诉你的方式.");
    } else if (status == 2) {
        cm.sendNext("你做了一个正确的决定！你可以感受到爱的精神在婚礼村发挥到淋漓尽致。当你想回来，你的目的地将在这里，所以不要担心.");
    } else if (status == 3) {
	   cm.saveLocation("AMORIA");
	   cm.warp(680000000, 0);
           cm.dispose();
    }
}
