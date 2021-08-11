/* 
 * 
 * Adobis's Mission I: Unknown Dead Mine (280010000)
 * 
 * Zakum PQ NPC (the one and only)
 */

var status = -1;
var selectedType;
var scrolls;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        cm.sendSimple("怎么样？都搜集好了吗？#b\r\n#L0#告诉我应该做什么？#l\r\n#L1#已经搜集好了物品！#l\r\n#L2#我要离开这里！#l");
    } else if (status == 1) {
        selectedType = selection;
        if (selection == 0) {
            cm.sendNext("为了解除扎昆的前置，你必须收集我需要的核心材料。")
            cm.safeDispose();
        } else if (selection == 1) {
            if (!cm.haveItem(4001018)) { //documents
                cm.sendNext("请给我#b#t4001018##k谢谢。")
                cm.safeDispose();
            } else {
                if (!cm.haveItem(4001015, 30)) { //documents
                    cm.sendYesNo("带来了是嘛??\r\n为了确保您能拿到酬劳请先空出空间");
                    scrolls = false;
                } else {
                    cm.sendYesNo("带来了是嘛??\r\n为了确保您能拿到酬劳请先空出空间");
                    scrolls = true;
                }
            }
        } else if (selection == 2) {
            cm.sendYesNo("你确定要退出？如果你是组队长，一旦你离开组队，那么这项任务就无法继续下去。是否决定退出？")
        }
    } else if (status == 2) {
        var eim = cm.getEventInstance();
        if (selectedType == 1) {
            cm.gainItem(4001018, -1);
            if (scrolls) {
                cm.gainItem(4001015, -30);
            }
            //give items/exp
            cm.givePartyItems(4031061, 1);
            if (scrolls) {
                cm.givePartyItems(2030007, 5);
                cm.givePartyExp(20000);
            } else {
                cm.givePartyExp(12000);
            }
            //clear PQ
            if (eim != null) {
                eim.finishPQ();
            }
            cm.dispose();
        } else if (selectedType == 2) {
            if (eim != null) {
                if (cm.isLeader()) {
                    eim.disbandParty();
                } else {
                    eim.leftParty(cm.getChar());
                }
            } else {
                cm.warp(280090000, 0);
            }
            cm.dispose();
        }
    }
}