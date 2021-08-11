/**
	Chief Tatamo - Leafre(240000000)
**/

var section;
var temp;
var cost;
var count;
var menu = "";
var itemID = new Array(4000226, 4000229, 4000236, 4000237, 4000261, 4000231, 4000238, 4000239, 4000241, 4000242, 4000234, 4000232, 4000233, 4000235, 4000243);
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
	    cm.dispose();
	}
        if (status > 2) {
            if (section == 0) {
                cm.sendOk("请慎重考虑。一旦你做出了决定，让我知道。");
            } else {
                cm.sendOk("想想吧，然后让我知道你的决定。");
            }
            cm.safeDispose();
        }
        status--;
    }
    if (status == 0) {
        cm.sendSimple("你找我有事吗？\r\n#L0##b魔法种子#k#l\r\n#L1##b为了神木村的行动#k#l");
    } else if (status == 1) {
        section = selection;
        if (section == 0) {
            cm.sendSimple("需要我帮助你？？\r\n#L0##b我想跟你买一些 #t4031346#.#k#l");
        } else if (section == 1) {
            cm.sendNext("更好的建设村落是村长的职责。所以需要更多更好的道具。你能为了村落捐献出在神木村附近收集到的道具吗？");
        } else {
            if (cm.isQuestActive(3759)) {
                if (cm.haveItem(4032531)) {
                    cm.sendNext("Dragon Moss Extract...? I already gave you that!");
                } else {
                    cm.sendNext("Dragon Moss Extract...Ah, I see. I will give it to you in this situation.");
                    cm.gainItem(4032531, 1);
                }
            } else {
                cm.sendNext("考虑好再来找我。");
            }
            cm.dispose();
        }
    } else if (status == 2) {
        if (section == 0) {
            cm.sendGetNumber("#b#t4031346##k 需要买多少个？？", 1, 1, 99);
        } else {
            for (var i = 0; i < itemID.length; i++) {
                menu += "\r\n#L" + i + "##b#t" + itemID[i] + "##k#l";
            }
            cm.sendNext("你想捐献出那种道具呢？" + menu);
            //cm.safeDispose();
        }
    } else if (status == 3) {
        if (section == 0) {
            if (selection == 0) {
                cm.sendOk("我不能卖你0个。");
                cm.safeDispose();
            } else {
                temp = selection;
                cost = temp * 30000;
                cm.sendYesNo("你要买 #b" + temp + " #t4031346##k 它将花费你 #b" + cost + " 金币#k. 你确定要购买？？?");
            }
        } else {
            temp = selection;
            if(cm.haveItem(itemID[temp])) {
				//cm.sendGetNumber("How many #b#t" + itemID[temp] + "#k's would you like to donate?\r\n#b< Owned : #c" + itemID[temp] + "# >#k", 0, 0, "#c" + itemID[temp] + "#");
				cm.sendGetNumber("你要捐多少个 #b#t" + itemID[temp] + "#k'我会给你很好的酬劳的！", 1, 1, 999);
            } else {
                cm.sendNext("我不认为你有这道具。");
                cm.safeDispose();
            }
        }
    } else if (status == 4) {
        if (section == 0) {
            if (cm.getMeso() < cost || !cm.canHold(4031346)) {
                cm.sendOk("请确认是否有足够的金币和道具栏位。");
            } else {
                cm.sendOk("再会~");
                cm.gainItem(4031346, temp);
                cm.gainMeso( - cost);
            }
            cm.safeDispose();
        } else {
            count = selection;
            cm.sendYesNo("你确定你想赞助 #b" + count + " #t" + itemID[temp] + "##k?");
        }
    } else if (status == 5) {
        if (count == 0 || !cm.haveItem(itemID[temp], count)) {
            cm.sendNext("请确认赞助项目是否足够。");
        } else {
            cm.gainItem(itemID[temp], -count);
            cm.sendNext("感谢你赞助。");
        }
        cm.safeDispose();
    }
}
