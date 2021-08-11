/* global cm */

var slot;
var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else if (mode === 0) {
        status--;
    } else {
        cm.dispose();
        return;
    }

    if (status === 0) {
        cm.sendGetNumber("请输入想要兑换的颗数. \r\n所需物品为#i4032225#\r\n#r(比值:1#i4032225#x1:#i4032226#x1)\r\n请注意身上背包的空间#k", 1, 1, 100);
    } else if (status === 1) {
        slot = selection;
        if (!cm.haveItem(4032225, slot)) {
            cm.sendNext("请确认是否有#i4032225#。");
            cm.dispose();
            return;
        }
        cm.sendYesNo("你确定要兑换吗？？");
    } else if (status === 2) {
        if (!cm.canHold(4032226, slot)) {
            cm.sendNext("背包空间不足。");
            cm.dispose();
            return;
        }
        cm.gainItem(4032225, -slot);
        cm.gainItem(4032226, slot);
        cm.sendOk("#b恭喜你成功拉!快快看你的包裹吧!#k");
        cm.dispose();
    } else {
		cm.dispose();
	}
}
